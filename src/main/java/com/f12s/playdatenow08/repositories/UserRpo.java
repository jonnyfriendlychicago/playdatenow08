package com.f12s.playdatenow08.repositories;

import com.f12s.playdatenow08.models.UserMdl;
import java.util.List;

import com.f12s.playdatenow08.pojos.UserSocialConnectionPjo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRpo extends CrudRepository<UserMdl, Long> {

    UserMdl findByEmail(String email);

    UserMdl findByUserName(String userName);

    UserMdl findByIdIs(Long id);

    List<UserMdl> findAll();

    // begin socialCon stuff
    @Query(
            value=
                    "select case when u.id = :keyword then 0 when userRelation.relatedUser is null then 1 else 2 end as statusRelationToAuthUser, "
                            + " userRelation.soconStatusCodeId as soconStatusCodeId, "

                            + " code.code as soconStatus, "

                            + " case when u.id = :keyword then 'authUserRecord' when userRelation.relatedUser is null then 'noRelation' when code.code is not null then code.code else  'soConError' end as soconStatusEnhanced, "

                            + " userRelation.relationInitiator as relationInitiator, "

                            + " u.id as id, u.about_me as aboutMe, u.address_line1 as addressLine1, u.address_line2 as addressLine2, u.city as city, u.created_at as createdAt, u.email as email, u.first_name as firstName, u.last_name as lastName, u.user_name as userName, u.zip_code as zipCode, u.home_name as homeName, st.full_name as stateName "
                            + " from playdatenow08.user u "
                            + " left join playdatenow08.stateterritory st on u.stateterritory_id = st.id "
                            + " left join "
                            + "(select sc.userone as relationInitiator, sc.usertwo as relatedUser, sc.soconstatus_code as soconStatusCodeId  from playdatenow08.socialconnection sc where userone = :keyword union all select sc.userone as relationInitiator, sc.userone as relatedUser, sc.soconstatus_code as soconStatusCodeId from playdatenow08.socialconnection sc where  usertwo  = :keyword ) userRelation on u.id = userRelation.relatedUser"
                            + " left join playdatenow08.code code on userRelation.soconStatusCodeId = code.id "
            , nativeQuery = true)
    List<UserSocialConnectionPjo> userSocialConnectionList (Long keyword);


// end rpo
}


//package com.f12s.playdatenow08.repositories;
//
//import com.f12s.playdatenow08.models.UserMdl;
//import java.util.List;
//import org.springframework.data.repository.CrudRepository;
//import org.springframework.stereotype.Repository;
//
//@Repository
//public interface UserRpo extends CrudRepository<UserMdl, Long> {
//
//    UserMdl findByEmail(String email);
//
//    UserMdl findByUserName(String userName);
//
//    List<UserMdl> findAll();
//
//    UserMdl findByIdIs(Long id);
//
//// end rpo
//}
