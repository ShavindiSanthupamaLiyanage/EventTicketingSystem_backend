package com.shavi.RealTimeEventTicketingSystem.controller;

import com.shavi.RealTimeEventTicketingSystem.dto.UserDto;
import com.shavi.RealTimeEventTicketingSystem.entity.Vendor;
import com.shavi.RealTimeEventTicketingSystem.service.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendors")
public class VendorController {

    @Autowired
    private VendorService vendorService;

    @PostMapping("/register")
    public ResponseEntity<String> registerVendor(@RequestBody UserDto userDto) {
        Vendor registeredVendor = vendorService.registerVendor(userDto);
        return ResponseEntity.ok("Vendor registered successfully with ID: " + registeredVendor.getId());
    }

    // Get all vendors
    @GetMapping("/vendors")
    public List<Vendor> getAllVendors() {
        return vendorService.getAllVendors();
    }

    // Get vendor by ID
    @GetMapping("/vendors/{id}")
    public ResponseEntity<Vendor> getVendorById(@PathVariable Long id) {
        Vendor vendor = vendorService.getVendorById(id); // This will throw ResourceNotFoundException if vendor not found
        return ResponseEntity.ok(vendor);
    }

    // Update an existing vendor
    @PutMapping("/{id}")
    public ResponseEntity<Vendor> updateVendor(@PathVariable Long id, @RequestBody Vendor updatedVendor) {
        Vendor vendor = vendorService.updateVendor(id, updatedVendor); // This will throw ResourceNotFoundException if vendor not found
        return ResponseEntity.ok(vendor);
    }

    // Delete event
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteVendor(@PathVariable Long id) {
        boolean deleted = vendorService.deleteVendor(id);
        return deleted
                ? ResponseEntity.ok("Vendor deleted successfully.")
                : ResponseEntity.status(404).body("Vendor not found.");
    }
}
