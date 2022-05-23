package com.example.springmongo.resources;

import com.example.springmongo.controller.ProductController;
import com.example.springmongo.model.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ProductResource.Product_RESOURCE)
public class ProductResource {
    public final static String Product_RESOURCE = "/products";

    ProductController productController;

    @Autowired
    public ProductResource(ProductController productController) {
        this.productController = productController;
    }

    @GetMapping
    public List<Product> products(){
        return productController.getAllProducts();
    }

    @GetMapping("{id}")
    public Product products(@PathVariable("id") int id){
        return productController.getProduct(id);
    }

    @PostMapping
    public void addProduct(@RequestBody Product product){
        productController.añadir(product);
    }

    @DeleteMapping("{id}")
    public void deleteProduct(@PathVariable("id") int id){
        productController.deleteById(id);
    }

    @PutMapping("{id}")
    public void putProduct(@PathVariable("id") int id, @RequestBody Product product){
        productController.actualizar(id,product);
    }

    @PatchMapping("{id}")
    public void patchProduct(@PathVariable("id") int id, @RequestBody JsonPatch patch) throws JsonPatchException, JsonProcessingException {
        productController.patchProduct(id,patch);
    }
}
