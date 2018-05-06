package com.apptriangle.pos.model;

/**
 * Created by zeeshan on 4/30/2018.
 */
public class User {
    public String user_id ;
    public String password ;
    public String role_id ;
    public String username;
    public String api_key ;
    public int shop_id ;

    public String toString(){
        return user_id;
    }

}
