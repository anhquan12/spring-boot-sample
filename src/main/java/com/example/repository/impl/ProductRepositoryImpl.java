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
        String baseSql = "from Product p where p.name like :name and p.price like :price order by p.name desc";
        String sql = "select p " + baseSql;
        String sqlCount = "select count(p) " + baseSql;
        Query query = entityManager.createQuery(sql, Product.class);
        Query queryCount = entityManager.createQuery(sqlCount, Long.class);
        query.setParameter("name", "%" +builder.getName()+"%");
        query.setParameter("price", "%" +builder.getPrice()+"%");
        queryCount.setParameter("name", "%" +builder.getName()+"%");
        queryCount.setParameter("price", "%" +builder.getPrice()+"%");
        Long total = (Long) queryCount.getSingleResult();
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        List<Product> list =  query.getResultList();
        return new PageImpl<>(list, pageable, total);
    }
}
