package com.f12s.playdatenow08.models;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn; // JRF manually adding
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name="playdate")
public class PlaydateMdl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(updatable=false)
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date createdAt;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date updatedAt;

//    // begin: entity-specific table fields

    private String eventName;

    private String locationName;

    private String locationAddy;

    private String addressLine1;

    private String addressLine2;

    private String city;

    private String zipCode;

    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date eventDate;

    private String startTimeTxt;

    private String eventDescription;

    private Integer maxCountKids;

    // Aug deployment note: presently not incorporating this element, for biz reasons.
    private Integer maxCountAdults;

    // fields added to house the host RSVP

    @NotBlank(message="RSVP status is required.")
    private String rsvpStatus;

    private Integer kidCount;

    private Integer adultCount;

    // end: entity-specific table fields

    // begin: joins

    // (1) joins to have "playdate_id" be captured in foreign table

    // join rsvp table
    @OneToMany(mappedBy="playdateMdl", fetch = FetchType.LAZY)
    private List<RsvpMdl> rsvpList;

    // (2) joins to put other tables' cols on this mdl

    // new ones (to be implemented after we prove/fix out the prototype above)
    // join code table, for locationtype
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="locationType")
    private CodeMdl locationType;

    // join code table, for playdateStatus
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="playdateStatus")
    private CodeMdl playdateStatus;

    // join code table, for playdateOrganizerRsvpStatus
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="playdateOrganizerRsvpStatus_code_id")
    private CodeMdl playdateOrganizerRsvpStatus;

    // join user table
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="createdby_id")
    private UserMdl userMdl;

    // lookup for stateterritory field
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="stateterritory_id")
    private StateterritoryMdl stateterritoryMdl;

    // end: joins

    // instantiate the model:
    public PlaydateMdl() {}

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

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getLocationAddy() {
        return locationAddy;
    }

    public void setLocationAddy(String locationAddy) {
        this.locationAddy = locationAddy;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public String getStartTimeTxt() {
        return startTimeTxt;
    }

    public void setStartTimeTxt(String startTimeTxt) {
        this.startTimeTxt = startTimeTxt;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public Integer getMaxCountKids() {
        return maxCountKids;
    }

    public void setMaxCountKids(Integer maxCountKids) {
        this.maxCountKids = maxCountKids;
    }

    public Integer getMaxCountAdults() {
        return maxCountAdults;
    }

    public void setMaxCountAdults(Integer maxCountAdults) {
        this.maxCountAdults = maxCountAdults;
    }

    public UserMdl getUserMdl() {
        return userMdl;
    }

    public void setUserMdl(UserMdl userMdl) {
        this.userMdl = userMdl;
    }

    public List<RsvpMdl> getRsvpList() {
        return rsvpList;
    }

    public void setRsvpList(List<RsvpMdl> rsvpList) {
        this.rsvpList = rsvpList;
    }

    public StateterritoryMdl getStateterritoryMdl() {
        return stateterritoryMdl;
    }

    public void setStateterritoryMdl(StateterritoryMdl stateterritoryMdl) {this.stateterritoryMdl = stateterritoryMdl;}

    public String getRsvpStatus() {
        return rsvpStatus;
    }

    public void setRsvpStatus(String rsvpStatus) {
        this.rsvpStatus = rsvpStatus;
    }

    public Integer getKidCount() {
        return kidCount;
    }

    public void setKidCount(Integer kidCount) {
        this.kidCount = kidCount;
    }

    public Integer getAdultCount() {
        return adultCount;
    }

    public void setAdultCount(Integer adultCount) {
        this.adultCount = adultCount;
    }

    public CodeMdl getLocationType() {
        return locationType;
    }

    public void setLocationType(CodeMdl locationType) {
        this.locationType = locationType;
    }

    public CodeMdl getPlaydateStatus() {
        return playdateStatus;
    }

    public void setPlaydateStatus(CodeMdl playdateStatus) {
        this.playdateStatus = playdateStatus;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public CodeMdl getPlaydateOrganizerRsvpStatus() {
        return playdateOrganizerRsvpStatus;
    }

    public void setPlaydateOrganizerRsvpStatus(CodeMdl playdateOrganizerRsvpStatus) {
        this.playdateOrganizerRsvpStatus = playdateOrganizerRsvpStatus;
    }

    // end: getters and setters

// end mdl
}

