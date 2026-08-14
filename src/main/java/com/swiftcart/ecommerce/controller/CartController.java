package com.swiftcart.ecommerce.controller;

import com.swiftcart.ecommerce.modal.Cart;
import com.swiftcart.ecommerce.modal.CartItem;
import com.swiftcart.ecommerce.modal.Product;
import com.swiftcart.ecommerce.modal.User;
import com.swiftcart.ecommerce.response.AddItemRequest;
import com.swiftcart.ecommerce.response.ApiResponse;
import com.swiftcart.ecommerce.service.CartItemService;
import com.swiftcart.ecommerce.service.CartService;
import com.swiftcart.ecommerce.service.ProductService;
import com.swiftcart.ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;
    private final CartItemService cartItemService;
    private final UserService userService;
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<Cart> findUserCartHandler(@RequestHeader("Authorization") String jwt)
        throws Exception {
        User user = userService.findByJwtToken(jwt);
        Cart cart = cartService.findUserCart(user);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @PutMapping("/add")
    public ResponseEntity<CartItem> addItemToCart(
            @RequestBody AddItemRequest request,
            @RequestHeader("Authorization") String jwt
            ) throws Exception {
        User user = userService.findByJwtToken(jwt);
        Product product = productService.findProductById(request.getProductId());

        CartItem item = cartService.addCartItem(user , product , request.getSize() , request.getQuantity());

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("Item Added to Cart Successfully");
        return new ResponseEntity<>(item , HttpStatus.CREATED);
    }

    @DeleteMapping("/item/{cartItemId}")
    public ResponseEntity<ApiResponse> deleteItemFromCart(
            @PathVariable Long cartItemId,
            @RequestHeader("Authorization") String jwt
    ) throws Exception{
        User user = userService.findByJwtToken(jwt);
        cartItemService.deleteCartItem(user.getId() , cartItemId);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("Item Deleted from Cart Successfully");
        return new ResponseEntity<>(apiResponse , HttpStatus.ACCEPTED);
    }

    @PutMapping("/item/{cartItemId}")
    public ResponseEntity<CartItem> updateCartItemHandler(
            @PathVariable Long cartItemId,
            @RequestBody CartItem cartItem,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        User user = userService.findByJwtToken(jwt);
        CartItem updatedCartItem = null;
        if(cartItem.getQuantity() > 0) {
            updatedCartItem = cartItemService.updateCartItem(user.getId() , cartItemId , cartItem);
        }
        return new ResponseEntity<>(updatedCartItem , HttpStatus.ACCEPTED);
    }
}
