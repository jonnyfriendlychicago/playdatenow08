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

//    UserMdl findById(Long id); // this line results in redSquig with msg: "'findById(Long)' in 'com.f12s.playdatenow08.repositories.UserRpo' clashes with 'findById(ID)' in 'org.springframework.data.repository.CrudRepository'; attempting to use incompatible return type"

    List<UserMdl> findAll();

    // (1) all users list

    // below is my attempt to exclude blockety-blocks
    @Query(
            value =
                    "select "
                            + " u.id as id, u.about_me as aboutMe, u.address_line1 as addressLine1, u.address_line2 as addressLine2, u.city as city, u.created_at as createdAt, u.email as email, u.first_name as firstName, u.last_name as lastName, u.user_name as userName, u.zip_code as zipCode, u.home_name as homeName, st.full_name as stateName "
                            + " , case "
                            + " when u.id = :authUserId then 'authUserRecord' "
                            + " when (ur.relatedUserId is null or code.code = 'soConReset') then 'noRelation' "
                            + " when (code.code = 'requestPending' and :authUserId = ur.initiatorUserId) then 'authUserSentRequest'  "
                            + " when (code.code = 'requestPending' and :authUserId = ur.ResponderUserId) then 'authUserReceivedRequest'  "
                            + " when code.code = 'friends' then 'friends'  "
                            + " when code.code = 'blocked' then 'blocked'  "
                            + " when code.code is not null then code.code "
                            + " else  'soConError' end as soconStatusEnhanced, "
                            + " ur.socialconnectionId as socialconnectionId "
                            + " from playdatenow08.user u "
                            + " left join playdatenow08.stateterritory st on u.stateterritory_id = st.id "
                            + " left join "
                            + "(select sc.id as socialconnectionId, sc.soconstatus_code_id as soconStatusCodeId, sc.initiator_user_id as initiatorUserId, sc.responder_user_id as responderUserId , sc.initiator_user_id as relatedUserId from playdatenow08.socialconnection sc where responder_user_id = :authUserId "
                            + " union all "
                            + " select sc.id as socialconnectionId, sc.soconstatus_code_id as soconStatusCodeId, sc.initiator_user_id as initiatorUserId, sc.responder_user_id as responderUserId , sc.responder_user_id as relatedUserId from playdatenow08.socialconnection sc where initiator_user_id = :authUserId "
                            + " ) ur on u.id = ur.relatedUserId"
                            + " left join playdatenow08.code code on ur.soconStatusCodeId = code.id "

                            + " left join "
                            + "(select sc.id as socialconnectionId, sc.soconstatus_code_id as soconStatusCodeId, sc.initiator_user_id as initiatorUserId, sc.responder_user_id as responderUserId , sc.initiator_user_id as relatedUserId from playdatenow08.socialconnection sc where responder_user_id = :authUserId and sc.blocker_user_id is not null"
                            + " union all "
                            + " select sc.id as socialconnectionId, sc.soconstatus_code_id as soconStatusCodeId, sc.initiator_user_id as initiatorUserId, sc.responder_user_id as responderUserId , sc.responder_user_id as relatedUserId from playdatenow08.socialconnection sc where initiator_user_id = :authUserId and sc.blocker_user_id is not null"
                            + " ) ur2 on u.id = ur2.relatedUserId"
                            + " where ur2.socialconnectionId is null"

                            + " order by u.id desc"
            , nativeQuery = true)
    List<UserSocialConnectionPjo> userSocialConnectionList(Long authUserId);

