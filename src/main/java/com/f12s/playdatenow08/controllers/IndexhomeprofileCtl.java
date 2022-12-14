package com.f12s.playdatenow08.controllers;

import com.f12s.playdatenow08.dataTransferObjects.UserUpdateDto;
import com.f12s.playdatenow08.models.*;
import com.f12s.playdatenow08.pojos.UserSocialConnectionPjo;
import com.f12s.playdatenow08.repositories.SocialconnectionRpo;
import com.f12s.playdatenow08.services.PlaydateSrv;
import com.f12s.playdatenow08.services.StateterritorySrv;
import com.f12s.playdatenow08.services.UserSrv;
import com.f12s.playdatenow08.validator.UserValidator;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class IndexhomeprofileCtl {

    @Autowired
    private UserSrv userSrv;

    @Autowired
    private UserValidator userValidator;

    @Autowired
    private StateterritorySrv stateterritorySrv;

    @Autowired
    private PlaydateSrv playdateSrv;

    @Autowired
    private SocialconnectionRpo socialconnectionRpo;

// ********************************************************************
// AUTHENTICATION METHODS
// ********************************************************************

    // boolean used by /login
    private boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || AnonymousAuthenticationToken.class.
                isAssignableFrom(authentication.getClass())) {
            return false;
        }
        return authentication.isAuthenticated();
    }

    @GetMapping("/login")
    public String login(
            @ModelAttribute("user") UserMdl userMdl
            , @RequestParam(value="error", required=false) String error
            , @RequestParam(value="logout", required=false) String logout
            , Model model
    ) {

        if (isAuthenticated()) {return "redirect:/home";} // if already authenticated, redirect away from /login path-page

        if(error != null) {
            model.addAttribute("errorMessage", "Invalid Credentials, Please try again.");
        }

        if(logout != null) {
            model.addAttribute("logoutMessage", "Logout Successful.");
        }

        return "login.jsp";
    }

    @GetMapping("/register")
    public String registerForm(
            @Valid @ModelAttribute("user") UserMdl userMdl
    ) {

        if (isAuthenticated()) {
            return "redirect:/home";
        }

        return "register.jsp";
    }

    @PostMapping("/register")
    public String registerPost(
            @Valid @ModelAttribute("user") UserMdl userMdl
            , BindingResult result
            , Model model
            , HttpSession session
            , HttpServletRequest request
            , RedirectAttributes redirectAttributes
    ) {

        userValidator.validate(userMdl, result);

        String password = userMdl.getPassword(); // Store the password before it is encrypted

        if (result.hasErrors()) {
            return "register.jsp";
        }

        // run the service to create the record, and one more thing real quick: will this be the first user record?  if so, Make it... SUPER ADMIN!
        if(userSrv.allUsers().size()==0) {
//        	userSrv.newUser(userMdl, "ROLE_SUPER_ADMIN");  // this line temporarily replaced with below line, so that all users are the same
            userSrv.newUser(userMdl, "ROLE_USER");
        } else {
            userSrv.newUser(userMdl, "ROLE_USER");
        }

        // Log in new user with the password we stored before encrypting it.  NOTE: the method listed immed below is built right after this mthd concludes
        authWithHttpServletRequest(request, userMdl.getEmail(), password);
        redirectAttributes.addFlashAttribute("successMsg", "Congratulations on joining PlayDateNOW!  Take a minute now (or later...) to complete your profile.");
        return "redirect:/home";
    }

    // login  method
    public void authWithHttpServletRequest(
            HttpServletRequest request
            , String email
            , String password
    ) {
        try {
            request.login(email, password);
        } catch (ServletException e) {
            System.out.println("Error while login: " + e);
        }
    }

    // JRF: temporarily disabling below user upgrade program
// 	// user upgrade program
//    @RequestMapping("/admin/{id}")
//    // JRF no idea (again...) why above says request mapping instead of post... doesn't make any sense, not sure if/how will work
//    // update on above: I think it's b/c they are doing this as a link instead of a form; same approach on the delete thing
// 	public String makeAdmin(
// 			@PathVariable("id") Long userId
// 			, Model model
// 			) {
//
// 		UserMdl userMdl = userSrv.findById(userId);
// 		userSrv.upgradeUser(userMdl);
//
// 		model.addAttribute("userList", userSrv.allUsers());
//
// 		return "redirect:/admin";
// 	}

