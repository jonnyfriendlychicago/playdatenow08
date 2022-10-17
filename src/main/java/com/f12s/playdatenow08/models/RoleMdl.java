package com.f12s.playdatenow08.models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "role")
public class RoleMdl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // BEGIN: entity-specific fields

    private String name;

    // END: entity-specific fields

    // BEGIN: joins

    // join user
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<UserMdl> userMdl;

    // END: joins

    // instantiate the model:
    public RoleMdl() {}

    // BEGIN: getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<UserMdl> getUserMdl() {
        return userMdl;
    }

    public void setUserMdl(List<UserMdl> userMdl) {
        this.userMdl = userMdl;
    }

    //END: getters and setters

}