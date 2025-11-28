package com.datta.amazon.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.datta.amazon.model.CartItem;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

	List<CartItem> findByCustomerId(Long Id);

	void deleteByCustomerId(Long customerId);

	Optional<CartItem> findByCustomerIdAndProductId(Long customerId, Long productId); // Note: _Id for foreign key

}
