package com.matfragg.shopping_car.api.authentication.model.entities;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.matfragg.shopping_car.api.authentication.model.enums.Roles;
import com.matfragg.shopping_car.api.shared.model.BaseModel;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * User entity
 * <p>
 *     This class represents a user in the system.
 * </p>
 */
@Getter
@Setter
@Entity
@Table(name = "users")
public class User extends BaseModel {
    @NotBlank
    @Size(max = 50)
    @Column(unique = true)
    private String username;

    @NotBlank
    @Size(max = 120)
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Set<Roles> roles = new HashSet<>();

    public User() {
        this.roles = new HashSet<>();
    }
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.roles = new HashSet<>();
    }

    public User(String username, String password, List<Roles> roles) {
        this(username, password);
        addRoles(roles);
    }

    /**
     * Add a role to the user
     * @param role the role to add
     * @return the user with the added role
     */
    public User addRole(Roles role) {
        this.roles.add(role);
        return this;
    }

    public User addRoles(List<Roles> roles) {
        this.roles.addAll(roles);
        return this;
    }
}
