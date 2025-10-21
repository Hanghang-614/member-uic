package com.wch.member.controller;

import com.wch.member.entity.User;
import com.wch.member.entity.Cart;
import com.wch.member.service.CartService;
import com.wch.member.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;


@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "*")
public class CartController {
    
    @Autowired
    private CartService cartService;
    
    @Autowired
    private UserService userService;
    

    @PostMapping("/login/{username}")
    public ResponseEntity<String> userLogin(@PathVariable String username) {
        Optional<User> userOpt = userService.findByUsername(username);
        if (!userOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        User user = userOpt.get();


        Optional<Cart> existingCart = cartService.getCartByUser(user);
        if (!existingCart.isPresent()) {
            cartService.createCart(user);
            return ResponseEntity.ok("用户 " + username + " 登录成功，创建了新的购物车");
        } else {
            return ResponseEntity.ok("用户 " + username + " 登录成功，找到了已存在的购物车");
        }
    }


    @PostMapping("/add/{username}")
    public ResponseEntity<String> addItem(
            @PathVariable String username,
            @RequestParam String item,
            @RequestParam BigDecimal price) {

        Optional<User> userOpt = userService.findByUsername(username);
        if (!userOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        User user = userOpt.get();
        cartService.addItemToCart(user, item, price);

        return ResponseEntity.ok("商品 " + item + " 已添加到 " + username + " 的购物车");
    }


    @PostMapping("/remove/{username}")
    public ResponseEntity<String> removeItem(
            @PathVariable String username,
            @RequestParam String item,
            @RequestParam BigDecimal price) {

        Optional<User> userOpt = userService.findByUsername(username);
        if (!userOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        User user = userOpt.get();
        cartService.removeItemFromCart(user, item, price);

        return ResponseEntity.ok("商品 " + item + " 已从 " + username + " 的购物车移除");
    }


    @GetMapping("/{username}")
    public ResponseEntity<Cart> getCart(@PathVariable String username) {
        Optional<User> userOpt = userService.findByUsername(username);
        if (!userOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        User user = userOpt.get();
        Optional<Cart> cart = cartService.getCartByUser(user);

        return cart.map(ResponseEntity::ok)
                  .orElse(ResponseEntity.notFound().build());
    }


    @PostMapping("/clear/{username}")
    public ResponseEntity<String> clearCart(@PathVariable String username) {
        Optional<User> userOpt = userService.findByUsername(username);
        if (!userOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        User user = userOpt.get();
        cartService.clearCart(user);

        return ResponseEntity.ok("用户 " + username + " 的购物车已清空");
    }


    @PostMapping("/logout/{username}")
    public ResponseEntity<String> userLogout(@PathVariable String username) {
        Optional<User> userOpt = userService.findByUsername(username);
        if (!userOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        User user = userOpt.get();


        cartService.clearUserCache(user);

        return ResponseEntity.ok("用户 " + username + " 已登出");
    }


    @GetMapping("/cache/stats")
    public ResponseEntity<String> getCacheStats() {
        String stats = cartService.getCacheStats();
        return ResponseEntity.ok(stats);
    }


    @GetMapping("/demo/equals-hashcode-bug/{username}")
    public ResponseEntity<String> demonstrateEqualsHashCodeBug(@PathVariable String username) {
        Optional<User> userOpt = userService.findByUsername(username);
        if (!userOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        
        User user1 = userOpt.get();
        

        User user2 = new User(user1.getUsername(), user1.getEmail(), user1.getRealName(), user1.getIsHot());
        user2.setId(user1.getId());
        
        StringBuilder result = new StringBuilder();
        result.append("equals/hashCode 不一致问题演示:\n");
        result.append("User1: ").append(user1).append("\n");
        result.append("User2: ").append(user2).append("\n");
        result.append("user1.equals(user2): ").append(user1.equals(user2)).append("\n");
        result.append("user1.hashCode(): ").append(user1.hashCode()).append("\n");
        result.append("user2.hashCode(): ").append(user2.hashCode()).append("\n");
        result.append("hashCode 相等: ").append(user1.hashCode() == user2.hashCode()).append("\n");
        result.append("\n问题说明:\n");
        result.append("- equals 返回 true（基于 id 比较）\n");
        result.append("- 但 hashCode 可能不同（使用默认的 Object.hashCode()）\n");
        result.append("- 这违反了 equals/hashCode 契约\n");
        result.append("- 在 HashMap/ConcurrentHashMap 中会导致数据不一致和内存泄漏\n");
        
        return ResponseEntity.ok(result.toString());
    }
}