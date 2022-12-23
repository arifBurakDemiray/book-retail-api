package com.bookretail.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.GrantedAuthority;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ERole implements GrantedAuthority {
    private String authority;

    public static final String SYSADMIN = "ROLE_SYSADMIN";
    public static final String USER = "ROLE_USER";

    private static final Map<String, Integer> roles = Map.of(
            SYSADMIN, 0,
            USER, 1
    );

    private static final Map<String, Integer> webRoles = Map.of(
            SYSADMIN, roles.get(SYSADMIN)
    );

    private static final Map<String, Integer> mobileRoles = Map.of(
            USER, roles.get(USER)
    );

    public static int valueOf(@NotNull String roleName) {
        return roles.getOrDefault(roleName, -1);
    }

    public static String stringValueOf(int roleId) {
        if (roleId > -1 && roles.size() > roleId) {
            return roles.keySet().stream().filter(role -> valueOf(role) == roleId).findFirst().orElse("");
        } else {
            return "";
        }
    }

    public static boolean isWebUser(String role) {
        return webRoles.containsKey(role);
    }

    public static boolean isMobileUser(String role) {
        return mobileRoles.containsKey(role);
    }

    @Override
    public String getAuthority() {
        return authority;
    }
}
