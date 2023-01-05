package com.f12s.playdatenow08.controllers;

import java.security.Principal;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import com.f12s.playdatenow08.models.CodeMdl;
import com.f12s.playdatenow08.services.CodeSrv;
import com.f12s.playdatenow08.validator.PlaydateValidator;
import com.f12s.playdatenow08.validator.RsvpValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.f12s.playdatenow08.models.RsvpMdl;
import com.f12s.playdatenow08.models.PlaydateMdl;
import com.f12s.playdatenow08.models.UserMdl;
import com.f12s.playdatenow08.pojos.PlaydateUserUnionRsvpUser;
import com.f12s.playdatenow08.services.RsvpSrv;
import com.f12s.playdatenow08.services.PlaydateSrv;
import com.f12s.playdatenow08.services.UserSrv;

@Controller
public class RsvpCtl {

    @Autowired
    private RsvpSrv rsvpSrv;

    @Autowired
    private UserSrv userSrv;

    @Autowired
    private PlaydateSrv playdateSrv;

    @Autowired
    private CodeSrv codeSrv;

    // adding below to mimic the userValidator program
    @Autowired
    private RsvpValidator rsvpValidator;

    @PostMapping("/playdate/{id}/rsvp/create")
    public String processRsvpNew(
            @PathVariable ("id") Long playdateId
//            , @Valid @ModelAttribute("rsvp") RsvpMdl rsvpMdl
            , @Valid @ModelAttribute("rsvp") RsvpMdl rsvpObjFromForm
            , BindingResult result
            , Model model
            , Principal principal
    ) {

        // authentication boilerplate for all mthd
        UserMdl authUserObj = userSrv.findByEmail(principal.getName()); model.addAttribute("authUser", authUserObj); model.addAttribute("authUserName", authUserObj.getUserName());

        // adding below to mimic the userValidator program; i think it's Obj, b/c that's what's in @ModelAttribute
        rsvpValidator.validate(rsvpObjFromForm, result);

        // (1) set up the as-is parent object (playdate!): needed for both the success path and error path
        PlaydateMdl playdateObj = playdateSrv.findById(playdateId);

        if(!result.hasErrors()) {

            // note: JRF not sure (2022-09-13) why this approach (using 'newOtc' object/etc.) is necessary here, but I do know from earlier testing that the create/validate/etc. won't work with the reg approach.
            // 2022.11.10: I think the problem with the regular approach might be something to do with the fact that the playdate ctrl method that rendered the playdate page did not include an rsvp obj, so gotta create it here and piece it together.
            // well, on second thought, that doesn't seem 100 right either.  oh well, moving on.

            // (1) instantiate the new object
            RsvpMdl newOtc = new RsvpMdl();
            // (2) infuse into that object all the values from the incoming model/form
            newOtc.setPlaydateMdl(playdateObj);
            newOtc.setUserMdl(authUserObj);
            newOtc.setRsvpStatus(rsvpObjFromForm.getRsvpStatus());
            newOtc.setKidCount(rsvpObjFromForm.getKidCount());
            newOtc.setAdultCount(rsvpObjFromForm.getAdultCount());
//            newOtc.setComment(rsvpObjFromForm.getComment());
            newOtc.setRespondentRsvpStatus(rsvpObjFromForm.getRespondentRsvpStatus());

            // (3) run the service to create the record
            rsvpSrv.create(newOtc);

            return "redirect:/playdate/" + playdateId ;

        } else {

            // (1) deliver the parent object to be displayed on page (note: instantiated herein above the 'if' statement)
            model.addAttribute("playdate", playdateObj);

            // (2) deliver list of unioned rsvp records for child record table
            List<PlaydateUserUnionRsvpUser> playdateRsvpList = rsvpSrv.playdateRsvpList(playdateId);
            model.addAttribute("playdateRsvpList", playdateRsvpList);

            // (3) calculate various RSVP-related stats and controls.
            List<RsvpMdl> rsvpList = rsvpSrv.returnAllRsvpForPlaydate(playdateObj); // list of rsvps, which we will use downstream
            Integer rsvpCount = 0; 							// here and below: instantiate the java variable that we will update in the loop
            Integer aggKidsCount = 0;
            Integer aggAdultsCount = 0;
            Boolean rsvpExistsCreatedByAuthUser = false;
            Integer openKidsSpots = 0;

            for (int i=0; i < rsvpList.size(); i++  ) { 	// for each record in the list of RSVPs...
                if ( rsvpList.get(i).getRsvpStatus().equals("In")) { // we only count the 'in' records towards totals
                    rsvpCount += 1;
                    aggKidsCount += rsvpList.get(i).getKidCount();
                    aggAdultsCount += rsvpList.get(i).getAdultCount();
                }

                if (rsvpList.get(i).getUserMdl().equals(authUserObj) )  // this 'if' sets needed flags if it exists for the logged in user, and delivers the RSVP object to the page as well
                {
                    rsvpExistsCreatedByAuthUser = true;
                    RsvpMdl rsvpObjForAuthUser = rsvpSrv.findById(rsvpList.get(i).getId());
                    model.addAttribute("rsvpObjForAuthUser", rsvpObjForAuthUser);
                }
            }

            if (playdateObj.getRsvpStatus().equals("In")) { // this 'if' accounts for the host family
                rsvpCount += 1;
                aggKidsCount += playdateObj.getKidCount();
                aggAdultsCount += playdateObj.getAdultCount();
            }

            openKidsSpots = playdateObj.getMaxCountKids() - aggKidsCount;

            model.addAttribute("playdate", playdateObj);
            model.addAttribute("rsvpCount", rsvpCount);
            model.addAttribute("aggKidsCount", aggKidsCount);
            model.addAttribute("rsvpExistsCreatedByAuthUser", rsvpExistsCreatedByAuthUser);
            model.addAttribute("rsvpList", rsvpList);
            model.addAttribute("aggAdultsCount", aggAdultsCount);
            model.addAttribute("openKidsSpots", openKidsSpots);
            // end: calculate various RSVP-related stats.

            // let's rock those rsvp status codes
            Long codeCategoryIdForPlaydateRsvpStatusCodes = Long.valueOf(5);
            List<CodeMdl> playdateRsvpStatusList = codeSrv.targetedCodeList(codeCategoryIdForPlaydateRsvpStatusCodes);
            model.addAttribute("playdateRsvpStatusList", playdateRsvpStatusList);

            return "playdate/record.jsp";
        }
    }

