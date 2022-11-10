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

    //    @NotEmpty(message="Username required.")
//    @Size(min=8, max=128, message="Username must be between 3 and 30 characters.")
    // above lines nuked for springSec
    private String userName;

    //    @NotEmpty(message="Email required.")
//    @Email(message="Please enter a valid email.")
    // above lines nuked for springSec
    private String email;

    private String firstName;

    private String lastName;

    private Date lastLogin; // added for springSec

    //    @NotEmpty(message="Password required.")
//    @Size(min=8, max=128, message="Password must be between 8 and 20 characters.")
    // above lines whacked for springSec, also added line below which doesn't seem right, but copied from BP
    @Size(min=3)
    private String password;

    @Transient
//    @NotEmpty(message="Confirm Password is required!")  // this is not necessary
//    @Size(min=3, max=128, message="Confirm Password must match password") // this is not necessary
//    private String confirm;
    // above line replaced by below; BP calls the field "passwordConfirm"
    private String passwordConfirm;

    private String aboutMe;

    private String addressLine1;

    private String addressLine2;

    private String city;

    private String zipCode;


    // end entity-specific fields

    // begin: joins

    // (1) joins for spring security
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<RoleMdl> roleMdl;

    // (2) joins to have "createdBy" be captured in a col in foreign table

    // join playdate
    @OneToMany(mappedBy="userMdl", fetch = FetchType.LAZY)
    private List<PlaydateMdl> playdateList;


    // join rsvp
    @OneToMany(mappedBy="userMdl", fetch = FetchType.LAZY)
    private List<RsvpMdl> rsvpList;

    // join codecategory
    @OneToMany(mappedBy="createdByUserMdl", fetch = FetchType.LAZY)
    private List<CodecategoryMdl> codecategoryList;

    // join code
    @OneToMany(mappedBy="createdByUserMdl", fetch = FetchType.LAZY)
    private List<CodeMdl> codeList;

    // (3) joins to put other cols on a user record

    // join stateterritory table
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="stateterritory_id")
    private StateterritoryMdl stateterritoryMdl;

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

    public List<CodecategoryMdl> getCodecategoryList() {
        return codecategoryList;
    }

    public void setCodecategoryList(List<CodecategoryMdl> codecategoryList) {
        this.codecategoryList = codecategoryList;
    }

    public List<CodeMdl> getCodeList() {
        return codeList;
    }

    public void setCodeList(List<CodeMdl> codeList) {
        this.codeList = codeList;
    }

    // end: getters and setters

}
