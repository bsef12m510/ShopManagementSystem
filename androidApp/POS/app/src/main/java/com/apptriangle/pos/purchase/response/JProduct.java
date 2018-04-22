package com.apptriangle.pos.purchase.response;

import com.apptriangle.pos.model.Brand;
import com.apptriangle.pos.model.Product;
import com.apptriangle.pos.model.ProductType;
import com.apptriangle.pos.model.UoM;

/**
 * Created by zeeshan on 4/22/2018.
 */
public class JProduct {
    private Integer product_id;
    private String product_name;
    private ProductType product_type;
    private Brand brand;
    private String specs;
    private UoM unit_of_msrmnt;
    private Double unit_price;
    private Object product_image;
    private Integer qty;
    private int otherThanCurrentInventoryQty;
    private boolean isChecked;
    public JProduct(){}
    public JProduct(Product tmp){
        this.product_id = tmp.getProductId();
        this.product_name = tmp.getProductName();
        this.product_type = tmp.getProductType();
        this.brand = tmp.getBrand();
        this.specs = tmp.getSpecs();
        this.unit_price = tmp.getUnitPrice();
        this.qty = tmp.getQty();
        this.otherThanCurrentInventoryQty = tmp.getOtherThanCurrentInventoryQty();
        this.isChecked = tmp.isChecked();
    }

    public Integer getProductId() {
        return product_id;
    }

    public void setProductId(Integer productId) {
        this.product_id = productId;
    }

    public String getProductName() {
        return product_name;
    }

    public void setProductName(String productName) {
        this.product_name = productName;
    }

    public ProductType getProductType() {
        return product_type;
    }

    public void setProductType(ProductType productType) {
        this.product_type = productType;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public String getSpecs() {
        return specs;
    }

    public void setSpecs(String specs) {
        this.specs = specs;
    }

    public UoM getUnitOfMsrmnt() {
        return unit_of_msrmnt;
    }

    public void setUnitOfMsrmnt(UoM unitOfMsrmnt) {
        this.unit_of_msrmnt = unitOfMsrmnt;
    }

    public Double getUnitPrice() {
        return unit_price;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unit_price = unitPrice;
    }

    public Object getProductImage() {
        return product_image;
    }

    public void setProductImage(Object productImage) {
        this.product_image = productImage;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public int getOtherThanCurrentInventoryQty() {
        return otherThanCurrentInventoryQty;
    }

    public void setOtherThanCurrentInventoryQty(int otherThanCurrentInventoryQty) {
        this.otherThanCurrentInventoryQty = otherThanCurrentInventoryQty;
    }
    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }


    public String title;

    public String toString()
    {
        return product_name;
    }



}
