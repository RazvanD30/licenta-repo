package en.ubb.networkconfiguration.business.service.impl;

import en.ubb.networkconfiguration.business.service.BranchService;
import en.ubb.networkconfiguration.business.service.NetworkService;
import en.ubb.networkconfiguration.business.service.UserService;
import en.ubb.networkconfiguration.business.validation.exception.NotFoundBussExc;
import en.ubb.networkconfiguration.persistence.dao.BranchRepo;
import en.ubb.networkconfiguration.persistence.dao.specification.BranchSpec;
import en.ubb.networkconfiguration.persistence.domain.authentication.User;
import en.ubb.networkconfiguration.persistence.domain.branch.NetworkBranch;
import en.ubb.networkconfiguration.persistence.domain.network.runtime.Network;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Iterator;
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
    public NetworkBranch create(NetworkBranch newBranch, NetworkBranch origin) throws NotFoundBussExc {

        if(origin != null) {
            origin.getNetworks().forEach(network -> newBranch.addNetwork(new Network(network)));
            newBranch.setSourceId(origin.getSourceId());
        }

        newBranch.setOwner(this.userService.findByUsername(newBranch.getOwner().getUsername())
                .orElseThrow(() -> new NotFoundBussExc("Owner not registered")));

        newBranch.setCreateDateTime(LocalDateTime.now());
        newBranch.setUpdateDateTime(LocalDateTime.now());

        return branchRepo.save(newBranch);
    }

    @Override
    public NetworkBranch pull(long toId, long fromId) throws NotFoundBussExc {
        //TODO to.setUpdateDateTime(LocalDateTime.now());


        NetworkBranch to = branchRepo.findById(toId)
                .orElseThrow(() -> new NotFoundBussExc("Destination network with id " + toId + " not found"));
        NetworkBranch from = branchRepo.findById(fromId)
                .orElseThrow(() -> new NotFoundBussExc("Origin network with id " + fromId + " not found"));


        Iterator<Network> iterator = to.getNetworks().iterator();
        while (iterator.hasNext()){
            Network next = iterator.next();
            if(next.getOriginId() != null){ // if it's not null it might be updated by origin
                from.getNetworks().stream()
                        .filter(n -> n.getOriginId() != null && n.getOriginId().equals(next.getOriginId())) // IF THEY BOTH COME FROM THE SAME NETWORK
                        .findFirst()
                        .ifPresent(conflict -> {
                            if(next.getUpdateDateTime().isBefore(conflict.getUpdateDateTime())){ // IF SOURCE IF OLDER THAN ORIGIN
                                iterator.remove();
                                to.addNetwork(conflict);
                            }
                        });
            } else {
                from.getNetworks().stream()
                        .filter(n -> n.getOriginId() != null && n.getOriginId().equals(next.getId()))
                        .findFirst()
                        .ifPresent(conflict -> {
                            if(next.getUpdateDateTime().isBefore(conflict.getUpdateDateTime())){ // IF SOURCE IF OLDER THAN ORIGIN
                                iterator.remove();
                                to.addNetwork(conflict);
                            }
                        });

            }


            if(from.getNetworks().stream().anyMatch(n -> n.getId().equals(next.getId()))){

            }

        }




        from.getNetworks().forEach(network -> {
            Iterator<Network> iterator = to.getNetworks().iterator();
            while(iterator.hasNext()){
                Network next = iterator.next();
                if(network.getId())
            }

            to.getNetworks().removeIf(next -> network.getId().equals(next.getId()));
            to.addNetwork(new Network(network));
        });
        return branchRepo.save(to);
    }

    @Override
    public NetworkBranch update(long id, @NotNull NetworkBranch newBranch) throws NotFoundBussExc {


        NetworkBranch networkBranch = this.branchRepo.findById(id)
                .orElseThrow(() -> new NotFoundBussExc("Branch with id " + id + " not found"));

        if(newBranch.getName() != null){
            networkBranch.setName(newBranch.getName());
        }

        if(newBranch.getType() != null){
            networkBranch.setType(newBranch.getType());
        }

        if(newBranch.getOwner() != null) {
            User owner = userService.findByUsername(newBranch.getOwner().getUsername())
                    .orElseThrow(() -> new NotFoundBussExc("Owner with username " + newBranch.getOwner().getUsername() + " not found"));
            networkBranch.setOwner(owner);
        }

        if(newBranch.getContributors() != null && !newBranch.getContributors().isEmpty()) {
            networkBranch.getContributors().forEach(networkBranch::removeContributor);
            for (User contrib : newBranch.getContributors()) {
                networkBranch.addContributor(userService.findByUsername(contrib.getUsername())
                        .orElseThrow(() -> new NotFoundBussExc("Contributor with username " + contrib.getUsername() + " not found"))
                );
            }
        }

        if(newBranch.getNetworks() != null && !newBranch.getNetworks().isEmpty()){
            networkBranch.getNetworks().forEach(networkBranch::removeNetwork);
            for(Network network: newBranch.getNetworks()){
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
