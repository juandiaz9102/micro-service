package com.nttdata.som.serviceorder;

import io.swagger.annotations.Api;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import springfox.documentation.builders.RequestHandlerSelectors;

@SpringBootApplication
@ComponentScans({ @ComponentScan(basePackages = { "com.nttdata.exceptions", "com.nttdata.interceptor", "com.nttdata.config", "com.nttdata.fulfillment.catalogdriver" }) })
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
