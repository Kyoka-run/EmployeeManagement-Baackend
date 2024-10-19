package com.employeemanagement.model;

import javax.persistence.*;

import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue
    @Column(name="user_id")
    private Integer id;

    @Column(name="user_name")
    private String username;

    @Column(name="user_passwd")
    private String password;

    @ElementCollection(fetch= FetchType.EAGER)
    @CollectionTable(
            name="roles",
            joinColumns = @JoinColumn(name="user_id")
    )
    @Column(name="user_role")
    private List<String> roles;

    public User() {
    }

    public User(Integer id, String username, String password, List<String> roles) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

}