    // getting create-for-other-users to work
    @PostMapping("/playdate/{id}/rsvp/createforuser")
    public String processRsvpCreateForUser(
            @PathVariable ("id") Long playdateId
            , @Valid @ModelAttribute("rsvp") RsvpMdl rsvpObjFromForm
            , BindingResult result
            , Model model
            , Principal principal
    ) {

        // authentication boilerplate for all mthd
        UserMdl authUserObj = userSrv.findByEmail(principal.getName()); model.addAttribute("authUser", authUserObj); model.addAttribute("authUserName", authUserObj.getUserName());

        // (1) set up the as-is parent object (playdate!): needed for both the success path and error path
        PlaydateMdl playdateObj = playdateSrv.findById(playdateId);

        if(!result.hasErrors()) {

            // note: JRF not sure (2022-09-13) why this approach (using 'newOtc' object/etc.) is necessary here, but I do know from earlier testing that the create/validate/etc. won't work with the reg approach.
            // 2022.11.10: I think the problem with the regular approach might be something to do with the fact that the playdate ctrl method that rendered the playdate page did not include an rsvp obj, so gotta create it here and piece it together.
            // well, on second thought, that doesn't seem 100 right either.  oh well, moving on.

            // (1) instantiate the new object
            RsvpMdl newOtc = new RsvpMdl();
            // (2) infuse into that object all the values from the incoming model/form
            newOtc.setPlaydateMdl(playdateObj);

//            newOtc.setUserMdl(authUserObj); ////
            newOtc.setUserMdl(rsvpObjFromForm.getUserMdl()); ////

            newOtc.setRsvpStatus(rsvpObjFromForm.getRsvpStatus());
            newOtc.setKidCount(rsvpObjFromForm.getKidCount());
            newOtc.setAdultCount(rsvpObjFromForm.getAdultCount());
//            newOtc.setComment(rsvpObjFromForm.getComment());

            // (3) run the service to create the record
            rsvpSrv.create(newOtc);

            return "redirect:/playdate/" + playdateId ;

        } else {

            // (1) deliver the parent object to be displayed on page (note: instantiated herein above the 'if' statement)
            model.addAttribute("playdate", playdateObj);

            // (2) deliver list of unioned rsvp records for child record table
            List<PlaydateUserUnionRsvpUser> playdateRsvpList = rsvpSrv.playdateRsvpList(playdateId);
            model.addAttribute("playdateRsvpList", playdateRsvpList);

            // (3) calculate various RSVP-related stats and controls.
            List<RsvpMdl> rsvpList = rsvpSrv.returnAllRsvpForPlaydate(playdateObj); // list of rsvps, which we will use downstream
            Integer rsvpCount = 0; 							// here and below: instantiate the java variable that we will update in the loop
            Integer aggKidsCount = 0;
            Integer aggAdultsCount = 0;
            Boolean rsvpExistsCreatedByAuthUser = false;
            Integer openKidsSpots = 0;

            for (int i=0; i < rsvpList.size(); i++  ) { 	// for each record in the list of RSVPs...
                if ( rsvpList.get(i).getRsvpStatus().equals("In")) { // we only count the 'in' records towards totals
                    rsvpCount += 1;
                    aggKidsCount += rsvpList.get(i).getKidCount();
                    aggAdultsCount += rsvpList.get(i).getAdultCount();
                }

                if (rsvpList.get(i).getUserMdl().equals(authUserObj) )  // this 'if' sets needed flags if it exists for the logged in user, and delivers the RSVP object to the page as well
                {
                    rsvpExistsCreatedByAuthUser = true;
                    RsvpMdl rsvpObjForAuthUser = rsvpSrv.findById(rsvpList.get(i).getId());
                    model.addAttribute("rsvpObjForAuthUser", rsvpObjForAuthUser);
                }
            }

            if (playdateObj.getRsvpStatus().equals("In")) { // this 'if' accounts for the host family
                rsvpCount += 1;
                aggKidsCount += playdateObj.getKidCount();
                aggAdultsCount += playdateObj.getAdultCount();
            }

            openKidsSpots = playdateObj.getMaxCountKids() - aggKidsCount;

            model.addAttribute("playdate", playdateObj);
            model.addAttribute("rsvpCount", rsvpCount);
            model.addAttribute("aggKidsCount", aggKidsCount);
            model.addAttribute("rsvpExistsCreatedByAuthUser", rsvpExistsCreatedByAuthUser);
            model.addAttribute("rsvpList", rsvpList);
            model.addAttribute("aggAdultsCount", aggAdultsCount);
            model.addAttribute("openKidsSpots", openKidsSpots);
            // end: calculate various RSVP-related stats.

            return "playdate/record.jsp";
        }
    }

