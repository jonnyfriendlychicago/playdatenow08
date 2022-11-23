package com.f12s.playdatenow08.controllers;

import com.f12s.playdatenow08.models.RsvpMdl;
import com.f12s.playdatenow08.models.SocialconnectionMdl;
import com.f12s.playdatenow08.models.UserMdl;
import com.f12s.playdatenow08.services.SocialconnectionSrv;
import com.f12s.playdatenow08.services.UserSrv;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
public class SocialconnectionCtl {

    @Autowired
    private SocialconnectionSrv socialconnectionSrv;

    @Autowired
    private UserSrv userSrv;

    @PostMapping("/socialconnection/create")
    public String processSoconNew(
            @Valid @ModelAttribute("soConObj") SocialconnectionMdl soConObj
            , BindingResult result
            , Model model
            , Principal principal
    ) {

        // authentication boilerplate for all mthd
        UserMdl authUserObj = userSrv.findByEmail(principal.getName()); model.addAttribute("authUser", authUserObj); model.addAttribute("authUserName", authUserObj.getUserName());

        if(!result.hasErrors()) {

            // (1) instantiate the new object
            SocialconnectionMdl newOtc = new SocialconnectionMdl();
            // (2) infuse into that object all the values from the incoming model/form
            newOtc.setUseroneUserMdl(authUserObj);
            newOtc.setUsertwoUserMdl(soConObj.getUsertwoUserMdl());

            // (3) run the service to create the record
            socialconnectionSrv.create(newOtc);

            return "redirect:/profile/";
        } else {
            System.out.println("soCon no bueno!");
            return "redirect:/profile/";

        }


    }

} // end cvtl
