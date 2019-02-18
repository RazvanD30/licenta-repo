package en.ubb.statistics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class StatisticsApp {
    public static void main(String[] args) {
        SpringApplication.run(StatisticsApp.class, args);
    }
}

