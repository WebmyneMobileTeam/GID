package com.smartcity.getitdoneandroid;

/**
 * Created by xitij on 18/6/16.
 */
public class AppUser {

    private String fname;
    private String lname;
    private String fbid;
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public AppUser() {
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getFbid() {
        return fbid;
    }

    public void setFbid(String fbid) {
        this.fbid = fbid;
    }

    public String getFullName(){
        return String.format("%s %s",getFname(),getLname());
    }
}
