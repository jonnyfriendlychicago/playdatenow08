package com.f12s.playdatenow08.controllers;

import com.f12s.playdatenow08.models.CodeMdl;
import com.f12s.playdatenow08.models.RsvpMdl;
import com.f12s.playdatenow08.models.SocialconnectionMdl;
import com.f12s.playdatenow08.models.UserMdl;
import com.f12s.playdatenow08.pojos.UserSocialConnectionPjo;
import com.f12s.playdatenow08.services.CodeSrv;
import com.f12s.playdatenow08.services.SocialconnectionSrv;
import com.f12s.playdatenow08.services.UserSrv;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class SocialconnectionCtl {

    @Autowired
    private SocialconnectionSrv socialconnectionSrv;

    @Autowired
    private UserSrv userSrv;

    @Autowired
    private CodeSrv codeSrv;

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
            newOtc.setSoconStatus(codeSrv.findCodeMdlByCode("requestPending"));

            // (3) run the service to create the record
            socialconnectionSrv.create(newOtc);

            return "redirect:/profile/";

        } else {
            System.out.println("soCon no bueno!");
            return "redirect:/profile/";
        }

    }

    // adding new
    // view all profile
    @GetMapping("/connection")
    public String displayConnectionAll(
            @ModelAttribute("soConObj") SocialconnectionMdl soConObj // I don't see if/how this is necessary.  old RSVP appendage. 2022.12.01
            , Model model
            , Principal principal
    ) {

        // authentication boilerplate for all mthd
        UserMdl authUserObj = userSrv.findByEmail(principal.getName());
        model.addAttribute("authUser", authUserObj);
        model.addAttribute("authUserName", authUserObj.getUserName());

        // (1) deliver list of user records to page
        List<UserMdl> profileList = userSrv.returnAll();
        model.addAttribute("profileList", profileList);

        // (2) soCon list

        List<UserSocialConnectionPjo> userSocialConnectionListSent = userSrv.userSocialConnectionListSent(authUserObj.getId());
        model.addAttribute("userSocialConnectionListSent", userSocialConnectionListSent);

        List<UserSocialConnectionPjo> userSocialConnectionListReceived = userSrv.userSocialConnectionListReceived(authUserObj.getId());
        model.addAttribute("userSocialConnectionListReceived", userSocialConnectionListReceived);

        List<UserSocialConnectionPjo> userSocialConnectionListFriends = userSrv.userSocialConnectionListFriends(authUserObj.getId());
        model.addAttribute("userSocialConnectionListFriends", userSocialConnectionListFriends);

        List<UserSocialConnectionPjo> userSocialConnectionListBlocked = userSrv.userSocialConnectionListBlocked(authUserObj.getId());
        model.addAttribute("userSocialConnectionListBlocked", userSocialConnectionListBlocked);


        return "connection/list.jsp";
    }





} // end cvtl
