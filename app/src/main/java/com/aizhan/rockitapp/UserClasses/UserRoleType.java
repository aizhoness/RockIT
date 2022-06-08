package com.aizhan.rockitapp.UserClasses;

public class UserRoleType {
    String idofuser;
    String typeofuser;

    public UserRoleType(){

    }

    public UserRoleType(String idofuser, String typeofuser){
        this.idofuser = idofuser;
        this.typeofuser = typeofuser;
    }

    public String getIdofuser() {
        return idofuser;
    }

    public String getTypeofuser() {
        return typeofuser;
    }
}
