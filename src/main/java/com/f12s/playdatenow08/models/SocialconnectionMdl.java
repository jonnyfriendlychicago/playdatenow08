package com.f12s.playdatenow08.models;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

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

//    private Integer status;

    // begin entity-specific fields

    @Transient
//    private Integer objectOrigin;
    private String objectOrigin;

    // end entity-specific fields
    // join user table, first time

    // begin: joins

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="soconstatus_code_id")
//    private CodeMdl soconStatus;
    private CodeMdl soconstatusCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="userone") // 2022.11.27: in near future, rename this: initiatorUser, which will result in field being named 'initiator_user' in the db, which is accurate
    private UserMdl useroneUserMdl; // I think... going forward, this second value should always be the same as the joincolunn name

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="usertwo")  // 2022.11.27: in near future, rename this: respondentUser, see discussion above
    private UserMdl usertwoUserMdl; // I think... going forward, this second value should always be the same as the joincolunn name

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="blocker_user_id")  //
    private UserMdl blockerUser; // I think... going forward, this second value should always be the same as the joincolunn name

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="initiator_user_id")  // added 2022.12.04, think this will support newly evolved design/model.
    private UserMdl initiatorUser; // I think... going forward, this second value should always be the same as the joincolunn name

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="responder_user_id")  // added 2022.12.04, think this will support newly evolved design/model.
    private UserMdl responderUser; // I think... going forward, this second value should always be the same as the joincolunn name

    // join to SocialconnectionhistoryMdl.socialconnection_id
    @OneToMany(mappedBy="socialconnection", fetch = FetchType.LAZY) // the mappedBy should be the same name as the col in other table (yes?)
    private List<SocialconnectionhistoryMdl> socialconnectionhistoryList;

    // end: joins

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

//    public Integer getStatus() {
//        return status;
//    }
//
//    public void setStatus(Integer status) {
//        this.status = status;
//    }

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

//    public CodeMdl getSoconStatus() {
//        return soconStatus;
//    }
//
//    public void setSoconStatus(CodeMdl soconStatus) {
//        this.soconStatus = soconStatus;
//    }
//
//    public UserMdl getBlockerUser() {
//        return blockerUser;
//    }


    public CodeMdl getSoconstatusCode() {
        return soconstatusCode;
    }

    public void setSoconstatusCode(CodeMdl soconstatusCode) {
        this.soconstatusCode = soconstatusCode;
    }

    public UserMdl getBlockerUser() {
        return blockerUser;
    }

    public void setBlockerUser(UserMdl blockerUser) {
        this.blockerUser = blockerUser;
    }

    public UserMdl getInitiatorUser() {
        return initiatorUser;
    }

    public void setInitiatorUser(UserMdl initiatorUser) {
        this.initiatorUser = initiatorUser;
    }

    public UserMdl getResponderUser() {
        return responderUser;
    }

    public void setResponderUser(UserMdl responderUser) {
        this.responderUser = responderUser;
    }

    public String getObjectOrigin() {
        return objectOrigin;
    }

    public void setObjectOrigin(String objectOrigin) {
        this.objectOrigin = objectOrigin;
    }

    public List<SocialconnectionhistoryMdl> getSocialconnectionhistoryList() {
        return socialconnectionhistoryList;
    }

    public void setSocialconnectionhistoryList(List<SocialconnectionhistoryMdl> socialconnectionhistoryList) {
        this.socialconnectionhistoryList = socialconnectionhistoryList;
    }

    // end: getters and setters



} // end Mdl
