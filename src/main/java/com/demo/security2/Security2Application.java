package com.demo.security2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class,
		HibernateJpaAutoConfiguration.class})

public class Security2Application {

	public static void main(String[] args) {
		SpringApplication.run(Security2Application.class, args);
	}

}
