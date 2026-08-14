package com.swiftcart.ecommerce.service.impl;

import com.swiftcart.ecommerce.config.JwtProvider;
import com.swiftcart.ecommerce.domain.USER_ROLE;
import com.swiftcart.ecommerce.modal.Cart;
import com.swiftcart.ecommerce.modal.Seller;
import com.swiftcart.ecommerce.modal.User;
import com.swiftcart.ecommerce.modal.VerificationCode;
import com.swiftcart.ecommerce.repository.CartRepository;
import com.swiftcart.ecommerce.repository.SellerRepository;
import com.swiftcart.ecommerce.repository.UserRepository;
import com.swiftcart.ecommerce.repository.VerificationCodeRepository;
import com.swiftcart.ecommerce.response.AuthResponse;
import com.swiftcart.ecommerce.response.LoginRequest;
import com.swiftcart.ecommerce.response.SignupRequest;
import com.swiftcart.ecommerce.service.AuthService;
import com.swiftcart.ecommerce.service.EmailService;
import com.swiftcart.ecommerce.utils.OtpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CartRepository cartRepository;
    private final JwtProvider jwtProvider;
    private final VerificationCodeRepository verificationCodeRepository;
    private final EmailService emailService;
    private final SellerRepository sellerRepository;
    private final CustomUserDetailServiceImpl customUserDetailService;

    @Override
    public void sendLoginOtp(String email , USER_ROLE role) throws Exception {
        String SIGNING_PREFIX = "signing_";
        if(email.startsWith(SIGNING_PREFIX)){
            email  = email.substring(SIGNING_PREFIX.length());

            if(role.equals(USER_ROLE.ROLE_SELLER)) {
                Seller seller = sellerRepository.findByEmail(email);
                if(seller == null) {
                    throw new Exception("Seller does not exist");
                }
            } else {
                User user = userRepository.findByEmail(email);
                if(user == null){
                    throw new Exception("User does not exist");
                }
            }
        }
        VerificationCode isExist = verificationCodeRepository.findByEmail(email);
        if(isExist != null){
            verificationCodeRepository.delete(isExist);
        }

        String otp = OtpUtil.generateOtp();

        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setOtp(otp);
        verificationCode.setEmail(email);
        verificationCodeRepository.save(verificationCode);

        String subject = "SwiftCart login/signup Otp";
        String Text = "your Login/Signup Otp is - " + otp;

        emailService.sendVerificationOtpEmail(email,otp,subject,Text);
    }

    @Override
    public String createUser(SignupRequest req) throws Exception {


        VerificationCode verificationCode = verificationCodeRepository.findByEmail(req.getEmail());
        if (verificationCode == null || !verificationCode.getOtp().equals(req.getOtp())) {
            throw new Exception(("Wrong Otp"));
        }


        User user = userRepository.findByEmail(req.getEmail());

        if(user == null) {
            User createdUser = new User();
            createdUser.setEmail(req.getEmail());
            createdUser.setFullName(req.getFullName());
            createdUser.setRole(USER_ROLE.ROLE_CUSTOMER);
            createdUser.setMobile("9354547924");
            createdUser.setPassword(passwordEncoder.encode(req.getOtp()));

            user = userRepository.save(createdUser);

            Cart cart = new Cart();
            cart.setUser(user);
            cartRepository.save(cart);
        }

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(USER_ROLE.ROLE_CUSTOMER.toString()));

        Authentication authentication = new UsernamePasswordAuthenticationToken(user , null , grantedAuthorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return jwtProvider.generateToken(authentication);
    }

    @Override
    public AuthResponse signing(LoginRequest req) {
        String username = req.getEmail();
        String otp = req.getOtp();

        Authentication authentication = authenticate(username , otp);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtProvider.generateToken(authentication);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(token);
        authResponse.setMessage("Login Success");

        Collection<? extends GrantedAuthority> grantedAuthorities = authentication.getAuthorities();
        String role_name = grantedAuthorities.isEmpty()?null : grantedAuthorities.iterator().next().getAuthority();
        authResponse.setRole(USER_ROLE.valueOf(role_name));
        return authResponse;
    }

    private Authentication authenticate(String username, String otp) {
        UserDetails userDetails = customUserDetailService.loadUserByUsername(username);

        if(userDetails == null){
            throw new BadCredentialsException("invalid username");
        }
        String SELLER_PREFIX = "seller_";
        if(username.startsWith(SELLER_PREFIX)){
            username  = username.substring(SELLER_PREFIX.length());
        }

        VerificationCode verificationCode = verificationCodeRepository.findByEmail(username);
        if(verificationCode == null || !verificationCode.getOtp().equals(otp)){
            throw new BadCredentialsException("invalid otp");
        }
        return new UsernamePasswordAuthenticationToken(
                userDetails ,
                null,
                userDetails.getAuthorities()
        );
    }
}
