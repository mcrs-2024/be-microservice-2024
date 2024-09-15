package com.microservice.admin.service.impl;

import com.microservice.admin.entitys.Users;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class UserPrincipal implements UserDetails {

    private Users user; // Thực thể người dùng từ cơ sở dữ liệu

    public UserPrincipal(Users user) {
        this.user = user;
    }

    public static UserPrincipal create(Users user) {
        return new UserPrincipal(user);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Trả về quyền hạn của người dùng
        // Ví dụ: bạn có thể trả về quyền hạn từ cơ sở dữ liệu
        return null;
    }

    @Override
    public String getPassword() {
        return user.getHashPassword(); // Trả về mật khẩu đã được mã hóa của người dùng
    }

    @Override
    public String getUsername() {
        return user.getUsername(); // Trả về tên đăng nhập của người dùng
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Kiểm tra hết hạn tài khoản (tùy thuộc vào yêu cầu của bạn)
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Kiểm tra khóa tài khoản (tùy thuộc vào yêu cầu của bạn)
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Kiểm tra hết hạn thông tin đăng nhập (tùy thuộc vào yêu cầu của bạn)
    }

    @Override
    public boolean isEnabled() {
        return true; // Kiểm tra tài khoản có đang kích hoạt không
    }
}
