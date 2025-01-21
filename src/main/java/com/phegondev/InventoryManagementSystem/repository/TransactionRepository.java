package com.phegondev.InventoryManagementSystem.repository;

import com.phegondev.InventoryManagementSystem.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionRepository extends MongoRepository<Transaction, String> {

    // Query to find transactions by year and month based on 'createdAt' field
    @Query("{ 'createdAt': { $gte: ?0, $lt: ?1 } }")
    List<Transaction> findAllByMonthAndYear(@Param("startOfMonth") String startOfMonth, @Param("endOfMonth") String endOfMonth);

    // Query to search transactions by description, status, product name, and SKU
    @Query("{ $or: [ " +
            "{ 'description': { $regex: ?0, $options: 'i' } }, " +
            "{ 'status': { $regex: ?0, $options: 'i' } }, " +
            "{ 'product.name': { $regex: ?0, $options: 'i' } }, " +
            "{ 'product.sku': { $regex: ?0, $options: 'i' } } ] }")
    Page<Transaction> searchTransactions(@Param("searchText") String searchText, Pageable pageable);
}
