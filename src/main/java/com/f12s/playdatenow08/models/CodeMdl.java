package com.f12s.playdatenow08.models;

import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Entity
@Table(name="code")
public class CodeMdl {

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
//    @NotBlank(message="code required.")
    private String code;

//    @NotBlank(message="codeType required.")
    private String codeType;

//    @NotBlank(message="displayValue required.")
    private String displayValue;

    private String description;

    // end: entity-specific table fields

    // begin: joins

    // join playdate
    @OneToMany(mappedBy="codeMdl", fetch = FetchType.LAZY)
    private List<PlaydateMdl> playdateList;

    // end: joins

    // instantiate the model:
    public CodeMdl() {};

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCodeType() {
        return codeType;
    }

    public void setCodeType(String codeType) {
        this.codeType = codeType;
    }

    public String getDisplayValue() {
        return displayValue;
    }

    public void setDisplayValue(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

//    public List<CodeMdl> getCodeList() {
//        return codeList;
//    }
//
//    public void setCodeList(List<CodeMdl> codeList) {
//        this.codeList = codeList;
//    }

    //    public List<CodeMdl> getCodeList() {
//        return codeList;
//    }
//
//    public void setCodeList(List<CodeMdl> codeList) {
//        this.codeList = codeList;
//    }

    public List<PlaydateMdl> getPlaydateList() {
        return playdateList;
    }

    public void setPlaydateList(List<PlaydateMdl> playdateList) {
        this.playdateList = playdateList;
    }


    // end: getters and setters









} // end mdl
