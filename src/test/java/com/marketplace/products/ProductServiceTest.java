package com.marketplace.products;

import com.marketplace.products.model.Product;
import com.marketplace.products.repository.ProductRepository;
import com.marketplace.products.service.ProductService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.AssertionErrors;

import java.util.Arrays;
import java.util.Collection;

import static org.mockito.Mockito.doReturn;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @MockBean
    private ProductRepository productRepository;

    @Test
    @DisplayName("Find product with id successfuly")
    public void testFindProductById() {
        Product mockProduct = new Product(1, "Product", "Description", 10, 1);

        doReturn(mockProduct).when(productRepository).findProductById(1);

        Product foundProduct = productService.findById(1);

        Assertions.assertNotNull(foundProduct);
        Assertions.assertSame("Product", foundProduct.getName());
    }

    @Test
    @DisplayName("Fail to find product with id")
    public void testFailToFindProductById() {
        doReturn(null).when(productRepository).findProductById(1);

        Product foundProduct = productService.findById(1);

        Assertions.assertNull(foundProduct);
    }

    @Test
    @DisplayName("Find all products")
    public void testFindAllProducts() {
        Product firstProduct = new Product(1, "1st Product", "Description", 10, 1);
        Product secondProduct = new Product(2, "2st Product", "Description", 10, 1);

        doReturn(Arrays.asList(firstProduct, secondProduct)).when(productRepository).findAll();

        Iterable<Product> allProducts = productService.findAll();

        Assertions.assertEquals(2, ((Collection<?>) allProducts).size());
    }

    @Test
    @DisplayName("Save a new product succesfully")
    public void testSuccefulProduct() {
        Product newProduct = new Product(1, "New product", "Opis", 10, 1);

        doReturn(newProduct).when(productRepository).save(newProduct);

        Product saveProduct = productService.save(newProduct);

        Assertions.assertSame("New product", newProduct.getName());
        Assertions.assertEquals(1, newProduct.getId());

    }

    @Test
    @DisplayName("Update an existing product successfuly")
    public void testUpdatingProductSuccessfuly() {
        Product existingProduct = new Product(1, "Product", "Description", 10, 1);
        Product updateProduct = new Product(1, "New Name", "Description", 10, 2);

        doReturn(existingProduct).when(productRepository).findProductById(1);
        doReturn(updateProduct).when(productRepository).save(existingProduct);

        Product update = productService.update(existingProduct);

        Assertions.assertEquals("New Name", update.getName());
    }

    @Test
    @DisplayName("Fail to update an existing product")
    public void testFailToUpdateExistingProduct(){
        Product mockProduct = new Product(1, "Product", "Description", 10, 1);

        doReturn(null).when(productRepository).findProductById(1);

        Product updateProduct = productService.update(mockProduct);


        AssertionErrors.assertNull("Product should be null", updateProduct);

    }

}
