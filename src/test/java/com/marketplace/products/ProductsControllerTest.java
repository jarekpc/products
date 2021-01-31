package com.marketplace.products;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.products.model.Product;
import com.marketplace.products.service.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ProductsControllerTest {

    @MockBean
    private ProductService productService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Test product found - GET /products/1")
    public void testGetProductByIdFindsProduct() throws Exception {

        //prepare mock product
        Product mockProduct = new Product(1, "My product", "Details of my product", 5, 1);

        //prepare mocked serviced method
        doReturn(mockProduct).when(productService).findById(mockProduct.getId());

        //perform Get method
        mockMvc.perform(MockMvcRequestBuilders.get("/products/{id}", 1))
                //validate 200 OK and json response
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))

                //validate response header
                .andExpect(header().string(HttpHeaders.ETAG, "\"1\""))
                .andExpect(header().string(HttpHeaders.LOCATION, "/products/1"))

                //validate response body
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("My product")))
                .andExpect(jsonPath("$.description", is("Details of my product")))
                .andExpect(jsonPath("$.quantity", is(5)))
                .andExpect(jsonPath("$.version", is(1)));

    }

    @Test
    @DisplayName("Add a new product - POST /products")
    public void testAddNewProduct() throws Exception {
        //prepare mock product
        Product newProduct = new Product("New Product", "New Product Description", 8);
        Product mockProduct = new Product(1, "New Product", "New Product Description", 8, 1);

        //prepare mock service method
        doReturn(mockProduct).when(productService).save(ArgumentMatchers.any());

        //Perform POST request
        mockMvc.perform(MockMvcRequestBuilders.post("/products")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(newProduct)))

                //Validare 201 C
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))

                //Validate response headers
                .andExpect(header().string(HttpHeaders.ETAG, "\"1\""))
                .andExpect(header().string(HttpHeaders.LOCATION, "/products/1"))

                //validate response body
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("New Product")))
                .andExpect(jsonPath("$.quantity",is(8)))
                .andExpect(jsonPath("$.version", is(1)));

    }


}
