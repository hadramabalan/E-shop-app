package cz.cvut.kbss.ear.eshop.rest;

import cz.cvut.kbss.ear.eshop.model.Cart;
import cz.cvut.kbss.ear.eshop.model.CartItem;
import cz.cvut.kbss.ear.eshop.service.CartService;
import cz.cvut.kbss.ear.eshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/cart")
public class CartController {

    private final CartService cartService;

    private final UserService userService;

    @Autowired
    public CartController(CartService cartService, UserService userService) {
        this.cartService = cartService;
        this.userService = userService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Cart getCart() {
        return userService.getCurrentUserCart();
    }

    @PutMapping(value = "/items", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addItem(@RequestBody CartItem item) {
        final Cart cart = getCart();
        cartService.addItem(cart, item);
    }

    @DeleteMapping(value = "/items", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void removeItem(@RequestBody CartItem item) {
        final Cart cart = getCart();
        cartService.removeItem(cart, item);
    }
}
