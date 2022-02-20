package cz.cvut.kbss.ear.eshop.service;

import cz.cvut.kbss.ear.eshop.dao.UserDao;
import cz.cvut.kbss.ear.eshop.model.Cart;
import cz.cvut.kbss.ear.eshop.model.User;
import cz.cvut.kbss.ear.eshop.util.Constants;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserDao dao;

    final User currentUser = new User(); // singleton simulating logged-in user

    @Autowired
    public UserService(UserDao dao) {
        this.dao = dao;
    }

    @Transactional
    public void persist(User user) {
        Objects.requireNonNull(user);
        if (user.getRole() == null) {
            user.setRole(Constants.DEFAULT_ROLE);
        }
        createCart(user);
        dao.persist(user);
    }

    private void createCart(User user) {
        // Admin cannot shop under their administrator account
        if (!user.isAdmin()) {
            user.setCart(new Cart());
        }
    }

    @Transactional(readOnly = true)
    public boolean exists(String username) {
        return dao.findByUsername(username) != null;
    }

    /**
     * Gets the currently logged-in user's cart.
     *
     * @return Current user's cart
     */
    public Cart getCurrentUserCart() {
        return currentUser.getCart();
    }
}
