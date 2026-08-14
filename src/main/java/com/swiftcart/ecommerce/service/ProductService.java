package com.swiftcart.ecommerce.service;

import com.swiftcart.ecommerce.exception.ProductionException;
import com.swiftcart.ecommerce.modal.Product;
import com.swiftcart.ecommerce.modal.Seller;
import com.swiftcart.ecommerce.response.CreateProductRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {
    public Product createProduct(CreateProductRequest req , Seller seller);
    public void deleteProduct(Long ProductId) throws ProductionException;
    public Product updateProduct(Long ProductId, Product product) throws ProductionException;
    Product findProductById(Long ProductId) throws ProductionException;
    List<Product> searchProducts(String query);
    public Page<Product> getAllProducts(
            String category,
            String brand,
            String colors,
            String sizes,
            Integer maxPrice,
            Integer minPrice,
            Integer minDiscount,
            String sort,
            String stock,
            Integer pageNumber
    );
    List<Product> getAllProductBySellerId(Long sellerId);
}
