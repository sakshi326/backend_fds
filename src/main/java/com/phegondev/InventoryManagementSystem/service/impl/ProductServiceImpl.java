package com.phegondev.InventoryManagementSystem.service.impl;

import com.phegondev.InventoryManagementSystem.dto.ProductDTO;
import com.phegondev.InventoryManagementSystem.dto.Response;
import com.phegondev.InventoryManagementSystem.entity.Category;
import com.phegondev.InventoryManagementSystem.entity.Product;
import com.phegondev.InventoryManagementSystem.exceptions.NotFoundException;
import com.phegondev.InventoryManagementSystem.repository.CategoryRepository;
import com.phegondev.InventoryManagementSystem.repository.ProductRepository;
import com.phegondev.InventoryManagementSystem.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    @Autowired
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final CategoryRepository categoryRepository;
    private final MongoTemplate mongoTemplate;
    private static final String IMAGE_DIRECTORY = System.getProperty("user.dir") + "/product-image/";
    private static final String IMAGE_DIRECTOR_FRONTEND = "C:/Users/jhasa/Downloads/ims-angular-spring-sakshi/ims-angular-spring/src/assets/products/";



    @Override
    public Response saveProduct(ProductDTO productDTO, MultipartFile imageFile) {
        Category category = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Category Not Found"));

        // map out product dto to product entity
        Product productToSave = Product.builder()
                .name(productDTO.getName())
                .sku(productDTO.getSku())
                .price(productDTO.getPrice())
                .stockQuantity(productDTO.getStockQuantity())
                .description(productDTO.getDescription())
                .category(category)
                .build();

        if (imageFile != null) {
            String imagePath = saveImageToFrontendPublicFolder(imageFile);
            productToSave.setImageUrl(imagePath);
        }


        // save the product to MongoDB
        productRepository.save(productToSave);

        return Response.builder()
                .status(200)
                .message("Product successfully saved")
                .build();
    }

    @Override
    public Response updateProduct(ProductDTO productDTO, MultipartFile imageFile) {
        Product existingProduct = productRepository.findById(productDTO.getProductId())
                .orElseThrow(() -> new NotFoundException("Product Not Found"));

        // check if image is associated with the update request
        if (imageFile != null && !imageFile.isEmpty()) {
            String imagePath = saveImageToFrontendPublicFolder(imageFile);
            existingProduct.setImageUrl(imagePath);
        }

        // Check if category is to be changed for the product
        if (productDTO.getCategoryId() != null && Integer.parseInt(productDTO.getCategoryId()) > 0) {
            Category category = categoryRepository.findById(productDTO.getCategoryId())
                    .orElseThrow(() -> new NotFoundException("Category Not Found"));
            existingProduct.setCategory(category);
        }

        // check and update fields
        if (productDTO.getName() != null && !productDTO.getName().isBlank()) {
            existingProduct.setName(productDTO.getName());
        }
        if (productDTO.getSku() != null && !productDTO.getSku().isBlank()) {
            existingProduct.setSku(productDTO.getSku());
        }
        if (productDTO.getDescription() != null && !productDTO.getDescription().isBlank()) {
            existingProduct.setDescription(productDTO.getDescription());
        }
        if (productDTO.getPrice() != null && productDTO.getPrice().compareTo(BigDecimal.ZERO) >= 0) {
            existingProduct.setPrice(productDTO.getPrice());
        }
        if (productDTO.getStockQuantity() != null && productDTO.getStockQuantity() >= 0) {
            existingProduct.setStockQuantity(productDTO.getStockQuantity());
        }

        // Update the product in MongoDB
        productRepository.save(existingProduct);

        return Response.builder()
                .status(200)
                .message("Product successfully Updated")
                .build();
    }

    @Override
    public Response getAllProducts() {
        List<Product> products = productRepository.findAll();
        List<ProductDTO> productDTOS = modelMapper.map(products, new TypeToken<List<ProductDTO>>() {}.getType());

        return Response.builder()
                .status(200)
                .message("success")
                .products(productDTOS)
                .build();
    }

    @Override
    public Response getProductById(String id) {
        Query query = new Query(Criteria.where("id").is(id));  // MongoDB query syntax
        Product product = mongoTemplate.findOne(query, Product.class);

        if (product == null) {
            throw new NotFoundException("Product Not Found");
        }

        return Response.builder()
                .status(200)
                .message("success")
                .product(modelMapper.map(product, ProductDTO.class))
                .build();
    }

    @Override
    public Response deleteProduct(String id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product Not Found"));

        // Deleting the product from MongoDB
        productRepository.delete(product);

        return Response.builder()
                .status(200)
                .message("Product successfully deleted")
                .build();
    }

    private String saveImageToFrontendPublicFolder(MultipartFile imageFile) {
        // Validate image check
        if (imageFile == null || !imageFile.getContentType().startsWith("image/")) {
            throw new IllegalArgumentException("Only image files are allowed");
        }

        // Ensure the directory exists
        File directory = new File(IMAGE_DIRECTOR_FRONTEND);
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                throw new IllegalArgumentException("Failed to create directory: " + IMAGE_DIRECTOR_FRONTEND);
            }
            log.info("Directory was created");
        }

        // Generate unique file name for the image
        String uniqueFileName =  imageFile.getOriginalFilename();

        // Construct the file path
        String imagePath = Paths.get(IMAGE_DIRECTOR_FRONTEND, uniqueFileName).toString();

//         Save the file
        try {
            File destinationFile = new File(imagePath);
            imageFile.transferTo(destinationFile);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error occurred while saving image: " + e.getMessage());
        }

        return "/public/products/"+uniqueFileName;
    }

}


//    private String saveImage(MultipartFile imageFile){
//        //validate image check
//        if (!imageFile.getContentType().startsWith("image/")){
//            throw new IllegalArgumentException("Only image files are allowed");
//        }
//        //create the directory to store images if it doesn't exist
//        File directory = new File(IMAGE_DIRECTORY);
//
//        if (!directory.exists()){
//            directory.mkdir();
//            log.info("Directory was created");
//        }
//        //generate unique file name for the image
//        String uniqueFileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
//        //get the absolute path of the image
//        String imagePath = IMAGE_DIRECTORY + uniqueFileName;
//
//        try {
//            File desctinationFile = new File(imagePath);
//            imageFile.transferTo(desctinationFile); //we are transfering(writing to this folder)
//
//        }catch (Exception e){
//            throw new IllegalArgumentException("Error occurend while saving image" + e.getMessage());
//        }
//
//        return imagePath;
//
//}
