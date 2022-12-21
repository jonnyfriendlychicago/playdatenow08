package com.f12s.playdatenow08.models;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="socialconnectionhistory")

public class SocialconnectionhistoryMdl {

    // begin: boilerplate on every table
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(updatable=false)
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date createdAt;

    // end: boilerplate on every table

    // begin: joins

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="actor_user_id")
    private UserMdl actorUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="socialconnection_id")
    private SocialconnectionMdl socialconnection;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="resultingsoconstatus_code_id")
    private CodeMdl resultingsoconstatusCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="soconactivity_code_id")
    private CodeMdl soconactivityCode;

    // end: joins

    // instantiate the model:
    public SocialconnectionhistoryMdl() {}

    // add methods to populate maintain createdAt/UpdatedAt
    @PrePersist
    protected void onCreate(){
        this.createdAt = new Date();
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

    public UserMdl getActorUser() {
        return actorUser;
    }

    public void setActorUser(UserMdl actorUser) {
        this.actorUser = actorUser;
    }

    public SocialconnectionMdl getSocialconnection() {
        return socialconnection;
    }

    public void setSocialconnection(SocialconnectionMdl socialconnection) {
        this.socialconnection = socialconnection;
    }

    public CodeMdl getResultingsoconstatusCode() {
        return resultingsoconstatusCode;
    }

    public void setResultingsoconstatusCode(CodeMdl resultingsoconstatusCode) {
        this.resultingsoconstatusCode = resultingsoconstatusCode;
    }

    public CodeMdl getSoconactivityCode() {
        return soconactivityCode;
    }

    public void setSoconactivityCode(CodeMdl soconactivityCode) {
        this.soconactivityCode = soconactivityCode;
    }

    // end: getters and setters

} // end mdl
