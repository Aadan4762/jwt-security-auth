package com.adan.security.service;

import com.adan.security.dto.ManufacturerResponse;
import com.adan.security.entity.Manufacturer;
import com.adan.security.entity.Product;
import com.adan.security.repository.ManufacturerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
@RequiredArgsConstructor
public class ManufacturerService implements ManufacturerServiceInterface {

    // Dependency injection of ManufacturerRepository
    private final ManufacturerRepository manufacturerRepository;

    // Method to create a new Manufacturer
    public Manufacturer createManufacturer(Manufacturer manufacturer){
        return manufacturerRepository.save(manufacturer);
    }

    // Method to retrieve all Manufacturer responses
    public List<ManufacturerResponse> getAllManufacturerResponses() {
        List<Manufacturer> manufacturers = manufacturerRepository.findAll();
        List<ManufacturerResponse> manufacturerResponses = new ArrayList<>();

        // Convert Manufacturer entities to ManufacturerResponse DTOs
        for (Manufacturer manufacturer : manufacturers) {
            ManufacturerResponse manufacturerResponse = new ManufacturerResponse();
            manufacturerResponse.setId(manufacturer.getId());
            manufacturerResponse.setName(manufacturer.getName());
            manufacturerResponse.setLocation(manufacturer.getLocation());
            manufacturerResponses.add(manufacturerResponse);
        }

        return manufacturerResponses;
    }

    // Method to retrieve a Manufacturer by its ID
    public Manufacturer getManufacturerById(int id) {
        return manufacturerRepository.findById(id).orElse(null);
    }

    // Method to update an existing Manufacturer
    public Manufacturer updateManufacturer(Manufacturer updatedManufacturer) {
        // Check if the Manufacturer with the given ID exists
        Manufacturer existingManufacturer = manufacturerRepository.findById(updatedManufacturer.getId()).orElse(null);

        // If the Manufacturer exists, update its properties and save it
        if (existingManufacturer != null) {
            existingManufacturer.setName(updatedManufacturer.getName());
            existingManufacturer.setLocation(updatedManufacturer.getLocation());
            return manufacturerRepository.save(existingManufacturer);
        }
        return null; // Return null if Manufacturer does not exist
    }

    // Method to delete a Manufacturer by its ID
    public void deleteManufacturer(int id) {
        // Check if the Manufacturer with the given ID exists
        Manufacturer existingManufacturer = manufacturerRepository.findById(id).orElse(null);

        // If the Manufacturer exists, delete it
        if (existingManufacturer != null) {
            manufacturerRepository.delete(existingManufacturer);
        }
        // You can choose to do nothing if the Manufacturer does not exist or throw an exception.
    }
}
