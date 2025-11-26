package com.smartstore.service;

import com.smartstore.dao.SupplierDAO;
import com.smartstore.model.Supplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupplierService {

    @Autowired
    private SupplierDAO supplierDAO;

    public List<Supplier> getAllSuppliers() {
        return supplierDAO.findAll();
    }

    public Supplier getSupplier(Integer id) {
        return supplierDAO.findById(id);
    }

    public void addSupplier(Supplier supplier) {
        supplierDAO.save(supplier);
    }

    public void updateSupplier(Supplier supplier) {
        supplierDAO.update(supplier);
    }

    public void deleteSupplier(Integer id) {
        supplierDAO.delete(id);
    }
}
