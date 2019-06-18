package en.ubb.configuration.examples;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class KeystoreRestController {

    private final PropertyService propertyService;

    @Autowired
    public KeystoreRestController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }


    @RequestMapping("/get")
    public String getKeystoreDetails() {
        return propertyService.getGithubUsername();
    }

}