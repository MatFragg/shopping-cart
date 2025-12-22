package com.matfragg.shopping_car.api.orders.repository;

import com.matfragg.shopping_car.api.authentication.model.entities.User;
import com.matfragg.shopping_car.api.customers.model.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    boolean existsByUser(User user);

    @Query("SELECT c FROM Customer c WHERE c.user.id = :userId")
    Optional<Customer> findByUserId(@Param("userId") Long userId);

    @Query("SELECT c FROM Customer c JOIN FETCH c.user WHERE c.user.id = :userId")
    Optional<Customer> findByUserIdWithUser(@Param("userId") Long userId);

    Optional<Customer> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);
}