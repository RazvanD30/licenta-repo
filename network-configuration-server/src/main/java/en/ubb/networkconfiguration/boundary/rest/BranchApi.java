package en.ubb.networkconfiguration.boundary.rest;

import en.ubb.networkconfiguration.boundary.dto.branch.BranchDto;
import en.ubb.networkconfiguration.boundary.util.DtoMapper;
import en.ubb.networkconfiguration.boundary.validation.exception.NotFoundException;
import en.ubb.networkconfiguration.business.service.BranchService;
import en.ubb.networkconfiguration.persistence.domain.network.NetworkBranch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("branch-management")
public class BranchApi {

    private final BranchService branchService;

    @Autowired
    public BranchApi(BranchService branchService) {
        this.branchService = branchService;
    }

    @PostMapping("create")
    public BranchDto createBranch(@RequestBody BranchDto dto) throws NotFoundException {
        if(dto.getSourceId() == null) {
            return DtoMapper.toDto(this.branchService.create(DtoMapper.fromDto(dto)));
        }
        return this.branchService.findById(dto.getSourceId()).map(source -> {
            return DtoMapper.toDto(this.branchService.create(DtoMapper.fromDto(dto),source));
        }).orElseThrow(() -> new NotFoundException("Source branch with id " + dto.getSourceId() + " does not exist."));
    }

    @GetMapping
    public List<BranchDto> getAll() {
        return this.branchService.getAll().stream()
                .map(DtoMapper::toDto)
                .collect(Collectors.toList());
    }

}