//    // below is my attempt to exclude blockety-blocks
//    @Query(
//            value =
//                    "select "
//                            + " u.id as id, u.about_me as aboutMe, u.address_line1 as addressLine1, u.address_line2 as addressLine2, u.city as city, u.created_at as createdAt, u.email as email, u.first_name as firstName, u.last_name as lastName, u.user_name as userName, u.zip_code as zipCode, u.home_name as homeName, st.full_name as stateName "
//                            + " , case "
//                            + " when u.id = :authUserId then 'authUserRecord' "
//                            + " when (ur.relatedUserId is null or ur.soconStatusCodeId = 12) then 'noRelation' "
//                            + " when (ur.soconStatusCodeId = 6 and :authUserId = ur.initiatorUserId) then 'authUserSentRequest'  "
//                            + " when (ur.soconStatusCodeId = 6 and :authUserId = ur.ResponderUserId) then 'authUserReceivedRequest'  "
//                            + " when ur.soconStatusCodeId = 9 then 'friends'  "
//                            + " when ur.soconStatusCodeId = 11 then 'blocked'  "
//                            + " when code.code is not null then code.code "
//                            + " else  'soConError' end as soconStatusEnhanced, "
//                            + " ur.socialconnectionId as socialconnectionId "
//                            + " from playdatenow08.user u "
//                            + " left join playdatenow08.stateterritory st on u.stateterritory_id = st.id "
//                            + " left join "
//                            + "(select sc.id as socialconnectionId, sc.soconstatus_code_id as soconStatusCodeId, sc.initiator_user_id as initiatorUserId, sc.responder_user_id as responderUserId , sc.initiator_user_id as relatedUserId from playdatenow08.socialconnection sc where responder_user_id = :authUserId "
//                            + " union all "
//                            + " select sc.id as socialconnectionId, sc.soconstatus_code_id as soconStatusCodeId, sc.initiator_user_id as initiatorUserId, sc.responder_user_id as responderUserId , sc.responder_user_id as relatedUserId from playdatenow08.socialconnection sc where initiator_user_id = :authUserId "
//                            + " ) ur on u.id = ur.relatedUserId"
//                            + " left join playdatenow08.code code on ur.soconStatusCodeId = code.id "
//
//                            + " left join "
//                            + "(select sc.id as socialconnectionId, sc.soconstatus_code_id as soconStatusCodeId, sc.initiator_user_id as initiatorUserId, sc.responder_user_id as responderUserId , sc.initiator_user_id as relatedUserId from playdatenow08.socialconnection sc where responder_user_id = :authUserId and sc.blocker_user_id is not null"
//                            + " union all "
//                            + " select sc.id as socialconnectionId, sc.soconstatus_code_id as soconStatusCodeId, sc.initiator_user_id as initiatorUserId, sc.responder_user_id as responderUserId , sc.responder_user_id as relatedUserId from playdatenow08.socialconnection sc where initiator_user_id = :authUserId and sc.blocker_user_id is not null"
//                            + " ) ur2 on u.id = ur2.relatedUserId"
//                            + " where ur2.socialconnectionId is null"
//
//                            + " order by u.id desc"
//            , nativeQuery = true)
//    List<UserSocialConnectionPjo> userSocialConnectionList(Long authUserId);


    // below working great before we start to excluded blockity-blocks from list
//    @Query(
//            value =
//                    "select "
//                            + " u.id as id, u.about_me as aboutMe, u.address_line1 as addressLine1, u.address_line2 as addressLine2, u.city as city, u.created_at as createdAt, u.email as email, u.first_name as firstName, u.last_name as lastName, u.user_name as userName, u.zip_code as zipCode, u.home_name as homeName, st.full_name as stateName "
//                            + " , case "
//                            + " when u.id = :authUserId then 'authUserRecord' "
//                            + " when (ur.relatedUserId is null or ur.soconStatusCodeId = 12) then 'noRelation' "
//                            + " when (ur.soconStatusCodeId = 6 and :authUserId = ur.initiatorUserId) then 'authUserSentRequest'  "
//                            + " when (ur.soconStatusCodeId = 6 and :authUserId = ur.ResponderUserId) then 'authUserReceivedRequest'  "
//                            + " when ur.soconStatusCodeId = 9 then 'friends'  "
//                            + " when code.code is not null then code.code "
//                            + " else  'soConError' end as soconStatusEnhanced, "
//                            + " ur.socialconnectionId as socialconnectionId "
//                            + " from playdatenow08.user u "
//                            + " left join playdatenow08.stateterritory st on u.stateterritory_id = st.id "
//                            + " left join "
//                            + "(select sc.id as socialconnectionId, sc.soconstatus_code_id as soconStatusCodeId, sc.initiator_user_id as initiatorUserId, sc.responder_user_id as responderUserId , sc.initiator_user_id as relatedUserId from playdatenow08.socialconnection sc where responder_user_id = :authUserId "
//                            + " union all "
//                            + " select sc.id as socialconnectionId, sc.soconstatus_code_id as soconStatusCodeId, sc.initiator_user_id as initiatorUserId, sc.responder_user_id as responderUserId , sc.responder_user_id as relatedUserId from playdatenow08.socialconnection sc where initiator_user_id = :authUserId "
//                            + " ) ur on u.id = ur.relatedUserId"
//                            + " left join playdatenow08.code code on ur.soconStatusCodeId = code.id "
//                            + " order by u.id desc"
//            , nativeQuery = true)
//    List<UserSocialConnectionPjo> userSocialConnectionList(Long authUserId);

    // (2) relations to authUser: sent requests

