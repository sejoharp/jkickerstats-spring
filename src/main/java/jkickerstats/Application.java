package jkickerstats;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

//		ApplicationContext ctx = SpringApplication.run(Application.class, args);
//		printManagedBeans(ctx);
//		printClasspath();

        System.out.println("");
        System.out.println("APPLICATION STARTED.");
    }

    protected static void printManagedBeans(ApplicationContext ctx) {
        System.out.println("---------------------");
        System.out.println("Let's inspect the beans provided by Spring Boot:");
        String[] beanNames = ctx.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        for (String beanName : beanNames) {
            System.out.println(beanName);
        }
    }

    public static void printClasspath() {
        System.out.println("---------------------");
        ClassLoader cl = ClassLoader.getSystemClassLoader();

        URL[] urls = ((URLClassLoader) cl).getURLs();

        for (URL url : urls) {
            System.out.println(url.getFile());
        }
        System.out.println("---------------------");
    }
}