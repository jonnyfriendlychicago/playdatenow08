package com.f12s.playdatenow08.repositories;

import com.f12s.playdatenow08.models.SocialconnectionMdl;
import com.f12s.playdatenow08.pojos.UserSocialConnectionPjo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SocialconnectionRpo extends CrudRepository<SocialconnectionMdl, Long> {

    // 2022.12.05: trying to get "find soCon if it exists

    @Query(
            value=
                    " select "
                            + "sc.* from playdatenow08.socialconnection sc where sc.initiator_user_id = :incomingUserA and sc.responder_user_id = :incomingUserB "
                            + " union all "
                            + " select sc.* from playdatenow08.socialconnection sc where sc.initiator_user_id = :incomingUserB and sc.responder_user_id = :incomingUserA "
            , nativeQuery = true)
    SocialconnectionMdl existingSoConBaby(Long incomingUserA, Long incomingUserB);

    // below, checker for soCon

    @Query(
            value=
                    " select "
                            + "sc.* from playdatenow08.socialconnection sc where sc.id = :soConIdFromForm "
            , nativeQuery = true)
    SocialconnectionMdl soConFromForm(Long soConIdFromForm);

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
                            + " else  'soConError' end as soconStatusEnhanced, "
                            + " ur.socialconnectionId as socialconnectionId "
                            + " from playdatenow08.user u "
                            + " left join playdatenow08.stateterritory st on u.stateterritory_id = st.id "
                            + " left join "
                            + "(select sc.id as socialconnectionId, sc.soconstatus_code_id as soconStatusCodeId, sc.initiator_user_id as initiatorUserId, sc.responder_user_id as responderUserId , sc.initiator_user_id as relatedUserId from playdatenow08.socialconnection sc where responder_user_id = :authUserId and initiator_user_id = :userProfileId "
                            + " union all "
                            + " select sc.id as socialconnectionId, sc.soconstatus_code_id as soconStatusCodeId, sc.initiator_user_id as initiatorUserId, sc.responder_user_id as responderUserId , sc.responder_user_id as relatedUserId from playdatenow08.socialconnection sc where responder_user_id = :userProfileId and initiator_user_id = :authUserId "
                            + " ) ur on u.id = ur.relatedUserId"
                            + " left join playdatenow08.code code on ur.soconStatusCodeId = code.id "
                            + " where u.id = :userProfileId "
            , nativeQuery = true)
    UserSocialConnectionPjo getOneUserSocialConnectionPjo(Long authUserId, Long userProfileId);

} // endRpo
