package com.shavi.RealTimeEventTicketingSystem.service;

import com.shavi.RealTimeEventTicketingSystem.entity.Vendor;
import com.shavi.RealTimeEventTicketingSystem.enums.UserType;
import com.shavi.RealTimeEventTicketingSystem.repository.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VendorService {
    private final VendorRepository vendorRepository;

    @Autowired
    public VendorService(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

//    public Vendor registerVendor(Vendor vendor) {
//        vendor.setUserType(UserType.VENDOR);
//        return vendorRepository.save(vendor);
//    }
}
