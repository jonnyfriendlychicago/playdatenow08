package com.f12s.playdatenow08.models;

import java.util.Date;
import java.util.List;
import javax.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;
import javax.validation.constraints.NotBlank;

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

    private float guiDisplayOrder;

    // end: entity-specific table fields

    // begin: joins

    // join playdate
    // below whacked 632pm
//    @OneToMany(mappedBy="codeMdl", fetch = FetchType.LAZY)
//    private List<PlaydateMdl> playdateList;

    // added 632pm
    // join playdate - locationType
    @OneToMany(mappedBy="locationType", fetch = FetchType.LAZY)
    private List<PlaydateMdl> playdateListlocationType;

    // join playdate - locationType
    @OneToMany(mappedBy="playdateStatus", fetch = FetchType.LAZY)
    private List<PlaydateMdl> playdateListplaydateStatus;

    // JRF 20221110: I don't think above two do anything for app at present.  We aren't every trying to get a list of playdates according to on of these mappings.
    // furthermore, I think the app runs just fine even if these don't exist.  But not gonna mess with it right now.

    // begin: new stuff nov 9th

    // join user table for createdBy field
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="createdby")
    private UserMdl createdByUserMdl;

    // lookup for codecategory field
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="codecategory_id")
    private CodecategoryMdl codecategoryMdl; // remember that the latter value here is the "mappedBy" value on the joined table!



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
//
//    public List<PlaydateMdl> getPlaydateList() {
//        return playdateList;
//    }
//
//    public void setPlaydateList(List<PlaydateMdl> playdateList) {
//        this.playdateList = playdateList;
//    }

    public UserMdl getCreatedByUserMdl() {
        return createdByUserMdl;
    }

    public void setCreatedByUserMdl(UserMdl createdByUserMdl) {
        this.createdByUserMdl = createdByUserMdl;
    }

    public CodecategoryMdl getCodecategoryMdl() {
        return codecategoryMdl;
    }

    public void setCodecategoryMdl(CodecategoryMdl codecategoryMdl) {
        this.codecategoryMdl = codecategoryMdl;
    }

    public float getGuiDisplayOrder() {
        return guiDisplayOrder;
    }

    public void setGuiDisplayOrder(float guiDisplayOrder) {
        this.guiDisplayOrder = guiDisplayOrder;
    }

    public List<PlaydateMdl> getPlaydateListlocationType() {
        return playdateListlocationType;
    }

    public void setPlaydateListlocationType(List<PlaydateMdl> playdateListlocationType) {
        this.playdateListlocationType = playdateListlocationType;
    }

    public List<PlaydateMdl> getPlaydateListplaydateStatus() {
        return playdateListplaydateStatus;
    }

    public void setPlaydateListplaydateStatus(List<PlaydateMdl> playdateListplaydateStatus) {
        this.playdateListplaydateStatus = playdateListplaydateStatus;
    }

    // end: getters and setters

} // end mdl