package com.swiftcart.ecommerce.service.impl;

import com.swiftcart.ecommerce.modal.HomeCategory;
import com.swiftcart.ecommerce.repository.HomeCategoryRepository;
import com.swiftcart.ecommerce.service.HomeCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeCategoryServiceImpl implements HomeCategoryService {

    private final HomeCategoryRepository homeCategoryRepository;

    @Override
    public HomeCategory createHomeCategory(HomeCategory homeCategory) {
        return homeCategoryRepository.save(homeCategory);
    }

    @Override
    public List<HomeCategory> createCategories(List<HomeCategory> homeCategories) {

        List<HomeCategory> newCategories = homeCategories.stream()
                .filter(category ->
                        !homeCategoryRepository.existsByCategoryIdAndSection(
                                category.getCategoryId(),
                                category.getSection()
                        )
                )
                .toList();

        if (!newCategories.isEmpty()) {
            homeCategoryRepository.saveAll(newCategories);
        }

        return homeCategoryRepository.findAll();
    }

    @Override
    public HomeCategory updateHomeCategory(HomeCategory homeCategory, Long id) throws Exception {
        HomeCategory existingCategory =  homeCategoryRepository.findById(id).
                orElseThrow(() -> new Exception("Category Not Found"));
        if(homeCategory.getImage() != null) {
            existingCategory.setImage(homeCategory.getImage());
        }
        if(homeCategory.getCategoryId() != null) {
            existingCategory.setCategoryId(homeCategory.getCategoryId());
        }
        return homeCategoryRepository.save(existingCategory);
    }

    @Override
    public List<HomeCategory> getAllHomeCategories() {
        return homeCategoryRepository.findAll();
    }
}
