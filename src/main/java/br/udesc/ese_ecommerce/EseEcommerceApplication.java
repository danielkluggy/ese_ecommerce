package br.udesc.ese_ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class EseEcommerceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EseEcommerceApplication.class, args);
	}

}
