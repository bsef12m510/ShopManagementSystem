package com.apptriangle.pos.sales.response;

import com.apptriangle.pos.model.Product;

/**
 * Created by zeeshan on 4/7/2018.
 */
public class GetProductsResponse {

    private Integer shopId;
    private Product product;
    private Integer prodQuant;

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getProdQuant() {
        return prodQuant;
    }

    public void setProdQuant(Integer prodQuant) {
        this.prodQuant = prodQuant;
    }

    public String toString(){
        return product.getProductName();
    }
}