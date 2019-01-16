package en.ubb.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class KeystoreRestController {


    @Value("${key-store-path}")
    private String keyStorePath;

    @Value("${key-store-password}")
    private String keyStorePassword;

    @RequestMapping("/message")
    public String getMessage() {
        String entry = "";
        try {
            KeystoreConfiguration.makeNewKeystoreEntry("test", "abc", keyStorePath, keyStorePassword);
            entry = KeystoreConfiguration.getValueFromKeystore("test", keyStorePath, keyStorePassword);
            System.out.println(entry);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return entry;
    }

}