//    @Query(
//            value =
//                    "select "
//                            + " ur.socialconnectionId as socialconnectionId, ur.relatedUser as relatedUser, ur.blockerUser as blockerUser, ur.initiatorUser as initiatorUser, "
//                            + " u.id as id, u.about_me as aboutMe, u.address_line1 as addressLine1, u.address_line2 as addressLine2, u.city as city, u.created_at as createdAt, u.email as email, u.first_name as firstName, u.last_name as lastName, u.user_name as userName, u.zip_code as zipCode, u.home_name as homeName, st.full_name as stateName "
//
//                            + " from (select sc.id as socialconnectionId, sc.userone as authUser, sc.usertwo as relatedUser, sc.soconstatus_code as soconStatusCodeId , sc.blocker_user_id as blockerUser, sc.initiator_user_id as initiatorUser  from playdatenow08.socialconnection sc where userone = :keyword union all select sc.id as socialconnectionId, sc.userone as relatedUser, sc.usertwo as authUser , sc.soconstatus_code as soconStatusCodeId , sc.blocker_user_id as blockerUser, sc.initiator_user_id as initiatorUser  from playdatenow08.socialconnection sc where  usertwo  = :keyword ) ur "
//                            + " join playdatenow08.user u on ur.relatedUser = u.id "
//                            + " left join playdatenow08.stateterritory st on u.stateterritory_id = st.id "
//                            + " left join playdatenow08.code c on ur.soconStatusCodeId = c.id "
//                            + " where c.code = 'requestPending' and ur.initiatorUser = :keyword  "
//            , nativeQuery = true)
//    List<UserSocialConnectionPjo> userSocialConnectionListSent(Long keyword);

//    @Query(
//            value =
//                    "select "
//                            + " ur.socialconnectionId as socialconnectionId, ur.initiatorUserId as initiatorUser, "
//                            + " u.id as id, u.about_me as aboutMe, u.address_line1 as addressLine1, u.address_line2 as addressLine2, u.city as city, u.created_at as createdAt, u.email as email, u.first_name as firstName, u.last_name as lastName, u.user_name as userName, u.zip_code as zipCode, u.home_name as homeName, st.full_name as stateName "
//                            + " from playdatenow08.user u "
//                            + " join "
//                            + "(select sc.id as socialconnectionId, sc.soconstatus_code_id as soconStatusCodeId, sc.initiator_user_id as initiatorUserId, sc.responder_user_id as responderUserId , sc.initiator_user_id as relatedUserId from playdatenow08.socialconnection sc where responder_user_id = :authUserId "
//                            + " union all "
//                            + " select sc.id as socialconnectionId, sc.soconstatus_code_id as soconStatusCodeId, sc.initiator_user_id as initiatorUserId, sc.responder_user_id as responderUserId , sc.responder_user_id as relatedUserId from playdatenow08.socialconnection sc where initiator_user_id = :authUserId "
//                            + " ) ur on u.id = ur.relatedUserId"
//                            + " left join playdatenow08.stateterritory st on u.stateterritory_id = st.id "
//                            + " left join playdatenow08.code c on ur.soconStatusCodeId = c.id "
//                            + " where c.code = 'requestPending' "
//            , nativeQuery = true)
//    List<UserSocialConnectionPjo> userSocialConnectionListSent(Long authUserId);

    // (5) relations to authUser: blocked

    @Query(
            value =
                    "select "
                            + " u.id as id, u.about_me as aboutMe, u.address_line1 as addressLine1, u.address_line2 as addressLine2, u.city as city, u.created_at as createdAt, u.email as email, u.first_name as firstName, u.last_name as lastName, u.user_name as userName, u.zip_code as zipCode, u.home_name as homeName, st.full_name as stateName "
                            + " , case "
                            + " when u.id = :authUserId then 'authUserRecord' "
                            + " when (ur.relatedUserId is null or ur.soconStatusCodeId = 12) then 'noRelation' "
                            + " when (ur.soconStatusCodeId = 6 and :authUserId = ur.initiatorUserId) then 'authUserSentRequest'  "
                            + " when (ur.soconStatusCodeId = 6 and :authUserId = ur.ResponderUserId) then 'authUserReceivedRequest'  "
                            + " when ur.soconStatusCodeId = 9 then 'friends'  "
                            + " when ur.soconStatusCodeId = 11 then 'blocked'  "
                            + " when code.code is not null then code.code "
                            + " else  'soConError' end as soconStatusEnhanced, "
                            + " ur.socialconnectionId as socialconnectionId "
                            + " from playdatenow08.user u "
                            + " left join playdatenow08.stateterritory st on u.stateterritory_id = st.id "
                            + " left join "
                            + "(select sc.id as socialconnectionId, sc.soconstatus_code_id as soconStatusCodeId, sc.initiator_user_id as initiatorUserId, sc.responder_user_id as responderUserId , sc.initiator_user_id as relatedUserId from playdatenow08.socialconnection sc where responder_user_id = :authUserId "
                            + " union all "
                            + " select sc.id as socialconnectionId, sc.soconstatus_code_id as soconStatusCodeId, sc.initiator_user_id as initiatorUserId, sc.responder_user_id as responderUserId , sc.responder_user_id as relatedUserId from playdatenow08.socialconnection sc where initiator_user_id = :authUserId "
                            + " ) ur on u.id = ur.relatedUserId"
                            + " left join playdatenow08.code code on ur.soconStatusCodeId = code.id "

                            + " left join "
                            + "(select sc.id as socialconnectionId, sc.soconstatus_code_id as soconStatusCodeId, sc.initiator_user_id as initiatorUserId, sc.responder_user_id as responderUserId , sc.initiator_user_id as relatedUserId from playdatenow08.socialconnection sc where responder_user_id = :authUserId and sc.blocker_user_id = :authUserId"
                            + " union all "
                            + " select sc.id as socialconnectionId, sc.soconstatus_code_id as soconStatusCodeId, sc.initiator_user_id as initiatorUserId, sc.responder_user_id as responderUserId , sc.responder_user_id as relatedUserId from playdatenow08.socialconnection sc where initiator_user_id = :authUserId and sc.blocker_user_id = :authUserId"
                            + " ) ur2 on u.id = ur2.relatedUserId"
                            + " where ur2.socialconnectionId is not null"

                            + " order by u.id desc"
            , nativeQuery = true)
    List<UserSocialConnectionPjo> userSocialConnectionListBlocked(Long authUserId);

    // old blocked

