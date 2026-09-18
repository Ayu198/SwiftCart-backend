package com.swiftcart.ecommerce.controller;

import com.swiftcart.ecommerce.modal.Deal;
import com.swiftcart.ecommerce.response.ApiResponse;
import com.swiftcart.ecommerce.service.DealService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/deals")
public class DealController {

    private final DealService dealService;

    @GetMapping()
    public ResponseEntity<List<Deal>> getDeal(
    ) {
        List<Deal> createdDeal = dealService.getDeals();
        return new ResponseEntity<>(createdDeal , HttpStatus.ACCEPTED);
    }

    @PostMapping()
    public ResponseEntity<Deal> createDeal(
            @RequestBody Deal deal
    ) {
        Deal createdDeal = dealService.createDeal(deal);
        return new ResponseEntity<>(createdDeal , HttpStatus.ACCEPTED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Deal> updateDeal(
            @PathVariable Long id,
            @RequestBody Deal deal
    ) throws Exception {
        Deal updatedDeal = dealService.updateDeal(deal , id);
        return  new ResponseEntity<>(updatedDeal , HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse>  deleteDeal(
            @PathVariable Long id
    ) throws Exception {
        dealService.deleteDeal(id);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("Deal Deleted Successfully");
        return new ResponseEntity<>(apiResponse, HttpStatus.ACCEPTED);
    }
}
