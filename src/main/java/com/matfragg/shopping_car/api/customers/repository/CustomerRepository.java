package com.matfragg.shopping_car.api.customers.repository;

import com.matfragg.shopping_car.api.authentication.model.entities.User;
import com.matfragg.shopping_car.api.customers.model.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    @Query("SELECT c FROM Customer c WHERE c.user.id = :userId")
    Optional<Customer> findByUserId(@Param("userId") Long userId);
    @Query("SELECT COUNT(c) > 0 FROM Customer c WHERE c.user.id = :userId")
    boolean existsByUserId(@Param("userId") Long userId);
    boolean existsByUser(User user);

}
