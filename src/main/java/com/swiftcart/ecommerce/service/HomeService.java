package com.swiftcart.ecommerce.service;

import com.swiftcart.ecommerce.modal.Home;
import com.swiftcart.ecommerce.modal.HomeCategory;

import java.util.List;

public interface HomeService {
    Home createHomePageData(List<HomeCategory> allCategories);
}
