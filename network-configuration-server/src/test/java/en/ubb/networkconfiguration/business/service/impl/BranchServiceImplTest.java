package en.ubb.networkconfiguration.business.service.impl;

import com.google.common.collect.Lists;
import en.ubb.networkconfiguration.business.service.NetworkService;
import en.ubb.networkconfiguration.business.service.UserService;
import en.ubb.networkconfiguration.persistence.dao.BranchRepo;
import en.ubb.networkconfiguration.persistence.domain.branch.NetworkBranch;
import en.ubb.networkconfiguration.persistence.domain.network.runtime.Network;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class BranchServiceImplTest {


    @InjectMocks
    private BranchServiceImpl branchService;

    @Mock
    private BranchRepo branchRepo;

    @Mock
    private NetworkService networkService;

    @Mock
    private UserService userService;

    @Test
    public void solveConflicts() {

        Network _1_original = Network.builder()
                .id(0L)
                .updateDateTime(LocalDateTime.parse("2006-12-03T10:15:30"))
                .build();

        Network _1_fork = Network.builder()
                .id(1L)
                .originId(0L)
                .updateDateTime(LocalDateTime.parse("2007-12-03T10:15:30"))
                .build();

        Network _2_original = Network.builder()
                .id(2L)
                .updateDateTime(LocalDateTime.parse("2006-12-03T10:15:30"))
                .build();

        Network _2_fork = Network.builder()
                .id(3L)
                .originId(2L)
                .updateDateTime(LocalDateTime.parse("2005-12-03T10:15:30"))
                .build();

        Network _3_extra = Network.builder()
                .id(4L)
                .updateDateTime(LocalDateTime.parse("2000-12-03T10:15:30"))
                .build();

        Network _4_present = Network.builder()
                .id(5L)
                .updateDateTime(LocalDateTime.parse("2000-12-03T10:15:30"))
                .build();

        NetworkBranch destination = NetworkBranch.builder()
                .id(0L)
                .networks(Lists.newArrayList(_1_original, _2_original, _4_present))
                .build();

        NetworkBranch origin = NetworkBranch.builder()
                .id(1L)
                .networks(Lists.newArrayList(_1_fork, _2_fork, _3_extra))
                .build();

        this.branchService.solveConflicts(destination, origin);


        assertEquals(4, destination.getNetworks().size());
        assertTrue(destination.getNetworks().stream().anyMatch(n -> n.getId().equals(1L)));
        assertTrue(destination.getNetworks().stream().anyMatch(n -> n.getId().equals(2L)));
        assertTrue(destination.getNetworks().stream().anyMatch(n -> n.getId().equals(4L)));
        assertTrue(destination.getNetworks().stream().anyMatch(n -> n.getId().equals(5L)));


    }
}