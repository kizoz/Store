package com.store.demo.domain;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "product_type")
public class TypeOfProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String type;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "type")
    private List<Product> products;

    public TypeOfProduct() {
    }

    @Override
    public String toString() {
        return "TypeOfProduct{" +
                "id=" + id +
                ", type='" + type + '\'' +
                '}';
    }

    public TypeOfProduct(String type) {
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
