package com.wch.member.service;

import com.wch.member.entity.Cart;
import com.wch.member.entity.User;

import java.math.BigDecimal;
import java.util.Optional;


public interface CartService {


    Optional<Cart> getCartByUser(User user);


    Cart createCart(User user);


    void addItemToCart(User user, String item, BigDecimal price);


    void removeItemFromCart(User user, String item, BigDecimal price);


    void clearCart(User user);


    void clearUserCache(User user);


    String getCacheStats();
}