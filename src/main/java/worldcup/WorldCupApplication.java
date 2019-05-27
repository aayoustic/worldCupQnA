package worldcup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created on 5/25/2019 12:15 PM.
 *
 * @author Aayush Shrivastava
 */
@SpringBootApplication
@ComponentScan(basePackageClasses = WorldCupApplication.class)
public class WorldCupApplication {
    public static void main(String[] args) {
        SpringApplication.run(WorldCupApplication.class, args);
    }
}
