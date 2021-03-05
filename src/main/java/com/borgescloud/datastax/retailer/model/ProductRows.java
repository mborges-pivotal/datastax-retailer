package com.borgescloud.datastax.retailer.model;

import lombok.Data;

@Data
public class ProductRows {

    private int count;
    private Product[] rows;
    
}
