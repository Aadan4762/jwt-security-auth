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
public class ManufacturerService {

    private final ManufacturerRepository manufacturerRepository;
    public Manufacturer createManufacturer(Manufacturer manufacturer){
        return manufacturerRepository.save(manufacturer);
    }

    public List<ManufacturerResponse> getAllManufacturerResponses() {
        List<Manufacturer> manufacturers = manufacturerRepository.findAll();
        List<ManufacturerResponse> manufacturerResponses = new ArrayList<>();

        for (Manufacturer manufacturer : manufacturers) {
            ManufacturerResponse manufacturerResponse = new ManufacturerResponse();
            manufacturerResponse.setId(manufacturer.getId());
            manufacturerResponse.setName(manufacturer.getName());
            manufacturerResponse.setLocation(manufacturer.getLocation());
            manufacturerResponses.add(manufacturerResponse);
        }

        return manufacturerResponses;
    }


    public Manufacturer getManufacturerById(int id) {
        return manufacturerRepository.findById(id).orElse(null);
    }

    public Manufacturer updateManufacturer(Manufacturer updatedManufacturer) {
        // Check if the product with the given ID exists
        Manufacturer existingManufacturer = manufacturerRepository.findById(updatedManufacturer.getId()).orElse(null);

        if (existingManufacturer != null) {
            existingManufacturer.setName(updatedManufacturer.getName());
            existingManufacturer.setLocation(updatedManufacturer.getLocation());
            return manufacturerRepository.save(existingManufacturer);
        }
        return null;
    }

    public void deleteManufacturer(int id) {
        // Check if the product with the given ID exists
        Manufacturer existingManufacturer = manufacturerRepository.findById(id).orElse(null);

        if (existingManufacturer != null) {
            // Delete the existing product
            manufacturerRepository.delete(existingManufacturer);
        }
        // You can choose to do nothing if the product does not exist or throw an exception.
    }
}
