package en.ubb.networkconfiguration.business.service.impl;

import en.ubb.networkconfiguration.business.service.BranchService;
import en.ubb.networkconfiguration.persistence.dao.BranchRepo;
import en.ubb.networkconfiguration.persistence.dao.specification.BranchSpec;
import en.ubb.networkconfiguration.persistence.domain.authentication.User;
import en.ubb.networkconfiguration.persistence.domain.network.NetworkBranch;
import en.ubb.networkconfiguration.persistence.domain.network.runtime.Network;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BranchServiceImpl implements BranchService {

    private final BranchRepo branchRepo;

    @Autowired
    public BranchServiceImpl(BranchRepo branchRepo) {
        this.branchRepo = branchRepo;
    }

    @Override
    public NetworkBranch create(NetworkBranch newBranch) {
        return branchRepo.save(newBranch);
    }

    @Override
    public NetworkBranch create(NetworkBranch newBranch, NetworkBranch origin) {
        origin.getNetworks().forEach(network -> {
            newBranch.addNetwork(new Network(network));
        });
        return branchRepo.save(newBranch);
    }

    @Override
    public NetworkBranch pull(NetworkBranch to, NetworkBranch from) {
        from.getNetworks().forEach(network -> {
            to.getNetworks().removeIf(next -> network.getId().equals(next.getId()));
            to.addNetwork(new Network(network));
        });
        return branchRepo.save(to);
    }

    @Override
    public boolean remove(NetworkBranch branch) {
        return branchRepo.findById(branch.getId()).map(b -> {
            branchRepo.delete(b);
            return true;
        }).orElse(false);
    }

    @Override
    public List<NetworkBranch> getAll() {
        return branchRepo.findAll();
    }

    @Override
    public List<NetworkBranch> getAllForOwner(User owner){
        return branchRepo.findAll(Specification.where(BranchSpec.hasOwner(owner)));
    }

    @Override
    public List<NetworkBranch> getAllForUser(User user) {
        return branchRepo.findAll(Specification.where(BranchSpec.hasContributor(user)));
    }

    @Override
    public Optional<NetworkBranch> findById(Long id) {
        return branchRepo.findById(id);
    }
}
