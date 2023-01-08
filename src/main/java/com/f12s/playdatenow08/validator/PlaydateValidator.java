package com.f12s.playdatenow08.validator;

import com.f12s.playdatenow08.models.CodeMdl;
import com.f12s.playdatenow08.models.CodecategoryMdl;
import com.f12s.playdatenow08.models.PlaydateMdl;
import com.f12s.playdatenow08.models.StateterritoryMdl;
import com.f12s.playdatenow08.repositories.CodecategoryRpo;
import com.f12s.playdatenow08.repositories.PlaydateRpo;
import com.f12s.playdatenow08.services.CodeSrv;
import com.f12s.playdatenow08.services.StateterritorySrv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;
import java.util.Optional;

@Component
public class PlaydateValidator implements Validator {

    @Autowired
    private PlaydateRpo playdateRpo;

    @Autowired
    private CodeSrv codeSrv;

    @Autowired
    private StateterritorySrv stateterritorySrv;

    @Autowired
    private CodecategoryRpo codecategoryRpo;

    @Override
    public boolean supports(Class<?> clazz) {
        return PlaydateMdl.class.equals(clazz);
    }

    @Override
    public void validate(
            Object object
            , Errors errors
    ) {

        // localize the object we're talking about
        PlaydateMdl playdateMdl = (PlaydateMdl) object;

        // begin validations

//        // playdateStatus
//        System.out.println("begin validation: playdateStatus");
//        // (1) get list of valid objects from code table
//        Long codeCategoryIdForPlaydateStatusCodes = Long.valueOf(2);
//        List<CodeMdl> playdateStatusList = codeSrv.targetedCodeList(codeCategoryIdForPlaydateStatusCodes);
//
//        // (2) iterate through the list, looking for objects that match user entry
//        int validPlaydateStatus = 0;
//        int a = 0;
//
//        for (a = 0; a < playdateStatusList.size(); a++) {
//            // all printStatements herein for initial testing purposes only
////            System.out.print("item: " + a + "\n");
////            System.out.print("playdateMdl.getPlaydateStatus().getCode(): " + playdateMdl.getPlaydateStatus().getCode() + "\n");
////            System.out.print("playdateStatusList.get(a).getCode(): " + playdateStatusList.get(a).getCode() + "\n");
//            if (
//                    !playdateMdl.getPlaydateStatus().getCode().equals( playdateStatusList.get(a).getCode() )
//            ) {
////                System.out.println("entry does not match this list item");
////                System.out.println("validPlaydateStatus: " + validPlaydateStatus);
//            } else {
//                validPlaydateStatus = 1;
////                System.out.println("validPlaydateStatus achieved!");
////                System.out.println("validPlaydateStatus: " + validPlaydateStatus);
////                System.out.println("we're done!");
//                break;
//            }
//        }
//

        // begin: playdateStatus
        System.out.println("begin validation: playdateStatus");
        // (1) instantiate essential variable, start as positive and only set to negative if a unacceptable condition met
        boolean validPlaydateStatus = true;

        // (2a) check: does incoming variable exist?
        if (playdateMdl.getPlaydateStatus() == null) {
            System.out.println("playdateStatus is null");
            validPlaydateStatus = false;
        } else {

            CodecategoryMdl targetedCodeCategory = codecategoryRpo.findCodecategoryMdlByCodeType("eventStatusType");
            List<CodeMdl> playdateStatusListTwo = codeSrv.targetedCodeListTwo(targetedCodeCategory);

            int z = 0;
            for (z = 0; z < playdateStatusListTwo.size(); z++) {
                System.out.print("new list, baby" + "\n");
                System.out.print("item: " + z + "\n");
                System.out.print("playdateStatusList.get(a).getCode(): " + playdateStatusListTwo.get(z).getCode() + "\n");

            }
            
            // (2b) since exists, now get list of valid objects from code table, then iterate through the list, looking for objects that match user entry
            Long codeCategoryIdForPlaydateStatusCodes = Long.valueOf(2);
            List<CodeMdl> playdateStatusList = codeSrv.targetedCodeList(codeCategoryIdForPlaydateStatusCodes);
            int a = 0;
            for (a = 0; a < playdateStatusList.size(); a++) {
                // all printStatements herein for initial testing purposes only
                System.out.print("item: " + a + "\n");
                System.out.print("playdateMdl.getPlaydateStatus().getCode(): " + playdateMdl.getPlaydateStatus().getCode() + "\n");
                System.out.print("playdateStatusList.get(a).getCode(): " + playdateStatusList.get(a).getCode() + "\n");
                if (
                        playdateMdl.getPlaydateStatus().getCode().equals( playdateStatusList.get(a).getCode() )
                ) {
                    validPlaydateStatus = true;
                    System.out.println("validPlaydateStatus: " + validPlaydateStatus);
                    System.out.println("validPlaydateStatus achieved!");
                    System.out.println("we're done!");
                    break;
                } else {
                    System.out.println("entry does not match this list item");
                    validPlaydateStatus = false;
                    System.out.println("validPlaydateStatus: " + validPlaydateStatus);
                }
            }
        }
        // (3) deliver error msgs if variable in negative/false status
        if (
                validPlaydateStatus == false
        ) {
            errors.rejectValue("playdateStatus", "Value");
        }
        // end: playdateStatus




        // playdateOrganizerRsvpStatus
        // (1) get list of valid objects
        Long codeCategoryIdForPlaydateRsvpStatusCodes = Long.valueOf(5);
        List<CodeMdl> playdateRsvpStatusList = codeSrv.targetedCodeList(codeCategoryIdForPlaydateRsvpStatusCodes);

        // (2) iterate through the list, looking for objects that match user entry
        int validPlaydateRsvpStatus = 0;
        int c = 0;

        for (c = 0; c < playdateRsvpStatusList.size(); c++) {
            // all printStatements herein for initial testing purposes only
//            System.out.print("playdateOrganizerRsvpStatus item: " + c + "\n");
//            System.out.print("playdateMdl.getPlaydateStatus().getCode(): " + playdateMdl.getPlaydateStatus().getCode() + "\n");
//            System.out.print("playdateStatusList.get(a).getCode(): " + playdateStatusList.get(a).getCode() + "\n");
            if (
                    !playdateMdl.getPlaydateOrganizerRsvpStatus().getCode().equals( playdateRsvpStatusList.get(c).getCode() )
            ) {
//                System.out.println("entry does not match this list item");
//                System.out.println("validPlaydateStatus: " + validPlaydateStatus);
            } else {
                validPlaydateRsvpStatus = 1;
//                System.out.println("validPlaydateRsvpStatus achieved!");
//                System.out.println("validPlaydateStatus: " + validPlaydateStatus);
//                System.out.println("we're done!");
                break;
            }
        }

        // (3) deliver any determination of error
        if (
                validPlaydateRsvpStatus == 0
        ) {
            errors.rejectValue("playdateOrganizerRsvpStatus", "Value");
        }

        // eventDate
        if (
                playdateMdl.getEventDate() == null
        ) {
            errors.rejectValue("eventDate", "Value");
        }

        // startTimeTxt
        // (1) get list of valid objects
        // note: this startTimeList is copy/pasted from the controller that delivers this list to the page in the first place
        String[] startTimeList = { "8:00am",	"8:30am",	"9:00am",	"9:30am",	"10:00am",	"10:30am",	"11:00am",	"11:30am",	"12:00pm",	"12:30pm",	"1:00pm",	"1:30pm",	"2:00pm",	"2:30pm",	"3:00pm",	"3:30pm",	"4:00pm",	"4:30pm",	"5:00pm",	"5:30pm",	"6:00pm",	"6:30pm",	"7:00pm",	"7:30pm",	"8:00pm",	"8:30pm"};

        // (2) iterate through the list, looking for objects that match user entry
        int validStartTime = 0;
        int i = 0;

        for (i = 0; i < startTimeList.length; i++) {
            // all printStatements herein for initial testing purposes only
//            System.out.print("i: " + i);
//            System.out.print("playdateMdl.getStartTimeTxt(): " + playdateMdl.getStartTimeTxt());
//            System.out.println("startTimeList[i]" + startTimeList[i]);
            if (
                    !playdateMdl.getStartTimeTxt().equals( startTimeList[i] )
            ) {
//                System.out.println("entry does not match this list item");
//                System.out.println("validStartTime: " + validStartTime);
            } else {
                validStartTime = 1;
//                System.out.println("validStartTime achieved!");
//                System.out.println("validStartTime: " + validStartTime);
//                System.out.println("we're done!");
                break;
            }
        }

        // (3) deliver any determination of error
        if (
                validStartTime == 0
        ) {
            errors.rejectValue("startTimeTxt", "Value");
        }

        // eventDescription
        if (
                playdateMdl.getEventDescription() == null ||
                        (playdateMdl.getEventDescription() != null && playdateMdl.getEventDescription().length() == 0 )

        ) {
            errors.rejectValue("eventDescription", "Size");
        }

        // maxCountKids
        if (
                playdateMdl.getMaxCountKids() == null ||
                        (playdateMdl.getMaxCountKids() != null && playdateMdl.getMaxCountKids() < 1)
        ) {
            errors.rejectValue("maxCountKids", "Size");
        }

//        // kidCount
//
//        String inRsvpStatus = "In";
//        if (
//                playdateMdl.getRsvpStatus() == inRsvpStatus &&
//                        (
//                            playdateMdl.getKidCount() == null
//                                    || (playdateMdl.getKidCount() != null && playdateMdl.getKidCount() < 1)
//                        )
//
//        ) {
//            errors.rejectValue("kidCount", "Size");
//        }

        // kidCount, take two
        if (
                playdateMdl.getPlaydateOrganizerRsvpStatus().getId() == 21 &&
                        (
                                playdateMdl.getKidCount() == null
                                        || (playdateMdl.getKidCount() != null && playdateMdl.getKidCount() < 1)
                        )

        ) {
            errors.rejectValue("kidCount", "rsvpInKidCountCombo");
        }

//        // adultCount
//        if (
//                playdateMdl.getAdultCount() == null ||
//                        (playdateMdl.getAdultCount() != null && playdateMdl.getAdultCount() < 1 )
//        ) {
//            errors.rejectValue("adultCount", "Size");
//        }

        // adultCount, take two
        if (
                playdateMdl.getPlaydateOrganizerRsvpStatus().getId() == 21 &&
                        (
                                playdateMdl.getAdultCount() == null
                                        || (playdateMdl.getAdultCount() != null && playdateMdl.getAdultCount() < 1)
                        )

        ) {
            errors.rejectValue("adultCount", "rsvpInAdultCountCombo");
        }

        // locationType
        // fyi, this ought to never be invoked as createNew has a default selection; this validation has been included just in case;
        if (
                playdateMdl.getLocationType() == null
        ) {
                errors.rejectValue("locationType", "Value");
        }

        // if locationType is somewhereElse... required related fields:

        // stateterritory
        System.out.println("begin state validation");
        // (1) determine if this validation applicable
        if (  // only proceed if locationType not null and other location
                playdateMdl.getLocationType() != null &&
                playdateMdl.getLocationType().equals(codeSrv.findCodeMdlByCode("otherLocation"))
        ) {
            System.out.println("locationType is otherLocation; begin stateTerr validation");
            // (1) instantiate essential variable, start as positive and only set to negative if a unacceptable condition met
            boolean stateterritoryValid = true;
            // (2) check: does incoming variable exist?
            if (playdateMdl.getStateterritoryMdl() == null) {
                System.out.println("stateTerr is null");
                stateterritoryValid = false;
            }
            // (3) deliver error msgs if variable in negative/false status
            if (
                    stateterritoryValid == false
            ) {
                errors.rejectValue("stateterritoryMdl", "stateterritoryMdlLocationTypeCombo");
            }

        } else {
            System.out.println("locationType is not otherLocation; skipped stateTerr validation");
        }

        // locationName
        if (
                playdateMdl.getLocationType() != null &&
                        playdateMdl.getLocationType().getId() == 2 &&
                        playdateMdl.getLocationName().length() == 0
        ) {
            errors.rejectValue("locationName", "locationNameLocationTypeCombo");
        }

        // below now a deprecated field
//        // locationAddy
//        if (
//                playdateMdl.getLocationType() != null &&
//                        playdateMdl.getLocationType().getId() == 2 &&
//                        playdateMdl.getLocationAddy().length() == 0
//        ) {
//            errors.rejectValue("locationAddy", "locationAddyLocationTypeCombo");
//        }

        // addressLine1
        if (
                playdateMdl.getLocationType() != null &&
                        playdateMdl.getLocationType().getId() == 2 &&
                        playdateMdl.getAddressLine1().length() == 0
        ) {
            errors.rejectValue("addressLine1", "addressLine1LocationTypeCombo");
        }

        // below field presently not in service for playdate model
//        // addressLine2
//        if (
//                playdateMdl.getLocationType() != null &&
//                        playdateMdl.getLocationType().getId() == 2 &&
//                        playdateMdl.getAddressLine2().length() == 0
//        ) {
//            errors.rejectValue("addressLine2", "addressLine2LocationTypeCombo");
//        }

        // city
        if (
                playdateMdl.getLocationType() != null &&
                        playdateMdl.getLocationType().getId() == 2 &&
                        playdateMdl.getCity().length() == 0
        ) {
            errors.rejectValue("city", "cityLocationTypeCombo");
        }



        // below field presently not in service for playdate model
//        // zipCode
//        if (
//                playdateMdl.getLocationType() != null &&
//                        playdateMdl.getLocationType().getId() == 2 &&
//                        playdateMdl.getZipCode().length() == 0
//        ) {
//            errors.rejectValue("zipCode", "zipCodeLocationTypeCombo");
//        }

    } // end validate function


} // end validator class

