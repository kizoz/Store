package com.store.demo.domain;

import javax.persistence.*;

@Entity
@Table(name = "product_type")
public class TypeOfProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String type;

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
}
