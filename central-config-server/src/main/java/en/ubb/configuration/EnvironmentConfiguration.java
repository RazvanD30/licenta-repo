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

            String keyStorePath = System.getProperty("key-store-name");
            String keyStorePassword = System.getProperty("key-store-password");

            Properties properties = new Properties();
            KeystoreConfiguration.makeNewKeystoreEntry("message", "I GOT THE MESSAGE", keyStorePath, keyStorePassword);
            properties.setProperty("message", KeystoreConfiguration.getValueFromKeystore("message", keyStorePath, keyStorePassword));

            PropertySource<?> propertySource = new PropertiesPropertySource("custom", properties);
            environment.getPropertySources().addLast(propertySource);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
