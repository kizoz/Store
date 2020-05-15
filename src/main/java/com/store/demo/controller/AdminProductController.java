package com.store.demo.controller;

import com.store.demo.domain.User;
import com.store.demo.service.AdminService;
import com.store.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;


@RestController
@RequestMapping(path = "/a")
public class AdminProductController {

    private final ProductService service;
    private final AdminService adminService;

    @Autowired
    public AdminProductController(ProductService service, AdminService adminService) {
        this.service = service;
        this.adminService = adminService;
    }

    @RequestMapping(path = "/add", method={RequestMethod.POST, RequestMethod.GET})
    public String addProduct(@RequestParam String name, @RequestParam Integer price, @RequestParam(defaultValue = "comp") String type){
        service.addProduct(name, price, type);
        return String.format("Saved %s %s %s", name, price, type);
    }

    @RequestMapping(path = "/addt", method = {RequestMethod.POST, RequestMethod.GET})       // Add type of product
    public String addType(@RequestParam String type){
        service.addType(type);
        return String.format("New type was saved %s",type);
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

    @GetMapping(path = "/getcust/{productName}")                                                    //  Get all customers who added {productName} no their cart
    public Iterable<String> getCustomers(@PathVariable String productName, @RequestParam int page){
        return service.showCustomers(productName, page).stream()
                .map(User::toString)
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/ban/{username}")
    public void banUser(@PathVariable String username){
        adminService.banUser(username);
    }

    @GetMapping(path = "/unban/{username}")
    public void unBanUser(@PathVariable String username){
        adminService.unBanUser(username);
    }
}
