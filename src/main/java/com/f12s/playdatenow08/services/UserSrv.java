package com.f12s.playdatenow08.services;

import com.f12s.playdatenow08.models.UserMdl;
import com.f12s.playdatenow08.pojos.UserSocialConnectionPjo;
import com.f12s.playdatenow08.repositories.RoleRpo;
import com.f12s.playdatenow08.repositories.UserRpo;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
//import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;

//import java.util.ArrayList;
//import org.mindrot.jbcrypt.BCrypt;

//import com.jonfriend.playdatenow_v04.models.LoginUserMdl;

@Service
public class UserSrv{

    @Autowired
    private UserRpo userRpo;

    @Autowired
    private RoleRpo roleRpo;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public void newUser(
            UserMdl usermdl
            , String role  // note: this is just a string variable called 'role'... not the roleMdl
    ) {
        usermdl.setPassword(bCryptPasswordEncoder.encode(usermdl.getPassword()));
        usermdl.setRoleMdl(roleRpo.findByName(role));
        userRpo.save(usermdl);
    }

    // JRF: temporarily disabling this updateUser program.
//    public void updateUser(
//    		UserMdl usermdl
//    		) {
//    	userRpo.save(usermdl);
//	}

    public UserMdl findByEmail(
            String email
    ) {
        return userRpo.findByEmail(email);
    }

    public List<UserMdl> allUsers(){
        return userRpo.findAll();
    }


    // below disabled from springSec for now
//	public UserMdl upgradeUser(
//			UserMdl usermdl
//			) {
//		usermdl.setRoleMdl(roleRpo.findByName("ROLE_ADMIN"));
//		return userRpo.save(usermdl);
//	}

    // below disabled from springSec for now
//	public void deleteUser(
//			UserMdl usermdl
//			) {
//		userRpo.delete(usermdl);
//	}

    // below used on every page beginning with /home.  Delivers user object of logged in user, so as to display various user attributes.
    public UserMdl findById(
            Long id
    ) {
        Optional<UserMdl> potentialUser = userRpo.findById(id);

        if(!potentialUser.isPresent()) {
            return null;}

        return potentialUser.get();

    }

    // returns all
    public List<UserMdl> returnAll(){
        return userRpo.findAll();
    }

    public UserMdl updateUserProfile(
            UserMdl sketchedUpdatedUserMdl
            , Errors errors
    ) {

        // try to find a user record with the proposed email addy
        Optional<UserMdl> userRecordWithMatchingEmailAddy = Optional.ofNullable(userRpo.findByEmail(sketchedUpdatedUserMdl.getEmail()));

        // Reject if record found in db with that email and the userID of such record is NOT the user we mging here.
        if(
                userRecordWithMatchingEmailAddy.isPresent()
                        &&
                        userRecordWithMatchingEmailAddy.get().getId() != sketchedUpdatedUserMdl.getId()

        ) {
            errors.rejectValue("email", "Match");
        }

        Optional<UserMdl> userRecordWithMatchingUserName = Optional.ofNullable(userRpo.findByUserName(sketchedUpdatedUserMdl.getUserName()));

        // Reject if record found in db with that userName and the userID of such record is NOT the user we mging here.
        if(
                userRecordWithMatchingUserName.isPresent()
                        &&
                        userRecordWithMatchingUserName.get().getId() != sketchedUpdatedUserMdl.getId()

        ) {
            errors.rejectValue("userName", "Match");
        }

        if(errors.hasErrors()) {
            return null; // Exit the method and go back to the controller to handle the response
        }

        return userRpo.save(sketchedUpdatedUserMdl);
    }


    // begin soCon stuff
    public List<UserSocialConnectionPjo> userSocialConnectionList(Long x) {return userRpo.userSocialConnectionList(x);}

// end srv
}


