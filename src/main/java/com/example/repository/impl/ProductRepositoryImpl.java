package com.example.repository.impl;

import com.example.builder.ProductBuilder;
import com.example.entity.Product;
import com.example.repository.ProductRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductRepositoryImpl implements ProductRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public Page<Product> findAll2(ProductBuilder builder, Pageable pageable) {
        String sql = "select p from Product p where p.name like :name and p.price like :price order by p.name desc ";
        Query query = entityManager.createQuery(sql, Product.class);
        query.setParameter("name", "%" +builder.getName()+"%");
        query.setParameter("price", "%" +builder.getPrice()+"%");
        String sqlCount = "SELECT\n" +
            "\tcount(id) from ( SELECT * FROM product WHERE NAME LIKE :name AND price LIKE :price ORDER BY NAME ASC ) as total\n";
        Query queryCount = entityManager.createNativeQuery(sqlCount);
        queryCount.setParameter("name", "%" +builder.getName()+"%");
        queryCount.setParameter("price", "%" +builder.getPrice()+"%");
        BigInteger total =(BigInteger) queryCount.getSingleResult();
        List<Product> list =  query.getResultList();
        int start = (int) pageable.getOffset();
        int end = (start + pageable.getPageSize()) > list.size() ? list.size() : (start + pageable.getPageSize());
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        return new PageImpl<>(list.subList(start, end), pageable, total.longValue());
    }
}
