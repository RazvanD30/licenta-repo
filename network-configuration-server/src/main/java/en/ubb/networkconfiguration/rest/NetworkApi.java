package en.ubb.networkconfiguration.rest;


import en.ubb.networkconfiguration.domain.NetworkConfig;
import en.ubb.networkconfiguration.repo.NetworkConfigRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NetworkApi {

    @Autowired
    private NetworkConfigRepo networkConfigRepo;

    public void  a(){

    }

}
