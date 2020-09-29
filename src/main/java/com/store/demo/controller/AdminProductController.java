package com.store.demo.controller;

import com.store.demo.DTO.AddProductDTO;
import com.store.demo.DTO.AddTypeDTO;
import com.store.demo.DTO.EditProductDTO;
import com.store.demo.domain.User;
import com.store.demo.service.AdminService;
import com.store.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public String addProduct(@RequestBody AddProductDTO productDTO){
        return String.format("Saved %s", service.addProduct(productDTO));
    }

    @PostMapping(path = "/addType")       // Add type of product
    public String addType(@RequestBody AddTypeDTO typeDTO){
        return String.format("New type was saved %s",service.addType(typeDTO));
    }

    @PostMapping(path="/edit")
    public String editProduct(@RequestBody EditProductDTO productDTO){
        return service.editProduct(productDTO);
    }

    @RequestMapping(path = "/delete/{id}")
    public String deleteProduct(@PathVariable("id")int id){
        return service.deleteById(id);
    }

    @GetMapping(path = "/getCustomer/{productName}")                                                    //  Get all customers who added {productName} no their cart
    public List<User> getCustomers(@PathVariable String productName, @RequestParam int page){
        return service.showCustomers(productName, page);
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
