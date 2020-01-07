package com.example.repository;

import com.example.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProductRepository
        extends PagingAndSortingRepository<Product, Integer> , ProductRepositoryCustom{


    Page<Product> findProductsByPrice(int price, Pageable pageable);

    Page<Product> findProductsByStatus(int status, Pageable pageable);

    Page<Product> findAllByNameAndPriceOrderByNameAsc(String name, int price, Pageable pageable);

    @Query(value = "from Product p where :name is null or p.name like %:name% " +
                    "and p.price like %:price% order by p.name desc ")
    Page<Product> findByNameAndPrice(String name, String price, Pageable pageable);
}
