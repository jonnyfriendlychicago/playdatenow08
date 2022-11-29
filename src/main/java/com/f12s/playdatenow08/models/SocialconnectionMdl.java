package com.f12s.playdatenow08.models;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="socialconnection")

public class SocialconnectionMdl {

    // begin boilerplate: on every table
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(updatable=false)
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date createdAt;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date updatedAt;

    private Integer status;

    // begin entity-specific fields

    // end entity-specific fields
    // join user table, first time

    // begin: joins
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="userone") // 2022.11.27: in near future, rename this: initiatorUser, which will result in field being named 'initiator_user' in the db, which is accurate
    private UserMdl useroneUserMdl; // I think... going forward, this second value should always be the same as the joincolunn name

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="usertwo")  // 2022.11.27: in near future, rename this: respondentUser, see discussion above
    private UserMdl usertwoUserMdl; // I think... going forward, this second value should always be the same as the joincolunn name

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="soconstatus_code")
    private CodeMdl soconStatus;

    // placeholder for second join col

    // begin: joins

    // instantiate the model:
    public SocialconnectionMdl() {}

    // add methods to populate maintain createdAt/UpdatedAt
    @PrePersist
    protected void onCreate(){
        this.createdAt = new Date();
    }
    @PreUpdate
    protected void onUpdate(){
        this.updatedAt = new Date();
    }

    // begin: getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public UserMdl getUseroneUserMdl() {
        return useroneUserMdl;
    }

    public void setUseroneUserMdl(UserMdl useroneUserMdl) {
        this.useroneUserMdl = useroneUserMdl;
    }

    public UserMdl getUsertwoUserMdl() {
        return usertwoUserMdl;
    }

    public void setUsertwoUserMdl(UserMdl usertwoUserMdl) {
        this.usertwoUserMdl = usertwoUserMdl;
    }

// end: getters and setters



} // end Mdl


//package com.f12s.playdatenow08.models;
//
//import org.springframework.format.annotation.DateTimeFormat;
//
//import javax.persistence.*;
//import java.util.Date;
//
//@Entity
//@Table(name="socialconnection")
//
//public class SocialconnectionMdl {
//
//    // begin boilerplate: on every table
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(updatable=false)
//    @DateTimeFormat(pattern="yyyy-MM-dd")
//    private Date createdAt;
//    @DateTimeFormat(pattern="yyyy-MM-dd")
//    private Date updatedAt;
//
//    private Integer status;
//
//    // begin entity-specific fields
//
//    // end entity-specific fields
//    // join user table, first time
//
//    // begin: joins
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name="userone")
//    private UserMdl useroneUserMdl; // I think... going forward, this second value should always be the same as the joincolunn name
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name="usertwo")
//    private UserMdl usertwoUserMdl; // I think... going forward, this second value should always be the same as the joincolunn name
//
//    // placeholder for second join col
//
//    // begin: joins
//
//    // instantiate the model:
//    public SocialconnectionMdl() {}
//
//    // add methods to populate maintain createdAt/UpdatedAt
//    @PrePersist
//    protected void onCreate(){
//        this.createdAt = new Date();
//    }
//    @PreUpdate
//    protected void onUpdate(){
//        this.updatedAt = new Date();
//    }
//
//    // begin: getters and setters
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public Date getCreatedAt() {
//        return createdAt;
//    }
//
//    public void setCreatedAt(Date createdAt) {
//        this.createdAt = createdAt;
//    }
//
//    public Date getUpdatedAt() {
//        return updatedAt;
//    }
//
//    public void setUpdatedAt(Date updatedAt) {
//        this.updatedAt = updatedAt;
//    }
//
//    public Integer getStatus() {
//        return status;
//    }
//
//    public void setStatus(Integer status) {
//        this.status = status;
//    }
//
//    public UserMdl getUseroneUserMdl() {
//        return useroneUserMdl;
//    }
//
//    public void setUseroneUserMdl(UserMdl useroneUserMdl) {
//        this.useroneUserMdl = useroneUserMdl;
//    }
//
//    public UserMdl getUsertwoUserMdl() {
//        return usertwoUserMdl;
//    }
//
//    public void setUsertwoUserMdl(UserMdl usertwoUserMdl) {
//        this.usertwoUserMdl = usertwoUserMdl;
//    }
//
//// end: getters and setters
//
//
//
//} // end Mdl
