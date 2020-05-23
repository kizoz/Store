package com.store.demo.DTO;

import com.store.demo.domain.TypeOfProduct;

public class OutputProductDTO {

    private String name;
    private Integer price;
    private String type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(TypeOfProduct typeOfProduct) {
        this.type = typeOfProduct.getType();
    }

    @Override
    public String toString() {
        return "Product {" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", type='" + type + '\'' +
                '}';
    }
}
