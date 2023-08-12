package com.example.inventory;

import com.example.inventory.model.Inventory;
import com.example.inventory.repository.InventoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.Set;

@SpringBootApplication
public class InventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(InventoryRepository inventoryRepository) {
		return args -> {
			inventoryRepository.saveAll(
					Set.of(Inventory.builder().sku("iphone13").barcode("1111").qty(100).createdDate(LocalDateTime.now()).build(),
							Inventory.builder().sku("iphone14").barcode("2222").qty(0).createdDate(LocalDateTime.now()).build()));
		};
	}
}
