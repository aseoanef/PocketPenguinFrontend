package com.example.pocketpenguin;

public class Products {
    private Integer id;
    private String name;
    private Float price;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Products(Integer id, String name, Float price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

}