    @GetMapping("playdate/{playdateId}/rsvp/{rsvpId}/edit")
    public String displayRsvpEdit(
            @PathVariable("playdateId") Long playdateId
            , @PathVariable("rsvpId") Long rsvpId
            , Model model
            , Principal principal
    ) {
        // authentication boilerplate for all mthd
        UserMdl authUserObj = userSrv.findByEmail(principal.getName()); model.addAttribute("authUser", authUserObj); model.addAttribute("authUserName", authUserObj.getUserName());

        // (1a) deliver the object to be displayed on page: primary object
        RsvpMdl rsvpObj = rsvpSrv.findById(rsvpId);
        model.addAttribute("rsvp", rsvpObj);

        // (1b) deliver the object to be displayed on page: secondary object
        PlaydateMdl playdateObj = rsvpObj.getPlaydateMdl(); // get the object that is the parent to the primary object, for all downstream functions
        model.addAttribute("playdate", playdateObj);

        // (3) deliver list of unioned rsvp records for child record table
        List<PlaydateUserUnionRsvpUser> playdateRsvpList = rsvpSrv.playdateRsvpList(playdateId);
        model.addAttribute("playdateRsvpList", playdateRsvpList);

        // (4) send non-changing static attributes to the page, so static values can be used
        Date playdateCreatedAt = playdateObj.getCreatedAt();
        model.addAttribute("playdateCreatedAt", playdateCreatedAt);
        Long playdateCreatedById = playdateObj.getUserMdl().getId();
        model.addAttribute("playdateCreatedById", playdateCreatedById);
        String playdateCreatedByUserName = playdateObj.getUserMdl().getUserName();
        model.addAttribute("playdateCreatedByUserName", playdateCreatedByUserName);

        // (5) calculate various RSVP-related stats and controls.
        List<RsvpMdl> rsvpList = rsvpSrv.returnAllRsvpForPlaydate(playdateObj); // list of rsvps, which we will use downstream
        Integer rsvpCount = 0; 							// here and below: instantiate the java variable that we will update in the loop
        Integer aggKidsCount = 0;
        Integer aggAdultsCount = 0;
        Integer openKidsSpots = 0;

        for (int i=0; i < rsvpList.size(); i++  ) { 	// for each record in the list of RSVPs...
            if ( rsvpList.get(i).getRsvpStatus().equals("In")) { // we only count the 'in' records towards totals
                rsvpCount += 1;
                aggKidsCount += rsvpList.get(i).getKidCount();
                aggAdultsCount += rsvpList.get(i).getAdultCount();
            }
        }

        if (playdateObj.getRsvpStatus().equals("In")) { // this 'if' accounts for the host family
            rsvpCount += 1;
            aggKidsCount += playdateObj.getKidCount();
            aggAdultsCount += playdateObj.getAdultCount();
        }

        openKidsSpots = playdateObj.getMaxCountKids() - aggKidsCount;

        model.addAttribute("rsvpCount", rsvpCount);
        model.addAttribute("aggKidsCount", aggKidsCount);
        model.addAttribute("rsvpList", rsvpList);
        model.addAttribute("aggAdultsCount", aggAdultsCount);
        model.addAttribute("openKidsSpots", openKidsSpots);
        // end: calculate various RSVP-related stats.


        // let's rock those rsvp status codes
        Long codeCategoryIdForPlaydateRsvpStatusCodes = Long.valueOf(5);
        List<CodeMdl> playdateRsvpStatusList = codeSrv.targetedCodeList(codeCategoryIdForPlaydateRsvpStatusCodes);
        model.addAttribute("playdateRsvpStatusList", playdateRsvpStatusList);

        return "rsvp/edit.jsp";
    }

