package com.f12s.playdatenow08.models;

import java.util.Date;
import java.util.List;
import javax.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name="codecategory")

public class CodecategoryMdl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // time-stamp fields
    @Column(updatable = false)
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date createdAt;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date updatedAt;

    // begin: entity-specific table fields

    private String codeType;

    private String description;

    // end: entity-specific table fields

    // begin: joins

    // join user table
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="created_by_user")
    private UserMdl createdByUserMdl;

    // be the codecategory lookup for codeMdl
//    @OneToMany(mappedBy="codecategoryMdl", fetch = FetchType.LAZY)
    @OneToMany(mappedBy="codecategory", fetch = FetchType.LAZY)
    private List<CodeMdl> codeList;

    // end: joins

    // instantiate the model:
    public CodecategoryMdl() {};

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

    public String getCodeType() {
        return codeType;
    }

    public void setCodeType(String codeType) {
        this.codeType = codeType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UserMdl getCreatedByUserMdl() {
        return createdByUserMdl;
    }

    public void setCreatedByUserMdl(UserMdl createdByUserMdl) {
        this.createdByUserMdl = createdByUserMdl;
    }

    public List<CodeMdl> getCodeList() {
        return codeList;
    }

    public void setCodeList(List<CodeMdl> codeList) {
        this.codeList = codeList;
    }

    // end: getters and setters

} // end mdl


//package com.f12s.playdatenow08.models;
//
//import java.util.Date;
//import java.util.List;
//import javax.persistence.*;
//import org.springframework.format.annotation.DateTimeFormat;
//import javax.validation.constraints.NotBlank;
//
//@Entity
//@Table(name="codecategory")
//
//public class CodecategoryMdl {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    // time-stamp fields
//    @Column(updatable = false)
//    @DateTimeFormat(pattern="yyyy-MM-dd")
//    private Date createdAt;
//    @DateTimeFormat(pattern="yyyy-MM-dd")
//    private Date updatedAt;
//
//    // begin: entity-specific table fields
//
//    private String codeType;
//
//    private String description;
//
//    // end: entity-specific table fields
//
//    // begin: joins
//
//    // join user table
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name="createdby")
//    private UserMdl createdByUserMdl;
//
//    // be the codecategory lookup for codeMdl
//    @OneToMany(mappedBy="codecategoryMdl", fetch = FetchType.LAZY)
//    private List<CodeMdl> codeList;
//
//    // end: joins
//
//    // instantiate the model:
//    public CodecategoryMdl() {};
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
//    public String getCodeType() {
//        return codeType;
//    }
//
//    public void setCodeType(String codeType) {
//        this.codeType = codeType;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//    public UserMdl getCreatedByUserMdl() {
//        return createdByUserMdl;
//    }
//
//    public void setCreatedByUserMdl(UserMdl createdByUserMdl) {
//        this.createdByUserMdl = createdByUserMdl;
//    }
//
//    public List<CodeMdl> getCodeList() {
//        return codeList;
//    }
//
//    public void setCodeList(List<CodeMdl> codeList) {
//        this.codeList = codeList;
//    }
//
//
//    // end: getters and setters
//
//
//
//} // end mdl
