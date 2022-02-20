package cz.cvut.kbss.ear.eshop.rest;

import cz.cvut.kbss.ear.eshop.exception.NotFoundException;
import cz.cvut.kbss.ear.eshop.model.Category;
import cz.cvut.kbss.ear.eshop.model.Product;
import cz.cvut.kbss.ear.eshop.service.CategoryService;
import cz.cvut.kbss.ear.eshop.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.List;


@RestController
@RequestMapping("/rest/categories")
public class CategoryController {

    private static final Logger LOG = LoggerFactory.getLogger(CategoryController.class);
    private final CategoryService service;
    private final ProductService productService;
    @Autowired
    public CategoryController(CategoryService service, ProductService productService) {
        this.service = service;
        this.productService = productService;
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Category getCategoryById(@PathVariable Integer id){
        final Category category = service.find(id);
        if(category == null){
            throw NotFoundException.create("Category", id);
        }
        return category;
    }

    @GetMapping(value = "/{id}/products", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Product> getProductByCategory(@PathVariable Integer id){
        final Category category = service.find(id);
        if(category == null){
            throw NotFoundException.create("Category", id);
        }
        return productService.findAll(category);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Category> getCategories(){
        return service.findAll();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createCategory(@RequestBody Category category) {
        service.persist(category);

        URI location = ServletUriComponentsBuilder.fromCurrentServletMapping().path("/rest/categories/{id}").build()
                .expand(category.getId()).toUri();

        HttpHeaders responseHeader = new HttpHeaders();
        responseHeader.setLocation(location);
        return new ResponseEntity<>(responseHeader, HttpStatus.CREATED);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping(value = "/{id}/products", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void addProductToCategory(@RequestBody Product product, @PathVariable Integer id) {
        Category category = service.find(id);
        if (category == null)
            throw NotFoundException.create("Category", id);

        productService.persist(product);
        service.addProduct(category, product);
    }
    
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{categoryId}/products/{productId}")
    public void removeProductFromCategory(@PathVariable("categoryId") Integer categoryId,
                                          @PathVariable("productId") Integer productId) {
        Category category = service.find(categoryId);
        if (category == null)
            throw NotFoundException.create("Category", categoryId);

        Product product = productService.find(productId);
        if (product == null)
            throw NotFoundException.create("Product", productId);

        service.removeProduct(category, product);
    }


}
