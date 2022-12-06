package com.f12s.playdatenow08.controllers;

import com.f12s.playdatenow08.models.*;
import com.f12s.playdatenow08.pojos.UserSocialConnectionPjo;
import com.f12s.playdatenow08.repositories.SocialconnectionRpo;
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
import java.util.Optional;

@Controller
public class SocialconnectionCtl {

    @Autowired
    private SocialconnectionSrv socialconnectionSrv;

    @Autowired
    private UserSrv userSrv;

    @Autowired
    private CodeSrv codeSrv;

    @Autowired
    private SocialconnectionRpo socialconnectionRpo;

    // view all socialConnection
    @GetMapping("/connection")
    public String displayConnectionAll(
            @ModelAttribute("soConObj") SocialconnectionMdl soConObj // I don't see if/how this is necessary.  old RSVP appendage. 2022.12.01;
            // that said, if comment out, redSqig error; I think it's b/c the friends list and blocked list still has an "add friend" button/functionality??
            , Model model
            , Principal principal
    ) {

        // authentication boilerplate for all mthd
        UserMdl authUserObj = userSrv.findByEmail(principal.getName()); model.addAttribute("authUser", authUserObj); model.addAttribute("authUserName", authUserObj.getUserName());

        // (1) soCon lists

        List<UserSocialConnectionPjo> userSocialConnectionListSent = userSrv.userSocialConnectionListSent(authUserObj.getId());
        model.addAttribute("userSocialConnectionListSent", userSocialConnectionListSent);

        // working thru below
        List<UserSocialConnectionPjo> userSocialConnectionListReceived = userSrv.userSocialConnectionListReceived(authUserObj.getId());
        model.addAttribute("userSocialConnectionListReceived", userSocialConnectionListReceived);

        List<UserSocialConnectionPjo> userSocialConnectionListFriends = userSrv.userSocialConnectionListFriends(authUserObj.getId());
        model.addAttribute("userSocialConnectionListFriends", userSocialConnectionListFriends);

        List<UserSocialConnectionPjo> userSocialConnectionListRequestCancelled = userSrv.userSocialConnectionListRequestCancelled(authUserObj.getId());
        model.addAttribute("userSocialConnectionListRequestCancelled", userSocialConnectionListRequestCancelled);

        List<UserSocialConnectionPjo> userSocialConnectionListBlocked = userSrv.userSocialConnectionListBlocked(authUserObj.getId());
        model.addAttribute("userSocialConnectionListBlocked", userSocialConnectionListBlocked);

        return "connection/list.jsp";
    }

