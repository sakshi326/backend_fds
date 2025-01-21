package com.phegondev.InventoryManagementSystem.service;

import com.phegondev.InventoryManagementSystem.dto.Response;
import com.phegondev.InventoryManagementSystem.dto.SupplierDTO;

public interface SupplierService {

    Response addSupplier(SupplierDTO supplierDTO);

    Response updateSupplier(String id, SupplierDTO supplierDTO);

    Response getAllSuppliers();

    Response getSupplierById(String id);

    Response deleteSupplier(String id);
}
