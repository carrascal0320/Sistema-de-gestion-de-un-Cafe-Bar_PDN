package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false)
  private String email;

  @Column(nullable = false)
  private String password;

  @Column(nullable = true)
  private String name;

  @Column(unique = true, nullable = true)
  private String phone;

  // Constructor vacío
  public User() {}

  //  para usuarios registrados con email y password
  public User(String email, String password, String name, String phone) {
    this.email = email;
    this.password = password;
    this.name = name;
    this.phone = phone;
  }

  //   (GitHub/Google)
  public User(String email, String name) {
    this.email = email;
    this.name = (name != null && !name.isEmpty()) ? name : "Usuario desconocido";
    this.password = "OAuthUser";
    this.phone = null;
  }

}
