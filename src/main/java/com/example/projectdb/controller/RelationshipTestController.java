package com.example.projectdb.controller;

import com.example.projectdb.entity.*;
import com.example.projectdb.repository.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test")
public class RelationshipTestController {

    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;
    private final CustomerRepository customerRepository;
    private final CustomerProfileRepository customerProfileRepository;
    private final CompanyCustomerRepository companyCustomerRepository;

    public RelationshipTestController(
            ProductRepository productRepository,
            SupplierRepository supplierRepository,
            CustomerRepository customerRepository,
            CustomerProfileRepository customerProfileRepository,
            CompanyCustomerRepository companyCustomerRepository) {

        this.productRepository = productRepository;
        this.supplierRepository = supplierRepository;
        this.customerRepository = customerRepository;
        this.customerProfileRepository = customerProfileRepository;
        this.companyCustomerRepository = companyCustomerRepository;
    }
    @PostMapping("/many-to-many")
    public String testManyToMany() {

        Product product = productRepository.findById(16).orElse(null);
        Supplier supplier = supplierRepository.findById(1).orElse(null);

        if (product == null || supplier == null) {
            return "Product or Supplier not found";
        }

        if (product.getSuppliers() == null) {
            product.setSuppliers(new java.util.HashSet<>());
        }

        product.getSuppliers().add(supplier);
        productRepository.save(product);

        return "M:N relationship saved successfully";
    }

    @PostMapping("/one-to-one")
    public String testOneToOne() {

        Customer customer = new Customer();
        customer.setFirstName("Test");
        customer.setLastName("Customer");
        customer.setEmail("testcustomer@example.com");
        customer.setPhone("0401234567");

        customer = customerRepository.save(customer);

        CustomerProfile profile = new CustomerProfile();
        profile.setCustomer(customer);
        profile.setPreferredLanguage("English");

        customerProfileRepository.save(profile);

        return "1:1 relationship saved successfully";
    }

    @PostMapping("/inheritance")
    public String testInheritance() {

        CompanyCustomer companyCustomer = new CompanyCustomer();

        companyCustomer.setFirstName("Test");
        companyCustomer.setLastName("Company");
        companyCustomer.setEmail("companytest@example.com");
        companyCustomer.setPhone("0409876543");

        companyCustomer.setCompanyName("Test Company Oy");
        companyCustomer.setBusinessId("1234567-8");

        companyCustomerRepository.save(companyCustomer);

        return "CompanyCustomer saved successfully";
    }
    @GetMapping("/inheritance/{id}")
    public String getCompanyCustomer(@PathVariable Integer id) {

        CompanyCustomer companyCustomer =
                companyCustomerRepository.findById(id).orElse(null);

        if (companyCustomer == null) {
            return "CompanyCustomer not found";
        }

        return "ID: " + companyCustomer.getId()
                + ", Name: " + companyCustomer.getFirstName()
                + " " + companyCustomer.getLastName()
                + ", Company: " + companyCustomer.getCompanyName()
                + ", Business ID: " + companyCustomer.getBusinessId();
    }
    @DeleteMapping("/many-to-many")
    public String deleteManyToMany() {

        Product product = productRepository.findById(16).orElse(null);
        Supplier supplier = supplierRepository.findById(1).orElse(null);

        if (product == null || supplier == null) {
            return "Product or Supplier not found";
        }

        if (product.getSuppliers() != null) {
            product.getSuppliers().remove(supplier);
            productRepository.save(product);
        }

        return "M:N relationship deleted successfully";
    }
}
