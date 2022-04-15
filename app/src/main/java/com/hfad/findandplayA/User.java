package com.hfad.findandplayA;


//the model class to store all the attributes of the user
public class User {

    String Name;
    String Nickname;
    Number Age;
    String Group;


    //empty constructor to be used while reading the values
    public User(){

    }

    //getters to read the values

    public String getName() {
        return Name;
    }

    public String getNickname() {
        return Nickname;
    }

    public Number getAge() {
        return Age;
    }

    public String getGroup() {
        return Group;
    }



    //setters to set the values

    public void setName( String name) { Name = name;}
    public void setNickname( String nickname) { Nickname = nickname;}
    public void setAge( Integer age) { Age = age;}
    public void setGroup( String group) { Group = group;}
}