//    @Query(
//            value =
//                    "select "
//
//                            + " ur.socialconnectionId as socialconnectionId, ur.relationInitiator as relationInitiator, "
//                            + " u.id as id, u.about_me as aboutMe, u.address_line1 as addressLine1, u.address_line2 as addressLine2, u.city as city, u.created_at as createdAt, u.email as email, u.first_name as firstName, u.last_name as lastName, u.user_name as userName, u.zip_code as zipCode, u.home_name as homeName, st.full_name as stateName "
//
//                            + " from (select sc.id as socialconnectionId, sc.userone as relationInitiator, sc.usertwo as relatedUser, sc.soconstatus_code as soconStatusCodeId from playdatenow08.socialconnection sc where userone = :keyword union all select sc.id as socialconnectionId, sc.userone as relationInitiator, sc.userone as relatedUser, sc.soconstatus_code as soconStatusCodeId from playdatenow08.socialconnection sc where usertwo  = :keyword ) ur "
//                            + " join playdatenow08.user u on ur.relatedUser = u.id "
//                            + " left join playdatenow08.stateterritory st on u.stateterritory_id = st.id "
//                            + " left join playdatenow08.code c on ur.soconStatusCodeId = c.id "
//                            + " where c.code = 'blocked' "
//            , nativeQuery = true)
//    List<UserSocialConnectionPjo> userSocialConnectionListBlocked(Long keyword);

    // (1) sent list
    @Query(
            value =
                    "select "
                            + " u.id as id, u.about_me as aboutMe, u.address_line1 as addressLine1, u.address_line2 as addressLine2, u.city as city, u.created_at as createdAt, u.email as email, u.first_name as firstName, u.last_name as lastName, u.user_name as userName, u.zip_code as zipCode, u.home_name as homeName, st.full_name as stateName "
                            + " , case "
                            + " when u.id = :authUserId then 'authUserRecord' "
                            + " when (ur.relatedUserId is null or ur.soconStatusCodeId = 12) then 'noRelation' "
                            + " when (ur.soconStatusCodeId = 6 and :authUserId = ur.initiatorUserId) then 'authUserSentRequest'  "
                            + " when (ur.soconStatusCodeId = 6 and :authUserId = ur.ResponderUserId) then 'authUserReceivedRequest'  "
                            + " when ur.soconStatusCodeId = 9 then 'friends'  "
                            + " when code.code is not null then code.code "
                            + " else  'soConError' end as soconStatusEnhanced, "
                            + " ur.socialconnectionId as socialconnectionId "
                            + " from playdatenow08.user u "
                            + " left join playdatenow08.stateterritory st on u.stateterritory_id = st.id "
                            + " left join "
                            + "(select sc.id as socialconnectionId, sc.soconstatus_code_id as soconStatusCodeId, sc.initiator_user_id as initiatorUserId, sc.responder_user_id as responderUserId , sc.initiator_user_id as relatedUserId from playdatenow08.socialconnection sc where responder_user_id = :authUserId "
                            + " union all "
                            + " select sc.id as socialconnectionId, sc.soconstatus_code_id as soconStatusCodeId, sc.initiator_user_id as initiatorUserId, sc.responder_user_id as responderUserId , sc.responder_user_id as relatedUserId from playdatenow08.socialconnection sc where initiator_user_id = :authUserId "
                            + " ) ur on u.id = ur.relatedUserId"
                            + " left join playdatenow08.code code on ur.soconStatusCodeId = code.id "
                            + " where ur.soconStatusCodeId = 6 and :authUserId = ur.initiatorUserId "
                            + " order by u.id desc"
            , nativeQuery = true)
    List<UserSocialConnectionPjo> userSocialConnectionListSent(Long authUserId);



