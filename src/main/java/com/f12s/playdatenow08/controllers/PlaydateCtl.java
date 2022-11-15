package com.f12s.playdatenow08.controllers;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import javax.validation.Valid;

import com.f12s.playdatenow08.models.*;
import com.f12s.playdatenow08.pojos.PlaydateUserUnionRsvpUser;
import com.f12s.playdatenow08.services.*;
import com.f12s.playdatenow08.validator.PlaydateValidator;
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

    @Autowired
    private CodeSrv codeSrv;

    // adding below to mimic the userValidator program
    @Autowired
    private PlaydateValidator playdateValidator;


    @GetMapping("/playdate")
    public String displayPlaydateAll(
            Principal principal
            , Model model
    ) {

        // authentication boilerplate for all mthd
        UserMdl authUserObj = userSrv.findByEmail(principal.getName()); model.addAttribute("authUser", authUserObj); model.addAttribute("authUserName", authUserObj.getUserName());

        // (1) deliver list of records for table
        List<PlaydateMdl> playdateList = playdateSrv.returnAll();
        model.addAttribute("playdateList", playdateList);

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

        // (1) deliver lists for drop-down fields
        String[] startTimeList = { "8:00am",	"8:30am",	"9:00am",	"9:30am",	"10:00am",	"10:30am",	"11:00am",	"11:30am",	"12:00pm",	"12:30pm",	"1:00pm",	"1:30pm",	"2:00pm",	"2:30pm",	"3:00pm",	"3:30pm",	"4:00pm",	"4:30pm",	"5:00pm",	"5:30pm",	"6:00pm",	"6:30pm",	"7:00pm",	"7:30pm",	"8:00pm",	"8:30pm"};
        model.addAttribute("startTimeList", startTimeList );

        Long codeCategoryIdForPlaydateLocationTypeCodes = Long.valueOf(1);
        List<CodeMdl> locationTypeList = codeSrv.targetedCodeList(codeCategoryIdForPlaydateLocationTypeCodes);
        model.addAttribute("locationTypeList", locationTypeList);

        Long codeCategoryIdForPlaydateStatusCodes = Long.valueOf(2);
        List<CodeMdl> playdateStatusList = codeSrv.targetedCodeList(codeCategoryIdForPlaydateStatusCodes);
        model.addAttribute("playdateStatusList", playdateStatusList);

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

        // adding below to mimic the userValidator program; i think it's Obj, b/c that's what's in @ModelAttribute
        playdateValidator.validate(playdateObj, result);

        if(!result.hasErrors()) {

            playdateObj.setUserMdl( authUserObj);
            playdateSrv.create(playdateObj);
            Long newlyCreatedPlaydateID = playdateObj.getId();
            redirectAttributes.addFlashAttribute("successMsg", "This playdate is gonna be awesome!  Make sure you invite some friends to RSVP.");
            return "redirect:/playdate/" + newlyCreatedPlaydateID;

        } else {

            // (1) deliver lists for drop-down fields
            String[] startTimeList = { "8:00am",	"8:30am",	"9:00am",	"9:30am",	"10:00am",	"10:30am",	"11:00am",	"11:30am",	"12:00pm",	"12:30pm",	"1:00pm",	"1:30pm",	"2:00pm",	"2:30pm",	"3:00pm",	"3:30pm",	"4:00pm",	"4:30pm",	"5:00pm",	"5:30pm",	"6:00pm",	"6:30pm",	"7:00pm",	"7:30pm",	"8:00pm",	"8:30pm"};
            model.addAttribute("startTimeList", startTimeList );

            Long codeCategoryIdForPlaydateLocationTypeCodes = Long.valueOf(1);
            List<CodeMdl> locationTypeList = codeSrv.targetedCodeList(codeCategoryIdForPlaydateLocationTypeCodes);
            model.addAttribute("locationTypeList", locationTypeList);

            Long codeCategoryIdForPlaydateStatusCodes = Long.valueOf(2);
            List<CodeMdl> playdateStatusList = codeSrv.targetedCodeList(codeCategoryIdForPlaydateStatusCodes);
            model.addAttribute("playdateStatusList", playdateStatusList);

            // (2) deliver error message
            model.addAttribute("validationErrorMsg", "Uh-oh! Please fix the errors noted below and submit again.  (Or cancel.)");

//            String[] startTimeList = { "8:00am",	"8:30am",	"9:00am",	"9:30am",	"10:00am",	"10:30am",	"11:00am",	"11:30am",	"12:00pm",	"12:30pm",	"1:00pm",	"1:30pm",	"2:00pm",	"2:30pm",	"3:00pm",	"3:30pm",	"4:00pm",	"4:30pm",	"5:00pm",	"5:30pm",	"6:00pm",	"6:30pm",	"7:00pm",	"7:30pm",	"8:00pm",	"8:30pm"};
//            model.addAttribute("startTimeList", startTimeList );
//
//            // create+send the list of all to the page, for drop-down
//            List<CodeMdl> codeList = codeSrv.returnAll();
//            model.addAttribute("codeList", codeList);
//
//            // create+send the list of locationTypes to the page, for drop-down
//            List<CodeMdl> locationTypeList = codeSrv.returnAll();
//            model.addAttribute("locationTypeList", locationTypeList);
//
//            // create+send the list of locationTypes to the page, for drop-down
//            List<CodeMdl> playdateStatusList = codeSrv.returnAll();
//            model.addAttribute("playdateStatusList", playdateStatusList);

            return "playdate/create.jsp";
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

        // (1) deliver the primary object to be displayed on page
        PlaydateMdl playdateObj = playdateSrv.findById(playdateId);
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

        model.addAttribute("rsvpCount", rsvpCount);
        model.addAttribute("aggKidsCount", aggKidsCount);
        model.addAttribute("rsvpExistsCreatedByAuthUser", rsvpExistsCreatedByAuthUser);
        model.addAttribute("rsvpList", rsvpList);
        model.addAttribute("aggAdultsCount", aggAdultsCount);
        model.addAttribute("openKidsSpots", openKidsSpots);
        // end: calculate various RSVP-related stats.

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

        // (1) get as-is object from db, then send to form for editing
        PlaydateMdl playdateObj = playdateSrv.findById(playdateId);
        model.addAttribute("playdate", playdateObj); // note: this addAttVar is not kosher, should be 'playdateObj' or better yet: asisPlaydateObj; will need to refactor this later.  'playdateObj' is a var/term currently used different way on postMapping

//        // (2) deliver lists for drop-down fields
//        String[] startTimeList = { "8:00am",	"8:30am",	"9:00am",	"9:30am",	"10:00am",	"10:30am",	"11:00am",	"11:30am",	"12:00pm",	"12:30pm",	"1:00pm",	"1:30pm",	"2:00pm",	"2:30pm",	"3:00pm",	"3:30pm",	"4:00pm",	"4:30pm",	"5:00pm",	"5:30pm",	"6:00pm",	"6:30pm",	"7:00pm",	"7:30pm",	"8:00pm",	"8:30pm"};
//        model.addAttribute("startTimeList", startTimeList );
//
//        List<CodeMdl> codeList = codeSrv.returnAll();
//        model.addAttribute("codeList", codeList);

        // (2) deliver lists for drop-down fields
        String[] startTimeList = { "8:00am",	"8:30am",	"9:00am",	"9:30am",	"10:00am",	"10:30am",	"11:00am",	"11:30am",	"12:00pm",	"12:30pm",	"1:00pm",	"1:30pm",	"2:00pm",	"2:30pm",	"3:00pm",	"3:30pm",	"4:00pm",	"4:30pm",	"5:00pm",	"5:30pm",	"6:00pm",	"6:30pm",	"7:00pm",	"7:30pm",	"8:00pm",	"8:30pm"};
        model.addAttribute("startTimeList", startTimeList );

        Long codeCategoryIdForPlaydateLocationTypeCodes = Long.valueOf(1);
        List<CodeMdl> locationTypeList = codeSrv.targetedCodeList(codeCategoryIdForPlaydateLocationTypeCodes);
        model.addAttribute("locationTypeList", locationTypeList);

        Long codeCategoryIdForPlaydateStatusCodes = Long.valueOf(2);
        List<CodeMdl> playdateStatusList = codeSrv.targetedCodeList(codeCategoryIdForPlaydateStatusCodes);
        model.addAttribute("playdateStatusList", playdateStatusList);

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
        // NOTE: this all could be done with native queries as well, but this is good function/loop practice.
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

        Boolean hasOneOrMoreRsvp = false; // at present, this is being used to show the delete button or not.
        if ( rsvpList.size() > 0 ) {
            hasOneOrMoreRsvp = true;
        }
        model.addAttribute("hasOneOrMoreRsvp", hasOneOrMoreRsvp);
        // END: calculate various RSVP-related stats and controls.

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

        // (1) get as-is object from db.  several implications downstream herein.
        PlaydateMdl playdateObj = playdateSrv.findById(playdateMdl.getId());

        // (2) validate if authenticated user has security to edit this profile record; if not, redirect user to record screen with flash msg.
        UserMdl recordCreatorUserMdl = playdateObj.getUserMdl();
        if(!authUserObj.equals(recordCreatorUserMdl)) {
            System.out.println("recordCreatorUserMdl != currentUserMdl, so redirected to record");
            redirectAttributes.addFlashAttribute("permissionErrorMsg", "This record can only be edited by its creator.  Any edits just attempted were discarded.");
            return "redirect:/playdate/" + playdateObj.getId();
        }

        // (3) if not errors detected, proceed with update
        if (!result.hasErrors()) {
            // (a) update the incoming playdate object by re-attaching any attribute of the object/record not managed by the form.
            playdateMdl.setUserMdl(playdateObj.getUserMdl());
            // (b) run the update service
            playdateSrv.update(playdateMdl);
            // (c) redirect to the record
            return "redirect:/playdate/" + playdateObj.getId();

        // (4) if errors detected, basically reconstitute the entire edit screen and return it to the user.
        } else {

            // (0) super duper important: do not re-deliver playdateObj to the page, because doing so will overrride all the values the user submitted / system rejected

            // (1) playdateObj already constituted in steps above, so not repeated here.

//            // (2) deliver lists for drop-down fields
//            String[] startTimeList = { "8:00am",	"8:30am",	"9:00am",	"9:30am",	"10:00am",	"10:30am",	"11:00am",	"11:30am",	"12:00pm",	"12:30pm",	"1:00pm",	"1:30pm",	"2:00pm",	"2:30pm",	"3:00pm",	"3:30pm",	"4:00pm",	"4:30pm",	"5:00pm",	"5:30pm",	"6:00pm",	"6:30pm",	"7:00pm",	"7:30pm",	"8:00pm",	"8:30pm"};
//            model.addAttribute("startTimeList", startTimeList );
//
//            List<CodeMdl> codeList = codeSrv.returnAll();
//            model.addAttribute("codeList", codeList);

            // (2) deliver lists for drop-down fields
            String[] startTimeList = { "8:00am",	"8:30am",	"9:00am",	"9:30am",	"10:00am",	"10:30am",	"11:00am",	"11:30am",	"12:00pm",	"12:30pm",	"1:00pm",	"1:30pm",	"2:00pm",	"2:30pm",	"3:00pm",	"3:30pm",	"4:00pm",	"4:30pm",	"5:00pm",	"5:30pm",	"6:00pm",	"6:30pm",	"7:00pm",	"7:30pm",	"8:00pm",	"8:30pm"};
            model.addAttribute("startTimeList", startTimeList );

            Long codeCategoryIdForPlaydateLocationTypeCodes = Long.valueOf(1);
            List<CodeMdl> locationTypeList = codeSrv.targetedCodeList(codeCategoryIdForPlaydateLocationTypeCodes);
            model.addAttribute("locationTypeList", locationTypeList);

            Long codeCategoryIdForPlaydateStatusCodes = Long.valueOf(2);
            List<CodeMdl> playdateStatusList = codeSrv.targetedCodeList(codeCategoryIdForPlaydateStatusCodes);
            model.addAttribute("playdateStatusList", playdateStatusList);

            // (3) deliver list of unioned rsvp records for child record table
            Long playdateId = playdateMdl.getId(); // this is needed here, because the id is not being supplied by path variable
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
            // NOTE: this all could be done with native queries as well, but this is good function/loop practice.
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
            Boolean hasOneOrMoreRsvp = false; // at present, this is being used to show the delete button or not.
            if ( rsvpList.size() > 0 ) {
                hasOneOrMoreRsvp = true;
            }
            model.addAttribute("hasOneOrMoreRsvp", hasOneOrMoreRsvp);
            // end: calculate various RSVP-related stats.

            // (6) deliver error msg to page
            model.addAttribute("validationErrorMsg", "Uh-oh! Please fix the errors noted below and submit again.  (Or cancel.)");

            return "playdate/edit.jsp";
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
