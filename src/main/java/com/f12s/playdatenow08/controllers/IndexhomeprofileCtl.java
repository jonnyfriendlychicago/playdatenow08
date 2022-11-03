package com.f12s.playdatenow08.controllers;

import com.f12s.playdatenow08.dataTransferObjects.UserUpdateDto;
import com.f12s.playdatenow08.models.UserMdl;
import com.f12s.playdatenow08.services.UserSrv;
import com.f12s.playdatenow08.validator.UserValidator;
import java.security.Principal;
import java.util.Date;
import java.util.List;
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
//            return "redirect:/";
//            return "redirect:/playdate";
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

        // will this be the first user record?  if so, Make it... SUPER ADMIN!
        if(userSrv.allUsers().size()==0) {
//        	userSrv.newUser(userMdl, "ROLE_SUPER_ADMIN");  // this line temporarily replaced with below line, so that all users are the same
            userSrv.newUser(userMdl, "ROLE_USER");
        } else {
            userSrv.newUser(userMdl, "ROLE_USER");
        }

        // Log in new user with the password we stored before encrypting it.  NOTE: the method listed immed below is built right after this mthd concludes
        authWithHttpServletRequest(request, userMdl.getEmail(), password);
        redirectAttributes.addFlashAttribute("successMsg", "Congratulations on joining PlayDateNOW!  Take a minute now (or later...) to complete your profile.");
//        return "redirect:/playdate";
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
            Model model
            , Principal principal
    ) {

        // authentication boilerplate for all mthd
        UserMdl authUserObj = userSrv.findByEmail(principal.getName()); model.addAttribute("authUser", authUserObj); model.addAttribute("authUserName", authUserObj.getUserName());

        List<UserMdl> profileList = userSrv.returnAll();
        model.addAttribute("profileList", profileList);
        return "profile/list.jsp";
    }

    @GetMapping("/profile/{id}")
    public String displayProfile(
            @PathVariable("id") Long userProfileId
            , Principal principal
            , Model model
    ) {

        // authentication boilerplate for all mthd
        UserMdl authUserObj = userSrv.findByEmail(principal.getName()); model.addAttribute("authUser", authUserObj); model.addAttribute("authUserName", authUserObj.getUserName());

        model.addAttribute("userProfile", userSrv.findById(userProfileId)); // grab the entire user object using the url parameter, then deliver to page


        String staticAddy = "910 W Belden Chicago IL";
        model.addAttribute("staticAddy", staticAddy);

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
        Date userProfileCreatedAt = authUserObj.getCreatedAt();

        // (2) acquire as-is object/values from db
        UserMdl userProfileObj = userSrv.findById(userProfileId);

        // (3) Populate the initially-empty userupdateObj (being sent to the page/form) with the values from the existing record
        userUpdateObj.setUserName(userProfileObj.getUserName());
        userUpdateObj.setEmail(userProfileObj.getEmail());
        userUpdateObj.setFirstName(userProfileObj.getFirstName());
        userUpdateObj.setLastName(userProfileObj.getLastName());
        userUpdateObj.setAboutMe(userProfileObj.getAboutMe());
        userUpdateObj.setCity(userProfileObj.getCity());
        userUpdateObj.setZipCode(userProfileObj.getZipCode());

        // (4) send the as-is object to the page, so static values can be used (createdAt, Id, etc.)
        model.addAttribute("userProfileId", userProfileId); // need for cancel button
        model.addAttribute("userProfileCreatedAt", userProfileCreatedAt); // static single value on the page

        return "profile/edit.jsp";
    }

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
        authUserObj.setCity(userUpdateObj.getCity() );
        authUserObj.setZipCode(userUpdateObj.getZipCode() );

        // (3) run the service to save the updated object
        userSrv.updateUserProfile(authUserObj, result);

        if (result.hasErrors() ) {
            // (4) send the as-is object to the page, so static values can be used (createdAt, Id, etc.)
            model.addAttribute("userProfileId", userProfileId);
            model.addAttribute("userProfileCreatedAt", userProfileCreatedAt);
            return "profile/edit.jsp";

        } else {
            return "redirect:/profile/" + authUserObj.getId();
        }
    }

// end of methods
}

