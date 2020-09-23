package com.upgrade;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import springfox.documentation.swagger2.annotations.EnableSwagger2;
/**
 * @author Rimte Rocher
 */
@SpringBootApplication
@EnableTransactionManagement
@EnableSwagger2
public class UpgradeApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(UpgradeApiApplication.class, args);
	}

}
