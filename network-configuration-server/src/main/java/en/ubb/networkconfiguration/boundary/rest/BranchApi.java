package en.ubb.networkconfiguration.boundary.rest;

import en.ubb.networkconfiguration.boundary.dto.authentication.PublicUserDto;
import en.ubb.networkconfiguration.boundary.dto.branch.BranchDto;
import en.ubb.networkconfiguration.boundary.util.DtoMapper;
import en.ubb.networkconfiguration.boundary.validation.exception.ForbiddenAccessException;
import en.ubb.networkconfiguration.boundary.validation.exception.NotFoundException;
import en.ubb.networkconfiguration.business.service.BranchService;
import en.ubb.networkconfiguration.business.service.UserService;
import en.ubb.networkconfiguration.business.validation.exception.ForbiddenAccessBussExc;
import en.ubb.networkconfiguration.business.validation.exception.NotFoundBussExc;
import en.ubb.networkconfiguration.persistence.domain.authentication.User;
import en.ubb.networkconfiguration.persistence.domain.branch.NetworkBranch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("branch-management")
public class BranchApi {

    private final BranchService branchService;

    private final UserService userService;

    @Autowired
    public BranchApi(BranchService branchService, UserService userService) {
        this.branchService = branchService;
        this.userService = userService;
    }

    @PostMapping
    public BranchDto create(@RequestBody BranchDto dto) throws NotFoundException {
        if(dto.getSourceId() == null) {
            try {
                NetworkBranch networkBranch = DtoMapper.fromDto(dto);
                networkBranch.setCurrentUsers(new ArrayList<>());
                return DtoMapper.toDto(this.branchService.create(networkBranch,null));
            } catch (NotFoundBussExc notFoundBussExc) {
                throw new NotFoundException(notFoundBussExc);
            }
        }

        NetworkBranch source = this.branchService.findById(dto.getSourceId())
                .orElseThrow(() -> new NotFoundException("Source branch with id " + dto.getSourceId() + " does not exist"));

        try {
            return DtoMapper.toDto(this.branchService.create(DtoMapper.fromDto(dto),source));
        } catch (NotFoundBussExc notFoundBussExc) {
            throw new NotFoundException(notFoundBussExc);
        }
    }

    @PostMapping("/workingBranch/{branchName}")
    public void assign(@PathVariable String branchName, @RequestBody String username) throws NotFoundException, ForbiddenAccessException {
        try {
            this.branchService.assign(branchName,username);
        } catch (NotFoundBussExc notFoundBussExc) {
            throw new NotFoundException(notFoundBussExc);
        } catch (ForbiddenAccessBussExc forbiddenAccessBussExc) {
            throw new ForbiddenAccessException(forbiddenAccessBussExc);
        }
    }

    @GetMapping("/workingBranch/{username}")
    public BranchDto getCurrentBranchForUsername(@PathVariable String username) throws NotFoundException {
        User user = this.userService.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found"));

        return user.getCurrentBranch() != null ? DtoMapper.toDto(user.getCurrentBranch()) : null;
    }

    @PutMapping
    public BranchDto update(@RequestBody BranchDto dto) throws NotFoundException {
        try {
            return DtoMapper.toDto(this.branchService.update(dto.getId(),DtoMapper.fromDto(dto)));
        } catch (NotFoundBussExc notFoundBussExc) {
            throw new NotFoundException(notFoundBussExc);
        }
    }

    @DeleteMapping(value = "/{id}", produces = "application/json")
    public BranchDto deleteById(@NotNull @PathVariable Long id) throws NotFoundException {
        try {
            BranchDto result = DtoMapper.toDto(this.branchService.findById(id)
                    .orElseThrow(() -> new NotFoundException("Branch with it " + id + "not found")));

           this.branchService.delete(id);
           return result;
        } catch (NotFoundBussExc notFoundBussExc) {
            throw new NotFoundException(notFoundBussExc);
        }
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public BranchDto getById(@NotNull @PathVariable Long id) throws NotFoundException {
        return this.branchService.findById(id).map(DtoMapper::toDto)
                .orElseThrow(() -> new NotFoundException("Branch not found"));
    }

    @GetMapping(value = "/withName/{name}", produces = "application/json")
    public BranchDto getByName(@NotNull @PathVariable String name) throws NotFoundException {
        return this.branchService.findByName(name).map(DtoMapper::toDto)
                .orElseThrow(() -> new NotFoundException("Branch not found"));
    }

    @GetMapping
    public List<BranchDto> getAll() {
        return this.branchService.getAll().stream()
                .map(DtoMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/withUser/{username}")
    public List<BranchDto> getAllForUser(@PathVariable String username) throws NotFoundException {
        try {
            return this.branchService.getAllForUser(username).stream()
                    .map(DtoMapper::toDto)
                    .collect(Collectors.toList());
        } catch (NotFoundBussExc notFoundBussExc) {
            throw new NotFoundException(notFoundBussExc);
        }
    }

    @GetMapping("/withOwner/{username}")
    public List<BranchDto> getAllForOwner(@PathVariable String username) throws NotFoundException {
        try {
            return this.branchService.getAllForOwner(username).stream()
                    .map(DtoMapper::toDto)
                    .collect(Collectors.toList());
        } catch (NotFoundBussExc notFoundBussExc) {
            throw new NotFoundException(notFoundBussExc);
        }
    }

    @GetMapping("/withContributor/{username}")
    public List<BranchDto> getAllForContributor(@PathVariable String username) throws NotFoundException {
        try {
            return this.branchService.getAllForContributor(username).stream()
                    .map(DtoMapper::toDto)
                    .collect(Collectors.toList());
        } catch (NotFoundBussExc notFoundBussExc) {
            throw new NotFoundException(notFoundBussExc);
        }
    }
}