    @PostMapping("/rsvp/edit")
    public String processRsvpEdit(
//            @Valid @ModelAttribute("rsvp") RsvpMdl rsvpMdl
            @Valid @ModelAttribute("rsvp") RsvpMdl rsvpObjToBe
            , BindingResult result
            , Model model
            , Principal principal
            , RedirectAttributes redirectAttributes
    ) {

        // authentication boilerplate for all mthd
        UserMdl authUserObj = userSrv.findByEmail(principal.getName()); model.addAttribute("authUser", authUserObj); model.addAttribute("authUserName", authUserObj.getUserName());

        // (1) get as-is object from db.  several implications downstream herein.
        RsvpMdl rsvpObjAsIs = rsvpSrv.findById(rsvpObjToBe.getId()); // this is so circular, refactor please
        PlaydateMdl playdateObj = rsvpObjAsIs.getPlaydateMdl();

        // (2) validate if authenticated user has security to edit this rsvp record; if not, redirect user to record screen with flash msg.
        UserMdl rsvpCreatorUserMdl = rsvpObjAsIs.getUserMdl();   // gets the userMdl obj saved to the existing playdateObj
        if(!authUserObj.equals(rsvpCreatorUserMdl)) {
            redirectAttributes.addFlashAttribute("permissionErrorMsg", "That RSVP can only be edited by its creator.  Any edits just attempted have been discarded.");
            return "redirect:/playdate/" + playdateObj.getId();
        }

        // (3) get the playdateID that we need for both the success and error paths
        Long playdateId = playdateObj.getId();

        // adding below to mimic the userValidator program; i think it's Obj, b/c that's what's in @ModelAttribute
        rsvpValidator.validate(rsvpObjToBe, result);

        // (4) if not errors detected, proceed with update
        if(!result.hasErrors()) {

            // (a) update the rsvpObjToBe to include existing Creator and parent playdate records
//            UserMdl rsvpCreatorUserMdl = rsvpObj.getUserMdl();
//            rsvpMdl.setUserMdl( rsvpCreatorUserMdl);
            rsvpObjToBe.setUserMdl( rsvpCreatorUserMdl);
//            rsvpMdl.setPlaydateMdl(playdateObj);
            rsvpObjToBe.setPlaydateMdl(playdateObj);
//            rsvpSrv.update(rsvpMdl);

            // (b) run the service to update the db
            rsvpSrv.update(rsvpObjToBe);

            // (c) redirect to the record
            return "redirect:/playdate/" + playdateId;

        } else {

            // (1) deliver list of unioned rsvp records for child record table
            List<PlaydateUserUnionRsvpUser> playdateRsvpList = rsvpSrv.playdateRsvpList(playdateId);
            model.addAttribute("playdateRsvpList", playdateRsvpList);

            // (2) send non-changing static attributes to the page, so static values can be used
            Date playdateCreatedAt = playdateObj.getCreatedAt();
            model.addAttribute("playdateCreatedAt", playdateCreatedAt);
            Long playdateCreatedById = playdateObj.getUserMdl().getId();
            model.addAttribute("playdateCreatedById", playdateCreatedById);
            String playdateCreatedByUserName = playdateObj.getUserMdl().getUserName();
            model.addAttribute("playdateCreatedByUserName", playdateCreatedByUserName);

            // (3) calculate various RSVP-related stats and controls.
            List<RsvpMdl> rsvpList = rsvpSrv.returnAllRsvpForPlaydate(playdateObj); // list of rsvps, which we will use downstream
            Integer rsvpCount = 0; 							// here and below: instantiate the java variable that we will update in the loop
            Integer aggKidsCount = 0;
            Integer aggAdultsCount = 0;
            Integer openKidsSpots = 0;

            for (int i=0; i < rsvpList.size(); i++  ) { 	// for each record in the list of RSVPs...
                if ( rsvpList.get(i).getRsvpStatus().equals("In")) { // we only count the 'in' records towards totals
                    rsvpCount += 1;
                    aggKidsCount += rsvpList.get(i).getKidCount();
                    aggAdultsCount += rsvpList.get(i).getAdultCount();
                }
            }

            if (playdateObj.getRsvpStatus().equals("In")) { // this 'if' accounts for the host family
                rsvpCount += 1;
                aggKidsCount += playdateObj.getKidCount();
                aggAdultsCount += playdateObj.getAdultCount();
            }

            openKidsSpots = playdateObj.getMaxCountKids() - aggKidsCount;

            model.addAttribute("playdate", playdateObj);
            model.addAttribute("rsvpCount", rsvpCount);
            model.addAttribute("aggKidsCount", aggKidsCount);
            model.addAttribute("rsvpList", rsvpList);
            model.addAttribute("aggAdultsCount", aggAdultsCount);
            model.addAttribute("openKidsSpots", openKidsSpots);
            // end: calculate various RSVP-related stats.

            // (4) deliver error msg to page
            model.addAttribute("validationErrorMsg", "Uh-oh! Please fix the errors noted below and submit again.  (Or cancel.)");


            // let's rock those rsvp status codes
            Long codeCategoryIdForPlaydateRsvpStatusCodes = Long.valueOf(5);
            List<CodeMdl> playdateRsvpStatusList = codeSrv.targetedCodeList(codeCategoryIdForPlaydateRsvpStatusCodes);
            model.addAttribute("playdateRsvpStatusList", playdateRsvpStatusList);


            return "rsvp/edit.jsp"; // not sure if/how it is advisable to have this return/redirect/whatev to the url that contains the playdate_id and rsvp_id
        }
    }

