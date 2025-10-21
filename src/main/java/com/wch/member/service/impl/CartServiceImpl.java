package com.wch.member.service.impl;

import com.wch.member.entity.Cart;
import com.wch.member.entity.User;
import com.wch.member.service.CartService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;


@Service
public class CartServiceImpl implements CartService {


    private static final ConcurrentHashMap<User, Cart> userCartCache = new ConcurrentHashMap<>();

    @Override
    public Optional<Cart> getCartByUser(User user) {
        Cart cart = userCartCache.get(user);
        if (cart != null) {
            System.out.println("从缓存获取购物车: " + cart);
            return Optional.of(cart);
        }
        
        System.out.println("缓存中未找到用户购物车，用户: " + user);
        return Optional.empty();
    }

    @Override
    public Cart createCart(User user) {
        Cart cart = new Cart(user.getId());
        

        userCartCache.put(user, cart);
        
        System.out.println("创建并缓存购物车: " + cart + " for user: " + user);
        System.out.println("当前缓存大小: " + userCartCache.size());
        
        return cart;
    }

    @Override
    public void addItemToCart(User user, String item, BigDecimal price) {
        Cart cart = userCartCache.get(user);
        if (cart == null) {
            cart = createCart(user);
        }
        
        cart.addItem(item, price);
        System.out.println("添加商品到购物车: " + item + ", 价格: " + price + ", 用户: " + user.getUsername());
    }

    @Override
    public void removeItemFromCart(User user, String item, BigDecimal price) {
        Cart cart = userCartCache.get(user);
        if (cart != null) {
            cart.removeItem(item, price);
            System.out.println("从购物车移除商品: " + item + ", 用户: " + user.getUsername());
        } else {
            System.out.println("警告：未找到用户购物车，无法移除商品。用户: " + user.getUsername());
        }
    }

    @Override
    public void clearCart(User user) {
        Cart cart = userCartCache.get(user);
        if (cart != null) {
            cart.clearCart();
            System.out.println("清空购物车，用户: " + user.getUsername());
        } else {
            System.out.println("警告：未找到用户购物车，无法清空。用户: " + user.getUsername());
        }
    }

    @Override
    public void clearUserCache(User user) {

        Cart removedCart = userCartCache.remove(user);
        
        if (removedCart != null) {
            System.out.println("成功清理用户缓存: " + user.getUsername());
        } else {
            System.out.println("警告：无法清理用户缓存，可能导致内存泄漏！用户: " + user.getUsername());
            System.out.println("当前缓存中的所有用户:");
            userCartCache.keySet().forEach(u -> 
                System.out.println("  - " + u.getUsername() + " (id=" + u.getId() + ", hashCode=" + u.hashCode() + ")")
            );
        }
        
        System.out.println("清理后缓存大小: " + userCartCache.size());
    }

    @Override
    public String getCacheStats() {
        StringBuilder stats = new StringBuilder();
        stats.append("购物车缓存统计信息:\n");
        stats.append("缓存大小: ").append(userCartCache.size()).append("\n");
        stats.append("缓存内容:\n");
        
        userCartCache.forEach((user, cart) -> {
            stats.append("  用户: ").append(user.getUsername())
                 .append(" (id=").append(user.getId())
                 .append(", hashCode=").append(user.hashCode())
                 .append(") -> 购物车商品数: ").append(cart.getItemCount())
                 .append(", 总金额: ").append(cart.getTotalAmount())
                 .append("\n");
        });
        
        return stats.toString();
    }
}