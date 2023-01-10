package com.f12s.playdatenow08.services;

import com.f12s.playdatenow08.models.CodeMdl;
import com.f12s.playdatenow08.models.SocialconnectionMdl;
import com.f12s.playdatenow08.models.SocialconnectionhistoryMdl;
import com.f12s.playdatenow08.models.UserMdl;
import com.f12s.playdatenow08.repositories.SocialconnectionhistoryRpo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SocialconnectionhistorySrv {

    @Autowired
    SocialconnectionhistoryRpo socialconnectionhistoryRpo;

    // creates one
    public SocialconnectionhistoryMdl create ( SocialconnectionhistoryMdl x) { return socialconnectionhistoryRpo.save(x); }

    public void createNew (
            SocialconnectionhistoryMdl socialconnectionhistoryObject,
            UserMdl authUserObj,
            SocialconnectionMdl soConObject,
            CodeMdl toBeSoconstatusCodeObj,
            CodeMdl socialconnectionhistoryActivityCodeObj
    ) {
        socialconnectionhistoryObject.setActorUser(authUserObj);
        socialconnectionhistoryObject.setSocialconnection(soConObject);
        socialconnectionhistoryObject.setResultingsoconstatusCode(toBeSoconstatusCodeObj);
        socialconnectionhistoryObject.setSoconactivityCode(socialconnectionhistoryActivityCodeObj);
        socialconnectionhistoryRpo.save( socialconnectionhistoryObject );
//        System.out.println("stuff accomplished in mega service now");
    } // end createNew

} // end srv