    // below is delete mthd before spring security; frozen.
//	// delete rsvp
//    @DeleteMapping("/rsvp/{id}")
//    public String deleteRsvp(
//    		@PathVariable("id") Long rsvpId
//    		, HttpSession session
//    		, RedirectAttributes redirectAttributes
//    		) {
//		// If no userId is found in session, redirect to logout.  JRF: put this on basically all methods now, except the login/reg pages
//		if(session.getAttribute("userId") == null) {return "redirect:/logout";}
//		Long authenticatedUserId = (Long) session.getAttribute("userId");
//
//		RsvpMdl rsvpObj = rsvpSrv.findById(rsvpId);
//		PlaydateMdl playdateObj = rsvpObj.getPlaydateMdl();
//		Long playdateID = playdateObj.getId();
//
//		UserMdl currentUserMdl = userSrv.findById(authenticatedUserId); //  gets the userModel object by calling the user service with the session user id
//		UserMdl rsvpCreatorUserMdl = rsvpObj.getUserMdl();   // gets the userMdl obj saved to the existing playdateObj
//
//		if(!currentUserMdl.equals(rsvpCreatorUserMdl)) {
//			redirectAttributes.addFlashAttribute("permissionErrorMsg", "That RSVP can only be deleted by its creator.  RSVP not deleted.");
//			return "redirect:/playdate/" + playdateObj.getId();
//		}
//
//		rsvpSrv.delete(rsvpObj);
//        return "redirect:/playdate/" + playdateID;
//    }

