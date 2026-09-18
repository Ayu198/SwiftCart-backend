package com.swiftcart.ecommerce.controller;

import com.swiftcart.ecommerce.modal.Home;
import com.swiftcart.ecommerce.modal.HomeCategory;
import com.swiftcart.ecommerce.service.HomeCategoryService;
import com.swiftcart.ecommerce.service.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class HomeCategoryController {
    private final HomeCategoryService homeCategoryService;
    private final HomeService homeService;

    @PostMapping("/home/categories")
    public ResponseEntity<Home> createHomeCategories(
            @RequestBody List<HomeCategory> homeCategories
    ) {
        List<HomeCategory> categories = homeCategoryService.createCategories(homeCategories);
        Home home = homeService.createHomePageData(categories);
        return  new ResponseEntity<>(home , HttpStatus.ACCEPTED);
    }

    @GetMapping("/admin/home-category")
    public ResponseEntity<List<HomeCategory>> getHomeCategories() throws Exception{
        List<HomeCategory> categories  = homeCategoryService.getAllHomeCategories();
        return ResponseEntity.ok(categories);
    }

    @PatchMapping("/admin/home-category/{id}")
    public ResponseEntity<HomeCategory> updateHomeCategory(
            @PathVariable Long id,
            @RequestBody HomeCategory category
    ) throws Exception {
        HomeCategory homeCategory = homeCategoryService.updateHomeCategory(category , id);
        return new ResponseEntity<>(homeCategory , HttpStatus.OK);
    }
}
