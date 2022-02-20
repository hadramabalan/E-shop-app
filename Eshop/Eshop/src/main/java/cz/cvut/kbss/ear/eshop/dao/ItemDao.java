package cz.cvut.kbss.ear.eshop.dao;

import cz.cvut.kbss.ear.eshop.model.Item;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Objects;

@Repository
public class ItemDao {

    @PersistenceContext
    private EntityManager em;

    public Item find(Integer id) {
        Objects.requireNonNull(id);
        return em.find(Item.class, id);
    }
}
