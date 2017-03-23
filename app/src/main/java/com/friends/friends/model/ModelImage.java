package com.friends.friends.model;

/**
 * Created by dell on 3/21/2017.
 */

public class ModelImage {

    private String user_id;
    private String url;

    public void setUserId(String userid){
        this.user_id=userid;
    }
    public String getUserId(){
        return this.user_id;
    }

    public void setUrl(String url){
        this.url=url;
    }
    public String getUrl(){
        return this.url;
    }


}
