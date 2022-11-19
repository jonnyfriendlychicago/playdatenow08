package com.f12s.playdatenow08.validator;

//import com.f12s.playdatenow08.models.UserMdl;
// above replaced by below
import com.f12s.playdatenow08.models.CodeMdl;
import com.f12s.playdatenow08.models.PlaydateMdl;
import com.f12s.playdatenow08.models.StateterritoryMdl;
import com.f12s.playdatenow08.repositories.PlaydateRpo;
import com.f12s.playdatenow08.repositories.UserRpo;
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

        // playdateStatus
        // (1) get list of valid objects from code table
        Long codeCategoryIdForPlaydateStatusCodes = Long.valueOf(2);
        List<CodeMdl> playdateStatusList = codeSrv.targetedCodeList(codeCategoryIdForPlaydateStatusCodes);

        // (2) iterate through the list, looking for objects that match user entry
        int validPlaydateStatus = 0;
        int a = 0;

        for (a = 0; a < playdateStatusList.size(); a++) {
            // all printStatements herein for initial testing purposes only
//            System.out.print("item: " + a + "\n");
//            System.out.print("playdateMdl.getPlaydateStatus().getCode(): " + playdateMdl.getPlaydateStatus().getCode() + "\n");
//            System.out.print("playdateStatusList.get(a).getCode(): " + playdateStatusList.get(a).getCode() + "\n");
            if (
                    !playdateMdl.getPlaydateStatus().getCode().equals( playdateStatusList.get(a).getCode() )
            ) {
//                System.out.println("entry does not match this list item");
//                System.out.println("validPlaydateStatus: " + validPlaydateStatus);
            } else {
                validPlaydateStatus = 1;
//                System.out.println("validPlaydateStatus achieved!");
//                System.out.println("validPlaydateStatus: " + validPlaydateStatus);
//                System.out.println("we're done!");
                break;
            }
        }

        if (
                validPlaydateStatus == 0
        ) {
            errors.rejectValue("playdateStatus", "Value");
        }

        // eventDate
        if (
                playdateMdl.getEventDate() == null
        ) {
            errors.rejectValue("eventDate", "Value");
        }

        // startTimeTxt
        // note: this startTimeList is copy/pasted from the controller that delivers this list to the page in the first place
        String[] startTimeList = { "8:00am",	"8:30am",	"9:00am",	"9:30am",	"10:00am",	"10:30am",	"11:00am",	"11:30am",	"12:00pm",	"12:30pm",	"1:00pm",	"1:30pm",	"2:00pm",	"2:30pm",	"3:00pm",	"3:30pm",	"4:00pm",	"4:30pm",	"5:00pm",	"5:30pm",	"6:00pm",	"6:30pm",	"7:00pm",	"7:30pm",	"8:00pm",	"8:30pm"};
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

        // kidCount
        if (
                playdateMdl.getKidCount() == null ||
                        (playdateMdl.getKidCount() != null && playdateMdl.getKidCount() < 1)
        ) {
            errors.rejectValue("kidCount", "Size");
        }

        // adultCount
        if (
                playdateMdl.getAdultCount() == null ||
                        (playdateMdl.getAdultCount() != null && playdateMdl.getAdultCount() < 1 )
        ) {
            errors.rejectValue("adultCount", "Size");
        }

        // locationType
        // fyi, this ought to never be invoked as createNew has a default selection
        if (
                playdateMdl.getLocationType() == null
        ) {
                errors.rejectValue("locationType", "Value");

        }

        // if locationType is somewhereElse... required related fields:

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

        // stateterritory
        // (1) instantiate essential variable
        int stateterritoryValid = 0;
        // (2) check to see if join at all
        if (
                playdateMdl.getStateterritoryMdl() == null
        ) {
            System.out.println("Invalid stateterritory selection; value does not join to lookup table.");
        } else {
            // (1) get list of valid objects
            List<StateterritoryMdl> stateterritoryList = stateterritorySrv.returnAll();
            // (2b) iterate through the list, looking for objects that match user entry
            int b = 0;

            for (b = 0; b < stateterritoryList.size(); b++) {
                // all printStatements herein for initial testing purposes only
                System.out.print("item: " + b + "\n");
                System.out.print("playdateMdl.getStateterritoryMdl().getAbbreviation(): " + playdateMdl.getStateterritoryMdl().getAbbreviation() + "\n");
                System.out.print("stateterritoryList.get(b).getAbbreviation(): " + stateterritoryList.get(b).getAbbreviation() + "\n");

                if (
                        !playdateMdl.getStateterritoryMdl().getAbbreviation().equals( stateterritoryList.get(b).getAbbreviation() )
                ) {
                    System.out.println("entry does not match this list item");
                    System.out.println("stateterritoryValid: " + stateterritoryValid);
                } else {
                    stateterritoryValid = 1;
                    System.out.println("stateterritoryValid achieved!");
                    System.out.println("stateterritoryValid: " + stateterritoryValid);
                    System.out.println("we're done!");
                    break;
                }
            } // end for-loop
        } // end else

        if (
                playdateMdl.getLocationType() != null &&
                        playdateMdl.getLocationType().getId() == 2 &&
                        stateterritoryValid == 0
        ) {
            errors.rejectValue("stateterritoryMdl", "stateterritoryMdlLocationTypeCombo");
//            System.out.println("we are ready to join and display an error msg...");
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

