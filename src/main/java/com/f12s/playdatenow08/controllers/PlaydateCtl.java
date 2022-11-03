package com.f12s.playdatenow08.controllers;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import javax.validation.Valid;

import com.f12s.playdatenow08.models.PlaydateMdl;
import com.f12s.playdatenow08.models.RsvpMdl;
import com.f12s.playdatenow08.models.StateterritoryMdl;
import com.f12s.playdatenow08.models.UserMdl;
import com.f12s.playdatenow08.pojos.PlaydateUserUnionRsvpUser;
import com.f12s.playdatenow08.services.PlaydateSrv;
import com.f12s.playdatenow08.services.RsvpSrv;
import com.f12s.playdatenow08.services.StateterritorySrv;
import com.f12s.playdatenow08.services.UserSrv;
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

@Controller
public class PlaydateCtl {

    @Autowired
    private PlaydateSrv playdateSrv;

    @Autowired
    private UserSrv userSrv;

    @Autowired
    private RsvpSrv rsvpSrv;

    @Autowired
    private StateterritorySrv stateterritorySrv;

    @GetMapping("/playdate")
    public String displayPlaydateAll(
            Principal principal
            , Model model
    ) {

        // authentication boilerplate for all mthd
        UserMdl authUserObj = userSrv.findByEmail(principal.getName()); model.addAttribute("authUser", authUserObj); model.addAttribute("authUserName", authUserObj.getUserName());

        List<PlaydateMdl> playdateList = playdateSrv.returnAll();
        model.addAttribute("playdateList", playdateList);

        List<StateterritoryMdl> stateterritoryList = stateterritorySrv.returnAll();
        model.addAttribute("stateterritoryList", stateterritoryList);

        return "playdate/list.jsp";
    }

    @GetMapping("/playdate/new")
    public String displayPlaydateNew(
            @ModelAttribute("playdate") PlaydateMdl playdateObj
            , Model model
            , Principal principal
    ) {

        // authentication boilerplate for all mthd
        UserMdl authUserObj = userSrv.findByEmail(principal.getName()); model.addAttribute("authUser", authUserObj); model.addAttribute("authUserName", authUserObj.getUserName());

        String[] startTimeList = { "8:00am",	"8:30am",	"9:00am",	"9:30am",	"10:00am",	"10:30am",	"11:00am",	"11:30am",	"12:00pm",	"12:30pm",	"1:00pm",	"1:30pm",	"2:00pm",	"2:30pm",	"3:00pm",	"3:30pm",	"4:00pm",	"4:30pm",	"5:00pm",	"5:30pm",	"6:00pm",	"6:30pm",	"7:00pm",	"7:30pm",	"8:00pm",	"8:30pm"};
        model.addAttribute("startTimeList", startTimeList );
        return "playdate/create.jsp";
    }

    @PostMapping("/playdate/new")
    public String processPlaydateNew(
            @Valid @ModelAttribute("playdate") PlaydateMdl playdateObj
            , BindingResult result
            , Model model
            , Principal principal
            , RedirectAttributes redirectAttributes
    ) {

        // authentication boilerplate for all mthd
        UserMdl authUserObj = userSrv.findByEmail(principal.getName()); model.addAttribute("authUser", authUserObj); model.addAttribute("authUserName", authUserObj.getUserName());

        if(result.hasErrors()) {
            model.addAttribute("validationErrorMsg", "Uh-oh! Please fix the errors noted below and submit again.  (Or cancel.)");
            String[] startTimeList = { "8:00am",	"8:30am",	"9:00am",	"9:30am",	"10:00am",	"10:30am",	"11:00am",	"11:30am",	"12:00pm",	"12:30pm",	"1:00pm",	"1:30pm",	"2:00pm",	"2:30pm",	"3:00pm",	"3:30pm",	"4:00pm",	"4:30pm",	"5:00pm",	"5:30pm",	"6:00pm",	"6:30pm",	"7:00pm",	"7:30pm",	"8:00pm",	"8:30pm"};
            model.addAttribute("startTimeList", startTimeList );
            return "playdate/create.jsp";

        } else {

            playdateObj.setUserMdl( authUserObj);
            playdateSrv.create(playdateObj);
            Long newlyCreatedPlaydateID = playdateObj.getId();
            redirectAttributes.addFlashAttribute("successMsg", "This playdate is gonna be awesome!  Make sure you invite some friends to RSVP.");
            return "redirect:/playdate/" + newlyCreatedPlaydateID;
        }
    }

