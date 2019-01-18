package en.ubb.configuration;

import com.ulisesbocchio.jasyptspringboot.annotation.EncryptablePropertySource;
import org.springframework.context.annotation.Configuration;

@Configuration
@EncryptablePropertySource("encrypted.properties")
public class JasyptConfiguration {
}
