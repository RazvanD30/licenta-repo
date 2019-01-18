package en.ubb.configuration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;

import java.util.Properties;

public class EnvironmentConfiguration implements EnvironmentPostProcessor {




    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {

        try {

            String keyStorePath = System.getProperty("keystore.name");
            String keyStorePassword = System.getProperty("keystore.password");
            Properties properties = new Properties();
            KeystoreConfiguration.makeNewKeystoreEntry("github.username", "RazvanD30", keyStorePath, keyStorePassword);
            KeystoreConfiguration.makeNewKeystoreEntry("github.password", "r7g8c8cd", keyStorePath, keyStorePassword);
            properties.setProperty("github.username", KeystoreConfiguration.getValueFromKeystore("github.username", keyStorePath, keyStorePassword));
            properties.setProperty("github.password", KeystoreConfiguration.getValueFromKeystore("github.password", keyStorePath, keyStorePassword));
            PropertySource<?> propertySource = new PropertiesPropertySource("CONFIDENTIAL", properties);
            environment.getPropertySources().addLast(propertySource);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
