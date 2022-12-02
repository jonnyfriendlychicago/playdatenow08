package com.f12s.playdatenow08.controllers;

import com.f12s.playdatenow08.models.*;
import com.f12s.playdatenow08.pojos.UserSocialConnectionPjo;
import com.f12s.playdatenow08.services.CodeSrv;
import com.f12s.playdatenow08.services.SocialconnectionSrv;
import com.f12s.playdatenow08.services.UserSrv;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @PostMapping("/connection/accept/{id}")
    public String acceptConnection(
            @PathVariable("id") Long socialconnectionId
            , Principal principal
            , RedirectAttributes redirectAttributes
    ) {

        // authentication boilerplate for all mthd
        UserMdl authUserObj = userSrv.findByEmail(principal.getName());
        // no model attributes here b/c no resulting page we are rending

        // (1) get the object from the path variable
        SocialconnectionMdl socialconnectionObj = socialconnectionSrv.findById(socialconnectionId);

        // (2) get objects for validation
        UserMdl socialconnectionUserTwo = socialconnectionObj.getUsertwoUserMdl();
        CodeMdl requestPendingCodeObj = codeSrv.findCodeMdlByCode("requestPending");

        // (3a) validate: is current user the recipient of a request?
        if(
                !authUserObj.equals(socialconnectionUserTwo)
        ) {
            redirectAttributes.addFlashAttribute("permissionErrorMsg", "This request can only be responded to by its intended invitation recipient.");
            return "redirect:/connection/" ;
        }

        // (3b) validate: is request presently in pending status?
        if(
//            socialconnectionObj.getSoconStatus().getCode() != "requestPending" // this line is not working; something about how the getCode format and the quoted text don't equate
            !socialconnectionObj.getSoconStatus().equals(requestPendingCodeObj)

        ) {
            redirectAttributes.addFlashAttribute("permissionErrorMsg", "Present relationship status does not allow accepting/declining of request.");
            return "redirect:/connection/" ;
        }

        // (4) update the object to have the intended soConStatus value
        socialconnectionObj.setSoconStatus(codeSrv.findCodeMdlByCode("friends"));

        // (5) run the update service on the object
        socialconnectionSrv.update(socialconnectionObj);

        return "redirect:/connection/";
    }

    @PostMapping("/connection/decline/{id}")
    public String declineConnection(
            @PathVariable("id") Long socialconnectionId
            , Principal principal
            , RedirectAttributes redirectAttributes
    ) {

        // authentication boilerplate for all mthd
        UserMdl authUserObj = userSrv.findByEmail(principal.getName());
        // no model attributes here b/c no resulting page we are rending

        // (1) get the object from the path variable
        SocialconnectionMdl socialconnectionObj = socialconnectionSrv.findById(socialconnectionId);

        // (2) get objects for validation
        UserMdl socialconnectionUserTwo = socialconnectionObj.getUsertwoUserMdl();
        CodeMdl requestPendingCodeObj = codeSrv.findCodeMdlByCode("requestPending");

        // (3a) validate: is current user the recipient of a request?
        if(
                !authUserObj.equals(socialconnectionUserTwo)
        ) {
            redirectAttributes.addFlashAttribute("permissionErrorMsg", "This request can only be responded to by its intended invitation recipient.");
            return "redirect:/connection/" ;
        }

        // no idea why below isn't working presently... screw it (for now)
        // (3b) validate: is request presently in pending status?
        if(
            !socialconnectionObj.getSoconStatus().equals(requestPendingCodeObj)
        ) {
            redirectAttributes.addFlashAttribute("permissionErrorMsg", "Present relationship status does not allow accepting/declining request.");
            return "redirect:/connection/" ;
        }

        // (4) update the object to have the intended soConStatus value
        socialconnectionObj.setSoconStatus(codeSrv.findCodeMdlByCode("requestRejected"));

        // (5) run the update service on the object
        socialconnectionSrv.update(socialconnectionObj);

        return "redirect:/connection/";
    }

} // end cvtl
