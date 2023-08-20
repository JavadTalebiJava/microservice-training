package com.example.inventory;

import com.example.inventory.model.Inventory;
import com.example.inventory.repository.InventoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.Set;

@EnableDiscoveryClient
@SpringBootApplication
public class InventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(InventoryRepository inventoryRepository) {
		return args -> {
			inventoryRepository.saveAll(
					Set.of(Inventory.builder().sku("1111").name("iphone13").qty(100).createdDate(LocalDateTime.now()).build(),
							Inventory.builder().sku("2222").name("iphone14").qty(1).createdDate(LocalDateTime.now()).build()));
		};
	}
}
