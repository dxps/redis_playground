package org.devisions.sb3redisom;

import com.redis.om.spring.annotations.EnableRedisDocumentRepositories;
import com.redis.om.spring.annotations.EnableRedisEnhancedRepositories;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRedisDocumentRepositories(basePackages = "org.devisions.sb3redisom.repos.doc*")
@EnableRedisEnhancedRepositories(basePackages = "org.devisions.sb3redisom.repos.hash*")
public class Sb3RedisomApp {

    public static void main(String[] args) {
        SpringApplication.run(Sb3RedisomApp.class, args);
    }

}