    @GetMapping("/playdate/{id}")
    public String displayPlaydate(
            @PathVariable("id") Long playdateId
            , @ModelAttribute("rsvp") RsvpMdl rsvpObj // enables delivery of RSVP record on the page
            , Model model
            , Principal principal
    ) {

        // authentication boilerplate for all mthd
        UserMdl authUserObj = userSrv.findByEmail(principal.getName()); model.addAttribute("authUser", authUserObj); model.addAttribute("authUserName", authUserObj.getUserName());

        PlaydateMdl playdateObj = playdateSrv.findById(playdateId); // display native playdate fields, etc.

        // begin: calculate various RSVP-related stats.  NOTE: this all could be done with native queries as well, but this is good function/loop practice.
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

        // begin: get/deliver list of unioned rsvp records
        List<PlaydateUserUnionRsvpUser> playdateRsvpList = rsvpSrv.playdateRsvpList(playdateId);
        model.addAttribute("playdateRsvpList", playdateRsvpList);

        return "playdate/record.jsp";
    }

    @GetMapping("/playdate/{id}/edit")
    public String editPlaydate(
            @PathVariable("id") Long playdateId
            , Model model
            , Principal principal
    ) {

        // authentication boilerplate for all mthd
        UserMdl authUserObj = userSrv.findByEmail(principal.getName()); model.addAttribute("authUser", authUserObj); model.addAttribute("authUserName", authUserObj.getUserName());

        PlaydateMdl playdateObj = playdateSrv.findById(playdateId);
        model.addAttribute("playdate", playdateObj);

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

        model.addAttribute("rsvpCount", rsvpCount);
        model.addAttribute("aggKidsCount", aggKidsCount);
        model.addAttribute("rsvpList", rsvpList);
        model.addAttribute("aggAdultsCount", aggAdultsCount);
        model.addAttribute("openKidsSpots", openKidsSpots);
        // end: calculate various RSVP-related stats.

        // get/deliver list of unioned rsvp records
        List<PlaydateUserUnionRsvpUser> playdateRsvpList = rsvpSrv.playdateRsvpList(playdateId);
        model.addAttribute("playdateRsvpList", playdateRsvpList);

        Boolean hasOneOrMoreRsvp = false; // at present, this is being used to show the delete button or not.
        if ( rsvpList.size() > 0 ) {
            hasOneOrMoreRsvp = true;
        }
        model.addAttribute("hasOneOrMoreRsvp", hasOneOrMoreRsvp);

        String[] startTimeList = { "8:00am",	"8:30am",	"9:00am",	"9:30am",	"10:00am",	"10:30am",	"11:00am",	"11:30am",	"12:00pm",	"12:30pm",	"1:00pm",	"1:30pm",	"2:00pm",	"2:30pm",	"3:00pm",	"3:30pm",	"4:00pm",	"4:30pm",	"5:00pm",	"5:30pm",	"6:00pm",	"6:30pm",	"7:00pm",	"7:30pm",	"8:00pm",	"8:30pm"};
        model.addAttribute("startTimeList", startTimeList );

        // get/deliver from playdate db record, Part 2 (so they can be addAtts that are independent from the playdate obj)
        model.addAttribute("playdateCreatedAt", playdateCreatedAt);
        model.addAttribute("playdateCreatedById", playdateCreatedById);
        model.addAttribute("playdateCreatedByUserName", playdateCreatedByUserName);

        return "playdate/edit.jsp";
    }

