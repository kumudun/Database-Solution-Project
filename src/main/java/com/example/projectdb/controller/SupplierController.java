package com.example.projectdb.controller;

import com.example.projectdb.entity.Supplier;
import com.example.projectdb.repository.SupplierRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
public class SupplierController {

    private final SupplierRepository supplierRepository;

    public SupplierController(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<Supplier> createSupplier(
            @RequestBody Supplier supplier) {

        supplier.setId(null);

        Supplier savedSupplier = supplierRepository.save(supplier);

        return ResponseEntity
                .status(201)
                .body(savedSupplier);
    }

    // READ all
    @GetMapping
    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }

    // READ one
    @GetMapping("/{id}")
    public ResponseEntity<Supplier> getSupplierById(
            @PathVariable Integer id) {

        return supplierRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Supplier> updateSupplier(
            @PathVariable Integer id,
            @RequestBody Supplier updatedSupplier) {

        return supplierRepository.findById(id)
                .map(supplier -> {

                    supplier.setName(updatedSupplier.getName());
                    supplier.setContactName(updatedSupplier.getContactName());
                    supplier.setPhone(updatedSupplier.getPhone());
                    supplier.setEmail(updatedSupplier.getEmail());

                    Supplier savedSupplier =
                            supplierRepository.save(supplier);

                    return ResponseEntity.ok(savedSupplier);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupplier(
            @PathVariable Integer id) {

        if (!supplierRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        supplierRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}