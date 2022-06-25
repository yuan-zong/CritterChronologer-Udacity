package com.udacity.jdnd.course3.critter.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CustomerService {

    @Autowired
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository){
        this.customerRepository = customerRepository;
    }

    public Customer findCustomerById(long id) {
        Optional<Customer> optionalCustomer = customerRepository.findById(id);
        if (optionalCustomer.isPresent()) {
            Customer customer = optionalCustomer.get();
            return customer;
        } else {
            throw new CustomerNotFoundException("Customer Not Found");
        }
    }

    public List<Customer> findAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer saveCustomer(Customer customer){
        return customerRepository.save(customer);
    }

}
