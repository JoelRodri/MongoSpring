package com.example.springmongo.resources;

import com.example.springmongo.controller.ShopController;
import com.example.springmongo.model.Product;
import com.example.springmongo.model.Shop;
import com.example.springmongo.service.SequenceGeneratorService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ShopResource.SHOP_RESOURCE)
public class ShopResource {
    public final static String SHOP_RESOURCE = "/shops";
    ShopController shopController;

    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;

    @Autowired
    public ShopResource(ShopController shopController) {
        this.shopController = shopController;
    }

    @GetMapping
    public List<Shop> shops(){
        return shopController.getAllShops();
    }

    @GetMapping("{id}")
    public Shop shop(@PathVariable("id") int id){
        return shopController.shopUser(id);
    }

    @PostMapping
    public void addShop(@RequestBody Shop shop){
        shop.setId(sequenceGeneratorService.generateSequence(Shop.SEQUENCE_NAME));
        shopController.addShop(shop);
    }

    @PostMapping("{id}")
    public void addProductOnShop(@RequestBody Product product, @PathVariable("id") int id){
        shopController.addProduct(product,id);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") int id){
        shopController.deleteShop(id);
    }

    @DeleteMapping("{id}/products/{index}")
    public void delete(@PathVariable("id") int id, @PathVariable("index") int index){
        shopController.deleteProductOnShop(id,index-1);
    }

    @PutMapping("{id}")
    public void putShop(@RequestBody Shop shop, @PathVariable("id") int id){
        shopController.putShop(shop, id);
    }

    @PatchMapping("{id}")
    public void patchShop(@PathVariable("id") int id, @RequestBody JsonPatch patch) throws JsonPatchException, JsonProcessingException {
        shopController.patchShop(id,patch);
    }
}

