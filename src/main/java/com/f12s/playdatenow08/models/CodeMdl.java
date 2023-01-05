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

    private String code;

    private String displayValue;

    private String description;

    private float guiDisplayOrder;

    // end: entity-specific table fields

    // begin: joins

    // (1) joins to put records from other tables on this table

    // join user table for createdBy field
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="created_by_user")
//    private UserMdl createdByUserMdl;
    private UserMdl createdByUser;

    // lookup for codecategory field
    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name="codecategory_id")
    @JoinColumn(name="codecategory") // note: as this is the only field in code table that looks up to codecategory table, we don't include an internally descriptive prefix on the col name
//    private CodecategoryMdl codecategoryMdl; // remember that the latter value here is the "mappedBy" value on the joined table!
    private CodecategoryMdl codecategory; // remember that the latter value here is the "mappedBy" value on the joined table!

    // (2) table-cols that pull records from this table

    // join playdate - locationType
    @OneToMany(mappedBy="locationType", fetch = FetchType.LAZY)
    private List<PlaydateMdl> playdateListlocationType;

    // join playdate - playdateStatus
    @OneToMany(mappedBy="playdateStatus", fetch = FetchType.LAZY)
    private List<PlaydateMdl> playdateListplaydateStatus;

    // join playdate - playdateOrganizerRsvpStatus
    @OneToMany(mappedBy="playdateOrganizerRsvpStatus", fetch = FetchType.LAZY)
    private List<PlaydateMdl> playdateListplaydateOrganizerRsvpStatus;

    // join rsvp - respondentRsvpStatus
    @OneToMany(mappedBy="respondentRsvpStatus", fetch = FetchType.LAZY)
    private List<RsvpMdl> rsvpListRespondentRsvpStatus;

//    @OneToMany(mappedBy="soconStatus", fetch = FetchType.LAZY)
//    private List<SocialconnectionMdl> socialconnectionListsoconStatus;

    @OneToMany(mappedBy="soconstatusCode", fetch = FetchType.LAZY)
    private List<SocialconnectionMdl> socialconnectionListSoconstatusCode;

    @OneToMany(mappedBy="resultingsoconstatusCode", fetch = FetchType.LAZY)
    private List<SocialconnectionhistoryMdl> socialconnectionhistoryListResultingsoconstatusCode;

    @OneToMany(mappedBy="soconactivityCode", fetch = FetchType.LAZY)
    private List<SocialconnectionhistoryMdl> socialconnectionhistoryListSoconactivityCode;

    // JRF 20221110: I don't think above two , no, three, do anything for app at present.  We aren't every trying to get a list of playdates according to on of these mappings.
    // furthermore, I think the app runs just fine even if these don't exist.  But not gonna mess with it right now.

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

//    public UserMdl getCreatedByUserMdl() {
//        return createdByUserMdl;
//    }
//
//    public void setCreatedByUserMdl(UserMdl createdByUserMdl) {
//        this.createdByUserMdl = createdByUserMdl;
//    }

    public UserMdl getCreatedByUser() {
        return createdByUser;
    }

    public void setCreatedByUser(UserMdl createdByUser) {
        this.createdByUser = createdByUser;
    }


//    public CodecategoryMdl getCodecategoryMdl() {
//        return codecategoryMdl;
//    }
//
//    public void setCodecategoryMdl(CodecategoryMdl codecategoryMdl) {
//        this.codecategoryMdl = codecategoryMdl;
//    }

    public CodecategoryMdl getCodecategory() {
        return codecategory;
    }

    public void setCodecategory(CodecategoryMdl codecategory) {
        this.codecategory = codecategory;
    }

//    public List<SocialconnectionMdl> getSocialconnectionListsoconStatus() {
//        return socialconnectionListsoconStatus;
//    }
//
//    public void setSocialconnectionListsoconStatus(List<SocialconnectionMdl> socialconnectionListsoconStatus) {
//        this.socialconnectionListsoconStatus = socialconnectionListsoconStatus;
//    }

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

    public List<SocialconnectionMdl> getSocialconnectionListSoconstatusCode() {
        return socialconnectionListSoconstatusCode;
    }

    public void setSocialconnectionListSoconstatusCode(List<SocialconnectionMdl> socialconnectionListSoconstatusCode) {
        this.socialconnectionListSoconstatusCode = socialconnectionListSoconstatusCode;
    }

    public List<SocialconnectionhistoryMdl> getSocialconnectionhistoryListResultingsoconstatusCode() {
        return socialconnectionhistoryListResultingsoconstatusCode;
    }

    public void setSocialconnectionhistoryListResultingsoconstatusCode(List<SocialconnectionhistoryMdl> socialconnectionhistoryListResultingsoconstatusCode) {
        this.socialconnectionhistoryListResultingsoconstatusCode = socialconnectionhistoryListResultingsoconstatusCode;
    }

    public List<SocialconnectionhistoryMdl> getSocialconnectionhistoryListSoconactivityCode() {
        return socialconnectionhistoryListSoconactivityCode;
    }

    public void setSocialconnectionhistoryListSoconactivityCode(List<SocialconnectionhistoryMdl> socialconnectionhistoryListSoconactivityCode) {
        this.socialconnectionhistoryListSoconactivityCode = socialconnectionhistoryListSoconactivityCode;
    }

    public List<PlaydateMdl> getPlaydateListplaydateOrganizerRsvpStatus() {
        return playdateListplaydateOrganizerRsvpStatus;
    }

    public void setPlaydateListplaydateOrganizerRsvpStatus(List<PlaydateMdl> playdateListplaydateOrganizerRsvpStatus) {
        this.playdateListplaydateOrganizerRsvpStatus = playdateListplaydateOrganizerRsvpStatus;
    }

    public List<RsvpMdl> getRsvpListRespondentRsvpStatus() {
        return rsvpListRespondentRsvpStatus;
    }

    public void setRsvpListRespondentRsvpStatus(List<RsvpMdl> rsvpListRespondentRsvpStatus) {
        this.rsvpListRespondentRsvpStatus = rsvpListRespondentRsvpStatus;
    }

    // end: getters and setters

} // end mdl
