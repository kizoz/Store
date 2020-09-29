package com.store.demo.controller;

import com.store.demo.DTO.OutputProductDTO;
import com.store.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class UserProductController {

    private final UserService service;

    @Autowired
    public UserProductController(UserService service) {
        this.service = service;
    }

    @GetMapping(path = "/all/{page}")
    public List<OutputProductDTO> showAllPageable(@PathVariable int page) {
        return service.findAllPage(page);
    }

    @RequestMapping(path = "/get/{id}")
    public OutputProductDTO getProductById(@PathVariable("id")int id){
        if(service.getById(id)!=null)
            return service.getById(id);
        else throw new IllegalArgumentException("Product does not exist");
    }

    @GetMapping(path = "/getByType/{type}")                 // Getting products by type
    public List<OutputProductDTO> getByType(@PathVariable String type, @RequestParam int page){
        return service.getByType(type, page);
    }

    @GetMapping(path = "/getOrder/{page}")                  // Getting orders for particular customer
    public List<OutputProductDTO> getOrders(@PathVariable int page){
        return service.showOrders(page);
    }

    @PostMapping(path = "/addOrder")
    public String addOrder(@RequestParam String productName){
        return String.format("%s was added to your cart", service.addOrder(productName));
    }
}