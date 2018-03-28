package com.apptriangle.pos.Login.response;

public class LoginResponse {

    private String user_id;
    private String api_key;
    private String user_name;
    private String role;
    private String password;
    private String shop_id;

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getQueries_limit() {
        return role;
    }

    public void setQueries_limit(String queries_limit) {
        this.role = queries_limit;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getApi_key() {
        return api_key;
    }

    public void setApi_key(String api_key) {
        this.api_key = api_key;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_email() {
        return password;
    }

    public void setUser_email(String user_email) {
        this.password = user_email;
    }


}