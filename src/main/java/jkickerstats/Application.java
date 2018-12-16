package jkickerstats;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;

@SpringBootApplication
public class Application {


    @Autowired
    MongoDbFactory mongoDbFactory;

    @Autowired
    MongoMappingContext mongoMappingContext;

    @Bean
    public MappingMongoConverter mappingMongoConverter() {

        DbRefResolver dbRefResolver = new DefaultDbRefResolver(mongoDbFactory);
        MappingMongoConverter converter = new MappingMongoConverter(dbRefResolver, mongoMappingContext);
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));

        return converter;
    }

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