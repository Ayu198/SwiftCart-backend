package com.swiftcart.ecommerce.service.impl;

import com.swiftcart.ecommerce.exception.ProductionException;
import com.swiftcart.ecommerce.modal.Category;
import com.swiftcart.ecommerce.modal.Product;
import com.swiftcart.ecommerce.modal.Seller;
import com.swiftcart.ecommerce.repository.CategoryRepository;
import com.swiftcart.ecommerce.repository.ProductRepository;
import com.swiftcart.ecommerce.response.CreateProductRequest;
import com.swiftcart.ecommerce.service.ProductService;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public Product createProduct(CreateProductRequest req, Seller seller) {
        Category category1 = categoryRepository.findByCategoryId(req.getCategory());

        int discountPercentage = CalculateDiscountPercentage(req.getMrpPrice() , req.getSellingPrice());

        if(category1 == null) {
            Category category = new Category();
            category.setCategoryId(req.getCategory());
            category.setLevel(1);
            category1 = categoryRepository.save(category);
        }

        Category category2 = categoryRepository.findByCategoryId(req.getCategory2());

        if(category2 == null) {
            Category category = new Category();
            category.setCategoryId(req.getCategory2());
            category.setParentCategory(category1);
            category.setLevel(2);
            category2 = categoryRepository.save(category);
        }

        Category category3 = categoryRepository.findByCategoryId(req.getCategory3());

        if(category3 == null) {
            Category category = new Category();
            category.setCategoryId(req.getCategory3());
            category.setParentCategory(category2);
            category.setLevel(3);
            category3 = categoryRepository.save(category);
        }

        Product product = new Product();
        product.setSeller(seller);
        product.setCategory(category3);
        product.setDescription(req.getDescription());
        product.setCreatedAt(LocalDateTime.now());
        product.setTitle(req.getTitle());
        product.setColor(req.getColor());
        product.setSellingPrice(req.getSellingPrice());
        product.setImages(req.getImages());
        product.setMrpPrice(req.getMrpPrice());
        product.setSizes(req.getSizes());
        product.setDiscountPercent(discountPercentage);

        return productRepository.save(product);
    }

    private int CalculateDiscountPercentage(int mrpPrice, int sellingPrice) {
        if(mrpPrice <= 0) {
            throw new IllegalArgumentException("MrpPrice must be greater than 0");
        }
        double discountPrice = mrpPrice - sellingPrice;
        double discountPercentage=(discountPrice/mrpPrice) * 100;
        return (int) discountPercentage;
    }

    @Override
    public void deleteProduct(Long ProductId) throws ProductionException {
        Product product = findProductById(ProductId);
        productRepository.delete(product);
    }

    @Override
    public Product updateProduct(Long ProductId, Product product) throws ProductionException {
        Product newProduct =  findProductById(ProductId);
        newProduct.setId(product.getId());
        return productRepository.save(newProduct);
    }

    @Override
    public Product findProductById(Long ProductId) throws ProductionException {
        return productRepository.findById(ProductId).orElseThrow(() ->
                new ProductionException("product not found with this id " + ProductId));
    }

    @Override
    public List<Product> searchProducts(String query) {
        return productRepository.searchProduct(query);
    }

    @Override
    public Page<Product> getAllProducts(String category, String brand, String colors, String sizes,
                                        Integer minPrice, Integer maxPrice, Integer minDiscount,
                                        String sort, String stock, Integer pageNumber) {
        Specification<Product> spec = (root , query , cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(category != null) {
                Join<Product, Category> categoryJoin = root.join("category");
                predicates.add(cb.equal(categoryJoin.get("categoryId"), category));
            }

            if(colors != null && !colors.isEmpty()) {
                predicates.add(cb.equal(root.get("color") , colors));
            }

            if(sizes != null && !sizes.isEmpty()) {
                predicates.add(cb.equal(root.get("sizes") , sizes));
            }

            if(maxPrice != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("sellingPrice"), maxPrice));
            }

            if(minPrice != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("sellingPrice"), minPrice));
            }

            if(minDiscount != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("discountPercent"), minDiscount));
            }

            if(stock != null && !stock.isEmpty()) {
                predicates.add(cb.equal(root.get("stock"), stock));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        Pageable pageable;
        if(sort != null && !sort.isEmpty()) {
            pageable = switch (sort) {
                case "price_low" -> PageRequest.of(pageNumber != null ? pageNumber : 0, 10,
                        Sort.by("sellingPrice").ascending());
                case "price_high" -> PageRequest.of(pageNumber != null ? pageNumber : 0, 10,
                        Sort.by("sellingPrice").descending());
                default -> PageRequest.of(pageNumber != null ? pageNumber : 0, 10,
                        Sort.unsorted());
            };
        } else {
            pageable = PageRequest.of(pageNumber != null ? pageNumber : 0, 10,
                    Sort.unsorted());
        }
        return productRepository.findAll(spec, pageable);
    }

    @Override
    public List<Product> getAllProductBySellerId(Long sellerId) {
        return productRepository.findBySellerId(sellerId);
    }
}
