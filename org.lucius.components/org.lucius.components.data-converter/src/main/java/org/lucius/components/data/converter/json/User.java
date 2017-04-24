/**
 * @(#)User.java 1.0 2017年4月24日
 * @Copyright:  Copyright 2007 - 2017 
 * @Description: 
 * 
 * Modification History:
 * Date:        2017年4月24日
 * Author:      lucius.lv
 * Version:     1.0.0.0
 * Description: (Initialize)
 * Reviewer:    
 * Review Date: 
 */
package org.lucius.components.data.converter.json;

public class User {

    public enum Gender {
        MALE, FEMALE
    };

    private int id;
    private String name;
    private String password;
    private Gender gender;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

}
