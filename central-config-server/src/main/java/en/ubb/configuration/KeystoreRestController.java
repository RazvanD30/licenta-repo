package en.ubb.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class KeystoreRestController {


    @Value("${key-store-name}")
    private String keyStorePath;

    @Value("${key-store-password}")
    private String keyStorePassword;

    @Value("${message:abc}")
    private String message;

    @RequestMapping("/message")
    public String getMessage() {
        try {
            KeystoreConfiguration.makeNewKeystoreEntry("message", "I GOT THE MESSAGE", keyStorePath, keyStorePassword);
            message = KeystoreConfiguration.getValueFromKeystore("message", keyStorePath, keyStorePassword);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return message;
    }

    @RequestMapping("/keystore")
    public String getKeystore() {
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

@RestController
class OtherController {

    @Value("${message:abc}")
    private String message;

    @RequestMapping("/message2")
    public String getMessage2() {
        return message;
    }
}