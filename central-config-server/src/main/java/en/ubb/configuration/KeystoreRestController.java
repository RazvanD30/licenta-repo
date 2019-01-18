package en.ubb.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class KeystoreRestController {


    @Value("${keystore.name}")
    private String keyStorePath;

    @Value("${keystore.password}")
    private String keyStorePassword;

    @Value("${github.username}")
    private String githubUsername;

    @RequestMapping("/keystore")
    public String getKeystoreDetails() {
        return keyStorePath + " " + keyStorePassword + " " + githubUsername;
    }

}