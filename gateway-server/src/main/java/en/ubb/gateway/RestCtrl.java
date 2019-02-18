package en.ubb.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("api/hello/client")
public class RestCtrl {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping
    public String hello(){
        String url = "http://microservice/api/hello/server";
        return restTemplate.getForObject(url, String.class);
    }




}