//    how to fix above:
//
//    the union should say:
//    both groups get InitiatorUserId, ResponderUserId, soConStatusCodeId, a field called "relatedUserId"
//    relatedUserId is the value that is NOT the authUserID
//    group 1 is where initiator = authUserId; in this situation, relatedUserId is the Responder
//    group 2 reverses this: responder = authUserId; in this situation, relatedUserId is the initiator
//
//            once above is done, you can join user table to this unioned table: u.id = relatedUser.relatedUserId
//
//    Then, we can do logic in the case statement, e.g.,
//            if soConStatusCodeId = 12, then 'noRelation'; who's initiator/responder doesn't matter.  alt, we could make the 6/9/11 statements, and otherwise it's a default: 'noRelation
//            if status code = 6 and authUserId = initiatorUserId, then 'authUserSentRequest'
//            if status code = 6 and authUserId = ResponderUserId, then 'authUserReceivedRequest'
//            if status code = 9 then 'friends' who's initiator/responder doesn't matter
//            if status code = 11 and authUserId = BlockerUserId, then 'blockedByAuthUser'
//
//    okay, also need the following:
//
//    somehow the union statement needs to exclude records where ( blocker is  null or (blocker is not null and blocker = authUserID)


// Old is below

//    // (1) all users list
//    @Query(
//            value =
//                    "select "
//                            + " u.id as id, u.about_me as aboutMe, u.address_line1 as addressLine1, u.address_line2 as addressLine2, u.city as city, u.created_at as createdAt, u.email as email, u.first_name as firstName, u.last_name as lastName, u.user_name as userName, u.zip_code as zipCode, u.home_name as homeName, st.full_name as stateName "
//                            + " , case "
//                            + " when u.id = :authUserId then 'authUserRecord' "
//                            + " when ur.relatedUser is null then 'noRelation' "
//                            + " when code.code is not null then code.code "
//                            + " else  'soConError' end as soconStatusEnhanced, "
//                            + " ur.socialconnectionId as socialconnectionId, ur.relationInitiator as relationInitiator"
//                            + " from playdatenow08.user u "
//                            + " left join playdatenow08.stateterritory st on u.stateterritory_id = st.id "
//                            + " left join "
//                            + "(select sc.id as socialconnectionId, sc.userone as relationInitiator, sc.usertwo as relatedUser, sc.soconstatus_code_id as soconStatusCodeId from playdatenow08.socialconnection sc where userone = :authUserId union all select sc.id as socialconnectionId, sc.userone as relationInitiator, sc.userone as relatedUser, sc.soconstatus_code_id as soconStatusCodeId from playdatenow08.socialconnection sc where  usertwo  = :authUserId ) ur on u.id = ur.relatedUser"
//                            + " left join playdatenow08.code code on ur.soconStatusCodeId = code.id "
//                            + " order by u.id desc"
//            , nativeQuery = true)
//    List<UserSocialConnectionPjo> userSocialConnectionList(Long authUserId);

    // (3) relations to authUser: received requests

    @Query(
            value =
                    "select "
                            + " case when u.id = :keyword then 'authUserRecord' when ur.relatedUser is null then 'noRelation' when c.code is not null then c.code else  'soConError' end as soconStatusEnhanced, "
                            + " ur.socialconnectionId as socialconnectionId, ur.relationInitiator as relationInitiator, "
                            + " u.id as id, u.about_me as aboutMe, u.address_line1 as addressLine1, u.address_line2 as addressLine2, u.city as city, u.created_at as createdAt, u.email as email, u.first_name as firstName, u.last_name as lastName, u.user_name as userName, u.zip_code as zipCode, u.home_name as homeName, st.full_name as stateName "

                            + " from (select sc.id as socialconnectionId, sc.userone as relationInitiator, sc.usertwo as relatedUser, sc.soconstatus_code as soconStatusCodeId from playdatenow08.socialconnection sc where userone = :keyword union all select sc.id as socialconnectionId, sc.userone as relationInitiator, sc.userone as relatedUser, sc.soconstatus_code as soconStatusCodeId from playdatenow08.socialconnection sc where usertwo  = :keyword ) ur "
                            + " join playdatenow08.user u on ur.relatedUser = u.id "
                            + " left join playdatenow08.stateterritory st on u.stateterritory_id = st.id "
                            + " left join playdatenow08.code c on ur.soconStatusCodeId = c.id "
                            + " where c.code = 'requestPending' and ur.relationInitiator <> :keyword  "
            , nativeQuery = true)
    List<UserSocialConnectionPjo> userSocialConnectionListReceived(Long keyword);

    // (4) relations to authUser: friends

    @Query(
            value =
                    "select "
                            + " case when u.id = :keyword then 'authUserRecord' when ur.relatedUser is null then 'noRelation' when c.code is not null then c.code else  'soConError' end as soconStatusEnhanced, "
                            + " ur.socialconnectionId as socialconnectionId, ur.relationInitiator as relationInitiator, "
                            + " u.id as id, u.about_me as aboutMe, u.address_line1 as addressLine1, u.address_line2 as addressLine2, u.city as city, u.created_at as createdAt, u.email as email, u.first_name as firstName, u.last_name as lastName, u.user_name as userName, u.zip_code as zipCode, u.home_name as homeName, st.full_name as stateName "

                            + " from (select sc.id as socialconnectionId, sc.userone as relationInitiator, sc.usertwo as relatedUser, sc.soconstatus_code as soconStatusCodeId from playdatenow08.socialconnection sc where userone = :keyword union all select sc.id as socialconnectionId, sc.userone as relationInitiator, sc.userone as relatedUser, sc.soconstatus_code as soconStatusCodeId from playdatenow08.socialconnection sc where usertwo  = :keyword ) ur "
                            + " join playdatenow08.user u on ur.relatedUser = u.id "
                            + " left join playdatenow08.stateterritory st on u.stateterritory_id = st.id "
                            + " left join playdatenow08.code c on ur.soconStatusCodeId = c.id "
                            + " where c.code = 'friends' "
            , nativeQuery = true)
    List<UserSocialConnectionPjo> userSocialConnectionListFriends(Long keyword);

    // (4) relations to authUser: cancelled requests

    @Query(
            value =
                    "select "

                            + " ur.socialconnectionId as socialconnectionId, ur.relationInitiator as relationInitiator, "
                            + " u.id as id, u.about_me as aboutMe, u.address_line1 as addressLine1, u.address_line2 as addressLine2, u.city as city, u.created_at as createdAt, u.email as email, u.first_name as firstName, u.last_name as lastName, u.user_name as userName, u.zip_code as zipCode, u.home_name as homeName, st.full_name as stateName "

                            + " from (select sc.id as socialconnectionId, sc.userone as relationInitiator, sc.usertwo as relatedUser, sc.soconstatus_code as soconStatusCodeId from playdatenow08.socialconnection sc where userone = :keyword union all select sc.id as socialconnectionId, sc.userone as relationInitiator, sc.userone as relatedUser, sc.soconstatus_code as soconStatusCodeId from playdatenow08.socialconnection sc where usertwo  = :keyword ) ur "
                            + " join playdatenow08.user u on ur.relatedUser = u.id "
                            + " left join playdatenow08.stateterritory st on u.stateterritory_id = st.id "
                            + " left join playdatenow08.code c on ur.soconStatusCodeId = c.id "
                            + " where c.code = 'requestCancelled' "
            , nativeQuery = true)
    List<UserSocialConnectionPjo> userSocialConnectionListRequestCancelled(Long keyword);



}

