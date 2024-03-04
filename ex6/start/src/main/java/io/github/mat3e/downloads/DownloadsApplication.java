package io.github.mat3e.downloads;

import io.github.mat3e.downloads.limiting.LimitingFacade;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.util.Arrays;

@SpringBootApplication
public class DownloadsApplication {

    @Bean
    Clock clock() {
        return Clock.systemUTC();
    }

    public static void main(String[] args) {
        SpringApplication.run(DownloadsApplication.class, args);
    }
}

@Component
class ContextChecker {
    @Autowired
    private ApplicationContext context;

    @Autowired
    private LimitingFacade limiting;

    @PostConstruct
    void printContext() {
        System.out.println("All beans: " + context.getBeanDefinitionCount());
        String[] beanNames = context.getBeanDefinitionNames();
        for (int i = 0; i < beanNames.length; i++) {
            System.out.println("\t" + (i < 9 ? " " : "") + (i + 1) + ". " + beanNames[i]);
        }
        System.out.println("\nHow about limiting?\n");
        System.out.println(Arrays.toString(context.getBeanNamesForType(LimitingFacade.class)));
        System.out.println(context.isSingleton("limitingFacade"));
        System.out.println(context.getBean("limitingFacade") == limiting);
    }
}
