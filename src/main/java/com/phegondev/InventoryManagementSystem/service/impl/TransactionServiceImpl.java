package com.phegondev.InventoryManagementSystem.service.impl;

import com.phegondev.InventoryManagementSystem.dto.Response;
import com.phegondev.InventoryManagementSystem.dto.TransactionDTO;
import com.phegondev.InventoryManagementSystem.dto.TransactionRequest;
import com.phegondev.InventoryManagementSystem.entity.Product;
import com.phegondev.InventoryManagementSystem.entity.Supplier;
import com.phegondev.InventoryManagementSystem.entity.Transaction;
import com.phegondev.InventoryManagementSystem.enums.TransactionStatus;
import com.phegondev.InventoryManagementSystem.enums.TransactionType;
import com.phegondev.InventoryManagementSystem.exceptions.NameValueRequiredException;
import com.phegondev.InventoryManagementSystem.exceptions.NotFoundException;
import com.phegondev.InventoryManagementSystem.repository.ProductRepository;
import com.phegondev.InventoryManagementSystem.repository.SupplierRepository;
import com.phegondev.InventoryManagementSystem.repository.TransactionRepository;
import com.phegondev.InventoryManagementSystem.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private final TransactionRepository transactionRepository;
    private final ModelMapper modelMapper;
    private final SupplierRepository supplierRepository;
    private final ProductRepository productRepository;

    @Override
    public Response restockInventory(TransactionRequest transactionRequest) {
        String productId = transactionRequest.getProductId();
        String supplierIdLong = transactionRequest.getSupplierId();
        String supplierId = supplierIdLong.toString();
        Integer quantity = Integer.parseInt(transactionRequest.getQuantity()); // Convert String to Integer

        if (supplierId == null) throw new NameValueRequiredException("Supplier ID is required");

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found"));

        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new NotFoundException("Supplier not found"));

        // Update stock quantity and save
        product.setStockQuantity(product.getStockQuantity() + quantity);
        productRepository.save(product);

        // Create a transaction
        Transaction transaction = Transaction.builder()
                .transactionType(TransactionType.PURCHASE)
                .status(TransactionStatus.COMPLETED)
                .productId(productId)
                .supplierId(supplierId)
                .totalProducts(quantity)
                .totalPrice(product.getPrice().multiply(BigDecimal.valueOf(quantity)))
                .description(transactionRequest.getDescription())
                .build();

        transactionRepository.save(transaction);

        return Response.builder()
                .status(200)
                .message("Transaction made successfully")
                .build();
    }

    @Override
    public Response sell(TransactionRequest transactionRequest) {
        String productId = transactionRequest.getProductId();
        String quantity = transactionRequest.getQuantity();

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found"));

        // Convert quantity from String to Integer
        int quantityInt = Integer.parseInt(quantity);

        // Update stock quantity and save
        product.setStockQuantity(product.getStockQuantity() - quantityInt);
        productRepository.save(product);

        // Create a transaction
        Transaction transaction = Transaction.builder()
                .transactionType(TransactionType.SALE)
                .status(TransactionStatus.COMPLETED)
                .productId(productId)
                .totalProducts(quantityInt) // Pass the integer value of quantity
                .totalPrice(product.getPrice().multiply(BigDecimal.valueOf(quantityInt))) // Multiply price by quantity
                .description(transactionRequest.getDescription())
                .build();

        transactionRepository.save(transaction);

        return Response.builder()
                .status(200)
                .message("Transaction sold successfully")
                .build();
    }

    @Override
    public Response returnToSupplier(TransactionRequest transactionRequest) {
        return null;
    }

//    @Override
//    public Response returnToSupplier(TransactionRequest transactionRequest) {
//        return null;
//    }

    @Override
    public Response getAllTransactions(int page, int size, String searchText) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<Transaction> transactionPage = transactionRepository.searchTransactions(searchText, pageable);

        List<TransactionDTO> transactionDTOS = modelMapper
                .map(transactionPage.getContent(), new TypeToken<List<TransactionDTO>>() {}.getType());

        transactionDTOS.forEach(transactionDTOItem -> {
            transactionDTOItem.setProductId(null);
            transactionDTOItem.setSupplierId(null);
        });

        return Response.builder()
                .status(200)
                .message("success")
                .transactions(transactionDTOS)
                .build();
    }

//    @Override
//    public Response getTransactionById(Long id) {
//        return null;
//    }

    @Override
    public Response getTransactionById(String id) {
        System.out.println(id);
        System.out.println(transactionRepository.findAll());
        System.out.println(transactionRepository.findById(id));
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Transaction not found"));

        TransactionDTO transactionDTO = modelMapper.map(transaction, TransactionDTO.class);

        return Response.builder()
                .status(200)
                .message("success")
                .transaction(transactionDTO)
                .build();
    }

    @Override
    public Response getAllTransactionByMonthAndYear(int month, int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1, 0, 0, 0); // Start of the month
        Date startDate = calendar.getTime();

        calendar.set(year, month, 1, 0, 0, 0); // Start of next month
        Date endDate = calendar.getTime();

        // Convert Dates to Strings in the required format
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String startDateString = sdf.format(startDate);
        String endDateString = sdf.format(endDate);

        // Assuming your repository method now expects String arguments
        List<Transaction> transactions = transactionRepository.findAllByMonthAndYear(startDateString, endDateString);

        List<TransactionDTO> transactionDTOS = modelMapper
                .map(transactions, new TypeToken<List<TransactionDTO>>() {}.getType());

        transactionDTOS.forEach(transactionDTOItem -> {
            transactionDTOItem.setProductId(null);
            transactionDTOItem.setSupplierId(null);
        });

        return Response.builder()
                .status(200)
                .message("success")
                .transactions(transactionDTOS)
                .build();
    }

    @Override
    public Response updateTransactionStatus(String transactionId, TransactionStatus transactionStatus) {
        return null;
    }


//    @Override
//    public Response updateTransactionStatus(String transactionId, TransactionStatus transactionStatus) {
//        return null;
//    }
}
