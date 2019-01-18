package en.ubb.configuration.examples;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PropertyService {

    @Value("${encrypted.github.username}")
    private String githubUsername;

    @Value("${encrypted.github.password}")
    private String githubPassword;

    public String getGithubUsername(){
        return githubUsername;
    }

    public String getGithubPassword(){
        return githubPassword;
    }
}