    @PostMapping("/socialconnection/create")
    public String processSoconNew(
            @Valid @ModelAttribute("soConObj") SocialconnectionMdl soConObj
            , BindingResult result
            , Model model
            , Principal principal
            , RedirectAttributes redirectAttributes
    ) {

        // authentication boilerplate for all mthd
        UserMdl authUserObj = userSrv.findByEmail(principal.getName()); model.addAttribute("authUser", authUserObj); model.addAttribute("authUserName", authUserObj.getUserName());

        if(!result.hasErrors()) {

            // (1) create variable that represents whether soCon record already exists between auth user and related user
            Optional<SocialconnectionMdl> preexistingSoConObj = Optional.ofNullable(socialconnectionRpo.existingSoConBaby(authUserObj.getId(), soConObj.getUsertwoUserMdl().getId()));

            // (2) run update-v-create based on value of that variable
            if ( preexistingSoConObj.isPresent() ) {
                System.out.println("we got a live one here!");
                System.out.println("preexistingSoConObj: " + preexistingSoConObj);

                // (1) get the preeixisting soCon object, using the auth user and the related user (incoming from form)
//                SocialconnectionMdl socialconnectionObj = socialconnectionSrv.findById(socialconnectionId);
                SocialconnectionMdl socialconnectionObj = socialconnectionRpo.existingSoConBaby(authUserObj.getId(), soConObj.getUsertwoUserMdl().getId());

                // (2) get objects for validation
//        UserMdl socialconnectionUserTwo = socialconnectionObj.getUsertwoUserMdl();
//        UserMdl socialconnectionUserOne = socialconnectionObj.getUseroneUserMdl();
//                UserMdl socialconnectionAuthorizedManager = preexistingSoConObj.getInitiatorUser(); // this replaced line above
                // none of above necessary anymore, it seems
                CodeMdl requiredSoConStatusCodeObj = codeSrv.findCodeMdlByCode("soConReset");

//                // (3a) this step regarding permissions is not germane to this method

                // (3b) validate: is request presently in required status?
                if(
                        !socialconnectionObj.getSoconStatus().equals(requiredSoConStatusCodeObj)
                ) {
                    redirectAttributes.addFlashAttribute("permissionErrorMsg", "Present relationship status does not allow submission of friend request.");
                    return "redirect:/connection/" ;
                }

                // (4) update the object to have the intended values
                socialconnectionObj.setSoconStatus(codeSrv.findCodeMdlByCode("requestPending")); // replaces above 12/5
                socialconnectionObj.setInitiatorUser(authUserObj); // added 12/5
                socialconnectionObj.setResponderUser(soConObj.getUsertwoUserMdl());
                socialconnectionObj.setUseroneUserMdl(authUserObj);
                socialconnectionObj.setUsertwoUserMdl(soConObj.getUsertwoUserMdl());

                // (5) run the update service on the object
                socialconnectionSrv.update(socialconnectionObj);

            } else {

            // (1) instantiate the new SoCon object, specifically as an object of BP'ed from the SocialconnectionMdl
            SocialconnectionMdl socialconnectionObj = new SocialconnectionMdl();

            // (2) infuse into that object all the values from the incoming model/form
                socialconnectionObj.setSoconStatus(codeSrv.findCodeMdlByCode("requestPending"));
                socialconnectionObj.setInitiatorUser(authUserObj);
                socialconnectionObj.setResponderUser(soConObj.getUsertwoUserMdl());
                socialconnectionObj.setUseroneUserMdl(authUserObj); // doesn't really matter in new design whether auth is userOne or userTwo.  make is userOne for sanity, see next line.
                socialconnectionObj.setUsertwoUserMdl(soConObj.getUsertwoUserMdl()); // how is this soConObj.usertwo thing populated?  hidden input on the object!

            // (3) run the service to create the record
            socialconnectionSrv.create(socialconnectionObj);
            }

        } else {
            System.out.println("soCon create attempted, but no bueno!");
        }
        return "redirect:/connection/";

    }



    @PostMapping("/connection/cancel/{id}")
    public String cancelConnection(
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
//        UserMdl socialconnectionUserTwo = socialconnectionObj.getUsertwoUserMdl();
//        UserMdl socialconnectionUserOne = socialconnectionObj.getUseroneUserMdl();
        UserMdl socialconnectionAuthorizedManager = socialconnectionObj.getInitiatorUser(); // this replaced line above
        CodeMdl requiredSoConStatusCodeObj = codeSrv.findCodeMdlByCode("requestPending");

        // (3a) validate: is current user the *sender* of a request?
        if(
                !authUserObj.equals(socialconnectionAuthorizedManager)
        ) {
            redirectAttributes.addFlashAttribute("permissionErrorMsg", "This request can only be cancelled to by its sender.");
            return "redirect:/connection/" ;
        }

        // (3b) validate: is request presently in pending status?
        if(
                !socialconnectionObj.getSoconStatus().equals(requiredSoConStatusCodeObj)
        ) {
            redirectAttributes.addFlashAttribute("permissionErrorMsg", "Present relationship status does not allow request cancellation.");
            return "redirect:/connection/" ;
        }

        // (4) update the object to have the intended values
//        socialconnectionObj.setSoconStatus(codeSrv.findCodeMdlByCode("requestCancelled"));
        socialconnectionObj.setSoconStatus(codeSrv.findCodeMdlByCode("soConReset")); // replaces above 12/5
        socialconnectionObj.setInitiatorUser(null); // added 12/5

        // (5) run the update service on the object
        socialconnectionSrv.update(socialconnectionObj);

        return "redirect:/connection/";
    }

