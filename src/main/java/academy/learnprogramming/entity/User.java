/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package academy.learnprogramming.entity;

import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 *
 * @author siya
 */
@Entity
@Table(name = "TodoUser")
@NamedQuery(name=User.FIND_ALL_USERS, query = "select u from User u order by u.fullName")
@NamedQuery(name=User.FIND_USER_BY_EMAIL, query = "select u from User u where u.email = :email")
public class User extends AbstractEntity {
    
    public static final String FIND_ALL_USERS = "user.findAllUsers";
    public static final String FIND_USER_BY_EMAIL = "user.findUserByEmail";
    
    @Size(min = 3,message = "full name should be at least 3 characters")
    @NotEmpty(message = "Full name cannot be empty")
    private String fullName;
    
    @NotEmpty(message = "email must be set")
    @Email(message = "email must be in the form user@domain.com")
    private String email;
    
    @Size(min = 8, message = "Password must have min of 8 chars")
    @NotEmpty(message = "Password cannot be null")
    private String password;
    
    private String salt;

    /**
     * @return the fullName
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * @param fullName the fullName to set
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the salt
     */
    public String getSalt() {
        return salt;
    }

    /**
     * @param salt the salt to set
     */
    public void setSalt(String salt) {
        this.salt = salt;
    }
}
