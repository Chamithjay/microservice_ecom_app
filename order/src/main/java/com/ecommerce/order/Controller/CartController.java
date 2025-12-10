package com.ecommerce.order.Controller;

import com.ecommerce.order.DTO.CartItemRequest;
import com.ecommerce.order.Model.CartItem;
import com.ecommerce.order.Service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @PostMapping
    public ResponseEntity<String> addToCart(
            @RequestHeader ("X-User_ID") String userId,
            @RequestBody CartItemRequest request){
        if(!cartService.addToCart(userId,request)){
            return ResponseEntity.badRequest().body("Product out of stock or User not found or Product not found");
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> removeFormCart(@RequestHeader("X-User_ID") String userId, @PathVariable Long productId){
        boolean deleted = cartService.deleteItemFromCart(userId,productId);
        return deleted? ResponseEntity.noContent().build(): ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<CartItem>> getCart(@RequestHeader("X-User_ID") String userId) {
        return ResponseEntity.ok(cartService.getCart(userId));
    }
}
