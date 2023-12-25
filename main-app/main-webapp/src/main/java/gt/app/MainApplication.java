package gt.app;

import brave.sampler.Sampler;
import gt.app.config.AppProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

@SpringBootApplication
@Slf4j
@EnableConfigurationProperties(AppProperties.class)
@EnableCaching
@EnableScheduling
public class MainApplication {

    public static void main(String[] args) throws UnknownHostException {

        SpringApplication app = new SpringApplication(MainApplication.class);
        Environment env = app.run(args).getEnvironment();

        log.info("""
                Access URLs:
                ----------------------------------------------------------
                \tLocal: \t\t\thttp://localhost:{}
                \tExternal: \t\thttp://{}:{}
                \tEnvironment: \t{}\s
                ----------------------------------------------------------""",
            env.getProperty("server.port"),
            InetAddress.getLocalHost().getHostAddress(),
            env.getProperty("server.port"),
            Arrays.toString(env.getActiveProfiles())
        );
    }
    //Force sample
    @Bean
    public Sampler defaultSampler() {
        return Sampler.ALWAYS_SAMPLE;
    }
}
