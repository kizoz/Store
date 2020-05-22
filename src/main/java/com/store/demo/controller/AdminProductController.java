package com.store.demo.controller;

import com.store.demo.domain.Product;
import com.store.demo.domain.TypeOfProduct;
import com.store.demo.domain.User;
import com.store.demo.service.AdminService;
import com.store.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;


@RestController
@RequestMapping(path = "/adm")
public class AdminProductController {

    private final ProductService service;
    private final AdminService adminService;

    @Autowired
    public AdminProductController(ProductService service, AdminService adminService) {
        this.service = service;
        this.adminService = adminService;
    }

    @PostMapping(path = "/add")
    public String addProduct(@RequestBody Product product){
        return String.format("Saved %s", service.addProduct(product));
    }

    @PostMapping(path = "/addType")       // Add type of product
    public String addType(@RequestBody TypeOfProduct type){
        return String.format("New type was saved %s",service.addType(type));
    }

    @RequestMapping(path="/edit/{id}")
    public String editProduct(@PathVariable("id") int id,
                              @RequestParam String name,
                              @RequestParam Integer price,
                              @RequestParam(defaultValue = "comp") String type){
        return service.editProduct(id, name, price, type);
    }

    @RequestMapping(path = "/delete/{id}")
    public String deleteProduct(@PathVariable("id")int id){
        return service.deleteById(id);
    }

    @GetMapping(path = "/getCustomer/{productName}")                                                    //  Get all customers who added {productName} no their cart
    public Iterable<String> getCustomers(@PathVariable String productName, @RequestParam int page){
        return service.showCustomers(productName, page).stream()
                .map(User::toString)
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/ban/{username}")
    public void banUser(@PathVariable String username){
        adminService.banUser(username);
    }

    @GetMapping(path = "/unBan/{username}")
    public void unBanUser(@PathVariable String username){
        adminService.unBanUser(username);
    }
}
