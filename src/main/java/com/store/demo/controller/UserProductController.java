package com.store.demo.controller;

import com.store.demo.domain.Product;
import com.store.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping
public class UserProductController {

    private final UserService service;

    @Autowired
    public UserProductController(UserService service) {
        this.service = service;
    }

    @GetMapping(path = "/all/{page}")
    public Iterable<String> showAllPageable(@PathVariable int page) {
        return service.findAllPage(page).stream()
                .map(Product::toString)
                .collect(Collectors.toList());
    }

    @RequestMapping(path = "/get/{id}")
    public String getProductById(@PathVariable("id")int id){
        if(service.getById(id)!=null)
            return service.getById(id).toString();
        else return "Product does not exist";
    }

    @GetMapping(path = "/getByType/{type}")                 // Getting products by type
    public Iterable<String> getByType(@PathVariable String type, @RequestParam int page){
        return service.getByType(type, page).stream()
                .map(Product::toString)
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/getOrder/{page}")                // Getting orders for particular customer
    public Iterable<String> getOrders(@PathVariable int page){
        return service.showOrders(page).stream()
                .map(Product::toString)
                .collect(Collectors.toList());
    }

    @PostMapping(path = "/addOrder")
    public String addOrder(@RequestParam String productName){
        service.addOrder(productName);
        return "Order was added";
    }
}