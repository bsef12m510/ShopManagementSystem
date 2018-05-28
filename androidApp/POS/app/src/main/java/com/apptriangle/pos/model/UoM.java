package com.apptriangle.pos.model;

/**
 * Created by zeeshan on 4/19/2018.
 */
public class UoM {
    public String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSr_no() {
        return sr_no;
    }

    public void setSr_no(Integer sr_no) {
        this.sr_no = sr_no;
    }

    public Integer sr_no;

    public String toString(){
        return description;
    }
}
