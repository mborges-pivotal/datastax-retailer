package com.borgescloud.datastax.retailer.model;

import lombok.Data;

@Data
public class Stock {
    
    private String store;
    private String uniqId;
    private String sku;
    private String nameTitle;
    private Integer totalUnits;
    private Float salePrice;

    public String getPrimaryKey() {
        return String.format("%s/%s/%s", store, uniqId, sku);
    }

    public ChangeSet createChangeSet(Stock o) {

        if (!o.store.equals(store) || !o.uniqId.equals(uniqId)) {
            throw new Error("Unique ids don't match for provided store");
        }

        ChangeSet cs = new ChangeSet();
        if (!o.sku.equals(sku)) {
            cs.addChanges("sku", o.sku);
        }

        if (!o.nameTitle.equals(nameTitle)) {
            cs.addChanges("name_title", o.nameTitle);
        }

        if (o.totalUnits != totalUnits) {
            cs.addChanges("name_title", ""+o.totalUnits);
        }

        if (o.salePrice != salePrice) {
            cs.addChanges("sale_price", ""+o.salePrice);
        }

        return cs;

    }

}
