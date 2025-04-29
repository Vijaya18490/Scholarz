package com.ddfschool.Scholarz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@EnableJpaRepositories(basePackages = "com.ddfschool.Scholarz.repository")
@EntityScan(basePackages = "com.ddfschool.Scholarz.model")
@SpringBootApplication(scanBasePackages = "com.ddfschool.Scholarz")
public class ScholarzApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScholarzApplication.class, args);
	}

}