    @PostMapping("/connection/accept/{id}")
    public String acceptConnection(
            @PathVariable("id") Long socialconnectionId
            , Principal principal
            , RedirectAttributes redirectAttributes
    ) {

        // authentication boilerplate for all mthd
        UserMdl authUserObj = userSrv.findByEmail(principal.getName()); // no model attributes here b/c no resulting page we are rending

        // (1) get the object from the path variable
        // YO!!!! big thought here: I THINK that we could adjust this whole thing to be better: make the form/button an object submitter;
        // if we did that, we could eliminate all the code for looking up that object by path variable;
        // additionally, removes possibilty for bad actor to mess with the variable and thus no need to validate for that in these methods. maybe??
        // it just might work... might be worth asking an old friend Cameron about this, yes?
        SocialconnectionMdl socialconnectionObj = socialconnectionSrv.findById(socialconnectionId);

        // (2) get objects for validation
        UserMdl socialconnectionUserTwo = socialconnectionObj.getUsertwoUserMdl();
        CodeMdl requiredSoConStatusCodeObj = codeSrv.findCodeMdlByCode("requestPending");

        // (3a) validate: is current user the recipient of a request?
        if(
                !authUserObj.equals(socialconnectionUserTwo)
        ) {
            redirectAttributes.addFlashAttribute("permissionErrorMsg", "This request can only be responded to by its intended invitation recipient.");
            return "redirect:/connection/" ;
        }

        // (3b) validate: is request presently in pending status?
        if(
            !socialconnectionObj.getSoconStatus().equals(requiredSoConStatusCodeObj)

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
        CodeMdl requiredSoConStatusCodeObj = codeSrv.findCodeMdlByCode("requestPending");

        // (3a) validate: is current user the recipient of a request?
        if(
                !authUserObj.equals(socialconnectionUserTwo)
        ) {
            redirectAttributes.addFlashAttribute("permissionErrorMsg", "This request can only be responded to by its intended invitation recipient.");
            return "redirect:/connection/" ;
        }

        // (3b) validate: is request presently in pending status?
        if(
            !socialconnectionObj.getSoconStatus().equals(requiredSoConStatusCodeObj)
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



    @PostMapping("/connection/reactivaterequest/{id}")
    public String reactivateRequestConnection(
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
//        UserMdl socialconnectionUserTwo = socialconnectionObj.getUsertwoUserMdl();
        UserMdl socialconnectionUserOne = socialconnectionObj.getUseroneUserMdl();
        CodeMdl requiredSoConStatusCodeObj = codeSrv.findCodeMdlByCode("requestCancelled");

        // (3a) validate: is current user the *sender* of a request?
        if(
                !authUserObj.equals(socialconnectionUserOne)
        ) {
            redirectAttributes.addFlashAttribute("permissionErrorMsg", "This request can only be reactivated to by its sender.");
            return "redirect:/connection/" ;
        }

        // (3b) validate: is request presently in pending status?
        if(
                !socialconnectionObj.getSoconStatus().equals(requiredSoConStatusCodeObj)
        ) {
            redirectAttributes.addFlashAttribute("permissionErrorMsg", "Present relationship status does not allow request reactivation.");
            return "redirect:/connection/" ;
        }

        // (4) update the object to have the intended soConStatus value
        socialconnectionObj.setSoconStatus(codeSrv.findCodeMdlByCode("requestPending"));

        // (5) run the update service on the object
        socialconnectionSrv.update(socialconnectionObj);

        return "redirect:/connection/";
    }

} // end ctl
