package en.ubb.networkconfiguration.business.service.impl;

import en.ubb.networkconfiguration.business.service.BranchService;
import en.ubb.networkconfiguration.business.service.NetworkService;
import en.ubb.networkconfiguration.business.service.UserService;
import en.ubb.networkconfiguration.business.validation.exception.ForbiddenAccessBussExc;
import en.ubb.networkconfiguration.business.validation.exception.NotFoundBussExc;
import en.ubb.networkconfiguration.persistence.dao.BranchRepo;
import en.ubb.networkconfiguration.persistence.dao.specification.BranchSpec;
import en.ubb.networkconfiguration.persistence.domain.authentication.User;
import en.ubb.networkconfiguration.persistence.domain.branch.NetworkBranch;
import en.ubb.networkconfiguration.persistence.domain.network.runtime.Network;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BranchServiceImpl implements BranchService {

    private final BranchRepo branchRepo;

    private final UserService userService;

    private final NetworkService networkService;

    @Autowired
    public BranchServiceImpl(BranchRepo branchRepo, UserService userService, NetworkService networkService) {
        this.branchRepo = branchRepo;
        this.userService = userService;
        this.networkService = networkService;
    }

    @Override
    @Transactional
    public NetworkBranch assign(String branchName, String username) throws NotFoundBussExc, ForbiddenAccessBussExc {
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new NotFoundBussExc("User not found"));
        NetworkBranch networkBranch = this.findByName(branchName)
                .orElseThrow(() -> new NotFoundBussExc("Branch with name " + branchName + " not found"));
        if (!networkBranch.getOwner().getUsername().equals(username)) {
            if (networkBranch.getContributors().stream()
                    .noneMatch(c -> c.getUsername().equals(username))) {
                throw new ForbiddenAccessBussExc("You are not a contributor or the owner of this branch");
            }
        }
        networkBranch.addCurrentUser(user);
        return branchRepo.save(networkBranch);
    }

    @Override
    @Transactional
    public NetworkBranch create(NetworkBranch newBranch, NetworkBranch origin) throws NotFoundBussExc {

        if (origin != null) {
            origin.getNetworks().forEach(network -> {
                newBranch.addNetwork(networkService.duplicate(network));
            });
            newBranch.setSourceId(origin.getSourceId());
        }

        List<User> contributors = new ArrayList<>();
        for (User contributor : newBranch.getContributors()) {
            contributors.add(this.userService.findByUsername(contributor.getUsername())
                    .orElseThrow(() -> new NotFoundBussExc("Owner not registered")));
        }
        newBranch.setContributors(contributors);
        newBranch.setOwner(this.userService.findByUsername(newBranch.getOwner().getUsername())
                .orElseThrow(() -> new NotFoundBussExc("Owner not registered")));

        newBranch.setCreateDateTime(LocalDateTime.now());
        newBranch.setUpdateDateTime(LocalDateTime.now());

        return branchRepo.save(newBranch);
    }

    @Override
    @Transactional
    public NetworkBranch pull(long toId, long fromId) throws NotFoundBussExc {
        NetworkBranch to = branchRepo.findById(toId)
                .orElseThrow(() -> new NotFoundBussExc("Destination network with id " + toId + " not found"));
        NetworkBranch from = branchRepo.findById(fromId)
                .orElseThrow(() -> new NotFoundBussExc("Origin network with id " + fromId + " not found"));
        solveConflicts(to, from);
        to.setUpdateDateTime(LocalDateTime.now());
        return branchRepo.save(to);
    }


    private boolean shareCommonOrigin(Network originN, Network sourceN) {
        return originN.getOriginId() != null && sourceN.getOriginId() != null && sourceN.getOriginId().equals(originN.getOriginId())
                || (sourceN.getOriginId() != null && sourceN.getOriginId().equals(originN.getId()))
                || (originN.getOriginId() != null && sourceN.getId().equals(originN.getOriginId()));
    }


    public void solveConflicts(NetworkBranch to, NetworkBranch from) {
        for (int i = 0; i < from.getNetworks().size(); i++) {
            Network sourceN = from.getNetworks().get(i);
            boolean shouldAdd = true;
            for (int j = 0; j < to.getNetworks().size(); j++) {
                Network originN = to.getNetworks().get(j);
                if (shareCommonOrigin(originN, sourceN)) {
                    if (originN.getUpdateDateTime().isBefore(sourceN.getUpdateDateTime())) {
                        to.removeNetwork(originN);
                    } else {
                        shouldAdd = false;
                    }
                    break;
                }
            }
            if (shouldAdd)
                to.addNetwork(sourceN);
        }
    }

    @Override
    public NetworkBranch update(long id, @NotNull NetworkBranch newBranch) throws NotFoundBussExc {


        NetworkBranch networkBranch = this.branchRepo.findById(id)
                .orElseThrow(() -> new NotFoundBussExc("Branch with id " + id + " not found"));

        if (newBranch.getName() != null) {
            networkBranch.setName(newBranch.getName());
        }

        if (newBranch.getType() != null) {
            networkBranch.setType(newBranch.getType());
        }

        if (newBranch.getOwner() != null) {
            User owner = userService.findByUsername(newBranch.getOwner().getUsername())
                    .orElseThrow(() -> new NotFoundBussExc("Owner with username " + newBranch.getOwner().getUsername() + " not found"));
            networkBranch.setOwner(owner);
        }

        if (newBranch.getContributors() != null && !newBranch.getContributors().isEmpty()) {
            networkBranch.getContributors().forEach(networkBranch::removeContributor);
            for (User contrib : newBranch.getContributors()) {
                networkBranch.addContributor(userService.findByUsername(contrib.getUsername())
                        .orElseThrow(() -> new NotFoundBussExc("Contributor with username " + contrib.getUsername() + " not found"))
                );
            }
        }

        if (newBranch.getNetworks() != null && !newBranch.getNetworks().isEmpty()) {
            networkBranch.getNetworks().forEach(networkBranch::removeNetwork);
            for (Network network : newBranch.getNetworks()) {
                networkBranch.addNetwork(networkService.findById(network.getId())
                        .orElseThrow(() -> new NotFoundBussExc("Network with id " + network.getId() + " not found"))
                );
            }
        }

        networkBranch.setUpdateDateTime(LocalDateTime.now());
        return branchRepo.save(networkBranch);
    }


    @Override
    public NetworkBranch delete(long id) throws NotFoundBussExc {
        return branchRepo.findById(id).map(b -> {
            branchRepo.delete(b);
            return b;
        }).orElseThrow(() -> new NotFoundBussExc("Branch with id " + id + " not found"));
    }


    @Override
    public Optional<NetworkBranch> findByName(String name) {
        return branchRepo.findOne(Specification.where(BranchSpec.hasName(name)));
    }

    @Override
    public List<NetworkBranch> getAll() {
        return branchRepo.findAll();
    }


    @Override
    public List<NetworkBranch> getAllForContributor(String username) throws NotFoundBussExc {
        return userService.findByUsername(username).map(contributor ->
                branchRepo.findAll(Specification.where(BranchSpec.hasContributor(contributor)))
        ).orElseThrow(() -> new NotFoundBussExc("User with username " + username + " not found"));
    }

    @Override
    public List<NetworkBranch> getAllForOwner(String username) throws NotFoundBussExc {
        return userService.findByUsername(username).map(owner ->
                branchRepo.findAll(Specification.where(BranchSpec.hasOwner(owner)))
        ).orElseThrow(() -> new NotFoundBussExc("User with username " + username + " not found"));
    }

    @Override
    public List<NetworkBranch> getAllForUser(String username) throws NotFoundBussExc {
        return userService.findByUsername(username).map(user ->
                branchRepo.findAll(Specification.where(BranchSpec.hasOwner(user))
                        .or(Specification.where(BranchSpec.hasContributor(user))))
        ).orElseThrow(() -> new NotFoundBussExc("User with username " + username + " not found"));
    }

    @Override
    public Optional<NetworkBranch> findById(long id) {
        return branchRepo.findById(id);
    }
}
