package com.imarium.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED) // 상속 구조를 위한 전략 설정
@Getter
@Setter
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Boolean isSavedLogin;

    @Column(nullable = false)
    private String name;

    // 연관 관계 설정 (One-to-Many)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<SearchHistory> searchHistories;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Review> reviews;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Col> columns;

    @Column(nullable = false)
    private Boolean isArtist; // This field indicates if the user is an artist

    public boolean isArtist() {
        return isArtist != null && isArtist;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Return user roles or permissions as GrantedAuthority
        return List.of(() -> "ROLE_" + (isArtist() ? "ARTIST" : "USER")); // Example role assignment
    }

    @Override
    public String getUsername() {
        return name;
    }
}