//********************************************************************
// HOME/PROFILE/ETC METHODS
//********************************************************************

    @GetMapping(value = {"/", "/home"})
    public String displayHome(
            Principal principal
            , Model model
    ) {

        // authentication boilerplate for all mthd
        UserMdl authUserObj = userSrv.findByEmail(principal.getName()); model.addAttribute("authUser", authUserObj); model.addAttribute("authUserName", authUserObj.getUserName());

        String staticAddy = "910 W Belden Chicago IL";
        model.addAttribute("staticAddy", staticAddy);


        // JRF temporarily removing below: updating last login and substituting admin.jsp for home is not desired
//		if(userMdl!=null) {
//			userMdl.setLastLogin(new Date());
//			userSrv.updateUser(userMdl);
//			// If the user is an ADMIN or SUPER_ADMIN they will be redirected to the admin page
//			if(userMdl.getRoleMdl().get(0).getName().contains("ROLE_SUPER_ADMIN") || userMdl.getRoleMdl().get(0).getName().contains("ROLE_ADMIN")) {
//				model.addAttribute("currentUser", userSrv.findByEmail(email));
//				model.addAttribute("userList", userSrv.allUsers());
//				return "admin.jsp";
//			}
//			// All other users are redirected to the home page
//		}

        return "home.jsp";
        // below is an example of how to redirect users away from this screen if that's desired
//        return "redirect:/playdate";
    }

    // JRF temporarily disabling this whole admin program
//  @RequestMapping("/admin")
//  public String adminDisplayPage(
//  		Principal principal
//  		, Model model
//  		) {
//
////  	String username = principal.getName();
////    above replaced by below
//  	String email = principal.getName();
//
////  	model.addAttribute("currentUser", userSrv.findByUsername(username));
//  	// above replaced by below
//  	model.addAttribute("currentUser", userSrv.findByEmail(email));
//  	model.addAttribute("userList", userSrv.allUsers());
//
//  	return "admin.jsp";
//  }

    // JRF: temporarily disabling this 'delete users' program.

