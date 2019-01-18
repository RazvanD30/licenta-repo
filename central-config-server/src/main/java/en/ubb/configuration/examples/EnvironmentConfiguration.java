package en.ubb.configuration.examples;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;

import java.util.Properties;

public class EnvironmentConfiguration implements EnvironmentPostProcessor {




    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {


        /*
        try {


            String encryptorSalt = System.getProperty("encryptor.salt");
            String encryptorPassword = System.getProperty("encryptor.password");
            String githubEncryptedUsername = System.getProperty("encrypted.github.username");
            String githubEncryptedPassword = System.getProperty("encrypted.github.password");

            TextEncryptor encryptor = Encryptors.text(encryptorPassword,encryptorSalt);

            String githubUsername = encryptor.decrypt(githubEncryptedUsername);
            String githubPassword = encryptor.decrypt(githubEncryptedPassword);


            Properties properties = new Properties();
            //KeystoreConfiguration.makeNewKeystoreEntry("github.username", "RazvanD30", keyStorePath, keyStorePassword);
            //KeystoreConfiguration.makeNewKeystoreEntry("github.password", "r7g8c8cd", keyStorePath, keyStorePassword);
            //properties.setProperty("github.username", KeystoreConfiguration.getValueFromKeystore("github.username", keyStorePath, keyStorePassword));
            //properties.setProperty("github.password", KeystoreConfiguration.getValueFromKeystore("github.password", keyStorePath, keyStorePassword));
            properties.setProperty("github.username",githubUsername);
            properties.setProperty("github.password",githubPassword);
            PropertySource<?> propertySource = new PropertiesPropertySource("CONFIDENTIAL", properties);
            environment.getPropertySources().addLast(propertySource);

        } catch (Exception e) {
            e.printStackTrace();
        }
        */
    }

}
