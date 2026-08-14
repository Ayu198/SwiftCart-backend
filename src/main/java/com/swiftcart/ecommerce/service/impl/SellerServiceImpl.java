package com.swiftcart.ecommerce.service.impl;

import com.swiftcart.ecommerce.config.JwtProvider;
import com.swiftcart.ecommerce.domain.AccountStatus;
import com.swiftcart.ecommerce.domain.USER_ROLE;
import com.swiftcart.ecommerce.exception.SellerException;
import com.swiftcart.ecommerce.modal.Address;
import com.swiftcart.ecommerce.modal.Seller;
import com.swiftcart.ecommerce.repository.AddressRepository;
import com.swiftcart.ecommerce.repository.SellerRepository;
import com.swiftcart.ecommerce.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SellerServiceImpl implements SellerService {

    private final SellerRepository sellerRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final AddressRepository addressRepository;

    @Override
    public Seller getSellerProfile(String jwt) throws SellerException {
        String email = jwtProvider.getEmailFromJwtToken(jwt);
        return this.getSellerByEmail(email);
    }

    @Override
    public Seller createSeller(Seller seller) throws SellerException {
        Seller sellerExists =  sellerRepository.findByEmail(seller.getEmail());
        if (sellerExists != null) {
            throw new SellerException("seller already exists, use different account");
        }
        Address savedAddress = addressRepository.save(seller.getPickupAddress());

        Seller newSeller = new Seller();
        newSeller.setPickupAddress(savedAddress);
        newSeller.setEmail(seller.getEmail());
        newSeller.setPassword(passwordEncoder.encode(seller.getPassword()));
        newSeller.setSellerName(seller.getSellerName());
        newSeller.setGSTIN(seller.getGSTIN());
        newSeller.setBusinessDetails(seller.getBusinessDetails());
        newSeller.setBankDetails(seller.getBankDetails());
        newSeller.setMobile(seller.getMobile());
        newSeller.setRole(USER_ROLE.ROLE_SELLER);

        return sellerRepository.save(newSeller);
    }

    @Override
    public Seller getSellerById(Long id) throws SellerException {
        return sellerRepository.findById(id).orElseThrow(() -> new SellerException("seller not found with id" + id));
    }

    @Override
    public Seller getSellerByEmail(String email) throws SellerException {
        Seller seller = sellerRepository.findByEmail(email);
        if(seller == null) {
            throw new SellerException("seller not found...");
        }
        return seller;
    }

    @Override
    public List<Seller> getAllSellers(AccountStatus status) {
        return sellerRepository.findByAccountStatus(status);
    }

    @Override
    public Seller updateSeller(Long id, Seller seller) throws SellerException {
        Seller existingSeller = this.getSellerById(id);

        if(seller.getSellerName() != null) {
            existingSeller.setSellerName(seller.getSellerName());
        }
        if(seller.getGSTIN() != null) {
            existingSeller.setGSTIN(seller.getGSTIN());
        }
        if(seller.getMobile() != null) {
            existingSeller.setMobile(seller.getMobile());
        }
        if(seller.getEmail() != null) {
            existingSeller.setEmail(seller.getEmail());
        }
        if(seller.getBusinessDetails() != null && seller.getBusinessDetails().getBusinessName() != null) {
            existingSeller.getBusinessDetails().setBusinessName(seller.getBusinessDetails().getBusinessName());
        }
        if(seller.getBankDetails() != null
        && seller.getBankDetails().getAccountHolderName() != null &&
        seller.getBankDetails().getAccountNumber() != null&&
        seller.getBankDetails().getIfscCode() != null) {
            existingSeller.getBankDetails().setAccountNumber(seller.getBankDetails().getAccountNumber());
            existingSeller.getBankDetails().setAccountHolderName(seller.getBankDetails().getAccountHolderName());
            existingSeller.getBankDetails().setIfscCode(seller.getBankDetails().getIfscCode());
        }
        if(seller.getPickupAddress() != null &&
        seller.getPickupAddress().getAddress() != null &&
        seller.getPickupAddress().getMobile() != null &&
        seller.getPickupAddress().getCity() != null &&
        seller.getPickupAddress().getState() != null) {
            existingSeller.getPickupAddress().setCity(seller.getPickupAddress().getCity());
            existingSeller.getPickupAddress().setState(seller.getPickupAddress().getState());
            existingSeller.getPickupAddress().setMobile(seller.getPickupAddress().getMobile());
            existingSeller.getPickupAddress().setAddress(seller.getPickupAddress().getAddress());
        }
        return sellerRepository.save(existingSeller);
    }

    @Override
    public void deleteSeller(Long id) throws SellerException {
        Seller seller = this.getSellerById(id);
        sellerRepository.delete(seller);
    }

    @Override
    public Seller verifyEmail(String email, String otp) throws SellerException {
        Seller seller = this.getSellerByEmail(email);
        seller.setEmailVerified(true);
        return sellerRepository.save(seller);
    }

    @Override
    public Seller updateSellerAccountStatus(Long sellerId, AccountStatus status) throws SellerException {
        Seller seller = this.getSellerById(sellerId);
        seller.setAccountStatus(status);
        return sellerRepository.save(seller);
    }
}