//  @RequestMapping("/delete/{id}")
//	public String deleteUser(
//			@PathVariable("id") Long userId
//			, HttpSession session
//			, Model model
//			) {
//		UserMdl userMdl = userSrv.findById(userId);
//		userSrv.deleteUser(userMdl);
//
//		model.addAttribute("userList", userSrv.allUsers());
//
//		return "redirect:/admin";
//	}

    // view all profile
    @GetMapping("/profile")
    public String displayProfileAll(
//            @ModelAttribute("soConObj") SocialconnectionMdl soConObj
            @ModelAttribute("soConObjForm") SocialconnectionMdl soConObj
            , Model model
            , Principal principal
    ) {

        // authentication boilerplate for all mthd
        UserMdl authUserObj = userSrv.findByEmail(principal.getName()); model.addAttribute("authUser", authUserObj); model.addAttribute("authUserName", authUserObj.getUserName());

//        // (1) deliver list of user records to page
//        List<UserMdl> profileList = userSrv.returnAll();
//        model.addAttribute("profileList", profileList);

        // above deprecated a while ago by below

        // (2) deliver list of user records to page
//        Long authUserId = authUserObj.getId();
//        List<UserSocialConnectionPjo> userSocialConnectionList = userSrv.userSocialConnectionList(authUserObj.getId());
        List<UserSocialConnectionPjo> userSocialConnectionList = userSrv.userSocialConnectionList( authUserObj.getId() );
        model.addAttribute("userSocialConnectionList", userSocialConnectionList);

        return "profile/list.jsp";
    }

    @GetMapping("/profile/{id}")
    public String displayProfile(
            @PathVariable("id") Long userProfileId
            , @ModelAttribute("soConObjForm") SocialconnectionMdl soConObj // soCon action buttons won't work without this line
            , Principal principal
            , Model model
    ) {

        // authentication boilerplate for all mthd
        UserMdl authUserObj = userSrv.findByEmail(principal.getName()); model.addAttribute("authUser", authUserObj); model.addAttribute("authUserName", authUserObj.getUserName());

        // 2022.12.15: note for future work: at present, EU can edit URL to have the id be literally anything, and as constructed, it returns a page with just blanks for all values; should redirect if not found.
        // update on above; now that we've got this pjo running, the page will blow up.  get back to this.

        // (1) set up variables
        UserSocialConnectionPjo userSocialConnectionPjo = socialconnectionRpo.getOneUserSocialConnectionPjo(authUserObj.getId(), userProfileId);

        // (2) check if user profile has been blocked by auth user, or if authUser is on the user profile's block list; if so, redirect
        if (Objects.equals(userSocialConnectionPjo.getSoconStatusEnhanced(), "blocked")) {
             return "redirect:/profile/";
         }

        // (3) deliver objects to page
        model.addAttribute("userProfile", userSrv.findById(userProfileId));
        model.addAttribute("userSocialConnectionPjo", userSocialConnectionPjo);

        // (4) concatenate all non-null address components saved by user, then deliver to page for gma
        String homeAddy = "";
        if (authUserObj.getAddressLine1() != null && authUserObj.getAddressLine1().length() > 0 ) {homeAddy += authUserObj.getAddressLine1();}
        if (authUserObj.getAddressLine2() != null && authUserObj.getAddressLine2().length() > 0 ) {
            if (homeAddy.length() > 0) {homeAddy += " ";}
            homeAddy += authUserObj.getAddressLine2();
        }
        if (authUserObj.getCity() != null && authUserObj.getCity().length() > 0 ) {
            if (homeAddy.length() > 0) {homeAddy += " ";}
            homeAddy += authUserObj.getCity();}
        if (authUserObj.getStateterritoryMdl() != null ) {
            if (homeAddy.length() > 0) {homeAddy += " ";}
            homeAddy += authUserObj.getStateterritoryMdl().getAbbreviation();}
        if (authUserObj.getZipCode() != null && authUserObj.getZipCode().length() > 0 ) {
            if (homeAddy.length() > 0) {homeAddy += " ";}
            homeAddy += authUserObj.getZipCode();}
        model.addAttribute("homeAddy", homeAddy);

        // (5) send list of playdates, for display in table
        List<PlaydateMdl> userHostedPlaydateListPast = playdateSrv.userHostedPlaydateListPast(userProfileId);
        model.addAttribute("userHostedPlaydateListPast", userHostedPlaydateListPast);

        List<PlaydateMdl> userHostedPlaydateListCurrentPlus = playdateSrv.userHostedPlaydateListCurrentPlus(userProfileId);
        model.addAttribute("userHostedPlaydateListCurrentPlus", userHostedPlaydateListCurrentPlus);

        // (6) use service to determine whether user is admin, send to page
        int authUserIsAdmin = userSrv.authUserIsAdmin(authUserObj);
        model.addAttribute("authUserIsAdmin", authUserIsAdmin);

        // above replaces the first draft that lies below; below was native to this mthd, did not use a service.

        // (6) ferret out whether admin or not, and send to page.. yes, this is a mess and needs to be a service. to do!
//        // 6a: instantiate essential variable
//        int authUserIsAdmin = 0;
//        // 6b: get list of roleMdl values, b/c that's how this many:many table set-up works
//        List<RoleMdl> authUserObjRoleMdlList = authUserObj.getRoleMdl();
//        // 6c: iterate through the list, looking for values where role id is 2 or 3, i.e. the admin roles; and yes, there is literally only one record in the list, so this iteration is "one and done"
//        int a = 0; // instantiate variable for iteration
//        for (a=0; a < authUserObjRoleMdlList.size(); a++ ) {
//            System.out.println("item: " + a );
//            System.out.println("authUserObjRoleMdlList.get(a).getId(): " + authUserObjRoleMdlList.get(a).getId() );
//            if (
//                    authUserObjRoleMdlList.get(a).getId().equals(Long.valueOf(2))
//                    ||
//                            authUserObjRoleMdlList.get(a).getId().equals(Long.valueOf(3))
//            ) authUserIsAdmin = 1;
//             System.out.println("authUserIsAdmin: " + authUserIsAdmin  + "\n" );
//        }
//        // 6d: send variable to page
//        model.addAttribute("authUserIsAdmin", authUserIsAdmin);

        return "profile/record.jsp";
    }

    @GetMapping("/profile/{id}/edit")
    public String displayProfileEdit(
            @ModelAttribute("userProfileTobe") UserUpdateDto userUpdateObj
            , @PathVariable("id") Long userProfileId
            , Model model
            , Principal principal
    ) {

        // authentication boilerplate for all mthd
        UserMdl authUserObj = userSrv.findByEmail(principal.getName()); model.addAttribute("authUser", authUserObj); model.addAttribute("authUserName", authUserObj.getUserName());

        // (1) get these values from the db, so they can be delivered as addAtt independent of obj that's in flux
        String authUserName = authUserObj.getUserName();

        // (2) acquire as-is object/values from db
        UserMdl userProfileObj = userSrv.findById(userProfileId);

        // (3) Populate the initially-empty userupdateObj (being sent to the page/form) with the values from the existing record
        userUpdateObj.setUserName(userProfileObj.getUserName());
        userUpdateObj.setEmail(userProfileObj.getEmail());
        userUpdateObj.setFirstName(userProfileObj.getFirstName());
        userUpdateObj.setLastName(userProfileObj.getLastName());
        userUpdateObj.setAboutMe(userProfileObj.getAboutMe());
        userUpdateObj.setAddressLine1(userProfileObj.getAddressLine1());
        userUpdateObj.setAddressLine2(userProfileObj.getAddressLine2());
        userUpdateObj.setCity(userProfileObj.getCity());
        userUpdateObj.setStateterritoryMdl(userProfileObj.getStateterritoryMdl());
        userUpdateObj.setZipCode(userProfileObj.getZipCode());
        userUpdateObj.setHomeName(userProfileObj.getHomeName());

        // (4) send non-changing static attributes to the page, so static values can be used (createdAt, Id, etc.)
        model.addAttribute("userProfileId", userProfileId); // need for cancel button; this derives from path variable, not sure if this line needed.
        Date userProfileCreatedAt = authUserObj.getCreatedAt();
        model.addAttribute("userProfileCreatedAt", userProfileCreatedAt); // static single value on the page

        // (5) send as-is object attribute values to the page, so as-is value shall be preselected in the drop-downs
        StateterritoryMdl intendedStateTerritoryObj = userProfileObj.getStateterritoryMdl();
        model.addAttribute("intendedStateTerritoryObj", intendedStateTerritoryObj);

        // above replaced by below: send the full as-is user object.  this seems to work okay.
//        model.addAttribute("userProfileObj", userProfileObj);

//        System.out.println(userProfileObj);
//        System.out.println(userProfileObj.getStateterritoryMdl());
//        System.out.println(userProfileObj.getStateterritoryMdl().getId());

        // (6) create+send the list of stateTerritory to the page, for drop-down purposes
        List<StateterritoryMdl> stateterritoryList = stateterritorySrv.returnAll();
        model.addAttribute("stateterritoryList", stateterritoryList);

        return "profile/edit.jsp";
    }

    // for future clean-up: this entire method has authUserObj being sent to update srv, but shoudl really just be the userObject from the page's userID value.
    // above entails updated the @postMapping../path/ to include the id of the profile.  bad move to have ever removed that.
    @PostMapping("/profile/edit")
    public String processProfileEdit(
            @Valid
            @ModelAttribute("userProfileTobe") UserUpdateDto userUpdateObj
            , BindingResult result
            , Model model
            , Principal principal
            , RedirectAttributes redirectAttributes
    ) {

        // authentication boilerplate for all mthd
        UserMdl authUserObj = userSrv.findByEmail(principal.getName()); model.addAttribute("authUser", authUserObj); model.addAttribute("authUserName", authUserObj.getUserName());

        // (1) get these values from the db, so they can be delivered as addAtt independent of obj that's in flux
        Long userProfileId = authUserObj.getId();
        String authUserName = authUserObj.getUserName();
        Date userProfileCreatedAt = authUserObj.getCreatedAt();

        // (2) overwrite the targeted fields in the userObj with values from the userupdateObj
        authUserObj.setUserName(userUpdateObj.getUserName());
//		userObj.setEmail(userupdateObj.getEmail()); // this line shall be enabled, once we figure out how to update the authentication object to contain the updated email addy
        authUserObj.setFirstName(userUpdateObj.getFirstName() );
        authUserObj.setLastName(userUpdateObj.getLastName() );
        authUserObj.setAboutMe(userUpdateObj.getAboutMe() );
        authUserObj.setAddressLine1(userUpdateObj.getAddressLine1());
        authUserObj.setAddressLine2(userUpdateObj.getAddressLine2());
        authUserObj.setCity(userUpdateObj.getCity() );
        authUserObj.setStateterritoryMdl(userUpdateObj.getStateterritoryMdl());
        authUserObj.setZipCode(userUpdateObj.getZipCode() );
        authUserObj.setHomeName(userUpdateObj.getHomeName());

        // all of below was intended to cleanly set the fk for stateterritory_id as null... but using 0 as the incoming value for "no selection" seems to do the trick, so whatever!
//        if (userUpdateObj.getStateterritoryMdl().getId() == Long.valueOf(99) ) {
//            authUserObj.setStateterritoryMdl(null);
//        } else {
//            authUserObj.setStateterritoryMdl(userUpdateObj.getStateterritoryMdl());
//        };

        // (3) run the service to save the updated object
        userSrv.updateUserProfile(authUserObj, result);

        if (!result.hasErrors() ) {
            return "redirect:/profile/" + authUserObj.getId();

        } else {

            // (a) send the as-is object to the page, so static values can be used (createdAt, Id, etc.)
            model.addAttribute("userProfileId", userProfileId);
            model.addAttribute("userProfileCreatedAt", userProfileCreatedAt);

            // adding below, trying to make that intended stateTerr value "stick"
            StateterritoryMdl intendedStateTerritoryObj = authUserObj.getStateterritoryMdl();
            model.addAttribute("intendedStateTerritoryObj", intendedStateTerritoryObj);

//            System.out.println("intendedStateTerritoryObj:" + intendedStateTerritoryObj);

            // (b) create+send the list of stateTerritory to the page, for drop-down purposes
            List<StateterritoryMdl> stateterritoryList = stateterritorySrv.returnAll();
            model.addAttribute("stateterritoryList", stateterritoryList);

            return "profile/edit.jsp";

        }
    }

    @GetMapping("/settings")
    public String displayUserSettings(
            @ModelAttribute("soConObjForm") SocialconnectionMdl soConObjForm
            , Model model
            , Principal principal
    ) {

        // authentication boilerplate for all mthd
        UserMdl authUserObj = userSrv.findByEmail(principal.getName()); model.addAttribute("authUser", authUserObj); model.addAttribute("authUserName", authUserObj.getUserName());

        // (1) soCon lists

        List<UserSocialConnectionPjo> userSocialConnectionListBlocked = userSrv.userSocialConnectionListBlocked(authUserObj.getId());
        model.addAttribute("userSocialConnectionListBlocked", userSocialConnectionListBlocked);

        return "settings.jsp";
    }

    @GetMapping("/error")
    public String displayError(
            Principal principal
            , Model model
    ) {

        // authentication boilerplate for all mthd
        UserMdl authUserObj = userSrv.findByEmail(principal.getName()); model.addAttribute("authUser", authUserObj); model.addAttribute("authUserName", authUserObj.getUserName());

        String staticAddy = "910 W Belden Chicago IL";
        model.addAttribute("staticAddy", staticAddy);


        // JRF temporarily removing below: updating last login and substituting admin.jsp for home is not desired
//		if(userMdl!=null) {
//			userMdl.setLastLogin(new Date());
//			userSrv.updateUser(userMdl);
//			// If the user is an ADMIN or SUPER_ADMIN they will be redirected to the admin page
//			if(userMdl.getRoleMdl().get(0).getName().contains("ROLE_SUPER_ADMIN") || userMdl.getRoleMdl().get(0).getName().contains("ROLE_ADMIN")) {
//				model.addAttribute("currentUser", userSrv.findByEmail(email));
//				model.addAttribute("userList", userSrv.allUsers());
//				return "admin.jsp";
//			}
//			// All other users are redirected to the home page
//		}

        return "error.jsp";
        // below is an example of how to redirect users away from this screen if that's desired
//        return "redirect:/playdate";
    }

} // end of methods

