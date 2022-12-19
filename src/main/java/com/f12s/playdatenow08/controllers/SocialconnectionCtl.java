package com.f12s.playdatenow08.controllers;

import com.f12s.playdatenow08.models.*;
import com.f12s.playdatenow08.pojos.UserSocialConnectionPjo;
import com.f12s.playdatenow08.repositories.SocialconnectionRpo;
import com.f12s.playdatenow08.repositories.UserRpo;
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
import java.util.Objects;
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

    // trying here 12/7
    @Autowired
    private UserRpo userRpo;

    // view all socialConnection
    @GetMapping("/connection")
    public String displayConnectionAll(
//            @ModelAttribute("soConObj") SocialconnectionMdl soConObj // I don't see if/how this is necessary.  old RSVP appendage. 2022.12.01;
            // that said, if comment out, redSqig error; I think it's b/c the friends list and blocked list still has an "add friend" button/functionality??
            // 12/7: WHOA!  above is absolutely required: it puts the soConObj on various records' action-forms in the various lists on the page!
            @ModelAttribute("soConObjForm") SocialconnectionMdl soConObjForm // I don't see if/how this is necessary.  old RSVP appendage. 2022.12.01;
            , Model model
            , Principal principal
    ) {

        // authentication boilerplate for all mthd
        UserMdl authUserObj = userSrv.findByEmail(principal.getName()); model.addAttribute("authUser", authUserObj); model.addAttribute("authUserName", authUserObj.getUserName());

        // (1) soCon lists

//        List<UserSocialConnectionPjo> userSocialConnectionListBlocked = userSrv.userSocialConnectionListBlocked(authUserObj.getId());
//        model.addAttribute("userSocialConnectionListBlocked", userSocialConnectionListBlocked);

        List<UserSocialConnectionPjo> userSocialConnectionListSent = userSrv.userSocialConnectionListSent(authUserObj.getId());
        model.addAttribute("userSocialConnectionListSent", userSocialConnectionListSent);

        List<UserSocialConnectionPjo> userSocialConnectionListReceived = userSrv.userSocialConnectionListReceived(authUserObj.getId());
        model.addAttribute("userSocialConnectionListReceived", userSocialConnectionListReceived);

        List<UserSocialConnectionPjo> userSocialConnectionListFriends = userSrv.userSocialConnectionListFriends(authUserObj.getId());
        model.addAttribute("userSocialConnectionListFriends", userSocialConnectionListFriends);

        return "connection/list.jsp";
    }

    @PostMapping("/socialconnection/request")
    // NOTE: this entire request needs an overhaul.
    // (1) prob do away with bindingresult/result and model/model
    // (2) someone need to validate whether incoming user value exist in db or not.  at present, hacking UI to be a bogus user.id just blows up page
    public String processSoconNew(
            @Valid @ModelAttribute("soConObjForm") SocialconnectionMdl soConObjForm
            , BindingResult result // I *think* this entire BR/result program can be taken out of this mthd, b/c there's no model attributes being validated here, nor any validator srv
            , Model model
            , Principal principal
            , RedirectAttributes redirectAttributes
    ) {

        // authentication boilerplate for all mthd
        UserMdl authUserObj = userSrv.findByEmail(principal.getName()); // no model attributes here b/c no resulting page we are rending

        if(!result.hasErrors()) {

            // (x) validate whether target user exists; if not, this entire mthd is fed.  Note, can't get any of this to work in a useful way.
//            Optional<Optional<UserMdl>> validResponderUserObject = Optional.of(userRpo.findById(soConObjForm.getResponderUser().getId())); // no idea WTF this is
//
//            UserMdl validResponderUserObject = userSrv.findById(soConObjForm.getResponderUser().getId());
//
//            Optional<UserMdl> validResponderUserObject = Optional.ofNullable(userRpo.findByIdIs(soConObjForm.getResponderUser().getId()));
//
//            boolean validResponderUserObject = userRpo.existsById(soConObjForm.getId();
//
//            System.out.println("soConObjForm.getResponderUser().getId(): " + soConObjForm.getResponderUser().getId());
//            System.out.println("validResponderUserObject: " + validResponderUserObject);
//
//            Long userFacingNumber = soConObjForm.getResponderUser().getId();
//
//            System.out.println(userFacingNumber);
//
//            if ( validResponderUserObject != null ) {
//                System.out.println("we got a good respondent object/ID here");
//            } else {
//                System.out.println("the submitted respondent object/ID is bullshit");
//            }
//
//            // (testing)
//
//            String soConObjFormDotobjectOrigin = soConObjForm.getObjectOrigin();
            System.out.println("soConObjForm.getObjectOrigin(): " + soConObjForm.getObjectOrigin() );

            // (1) create variables
            Optional<SocialconnectionMdl> preexistingSoConObj = Optional.ofNullable(socialconnectionRpo.existingSoConBaby(authUserObj.getId(), soConObjForm.getResponderUser().getId())); // represents whether soCon record already exists between auth user and related user
            CodeMdl requiredSoConStatusCodeObj = codeSrv.findCodeMdlByCode("soConReset"); // used in the update path
            CodeMdl toBeSoconstatusCodeObj = codeSrv.findCodeMdlByCode("requestPending");
//            Long soConObjFormDotId = soConObjForm.getId();

            // (2) run update-v-create based on value of that variable
            if ( preexistingSoConObj.isPresent()) {

                // (2a1) get the as-is soCon object, using the auth user and the related user (incoming from form).  MORE: This object needs to be called separately from preexistingSoConObj listed above.  get/set methods downstream won't work on preexistingSoConObj, not entirely sure why.
                SocialconnectionMdl soConObject = socialconnectionRpo.existingSoConBaby(authUserObj.getId(), soConObjForm.getResponderUser().getId());

                // (2a2) validate: is request presently in required status?
                if( !soConObject.getSoconstatusCode().equals(requiredSoConStatusCodeObj)) {
                    redirectAttributes.addFlashAttribute("permissionErrorMsg", "Present relationship status does not allow submission of friend request.");
                    return "redirect:/profile/" ;
                }

                // note: no need to validate who is initiator/requestor here: if status is soConReset, we are gonna reset all that anyway.  soconStatus values are very powerful.

                // (2a3) update the object to have the intended values
                soConObject.setSoconstatusCode(toBeSoconstatusCodeObj);
                soConObject.setInitiatorUser(authUserObj);
                soConObject.setResponderUser(soConObjForm.getResponderUser());

                // (2a4) run the update service on the object
                socialconnectionSrv.update(soConObject);

//                if (Objects.equals(soConObjForm.getObjectOrigin(), "profileRecord")) {
//                    return "redirect:/profile/" + soConObjForm.getResponderUser().getId();
//                }

            } else {

                // (2b1) instantiate the new SoCon object, specifically as an object of BP'ed from the SocialconnectionMdl
                SocialconnectionMdl soConObject = new SocialconnectionMdl();

                // (2b2) validate: no required on create path

                // (2b3) infuse into that object all the values from the incoming model/form
                soConObject.setSoconstatusCode(toBeSoconstatusCodeObj);
                soConObject.setInitiatorUser(authUserObj); // this just makes sense: the authuser is setting this in motion, so make him the initiator!
                soConObject.setResponderUser(soConObjForm.getResponderUser());

                // (2b4) run the service to create the record
                socialconnectionSrv.create(soConObject);
            }

        } else {
            System.out.println("soCon request attempted, but no bueno!");
        }

        if (Objects.equals(soConObjForm.getObjectOrigin(), "profileRecord")) {
            return "redirect:/profile/" + soConObjForm.getResponderUser().getId();
        } else {
            return "redirect:/profile/";
        }

    }

    @PostMapping("/socialconnection/cancel")
    public String cancelConnection(
//            @PathVariable("id") Long socialconnectionId
            @Valid @ModelAttribute("soConObjForm") SocialconnectionMdl soConObjForm
//            , BindingResult result // I *think* this entire BR/result program can be taken out of this mthd, b/c there's no model attributes being validated here, nor any validator srv
//            , Model model  // similar to above: not used here
            , Principal principal
            , RedirectAttributes redirectAttributes
    ) {

        // authentication boilerplate for all mthd
        UserMdl authUserObj = userSrv.findByEmail(principal.getName()); // no model attributes here b/c no resulting page we are rendering

        // testing/proving:
//        System.out.println("soConObjForm.getId(): " + soConObjForm.getId());
//        System.out.println("soConObjForm.getInitiatorUser(): " + soConObjForm.getInitiatorUser());
//        System.out.println("soConObjForm.getResponderUser(): " + soConObjForm.getResponderUser());
//        System.out.println("soConObjForm.getSoconstatusCode(): " + soConObjForm.getSoconstatusCode());

        // (1) validate: does soConObj (uid'ed by incoming hidden field on soConObjForm) exist?
        Optional<SocialconnectionMdl> soConFromForm = Optional.ofNullable(socialconnectionRpo.soConFromForm(soConObjForm.getId()));  // represents whether soCon record exists

//        if (!soConFromForm.isPresent() ) {
        if (soConFromForm.isEmpty()) { // replaced with '.isEmpty' as suggested by IJ-IDEA
//            System.out.println("Hidden id value on GUI form was tampered to be invalid, so cancel process was aborted, and user shall be returned to connections list page with no further action.");
            redirectAttributes.addFlashAttribute("permissionErrorMsg", "Relationship error.  No records saved or updated.");
            return "redirect:/profile/";
        }

        // (2) get the as-is soCon object, using the auth user and the related user (incoming from form).  MORE: This object needs to be called separately from preexistingSoConObj listed above.  get/set methods downstream won't work on preexistingSoConObj, not entirely sure why.
        SocialconnectionMdl soConObject = socialconnectionSrv.findById(soConObjForm.getId());

        // (3) create variables
        CodeMdl requiredSoConStatusCodeObj = codeSrv.findCodeMdlByCode("requestPending"); // used in the update path
        CodeMdl toBeSoconstatusCodeObj = codeSrv.findCodeMdlByCode("soConReset");
        UserMdl soConObjAuthorizedManager = soConObject.getInitiatorUser(); // this replaced line above

        // (4a) validate: is request presently in required status?
        if (!soConObject.getSoconstatusCode().equals(requiredSoConStatusCodeObj)) {
            redirectAttributes.addFlashAttribute("permissionErrorMsg", "Present relationship status does not allow cancellation of friend request.");
            return "redirect:/profile/";
        }

        // (4b) validate: is current user authorized to take such action on the soConObj?
        if (!authUserObj.equals(soConObjAuthorizedManager)) {
            redirectAttributes.addFlashAttribute("permissionErrorMsg", "This request can only be cancelled to by its sender.");
            return "redirect:/profile/";
        }

        // (5) update the object to have the intended values
        soConObject.setSoconstatusCode(toBeSoconstatusCodeObj);

        // (6) run the update service on the object
        socialconnectionSrv.update(soConObject);

//        return "redirect:/profile/";
        if ( Objects.equals(soConObjForm.getObjectOrigin(), "profileRecord") ) {
            return "redirect:/profile/" + soConObject.getResponderUser().getId();
        } else if (  Objects.equals(soConObjForm.getObjectOrigin(), "connectionList")  ) {
            return "redirect:/connection/" ;
        } else {
            return "redirect:/profile/";
        }
    }

    @PostMapping("/socialconnection/decline")
    public String declineConnection(
            @Valid @ModelAttribute("soConObjForm") SocialconnectionMdl soConObjForm
            , Principal principal
            , RedirectAttributes redirectAttributes
    ) {

        // authentication boilerplate for all mthd
        UserMdl authUserObj = userSrv.findByEmail(principal.getName()); // no model attributes here b/c no resulting page we are rendering

        // (1) validate: does soConObj (uid'ed by incoming hidden field on soConObjForm) exist?
        Optional<SocialconnectionMdl> soConFromForm = Optional.ofNullable(socialconnectionRpo.soConFromForm(soConObjForm.getId()));  // represents whether soCon record exists

        if (soConFromForm.isEmpty()) {
            redirectAttributes.addFlashAttribute("permissionErrorMsg", "Relationship error.  No records saved or updated.");
            return "redirect:/profile/";
        }

        // (2) get the as-is soCon object, using the auth user and the related user (incoming from form).  MORE: This object needs to be called separately from preexistingSoConObj listed above.  get/set methods downstream won't work on preexistingSoConObj, not entirely sure why.
        SocialconnectionMdl soConObject = socialconnectionSrv.findById(soConObjForm.getId());

        // (3) create variables
        CodeMdl requiredSoConStatusCodeObj = codeSrv.findCodeMdlByCode("requestPending"); // used in the update path
        CodeMdl toBeSoconstatusCodeObj = codeSrv.findCodeMdlByCode("soConReset");
        UserMdl soConObjAuthorizedManager = soConObject.getResponderUser(); // now it's the respondent only who can take this action

        // (4a) validate: is request presently in required status?
        if (!soConObject.getSoconstatusCode().equals(requiredSoConStatusCodeObj)) {
            redirectAttributes.addFlashAttribute("permissionErrorMsg", "Present relationship status does not allow cancellation of friend request.");
            return "redirect:/profile/";
        }

        // (4b) validate: is current user authorized to take such action on the soConObj?
        if (!authUserObj.equals(soConObjAuthorizedManager)) {
            redirectAttributes.addFlashAttribute("permissionErrorMsg", "This request can only be declined to by its recipient.");
            return "redirect:/profile/";
        }

        // (5) update the object to have the intended values
        soConObject.setSoconstatusCode(toBeSoconstatusCodeObj);

        // (6) run the update service on the object
        socialconnectionSrv.update(soConObject);

//        return "redirect:/profile/";
        if ( Objects.equals(soConObjForm.getObjectOrigin(), "profileRecord") ) {
//            return "redirect:/profile/" + soConObject.getResponderUser().getId();
            return "redirect:/profile/" + soConObject.getInitiatorUser().getId() ;
        } else if (  Objects.equals(soConObjForm.getObjectOrigin(), "connectionList")  ) {
            return "redirect:/connection/" ;
        } else {
            return "redirect:/profile/";
        }
    }

    @PostMapping("/socialconnection/accept")
    public String acceptConnection(
            @Valid @ModelAttribute("soConObjForm") SocialconnectionMdl soConObjForm
            , Principal principal
            , RedirectAttributes redirectAttributes
    ) {

        // authentication boilerplate for all mthd
        UserMdl authUserObj = userSrv.findByEmail(principal.getName()); // no model attributes here b/c no resulting page we are rendering

        // (1) validate: does soConObj (uid'ed by incoming hidden field on soConObjForm) exist?
        Optional<SocialconnectionMdl> soConFromForm = Optional.ofNullable(socialconnectionRpo.soConFromForm(soConObjForm.getId()));  // represents whether soCon record exists

        if (soConFromForm.isEmpty()) {
//            System.out.println("Hidden id value on GUI form was tampered to be invalid, so cancel process was aborted, and user shall be returned to connections list page with no further action.");
            redirectAttributes.addFlashAttribute("permissionErrorMsg", "Relationship error.  No records saved or updated.");
            return "redirect:/profile/";
        }

        // (2) get the as-is soCon object, using the auth user and the related user (incoming from form).  MORE: This object needs to be called separately from preexistingSoConObj listed above.  get/set methods downstream won't work on preexistingSoConObj, not entirely sure why.
        SocialconnectionMdl soConObject = socialconnectionSrv.findById(soConObjForm.getId());

        // (3) create variables
        CodeMdl requiredSoConStatusCodeObj = codeSrv.findCodeMdlByCode("requestPending"); // used in the update path
        CodeMdl toBeSoconstatusCodeObj = codeSrv.findCodeMdlByCode("friends");
        UserMdl soConObjAuthorizedManager = soConObject.getResponderUser(); // this line replaced above; now it's the respondent only who can take this action

        // (4a) validate: is request presently in required status?
        if (!soConObject.getSoconstatusCode().equals(requiredSoConStatusCodeObj)) {
            redirectAttributes.addFlashAttribute("permissionErrorMsg", "Present relationship status does not allow acceptance of friend request.");
            return "redirect:/profile/";
        }

        // (4b) validate: is current user authorized to take such action on the soConObj?
        if (!authUserObj.equals(soConObjAuthorizedManager)) {
            redirectAttributes.addFlashAttribute("permissionErrorMsg", "This request can only be accepted to by its recipient.");
            return "redirect:/profile/";
        }

        // (5) update the object to have the intended values
        soConObject.setSoconstatusCode(toBeSoconstatusCodeObj);

        // (6) run the update service on the object
        socialconnectionSrv.update(soConObject);

//        return "redirect:/profile/";
        if ( Objects.equals(soConObjForm.getObjectOrigin(), "profileRecord") ) {
//            return "redirect:/profile/" + soConObject.getResponderUser().getId();
            return "redirect:/profile/" + soConObject.getInitiatorUser().getId() ;
        } else if (  Objects.equals(soConObjForm.getObjectOrigin(), "connectionList")  ) {
            return "redirect:/connection/" ;
        } else {
            return "redirect:/profile/";
        }
    }

    @PostMapping("/socialconnection/unfriend")
    public String unfriendConnection(
            @Valid @ModelAttribute("soConObjForm") SocialconnectionMdl soConObjForm
            , Principal principal
            , RedirectAttributes redirectAttributes
    ) {

        // authentication boilerplate for all mthd
        UserMdl authUserObj = userSrv.findByEmail(principal.getName()); // no model attributes here b/c no resulting page we are rendering

        // (1) validate: does soConObj (uid'ed by incoming hidden field on soConObjForm) exist?
        Optional<SocialconnectionMdl> soConFromForm = Optional.ofNullable(socialconnectionRpo.soConFromForm(soConObjForm.getId()));  // represents whether soCon record exists

        if (soConFromForm.isEmpty()) {
//            System.out.println("Hidden id value on GUI form was tampered to be invalid, so cancel process was aborted, and user shall be returned to connections list page with no further action.");
            redirectAttributes.addFlashAttribute("permissionErrorMsg", "Relationship error.  No records saved or updated.");
            return "redirect:/profile/";
        }

        // (2) get the as-is soCon object, using the auth user and the related user (incoming from form).  MORE: This object needs to be called separately from preexistingSoConObj listed above.  get/set methods downstream won't work on preexistingSoConObj, not entirely sure why.
        SocialconnectionMdl soConObject = socialconnectionSrv.findById(soConObjForm.getId());

        // (3) create variables
        CodeMdl requiredSoConStatusCodeObj = codeSrv.findCodeMdlByCode("friends"); // used in the update path
        CodeMdl toBeSoconstatusCodeObj = codeSrv.findCodeMdlByCode("soConReset");
        UserMdl soConObjAuthorizedManager = soConObject.getResponderUser(); // this is one side of the relationship
        UserMdl soConObjAuthorizedManager2 = soConObject.getInitiatorUser(); // this is the other side of the relationship

        // (4a) validate: is request presently in required status?
        if (!soConObject.getSoconstatusCode().equals(requiredSoConStatusCodeObj)) {
            redirectAttributes.addFlashAttribute("permissionErrorMsg", "Present relationship status does not allow removal of friend.");
            return "redirect:/profile/";
        }

        // (4b) validate: is current user authorized to take such action on the soConObj?
        if (
                !authUserObj.equals(soConObjAuthorizedManager) &&
                        !authUserObj.equals(soConObjAuthorizedManager2)
        ){
            redirectAttributes.addFlashAttribute("permissionErrorMsg", "Only a friend can dissolve the friendship.");
            return "redirect:/profile/";
        }

        // (5) update the object to have the intended values
        soConObject.setSoconstatusCode(toBeSoconstatusCodeObj);

        // (6) run the update service on the object
        socialconnectionSrv.update(soConObject);

        // (7) reaffirm the userid that is the newly unfriended user:
        // first, set redirect as initiator as default: assume authUser is the initiator, so have the redirect be the responder
        Long profileRedirectId = soConObject.getResponderUser().getId();
        // but if authUser  the responder, then do the reverse: have the redirect be to the initiator
        if ( authUserObj.equals(soConObject.getResponderUser() ) ) {
            profileRedirectId = soConObject.getInitiatorUser().getId();
        }

//        return "redirect:/profile/";
        if ( Objects.equals(soConObjForm.getObjectOrigin(), "profileRecord") ) {
            ///
            return "redirect:/profile/" + profileRedirectId ;
        } else if (  Objects.equals(soConObjForm.getObjectOrigin(), "connectionList")  ) { // note, all this elseIf/else is deprecated for this scenario: unfriending only happens on use profile now
            return "redirect:/connection/" ;
        } else {
            return "redirect:/profile/";
        }
    }

    @PostMapping("/socialconnection/block")
    // (2) somehow need to validate whether incoming user value exist in db or not.  at present, hacking UI to be a bogus user.id just blows up page
    public String blockConnection(
            @Valid @ModelAttribute("soConObjForm") SocialconnectionMdl soConObjForm
            , Principal principal
            , RedirectAttributes redirectAttributes
    ) {

        // authentication boilerplate for all mthd
        UserMdl authUserObj = userSrv.findByEmail(principal.getName()); // no model attributes here b/c no resulting page we are rending

        // (1) create variables
        Optional<SocialconnectionMdl> preexistingSoConObj = Optional.ofNullable(socialconnectionRpo.existingSoConBaby(authUserObj.getId(), soConObjForm.getResponderUser().getId())); // represents whether soCon record already exists between auth user and related user
//            CodeMdl requiredSoConStatusCodeObj = codeSrv.findCodeMdlByCode("soConReset"); // used in the update path
        CodeMdl prohibitedSoConStatusCodeObj = codeSrv.findCodeMdlByCode("blocked"); // replaces above
        CodeMdl toBeSoconstatusCodeObj = codeSrv.findCodeMdlByCode("blocked");

        // (2) run update-v-create based on value of that variable
        if ( preexistingSoConObj.isPresent()) {

            // (2a1) get the as-is soCon object, using the auth user and the related user (incoming from form).  MORE: This object needs to be called separately from preexistingSoConObj listed above.  get/set methods downstream won't work on preexistingSoConObj, not entirely sure why.
            SocialconnectionMdl soConObject = socialconnectionRpo.existingSoConBaby(authUserObj.getId(), soConObjForm.getResponderUser().getId());

            // (2a2) validate: is request presently in *prohibited* status?
            if( soConObject.getSoconstatusCode().equals(prohibitedSoConStatusCodeObj)) {
                redirectAttributes.addFlashAttribute("permissionErrorMsg", "Present relationship status does not allow user blocking.");
                return "redirect:/profile/" ;
            }

            // (2a3) update the object to have the intended values
            soConObject.setSoconstatusCode(toBeSoconstatusCodeObj);
            soConObject.setBlockerUser(authUserObj); // *do* set the blocker value

            // (2a4) run the update service on the object
            socialconnectionSrv.update(soConObject);

        } else {

            // (2b1) instantiate the new SoCon object, specifically as an object of BP'ed from the SocialconnectionMdl
            SocialconnectionMdl soConObject = new SocialconnectionMdl();

            // (2b2) validate: no required on create path

            // (2b3) infuse into that object all the values from the incoming model/form
            soConObject.setSoconstatusCode(toBeSoconstatusCodeObj);
            soConObject.setInitiatorUser(authUserObj); // this just makes sense: the authuser is setting this in motion, so make him the initiator!
            soConObject.setResponderUser(soConObjForm.getResponderUser());
            soConObject.setBlockerUser(authUserObj); // *do* set the blocker value

            // (2b4) run the service to create the record
            socialconnectionSrv.create(soConObject);
        }

        redirectAttributes.addFlashAttribute("permissionErrorMsg", "User has been blocked.  To view all users you've blocked, click Display Blocked Users below.");
//        return "redirect:/profile/";
        return "redirect:/settings/";

    }

    @PostMapping("/socialconnection/unblock")
    public String unblockConnection(
            @Valid @ModelAttribute("soConObjForm") SocialconnectionMdl soConObjForm
            , Principal principal
            , RedirectAttributes redirectAttributes
    ) {

        // authentication boilerplate for all mthd
        UserMdl authUserObj = userSrv.findByEmail(principal.getName()); // no model attributes here b/c no resulting page we are rendering

        // (1) validate: does soConObj (uid'ed by incoming hidden field on soConObjForm) exist?
        Optional<SocialconnectionMdl> soConFromForm = Optional.ofNullable(socialconnectionRpo.soConFromForm(soConObjForm.getId()));  // represents whether soCon record exists

        if (soConFromForm.isEmpty()) {
            redirectAttributes.addFlashAttribute("permissionErrorMsg", "Relationship error.  No records saved or updated.");
            return "redirect:/profile/";
        }

        // (2) get the as-is soCon object, using the auth user and the related user (incoming from form).  MORE: This object needs to be called separately from preexistingSoConObj listed above.  get/set methods downstream won't work on preexistingSoConObj, not entirely sure why.
        SocialconnectionMdl soConObject = socialconnectionSrv.findById(soConObjForm.getId());

        // (3) create variables
        CodeMdl requiredSoConStatusCodeObj = codeSrv.findCodeMdlByCode("blocked"); // used in the update path
        CodeMdl toBeSoconstatusCodeObj = codeSrv.findCodeMdlByCode("soConReset");
        UserMdl soConObjAuthorizedManager = soConObject.getBlockerUser(); // this replaced line above

        // (4a) validate: is request presently in required status?
        if (!soConObject.getSoconstatusCode().equals(requiredSoConStatusCodeObj)) {
            redirectAttributes.addFlashAttribute("permissionErrorMsg", "Present relationship status does not allow unblocking.");
            return "redirect:/profile/";
        }

        // (4b) validate: is current user authorized to take such action on the soConObj?
        if (!authUserObj.equals(soConObjAuthorizedManager)) {
            redirectAttributes.addFlashAttribute("permissionErrorMsg", "Unblocking action can only be performed by original relationship blocker.");
            return "redirect:/profile/";
        }

        // (5) update the object to have the intended values
        soConObject.setSoconstatusCode(toBeSoconstatusCodeObj);
        soConObject.setBlockerUser(null);

        // (6) run the update service on the object
        socialconnectionSrv.update(soConObject);

        // (7) reaffirm the userid that is the newly unblocked user:
        // first, set redirect as initiator as default: assume authUser is the initiator, so have the redirect be the responder
        Long profileRedirectId = soConObject.getResponderUser().getId();
        // but if authUser  the responder, then do the reverse: have the redirect be to the initiator
        if ( authUserObj.equals(soConObject.getResponderUser() ) ) {
            profileRedirectId = soConObject.getInitiatorUser().getId();
        }

//        return "redirect:/profile/";
        return "redirect:/profile/" + profileRedirectId ;
    }

} // end ctl
