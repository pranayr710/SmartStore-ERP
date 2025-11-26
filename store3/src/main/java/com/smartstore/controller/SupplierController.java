package com.smartstore.controller;

import com.smartstore.model.Supplier;
import com.smartstore.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
@CrossOrigin(origins = "*")
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

    @GetMapping
    public List<Supplier> getAllSuppliers() {
        return supplierService.getAllSuppliers();
    }

    @GetMapping("/{id}")
    public Supplier getSupplier(@PathVariable Integer id) {
        return supplierService.getSupplier(id);
    }

    @PostMapping
    public void createSupplier(@RequestBody Supplier supplier) {
        supplierService.addSupplier(supplier);
    }

    @PutMapping("/{id}")
    public void updateSupplier(@PathVariable Integer id, @RequestBody Supplier supplier) {
        supplier.setId(id);
        supplierService.updateSupplier(supplier);
    }

    @DeleteMapping("/{id}")
    public void deleteSupplier(@PathVariable Integer id) {
        supplierService.deleteSupplier(id);
    }
}
