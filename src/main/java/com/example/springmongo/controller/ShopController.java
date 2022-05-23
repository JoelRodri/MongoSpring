package com.example.springmongo.controller;

import com.example.springmongo.model.Product;
import com.example.springmongo.model.Shop;
import com.example.springmongo.repositories.ShopDao;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.*;

@Controller
public class ShopController {
    ShopDao shopDao;
    ProductController productController;
    @Autowired
    public ShopController(ShopDao shopDao, ProductController productController) {
        this.shopDao = shopDao;
        this.productController = productController;
    }

    public List<Shop> getAllShops() {
        return shopDao.findAll();
    }

    public Shop shopUser(int id) {
        return shopDao.findById(id).get();
    }

    public void addShop(Shop shop) {
        List<Product> products = shop.getProducts();
        productController.addAllProducts(products);
        shopDao.save(shop);
    }

    public void deleteShop(int id) {
        Shop shop = shopUser(id);
        shopDao.delete(shop);
    }

    public void putShop(Shop shop, int id) {

        Shop real = shopUser(id);
        real.setEmail(shop.getEmail());
        real.setName(shop.getName());

        List<Product> products = shop.getProducts();
        for (Product p: real.getProducts()){
            for (Product pp: products){
                if (p.getId() == pp.getId()){
                    p.setName(pp.getName());
                    p.setPrecio(pp.getPrecio());
                    p.setQuantity(pp.getQuantity());
                }
            }
        }
        productController.actualizarTodo(shop.getProducts());

        shopDao.save(real);
    }

    public void patchShop(int id, JsonPatch patch) throws JsonPatchException, JsonProcessingException {
        Shop shop = shopUser(id);
        Shop shopPatched = applyPatch(patch, shop);

        if (shop.getProducts().size() == shopPatched.getProducts().size()){
            productController.actualizarTodo(shopPatched.getProducts());
        }else if (shop.getProducts().size() < shopPatched.getProducts().size()){
            productController.addAllProducts(shopPatched.getProducts());
        }

        shopDao.save(shopPatched);

    }

    private Shop applyPatch(JsonPatch patch, Shop targetShop) throws JsonPatchException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode patched = patch.apply(objectMapper.convertValue(targetShop, JsonNode.class));
        return objectMapper.treeToValue(patched, Shop.class);
    }

    public void addProduct(Product product, int id) {
        Shop u = shopUser(id);
        productController.añadir(product);
        Product p = productController.getProduct(product.getId());
        boolean encontrar = false;
        for (Product pp : u.getProducts()){
            if (pp.getId() == p.getId()) {
                encontrar = true;
                break;
            }
        }
        if (!encontrar){
            u.addProduct(p);
        }
        shopDao.save(u);
    }

    public void deleteProductOnShop(int id, int index) {
        Shop u = shopUser(id);
        u.getProducts().remove(index);
        shopDao.save(u);
    }

}
    /*
    {
        "op":"replace",
        "path":"/products/0/name",
        "value":"afafqwr"
    }
    {
         "op":"add",
        "path":"/products/0",
        "value": {"id": 4}
    }
    {
        "op":"remove",
        "path":"/fullName"
    }
    {
        "op":"move",
        "from":"/products/0",
        "path":"/products/2"
    }
    {
        "op":"copy",
        "from":"/products/0",
        "path":"/products/3"
    }
    {
        "op":"test",
        "path":"/fullName",
        "value":"Joel"
    }
*/