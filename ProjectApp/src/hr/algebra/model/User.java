/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.model;

/**
 *
 * @author Tin
 */
public class User {
    private int IDUser;
    private final String Username;
    private final String Password;
    private String Role;
    private int AccessLevel;

    public User(String Username, String Password) {
        this.Username = Username;
        this.Password = Password;
    }

    public User(int IDUser, String Username, String Password, String Role, int AccessLevel) {
        this(Username, Password);
        this.IDUser = IDUser;
        this.Role = Role;
        this.AccessLevel = AccessLevel;
    }

    public int getIDUser() {
        return IDUser;
    }

    public String getUsername() {
        return Username;
    }

    public String getPassword() {
        return Password;
    }

    public String getRole() {
        return Role;
    }

    public int getAccessLevel() {
        return AccessLevel;
    }
}
