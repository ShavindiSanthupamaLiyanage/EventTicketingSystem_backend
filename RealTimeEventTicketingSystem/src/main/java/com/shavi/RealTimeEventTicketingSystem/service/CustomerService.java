package com.shavi.RealTimeEventTicketingSystem.service;

import com.shavi.RealTimeEventTicketingSystem.entity.Customer;
import com.shavi.RealTimeEventTicketingSystem.enums.UserType;
import com.shavi.RealTimeEventTicketingSystem.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

//    public Customer registerCustomer(Customer customer) {
//        customer.setUserType(UserType.CUSTOMER);
//        return customerRepository.save(customer);
//    }
}
