package com.f12s.playdatenow08.services;


import com.f12s.playdatenow08.models.SocialconnectionMdl;
import com.f12s.playdatenow08.repositories.SocialconnectionRpo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SocialconnectionSrv {

    @Autowired
    SocialconnectionRpo socialconnectionRpo;

    // creates one
    public SocialconnectionMdl create(SocialconnectionMdl x) { return socialconnectionRpo.save(x);}

    // updates one
    public SocialconnectionMdl update(SocialconnectionMdl x) { return socialconnectionRpo.save(x);}

}
