package cz.cvut.kbss.ear.eshop.service;

import cz.cvut.kbss.ear.eshop.environment.Generator;
import cz.cvut.kbss.ear.eshop.model.Category;
import cz.cvut.kbss.ear.eshop.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class CategoryServiceTest {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private CategoryService sut;

    @Test
    public void addProductAddsProductToTargetCategory() {
        final Product p = Generator.generateProduct();
        final Category category = new Category();
        category.setName("test");
        em.persist(p);
        sut.persist(category);
        sut.addProduct(category, p);

        final Product result = em.find(Product.class, p.getId());
        assertTrue(result.getCategories().stream().anyMatch(c -> c.getId().equals(category.getId())));
    }

    @Test
    public void addProductAddsProductToAnotherCategory() {
        final Product p = Generator.generateProduct();
        final Category catOne = new Category();
        catOne.setName("Category one");
        p.addCategory(catOne);
        em.persist(p);
        em.persist(catOne);
        final Category catTwo = new Category();
        catTwo.setName("Category two");
        em.persist(catTwo);

        sut.addProduct(catTwo, p);
        final Product result = em.find(Product.class, p.getId());
        assertTrue(result.getCategories().stream().anyMatch(c -> c.getId().equals(catOne.getId())));
        assertTrue(result.getCategories().stream().anyMatch(c -> c.getId().equals(catTwo.getId())));
    }

    @Test
    public void removeProductRemovesProductFromCategory() {
        final Product p = Generator.generateProduct();
        final Category category = new Category();
        category.setName("test");
        p.addCategory(category);
        em.persist(p);
        em.persist(category);
        sut.removeProduct(category, p);

        final Product result = em.find(Product.class, p.getId());
        assertFalse(result.getCategories().stream().anyMatch(c -> c.getId().equals(category.getId())));
    }
}
