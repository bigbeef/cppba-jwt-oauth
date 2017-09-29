package com.cppba.core.bean;

import java.util.Arrays;

public class JwtUser {

    private Long id;

    private String userName;

    private String[] roles;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String[] getRoles() {
        return roles;
    }

    public void setRoles(String[] roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "UserJwt{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", roles=" + Arrays.toString(roles) +
                '}';
    }
}
