package com.swiftcart.ecommerce.repository;

import com.swiftcart.ecommerce.domain.HomeCategorySection;
import com.swiftcart.ecommerce.modal.HomeCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HomeCategoryRepository extends JpaRepository<HomeCategory, Long> {

    boolean existsByCategoryIdAndSection(
            String categoryId,
            HomeCategorySection section
    );
}