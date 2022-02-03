/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.model;

import java.util.Optional;

/**
 *
 * @author Tin
 */
public class Person {
    
    private int idInRole;
    private int personID;
    private String fullName;
    private String alternateName;

    public Person(String fullName) {
        this.fullName = fullName;
    }

    public Person(int personID, String fullName, String alternateName) {
        this(fullName);
        this.personID = personID;
        this.alternateName = alternateName;
    }

    public Person(int idInRole, int personID, String fullName, String alternateName) {
        this(personID, fullName, alternateName);
        this.idInRole = idInRole;
    }


    public int getIdInRole() {
        return idInRole;
    }

    public int getPersonID() {
        return personID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAlternateName() {
        return alternateName;
    }

    public void setAlternateName(String alternateName) {
        this.alternateName = alternateName;
    }
   
    
}
