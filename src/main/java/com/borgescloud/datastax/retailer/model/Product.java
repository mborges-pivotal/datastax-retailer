package com.borgescloud.datastax.retailer.model;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.Data;

@Data
public class Product {

    @JsonAlias("uniq_id")
    private String uniqId;
    private String sku;
    @JsonAlias("name_title")
    private String nameTitle;
    private String category;
    @JsonAlias("list_price")
    private float listPrice;
    @JsonAlias("sale_price")
    private float salePrice;

    public String getPrimaryKey() {
        return String.format("%s/%s", uniqId, sku);
    }    
    
}
