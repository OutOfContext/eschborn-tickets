package com.primiq.backend.model.dao;

import com.primiq.backend.model.dao.base.AbstractUser;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "app_users")
public class AppUser extends AbstractUser {

  @Column(nullable = false, unique = true)
  private String username;

  @Column(nullable = false)
  private boolean enabled = true;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "app_user_roles", joinColumns = @JoinColumn(name = "app_user_id"))
  @Column(name = "role_name", nullable = false)
  private Set<String> roles = new HashSet<>();
}

