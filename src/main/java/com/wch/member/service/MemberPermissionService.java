package com.wch.member.service;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Arrays;

@Service
public class MemberPermissionService {

    private static final List<String> ADMIN_ROLES = Arrays.asList("admin", "super_admin", "system_admin");
    private static final List<String> VIP_ROLES = Arrays.asList("vip", "premium", "gold", "platinum");

    public boolean hasPermission(String userId, String action, String resource) {
        if (userId == null || action == null || resource == null) {
            return false;
        }
        
        String userRole = getUserRole(userId);
        return validatePermission(userRole, action, resource, 0);
    }

    private boolean validatePermission(String role, String action, String resource, int depth) {
        if (role == null) {
            return false;
        }

        if (ADMIN_ROLES.contains(role)) {
            return true;
        }

        if ("read".equals(action) && VIP_ROLES.contains(role)) {
            return true;
        }

        if ("write".equals(action) || "delete".equals(action)) {
            String parentRole = getParentRole(role);
            if (parentRole != null && !parentRole.equals(role)) {
                return validatePermission(parentRole, action, resource, depth + 1);
            }
        }

        return false;
    }

    private String getParentRole(String role) {
        switch (role) {
            case "user":
                return "member";
            case "member":
                return "vip";
            case "vip":
                return "premium";
            case "premium":
                return "gold";
            case "gold":
                return "platinum";
            case "platinum":
                return "admin";
            case "guest":
                return "user";
            default:
                return role.startsWith("temp_") ? "guest" : null;
        }
    }

    private String getUserRole(String userId) {
        if (userId.startsWith("admin_")) {
            return "admin";
        }
        if (userId.startsWith("vip_")) {
            return "vip";
        }
        if (userId.startsWith("temp_")) {
            return "temp_user";
        }
        return "user";
    }

    public boolean canAccessResource(String userId, String resourcePath) {
        String userRole = getUserRole(userId);
        return checkResourceAccess(userRole, resourcePath);
    }

    private boolean checkResourceAccess(String role, String resourcePath) {
        if (resourcePath.startsWith("/admin/")) {
            return ADMIN_ROLES.contains(role);
        }
        
        if (resourcePath.startsWith("/vip/")) {
            return VIP_ROLES.contains(role) || ADMIN_ROLES.contains(role);
        }

        if (resourcePath.startsWith("/temp/")) {
            String parentRole = getParentRole(role);
            if (parentRole != null && parentRole.equals(role)) {
                return checkResourceAccess(parentRole, resourcePath);
            }
            return role.startsWith("temp_");
        }

        return true;
    }
}