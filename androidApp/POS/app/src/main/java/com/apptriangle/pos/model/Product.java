package com.apptriangle.pos.model;

/**
 * Created by zawan on 4/5/18.
 */

public class Product {
    private Integer product_id;
    private String product_name;
    private ProductType product_type;
    private Brand brand;
    private String specs;
    private UoM unit_of_msrmnt;
//    private String unit_of_msrmnt;
    private Double unit_price;
    private Object product_image;
    private Integer qty;
    private int otherThanCurrentInventoryQty;
    private boolean isChecked;
    private User user;


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
