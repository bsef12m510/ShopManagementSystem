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

    public String getSr_no() {
        return sr_no;
    }

    public void setSr_no(String sr_no) {
        this.sr_no = sr_no;
    }

    public String sr_no;

    public String toString(){
        return description;
    }
}
