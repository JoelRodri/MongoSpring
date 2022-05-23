package com.example.springmongo.controller;

import com.example.springmongo.model.Product;
import com.example.springmongo.repositories.ProductDao;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class ProductController {
    ProductDao productDao;


    @Autowired
    public ProductController(ProductDao productDao) {
        this.productDao = productDao;
    }

    public List<Product> getAllProducts() {
        return productDao.findAll();
    }

    public void addAllProducts(List<Product> products) {
        productDao.saveAll(products);
    }

    public void añadir(Product product){
        if (!productDao.existsById(product.getId())){
            productDao.save(product);
        }
    }

    public Product getProduct(int id){
        return productDao.findById(id).get();
    }

    public void deleteById(int id){
        productDao.deleteById(id);
    }

    public void actualizar(int id, Product product) {
        Product real = getProduct(id);

        real.setName(product.getName());
        real.setPrecio(product.getPrecio());
        real.setQuantity(product.getQuantity());

        productDao.save(real);
    }

    public void actualizarTodo(List<Product> products){
        for (Product p : getAllProducts()){
            for (Product pp: products){
                if (p.getId() == pp.getId()){
                    p.setName(pp.getName());
                    p.setPrecio(pp.getPrecio());
                    p.setQuantity(pp.getQuantity());
                    productDao.save(p);
                }
            }
        }
    }

    public void patchProduct(int id, JsonPatch patch) throws JsonPatchException, JsonProcessingException {
        Product product = getProduct(id);
        Product productPatched = applyPatch(patch, product);

        productDao.save(productPatched);

    }

    private Product applyPatch(JsonPatch patch, Product targetProduct) throws JsonPatchException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode patched = patch.apply(objectMapper.convertValue(targetProduct, JsonNode.class));
        return objectMapper.treeToValue(patched, Product.class);
    }

}