//package com.f12s.playdatenow08.services;
//
//import com.f12s.playdatenow08.models.UserMdl;
//import com.f12s.playdatenow08.repositories.RoleRpo;
//import com.f12s.playdatenow08.repositories.UserRpo;
//import java.util.List;
//import java.util.Optional;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.stereotype.Service;
////import org.springframework.validation.BindingResult;
//import org.springframework.validation.Errors;
//
////import java.util.ArrayList;
////import org.mindrot.jbcrypt.BCrypt;
//
////import com.jonfriend.playdatenow_v04.models.LoginUserMdl;
//
//@Service
//public class UserSrv{
//
//    @Autowired
//    private UserRpo userRpo;
//
//    @Autowired
//    private RoleRpo roleRpo;
//
//    @Autowired
//    private BCryptPasswordEncoder bCryptPasswordEncoder;
//
//    public void newUser(
//            UserMdl usermdl
//            , String role  // note: this is just a string variable called 'role'... not the roleMdl
//    ) {
//        usermdl.setPassword(bCryptPasswordEncoder.encode(usermdl.getPassword()));
//        usermdl.setRoleMdl(roleRpo.findByName(role));
//        userRpo.save(usermdl);
//    }
//
//    // JRF: temporarily disabling this updateUser program.
////    public void updateUser(
////    		UserMdl usermdl
////    		) {
////    	userRpo.save(usermdl);
////	}
//
//    public UserMdl findByEmail(
//            String email
//    ) {
//        return userRpo.findByEmail(email);
//    }
//
//    public List<UserMdl> allUsers(){
//        return userRpo.findAll();
//    }
//
//
//    // below disabled from springSec for now
////	public UserMdl upgradeUser(
////			UserMdl usermdl
////			) {
////		usermdl.setRoleMdl(roleRpo.findByName("ROLE_ADMIN"));
////		return userRpo.save(usermdl);
////	}
//
//    // below disabled from springSec for now
////	public void deleteUser(
////			UserMdl usermdl
////			) {
////		userRpo.delete(usermdl);
////	}
//
//    // below used on every page beginning with /home.  Delivers user object of logged in user, so as to display various user attributes.
//    public UserMdl findById(
//            Long id
//    ) {
//        Optional<UserMdl> potentialUser = userRpo.findById(id);
//
//        if(!potentialUser.isPresent()) {
//            return null;}
//
//        return potentialUser.get();
//
//    }
//
//    // returns all
//    public List<UserMdl> returnAll(){
//        return userRpo.findAll();
//    }
//
//    public UserMdl updateUserProfile(
//            UserMdl sketchedUpdatedUserMdl
//            , Errors errors
//    ) {
//
//        // try to find a user record with the proposed email addy
//        Optional<UserMdl> userRecordWithMatchingEmailAddy = Optional.ofNullable(userRpo.findByEmail(sketchedUpdatedUserMdl.getEmail()));
//
//        // Reject if record found in db with that email and the userID of such record is NOT the user we mging here.
//        if(
//                userRecordWithMatchingEmailAddy.isPresent()
//                        &&
//                        userRecordWithMatchingEmailAddy.get().getId() != sketchedUpdatedUserMdl.getId()
//
//        ) {
//            errors.rejectValue("email", "Match");
//        }
//
//        Optional<UserMdl> userRecordWithMatchingUserName = Optional.ofNullable(userRpo.findByUserName(sketchedUpdatedUserMdl.getUserName()));
//
//        // Reject if record found in db with that userName and the userID of such record is NOT the user we mging here.
//        if(
//                userRecordWithMatchingUserName.isPresent()
//                        &&
//                        userRecordWithMatchingUserName.get().getId() != sketchedUpdatedUserMdl.getId()
//
//        ) {
//            errors.rejectValue("userName", "Match");
//        }
//
//        if(errors.hasErrors()) {
//            return null; // Exit the method and go back to the controller to handle the response
//        }
//
//        return userRpo.save(sketchedUpdatedUserMdl);
//    }
//
//// end srv
//}
