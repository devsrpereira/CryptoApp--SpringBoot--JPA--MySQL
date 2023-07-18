package com.srdevpereira.repository;

import com.srdevpereira.DTO.CoinTrasationDTO;
import com.srdevpereira.entities.Coin;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class CoinRepository {

    @Autowired
    private EntityManager entityManager;

    @Transactional
    public Coin insert (Coin coin){
        entityManager.persist(coin);
        return coin;
    }

    @Transactional
    public Coin update(Coin coin){
        entityManager.merge(coin);
        return coin;
    }

    public List<CoinTrasationDTO> getAll(){
        String jpql = "select new com.srdevpereira.DTO.CoinTrasationDTO(c.name, sum(c.quantity)) from Coin c group by c.name";
        TypedQuery<CoinTrasationDTO> query = entityManager.createQuery(jpql, CoinTrasationDTO.class);
        return query.getResultList();
    }
    public List<Coin> getByName(String name){
        String jqpl = "select c from Coin c where c.name like :name";
        TypedQuery<Coin> query = entityManager.createQuery(jqpl, Coin.class);
        query.setParameter("name", "%"+name+"%"); //facilita a busca pois localiza qualquer parte do texto como "NAME"
        return query.getResultList();
    }

/*
    public int remove(int id){
        return jdbcTemplate.update(DELETE, id);
    }
*/
}
