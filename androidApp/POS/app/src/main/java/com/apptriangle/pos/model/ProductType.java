package com.apptriangle.pos.model;

/**
 * Created by zeeshan on 4/7/2018.
 */
public class ProductType {

    private Integer type_id;
    private String type_name;

    public Integer getTypeId() {
        return type_id;
    }

    public void setTypeId(Integer typeId) {
        this.type_id = typeId;
    }

    public String getTypeName() {
        return type_name;
    }

    public void setTypeName(String typeName) {
        this.type_name = typeName;
    }

    public String toString()
    {
        return type_name;
    }
}

