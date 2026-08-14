package com.swiftcart.ecommerce.controller;

import com.swiftcart.ecommerce.exception.ProductionException;
import com.swiftcart.ecommerce.exception.SellerException;
import com.swiftcart.ecommerce.modal.Product;
import com.swiftcart.ecommerce.modal.Seller;
import com.swiftcart.ecommerce.response.CreateProductRequest;
import com.swiftcart.ecommerce.service.ProductService;
import com.swiftcart.ecommerce.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sellers/products")
public class SellerProductController {

    private final ProductService productService;
    private final SellerService sellerService;

    @GetMapping()
    public ResponseEntity<List<Product>> getProductBySellerId(@RequestHeader("Authorization") String jwt)
            throws SellerException {
        Seller seller = sellerService.getSellerProfile(jwt);
        List<Product> products = productService.getAllProductBySellerId(seller.getId());
        return ResponseEntity.ok(products);
    }

    @PostMapping()
    public ResponseEntity<Product> createProduct(@RequestBody CreateProductRequest request,
                                                 @RequestHeader("Authorization") String jwt) throws SellerException {
        Seller seller =  sellerService.getSellerProfile(jwt);

        Product product = productService.createProduct(request , seller);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    @DeleteMapping("{productId}")
    public ResponseEntity<Void> deleteProduct(@RequestParam Long productId) {
        try {
            productService.deleteProduct(productId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch(ProductionException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long productId ,
                                                 @RequestBody Product product) throws ProductionException {

        Product updateProduct = productService.updateProduct(productId, product);
        return new ResponseEntity<>(updateProduct, HttpStatus.OK);
    }
}
