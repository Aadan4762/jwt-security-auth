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



    @PostMapping("/create")
    public ResponseEntity<String> createManufacturer(@RequestBody ManufacturerRequest manufacturerRequest) {

        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setName(manufacturerRequest.getName());
        manufacturer.setLocation(manufacturerRequest.getLocation());

        Manufacturer createdManufacturer = manufacturerService.createManufacturer(manufacturer);
        if (createdManufacturer != null) {
            String message = "Manufacturer has been created successfully.";
            return new ResponseEntity<>(message, HttpStatus.CREATED);
        } else {
            String errorMessage = "Failed to create the manufacturer.";
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/all")
    public ResponseEntity<?> getAllTheManufacturers() {
        List<ManufacturerResponse> manufacturerResponses = manufacturerService.getAllManufacturerResponses();

        if (manufacturerResponses.isEmpty()) {
            return ResponseEntity.ok("No manufacturer available.");
        } else {
            return ResponseEntity.ok(manufacturerResponses);
        }
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateManufacturer(
            @PathVariable int id,
            @RequestBody ManufacturerRequest manufacturerRequest) {
        Manufacturer existingManufacturer = manufacturerService.getManufacturerById(id);

        if (existingManufacturer == null) {
            return ResponseEntity.notFound().build();
        }

        existingManufacturer.setName(manufacturerRequest.getName());
        existingManufacturer.setLocation(manufacturerRequest.getLocation());
        manufacturerService.updateManufacturer(existingManufacturer);
        return ResponseEntity.ok("Manufacturer updated successfully.");
    }



    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteManufacturer(@PathVariable int id) {
        Manufacturer existingManufacturer = manufacturerService.getManufacturerById(id);

        if (existingManufacturer == null) {
            return ResponseEntity.notFound().build();
        }

        manufacturerService.deleteManufacturer(id);
        return ResponseEntity.ok("Manufacturer with ID " + id + " deleted successfully.");
    }



    @GetMapping("/get/{id}")
    public ResponseEntity<?> getManufacturerById(@PathVariable int id) {
        Manufacturer existingManufacturer = manufacturerService.getManufacturerById(id);

        if (existingManufacturer == null) {
            String errorMessage = "Manufacturer with ID " + id + " is not available.";
            return ResponseEntity.ok(errorMessage);
        }
        ManufacturerResponse manufacturerResponse = new ManufacturerResponse();
        manufacturerResponse.setId(existingManufacturer.getId());
        manufacturerResponse.setName(existingManufacturer.getName());
        manufacturerResponse.setLocation(existingManufacturer.getLocation());
        return ResponseEntity.ok(manufacturerResponse);
    }
}
