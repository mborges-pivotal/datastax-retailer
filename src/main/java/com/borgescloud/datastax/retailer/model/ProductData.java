package com.borgescloud.datastax.retailer.model;

import lombok.Data;

@Data
public class ProductData {

    private int count;
    private Product[] data;
    
}
