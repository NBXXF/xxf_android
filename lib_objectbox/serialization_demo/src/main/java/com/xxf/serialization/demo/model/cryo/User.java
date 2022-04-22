package com.xxf.serialization.demo.model.cryo;

import java.util.List;

public final class User
{
    String firstName;
    String lastName;
    String email;
    List<String> subNode;

    public User() {}

    public User(String email)
    {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getSubNode() {
        return subNode;
    }

    public void setSubNode(List<String> subNode) {
        this.subNode = subNode;
    }
// getters and setters
}