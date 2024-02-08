package com.adan.security.controller;

import com.adan.security.dto.ManufacturerRequest;
import com.adan.security.dto.ManufacturerResponse;
import com.adan.security.dto.ProductRequest;
import com.adan.security.dto.ProductResponse;
import com.adan.security.entity.Manufacturer;
import com.adan.security.entity.Product;
import com.adan.security.service.ManufacturerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/v2/manufacturers")
@RequiredArgsConstructor
public class ManufacturerController {

    private final ManufacturerService manufacturerService;

    // Create a new manufacturer
    @PostMapping("/create")
    public ResponseEntity<String> createManufacturer(@RequestBody ManufacturerRequest manufacturerRequest) {
        // Create a new Manufacturer object from the request
        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setName(manufacturerRequest.getName());
        manufacturer.setLocation(manufacturerRequest.getLocation());

        // Call the service to create the manufacturer
        Manufacturer createdManufacturer = manufacturerService.createManufacturer(manufacturer);

        // Check if the manufacturer was successfully created
        if (createdManufacturer != null) {
            String message = "Manufacturer has been created successfully.";
            return new ResponseEntity<>(message, HttpStatus.CREATED);
        } else {
            String errorMessage = "Failed to create the manufacturer.";
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get all manufacturers
    @GetMapping("/all")
    public ResponseEntity<?> getAllTheManufacturers() {
        // Retrieve all manufacturer responses from the service
        List<ManufacturerResponse> manufacturerResponses = manufacturerService.getAllManufacturerResponses();

        // Check if there are any manufacturers available
        if (manufacturerResponses.isEmpty()) {
            return ResponseEntity.ok("No manufacturer available.");
        } else {
            return ResponseEntity.ok(manufacturerResponses);
        }
    }

    // Update an existing manufacturer
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateManufacturer(
            @PathVariable int id,
            @RequestBody ManufacturerRequest manufacturerRequest) {
        // Retrieve the existing manufacturer by its ID
        Manufacturer existingManufacturer = manufacturerService.getManufacturerById(id);

        // Check if the manufacturer exists
        if (existingManufacturer == null) {
            return ResponseEntity.notFound().build();
        }

        // Update the manufacturer with the new information from the request
        existingManufacturer.setName(manufacturerRequest.getName());
        existingManufacturer.setLocation(manufacturerRequest.getLocation());
        manufacturerService.updateManufacturer(existingManufacturer);
        return ResponseEntity.ok("Manufacturer updated successfully.");
    }

    // Delete an existing manufacturer
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteManufacturer(@PathVariable int id) {
        // Retrieve the existing manufacturer by its ID
        Manufacturer existingManufacturer = manufacturerService.getManufacturerById(id);

        // Check if the manufacturer exists
        if (existingManufacturer == null) {
            return ResponseEntity.notFound().build();
        }

        // Call the service to delete the manufacturer
        manufacturerService.deleteManufacturer(id);
        return ResponseEntity.ok("Manufacturer with ID " + id + " deleted successfully.");
    }

    // Get a manufacturer by its ID
    @GetMapping("/get/{id}")
    public ResponseEntity<?> getManufacturerById(@PathVariable int id) {
        // Retrieve the existing manufacturer by its ID
        Manufacturer existingManufacturer = manufacturerService.getManufacturerById(id);

        // Check if the manufacturer exists
        if (existingManufacturer == null) {
            String errorMessage = "Manufacturer with ID " + id + " is not available.";
            return ResponseEntity.ok(errorMessage);
        }
        // Convert the manufacturer to a response object and return it
        ManufacturerResponse manufacturerResponse = new ManufacturerResponse();
        manufacturerResponse.setId(existingManufacturer.getId());
        manufacturerResponse.setName(existingManufacturer.getName());
        manufacturerResponse.setLocation(existingManufacturer.getLocation());
        return ResponseEntity.ok(manufacturerResponse);
    }
}