    @PostMapping("/playdate/edit")
    public String PostTheEditPlaydate(
            @Valid
            @ModelAttribute("playdate") PlaydateMdl playdateMdl
            , BindingResult result
            , Model model
            , Principal principal
            , RedirectAttributes redirectAttributes
    ) {

        // authentication boilerplate for all mthd
        UserMdl authUserObj = userSrv.findByEmail(principal.getName()); model.addAttribute("authUser", authUserObj); model.addAttribute("authUserName", authUserObj.getUserName());

        PlaydateMdl playdateObj = playdateSrv.findById(playdateMdl.getId());

        // get/deliver from playdate db record, Part 1 (so they can be addAtts that are independent from the playdate obj)
        Date playdateCreatedAt = playdateObj.getCreatedAt();
        Long playdateCreatedById = playdateObj.getUserMdl().getId();
        String playdateCreatedByUserName = playdateObj.getUserMdl().getUserName();

        UserMdl recordCreatorUserMdl = playdateObj.getUserMdl();

        if(!authUserObj.equals(recordCreatorUserMdl)) {
            System.out.println("recordCreatorUserMdl != currentUserMdl, so redirected to record");
            redirectAttributes.addFlashAttribute("permissionErrorMsg", "This record can only be edited by its creator.  Any edits just attempted were discarded.");
            return "redirect:/playdate/" + playdateObj.getId();
        }

        if (result.hasErrors()) {
            // note: do not re-deliver playdateObj: doing so will overrride all the values the users submitted / system rejected
            model.addAttribute("validationErrorMsg", "Uh-oh! Please fix the errors noted below and submit again.  (Or cancel.)");
            Long playdateId = playdateMdl.getId();

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

            model.addAttribute("rsvpCount", rsvpCount);
            model.addAttribute("aggKidsCount", aggKidsCount);
            model.addAttribute("rsvpList", rsvpList);
            model.addAttribute("aggAdultsCount", aggAdultsCount);
            model.addAttribute("openKidsSpots", openKidsSpots);
            // end: calculate various RSVP-related stats.

            // get/deliver list of unioned rsvp records
            List<PlaydateUserUnionRsvpUser> playdateRsvpList = rsvpSrv.playdateRsvpList(playdateId);
            model.addAttribute("playdateRsvpList", playdateRsvpList);

            Boolean hasOneOrMoreRsvp = false; // at present, this is being used to show the delete button or not.
            if ( rsvpList.size() > 0 ) {
                hasOneOrMoreRsvp = true;
            }
            model.addAttribute("hasOneOrMoreRsvp", hasOneOrMoreRsvp);

            String[] startTimeList = { "8:00am",	"8:30am",	"9:00am",	"9:30am",	"10:00am",	"10:30am",	"11:00am",	"11:30am",	"12:00pm",	"12:30pm",	"1:00pm",	"1:30pm",	"2:00pm",	"2:30pm",	"3:00pm",	"3:30pm",	"4:00pm",	"4:30pm",	"5:00pm",	"5:30pm",	"6:00pm",	"6:30pm",	"7:00pm",	"7:30pm",	"8:00pm",	"8:30pm"};
            model.addAttribute("startTimeList", startTimeList );

            // get/deliver from playdate db record, Part 2 (so they can be addAtts that are independent from the playdate obj)
            model.addAttribute("playdateCreatedAt", playdateCreatedAt);
            model.addAttribute("playdateCreatedById", playdateCreatedById);
            model.addAttribute("playdateCreatedByUserName", playdateCreatedByUserName);

            return "playdate/edit.jsp";

        } else {
            playdateMdl.setUserMdl(playdateObj.getUserMdl()); // deliver any attribute of the object/record not managed by the form
            playdateSrv.update(playdateMdl);
            return "redirect:/playdate/" + playdateObj.getId();
        }
    }

    @RequestMapping("/playdate/delete/{id}")
    public String deletePlaydate(
            @PathVariable("id") Long playdateId
            , Principal principal
            , RedirectAttributes redirectAttributes
    ) {

        // authentication boilerplate for all mthd
        UserMdl authUserObj = userSrv.findByEmail(principal.getName());
        // no model attributes here b/c no resulting page we are rending

        PlaydateMdl playdateObj = playdateSrv.findById(playdateId);
        UserMdl recordCreatorUserMdl = playdateObj.getUserMdl();   // gets the userMdl obj saved to the existing playdateObj

        if(!authUserObj.equals(recordCreatorUserMdl)) {
            System.out.println("recordCreatorUserMdl != currentUserMdl, so redirected to record");
            redirectAttributes.addFlashAttribute("permissionErrorMsg", "This record can only be deleted by its creator.");
            return "redirect:/playdate/" + playdateObj.getId();
        }

        List<RsvpMdl> rsvpList = playdateObj.getRsvpList(); // instantiate the java list

        if ( rsvpList.size() > 0 ) {
            redirectAttributes.addFlashAttribute("permissionErrorMsg", "This event has rsvp records, so it cannot be deleted.  If all user RSVPs get deleted, you can then delete this event.  Event no longer happening?  Then update the playdateStatus to be Cancelled.");
            return "redirect:/playdate/" + playdateObj.getId();
        }

        playdateSrv.delete(playdateObj);

        return "redirect:/playdate";
    }

// end of methods
}
