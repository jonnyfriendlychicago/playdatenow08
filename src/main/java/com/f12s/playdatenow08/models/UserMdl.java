package com.f12s.playdatenow08.models;

import java.util.Date;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name="user")
public class UserMdl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(updatable=false)
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date createdAt;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date updatedAt;

    // begin: entity-specific fields

    private String userName;

    private String email;

    private String firstName;

    private String lastName;

    private Date lastLogin; // added for springSec

//    @Size(min=3)
    private String password;

    @Transient
    private String passwordConfirm;

    private String aboutMe;

    private String addressLine1;

    private String addressLine2;

    private String city;

    private String zipCode;

    private String homeName;

    // end entity-specific fields

    // begin: joins

    // (2) joins to make this table listable from other tables

    // join codecategory
//    @OneToMany(mappedBy="createdByUserMdl", fetch = FetchType.LAZY)
    @OneToMany(mappedBy="createdByUser", fetch = FetchType.LAZY)
//    private List<CodecategoryMdl> codecategoryList;
    private List<CodecategoryMdl> codecategoryCreatedbyList;

    // join code
//    @OneToMany(mappedBy="createdByUserMdl", fetch = FetchType.LAZY)
    @OneToMany(mappedBy="createdByUser", fetch = FetchType.LAZY)
    private List<CodeMdl> codeList;

    // join playdate
    @OneToMany(mappedBy="userMdl", fetch = FetchType.LAZY)
    private List<PlaydateMdl> playdateList;

    // join rsvp
    @OneToMany(mappedBy="userMdl", fetch = FetchType.LAZY)
    private List<RsvpMdl> rsvpList;



    // new: trying to make social happen

    // join socialconnection
    @OneToMany(mappedBy="useroneUserMdl", fetch = FetchType.LAZY) // the mappedBy should be the same name as the col in other table (yes?)
    private List<SocialconnectionMdl> useroneSocialconnectionList;

    @OneToMany(mappedBy="usertwoUserMdl", fetch = FetchType.LAZY) // the mappedBy should be the same name as the col in other table (yes?)
    private List<SocialconnectionMdl> usertwoSocialconnectionList;

    @OneToMany(mappedBy="blockerUser", fetch = FetchType.LAZY) // the mappedBy should be the same name as the col in other table (yes?)
    private List<SocialconnectionMdl> blockerSocialconnectionList;

    @OneToMany(mappedBy="initiatorUser", fetch = FetchType.LAZY) // the mappedBy should be the same name as the col in other table (yes?)
    private List<SocialconnectionMdl> initiatorSocialconnectionList;

    @OneToMany(mappedBy="responderUser", fetch = FetchType.LAZY) // the mappedBy should be the same name as the col in other table (yes?)
    private List<SocialconnectionMdl> responderSocialconnectionList;

    // (2) joins to put other tables' cols on this mdl

    // join stateterritory table
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="stateterritory_id")
    private StateterritoryMdl stateterritoryMdl;

    // (3) joins for spring security
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<RoleMdl> roleMdl;

    // end: joins

    // instantiate the mdl
    public UserMdl() {}

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

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    public List<RoleMdl> getRoleMdl() {
        return roleMdl;
    }

    public void setRoleMdl(List<RoleMdl> roleMdl) {
        this.roleMdl = roleMdl;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // whacked for SpringSec
//	public String getConfirm() {
//		return confirm;
//	}
//
//	public void setConfirm(String confirm) {
//		this.confirm = confirm;
//	}

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
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

    public String getAddressLine1() {return addressLine1;}

    public void setAddressLine1(String addressLine1) {this.addressLine1 = addressLine1;}

    public String getAddressLine2() {return addressLine2;}

    public void setAddressLine2(String addressLine2) {this.addressLine2 = addressLine2;}



    public List<PlaydateMdl> getPlaydateList() {
        return playdateList;
    }

    public void setPlaydateList(List<PlaydateMdl> playdateList) {
        this.playdateList = playdateList;
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

//    public List<CodecategoryMdl> getCodecategoryList() {
//        return codecategoryList;
//    }
//
//    public void setCodecategoryList(List<CodecategoryMdl> codecategoryList) {
//        this.codecategoryList = codecategoryList;
//    }


    public List<CodecategoryMdl> getCodecategoryCreatedbyList() {
        return codecategoryCreatedbyList;
    }

    public void setCodecategoryCreatedbyList(List<CodecategoryMdl> codecategoryCreatedbyList) {
        this.codecategoryCreatedbyList = codecategoryCreatedbyList;
    }

    public List<CodeMdl> getCodeList() {
        return codeList;
    }

    public void setCodeList(List<CodeMdl> codeList) {
        this.codeList = codeList;
    }

    public String getHomeName() {
        return homeName;
    }

    public void setHomeName(String homeName) {
        this.homeName = homeName;
    }

    public List<SocialconnectionMdl> getUseroneSocialconnectionList() {
        return useroneSocialconnectionList;
    }

    public void setUseroneSocialconnectionList(List<SocialconnectionMdl> useroneSocialconnectionList) {
        this.useroneSocialconnectionList = useroneSocialconnectionList;
    }

    public List<SocialconnectionMdl> getUsertwoSocialconnectionList() {
        return usertwoSocialconnectionList;
    }

    public void setUsertwoSocialconnectionList(List<SocialconnectionMdl> usertwoSocialconnectionList) {
        this.usertwoSocialconnectionList = usertwoSocialconnectionList;
    }

    public List<SocialconnectionMdl> getBlockerSocialconnectionList() {
        return blockerSocialconnectionList;
    }

    public void setBlockerSocialconnectionList(List<SocialconnectionMdl> blockerSocialconnectionList) {
        this.blockerSocialconnectionList = blockerSocialconnectionList;
    }

    public List<SocialconnectionMdl> getInitiatorSocialconnectionList() {
        return initiatorSocialconnectionList;
    }

    public void setInitiatorSocialconnectionList(List<SocialconnectionMdl> initiatorSocialconnectionList) {
        this.initiatorSocialconnectionList = initiatorSocialconnectionList;
    }

    public List<SocialconnectionMdl> getResponderSocialconnectionList() {
        return responderSocialconnectionList;
    }

    public void setResponderSocialconnectionList(List<SocialconnectionMdl> responderSocialconnectionList) {
        this.responderSocialconnectionList = responderSocialconnectionList;
    }

    // end: getters and setters

}
