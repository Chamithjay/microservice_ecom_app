package com.ecommerce.order.Service;

import com.ecommerce.order.Client.ProductServiceClient;
import com.ecommerce.order.Client.UserServiceClient;
import com.ecommerce.order.DTO.CartItemRequest;
import com.ecommerce.order.DTO.ProductResponse;
import com.ecommerce.order.DTO.UserResponse;
import com.ecommerce.order.Model.CartItem;
import com.ecommerce.order.Repository.CartItemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {
    private final CartItemRepository cartItemRepository;
    private final ProductServiceClient productServiceClient;
    private final UserServiceClient userServiceClient;
    public boolean addToCart(String userId, CartItemRequest request) {
        ProductResponse productResponse = productServiceClient.getProductById(request.getProductId());
        if(productResponse==null)
            return false;

        UserResponse userResponse = userServiceClient.getUserById(userId);
        if(userResponse==null)
            return false;
        if(productResponse.getStockQuantity()<request.getQuantity()){
            return false;
        }
        CartItem existingCartItem = cartItemRepository.findByUserIdAndProductId(userId, request.getProductId());
        if(existingCartItem!=null){
            //update the quantity
            existingCartItem.setQuantity(existingCartItem.getQuantity()+ request.getQuantity());
            existingCartItem.setPrice(productResponse.getPrice().multiply(BigDecimal.valueOf(existingCartItem.getQuantity())));
        }else{
            //create new cartItem
            CartItem cartItem = new CartItem();
            cartItem.setUserId(userId);
            cartItem.setProductId(request.getProductId());
            cartItem.setQuantity(request.getQuantity());
            cartItem.setPrice(productResponse.getPrice().multiply(BigDecimal.valueOf(request.getQuantity())));
            cartItemRepository.save((cartItem));
        }
        return true;

    }

    public boolean deleteItemFromCart(String userId, Long productId) {
        CartItem cartItem = cartItemRepository.findByUserIdAndProductId(userId,productId);
        if(cartItem!=null){
            cartItemRepository.delete(cartItem);
            return true;
        }
        return false;
    }


    public List<CartItem> getCart(String userId) {
        return cartItemRepository.findByUserId(userId);

    }

    public void clearCart(String userId) {
        cartItemRepository.deleteByUserId(userId);
    }
}
