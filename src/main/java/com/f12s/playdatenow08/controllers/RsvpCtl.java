package com.f12s.playdatenow08.controllers;

import java.security.Principal;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

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

    @PostMapping("/playdate/{id}/rsvp/create")
    public String processRsvpNew(
            @PathVariable ("id") Long playdateId
            , @Valid @ModelAttribute("rsvp") RsvpMdl rsvpMdl
            , BindingResult result
            , Model model
            , Principal principal
    ) {

        // authentication boilerplate for all mthd
        UserMdl authUserObj = userSrv.findByEmail(principal.getName()); model.addAttribute("authUser", authUserObj); model.addAttribute("authUserName", authUserObj.getUserName());

        PlaydateMdl playdateObj = playdateSrv.findById(playdateId);

        if(result.hasErrors()) {
            model.addAttribute("playdate", playdateObj);
            return "playdate/record.jsp";
        } else {

            // note: JRF not sure (2022-09-13) why this approach (using 'newOtc' object/etc.) is necessary here, but I do know from earlier testing that the create/validate/etc. won't work with the reg approach

            // (1) instantiate the new object
            RsvpMdl newOtc = new RsvpMdl();
            // (2) infuse into that object all the values from the incoming model/form
            newOtc.setPlaydateMdl(playdateObj);
            newOtc.setUserMdl(authUserObj);
            newOtc.setRsvpStatus(rsvpMdl.getRsvpStatus());
            newOtc.setKidCount(rsvpMdl.getKidCount());
            newOtc.setAdultCount(rsvpMdl.getAdultCount());
            newOtc.setComment(rsvpMdl.getComment());

            // (3) create the record
            rsvpSrv.create(newOtc);

            return "redirect:/playdate/" + playdateId ;
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

        RsvpMdl rsvpObj = rsvpSrv.findById(rsvpId); // get the object that is the primary object displayed on this page
        model.addAttribute("rsvp", rsvpObj);
        PlaydateMdl playdateObj = rsvpObj.getPlaydateMdl(); // get the object that is the parent to the primary object, for all downstream functions

        // get/deliver from playdate db record, Part 1 (so they can be addAtts that are independent from the playdate obj)
        Date playdateCreatedAt = playdateObj.getCreatedAt();
        Long playdateCreatedById = playdateObj.getUserMdl().getId();
        String playdateCreatedByUserName = playdateObj.getUserMdl().getUserName();

        // begin: calculate various RSVP-related stats.  NOTE: this all could be done with native queries as well, but this is good function/loop practice.
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

        // get/deliver list of unioned rsvp records
        List<PlaydateUserUnionRsvpUser> playdateRsvpList = rsvpSrv.playdateRsvpList(playdateId);
        model.addAttribute("playdateRsvpList", playdateRsvpList);

        // get/deliver from playdate db record, Part 2 (so they can be addAtts that are independent from the playdate obj)
        model.addAttribute("playdateCreatedAt", playdateCreatedAt);
        model.addAttribute("playdateCreatedById", playdateCreatedById);
        model.addAttribute("playdateCreatedByUserName", playdateCreatedByUserName);

        return "rsvp/edit.jsp";
    }

    @PostMapping("/rsvp/edit")
    public String processRsvpEdit(
            @Valid @ModelAttribute("rsvp") RsvpMdl rsvpMdl
            , BindingResult result
            , Model model
            , Principal principal
            , RedirectAttributes redirectAttributes
    ) {

        // authentication boilerplate for all mthd
        UserMdl authUserObj = userSrv.findByEmail(principal.getName()); model.addAttribute("authUser", authUserObj); model.addAttribute("authUserName", authUserObj.getUserName());

        RsvpMdl rsvpObj = rsvpSrv.findById(rsvpMdl.getId()); // this is so circular, refactor please
        PlaydateMdl playdateObj = rsvpObj.getPlaydateMdl();

        // get/deliver from playdate db record, Part 1 (so they can be addAtts that are independent from the playdate obj)
        Date playdateCreatedAt = playdateObj.getCreatedAt();
        Long playdateCreatedById = playdateObj.getUserMdl().getId();
        String playdateCreatedByUserName = playdateObj.getUserMdl().getUserName();
        Long playdateId = playdateObj.getId();

        UserMdl rsvpCreatorUserMdl = rsvpObj.getUserMdl();   // gets the userMdl obj saved to the existing playdateObj

        if(!authUserObj.equals(rsvpCreatorUserMdl)) {
            redirectAttributes.addFlashAttribute("permissionErrorMsg", "That RSVP can only be edited by its creator.  Any edits just attempted have been discarded.");
            return "redirect:/playdate/" + playdateObj.getId();
        }

        if(result.hasErrors()) { // examples of errors: submit with null kidsCount, etc.
            // begin: calculate various RSVP-related stats.  NOTE: this all could be done with native queries as well, but this is good function/loop practice.
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

            // get/deliver list of unioned rsvp records
            List<PlaydateUserUnionRsvpUser> playdateRsvpList = rsvpSrv.playdateRsvpList(playdateId);
            model.addAttribute("playdateRsvpList", playdateRsvpList);

            // get/deliver from playdate db record, Part 2 (so they can be addAtts that are independent from the playdate obj)
            model.addAttribute("playdateCreatedAt", playdateCreatedAt);
            model.addAttribute("playdateCreatedById", playdateCreatedById);
            model.addAttribute("playdateCreatedByUserName", playdateCreatedByUserName);
            return "rsvp/edit.jsp"; // not sure if/how it is advisable to have this return/redirect/whatev to the url that contains the playdate_id and rsvp_id

        } else {
            UserMdl origCreatorUserMdl = rsvpObj.getUserMdl();
            rsvpMdl.setUserMdl( origCreatorUserMdl);
            rsvpMdl.setPlaydateMdl(playdateObj);
            rsvpSrv.update(rsvpMdl);

            return "redirect:/playdate/" + playdateId;
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
        // no model attributes here b/c no resulting page we are rending

        RsvpMdl rsvpObj = rsvpSrv.findById(rsvpId);
        PlaydateMdl playdateObj = rsvpObj.getPlaydateMdl();
        Long playdateID = playdateObj.getId();

        UserMdl rsvpCreatorUserMdl = rsvpObj.getUserMdl();

        if(!authUserObj.equals(rsvpCreatorUserMdl)) {
            redirectAttributes.addFlashAttribute("permissionErrorMsg", "That RSVP can only be deleted by its creator.  RSVP not deleted.");
            return "redirect:/playdate/" + playdateObj.getId();
        }

        rsvpSrv.delete(rsvpObj);
        return "redirect:/playdate/" + playdateID;
    }

// end of methods
}