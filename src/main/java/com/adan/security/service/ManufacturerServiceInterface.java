package com.adan.security.service;

import com.adan.security.dto.ManufacturerResponse;
import com.adan.security.entity.Manufacturer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ManufacturerServiceInterface {
    Manufacturer createManufacturer(Manufacturer manufacturer);

    List<ManufacturerResponse> getAllManufacturerResponses();

    Manufacturer getManufacturerById(int id);

    Manufacturer updateManufacturer(Manufacturer updatedManufacturer);

    void deleteManufacturer(int id);
}