    // below is the deleteMapping method which should work.... but won't.  replaced by wholly new approach that follows
//	// delete rsvp
//    @DeleteMapping("/rsvp/{id}")
//    public String deleteRsvp(
//    		@PathVariable("id") Long rsvpId
////    		, HttpSession session
//			, Principal principal // added for spring
//    		, RedirectAttributes redirectAttributes
//    		) {
////		// If no userId is found in session, redirect to logout.  JRF: put this on basically all methods now, except the login/reg pages
////		if(session.getAttribute("userId") == null) {return "redirect:/logout";}
////		Long authenticatedUserId = (Long) session.getAttribute("userId");
//
//		// above replaced by below
//    	// authentication boilerplate for all mthd
//		UserMdl authUserObj = userSrv.findByEmail(principal.getName());
////		model.addAttribute("authUser", authUserObj);
////		model.addAttribute("authUserName", authUserObj.getUserName()); // set the "as-is" username, so it can be statically posted to the top right nav bar
//
//		RsvpMdl rsvpObj = rsvpSrv.findById(rsvpId);
//		PlaydateMdl playdateObj = rsvpObj.getPlaydateMdl();
//		Long playdateID = playdateObj.getId();
//
////		UserMdl currentUserMdl = userSrv.findById(authenticatedUserId); //  gets the userModel object by calling the user service with the session user id
//		// above no longer needed
//		UserMdl rsvpCreatorUserMdl = rsvpObj.getUserMdl();   // gets the userMdl obj saved to the existing playdateObj
//
////		if(!currentUserMdl.equals(rsvpCreatorUserMdl)) {
//		// above replaced by below
//		if(!authUserObj.equals(rsvpCreatorUserMdl)) {
//			redirectAttributes.addFlashAttribute("permissionErrorMsg", "That RSVP can only be deleted by its creator.  RSVP not deleted.");
//			return "redirect:/playdate/" + playdateObj.getId();
//		}
//
//		rsvpSrv.delete(rsvpObj);
//        return "redirect:/playdate/" + playdateID;
//    }

    @RequestMapping("/rsvp/delete/{id}")
    public String deleteRsvp(
            @PathVariable("id") Long rsvpId
            , Principal principal
            , RedirectAttributes redirectAttributes
    ) {

        // authentication boilerplate for all mthd
        UserMdl authUserObj = userSrv.findByEmail(principal.getName());

        // (1) get primary object, needed for both success and error paths
        RsvpMdl rsvpObj = rsvpSrv.findById(rsvpId);
//        PlaydateMdl playdateObj = rsvpObj.getPlaydateMdl();
//        Long playdateID = playdateObj.getId();

        // (2) get playdateId, also needed for success/error paths
        Long playdateID = rsvpObj.getPlaydateMdl().getId();

//        // (3) validate if authenticated user has security to delete this rsvp record; if not, redirect user to record screen with flash msg.
//        UserMdl rsvpCreatorUserMdl = rsvpObj.getUserMdl();
//        if(!authUserObj.equals(rsvpCreatorUserMdl)) {
//            redirectAttributes.addFlashAttribute("permissionErrorMsg", "The RSVP can only be deleted by its creator.  RSVP not deleted.");
////            return "redirect:/playdate/" + playdateObj.getId();
//            return "redirect:/playdate/" + playdateID;
//        }

        // (3) validate if authenticated user has security to delete this rsvp record; if not, redirect user to record screen with flash msg.
        UserMdl rsvpCreatorUserMdl = rsvpObj.getUserMdl();
        System.out.println("rsvpObj.getUserMdl().getUserName(): " + rsvpObj.getUserMdl().getUserName() );

        UserMdl playdateCreatorUserObj = rsvpObj.getPlaydateMdl().getUserMdl();
        System.out.println("rsvpObj.getPlaydateMdl().getUserMdl().getUserName()" + rsvpObj.getPlaydateMdl().getUserMdl().getUserName()  );


        if(
                !authUserObj.equals(rsvpCreatorUserMdl)
                && !authUserObj.equals(playdateCreatorUserObj)

        ) {
            redirectAttributes.addFlashAttribute("permissionErrorMsg", "You do not have permission to delete the RSVP/invite.  RSVP not deleted.");
//            return "redirect:/playdate/" + playdateObj.getId();
            return "redirect:/playdate/" + playdateID;
        }

        // (4) run the delete service and redirect to parent record
        rsvpSrv.delete(rsvpObj);
        return "redirect:/playdate/" + playdateID;
    }

} // end of methods