package com.apptriangle.pos.model;

/**
 * Created by zawan on 4/5/18.
 */

public class Brand {
    private Integer brand_id;
    private String brand_name;
    private Object brand_icon;

    public Integer getBrandId() {
        return brand_id;
    }

    public void setBrandId(Integer brandId) {
        this.brand_id = brandId;
    }

    public String getBrandName() {
        return brand_name;
    }

    public void setBrandName(String brandName) {
        this.brand_name = brandName;
    }

    public Object getBrandIcon() {
        return brand_icon;
    }

    public void setBrandIcon(Object brandIcon) {
        this.brand_icon = brandIcon;
    }
    public String title;
    public String toString()
    {
        return brand_name;
    }
}
