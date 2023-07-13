package com.srdevpereira.repository;

import com.srdevpereira.DTO.CoinTrasationDTO;
import com.srdevpereira.entities.Coin;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class CoinRepository {

    private EntityManager entityManager;

    public CoinRepository(JdbcTemplate jdbcTemplate) {
        this.entityManager = entityManager;
    }

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
        Object[] attr = new Object[]{name};
        return jdbcTemplate.query(SELECT_BY_NAME, new RowMapper<Coin>() {
            @Override
            public Coin mapRow(ResultSet rs, int rowNum) throws SQLException {
                Coin coin = new Coin();
                coin.setId(rs.getInt("id"));
                coin.setName(rs.getString("name"));
                coin.setPrice(rs.getBigDecimal("price"));
                coin.setQuantity(rs.getBigDecimal("quantity"));
                coin.setDateTime(rs.getTimestamp("datetime"));

                return coin;
            }
        }, attr);
    }

    public int remove(int id){
        return jdbcTemplate.update(DELETE, id);
    }

}
