package com.f12s.playdatenow08.repositories;


import com.f12s.playdatenow08.models.PlaydateMdl;
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
                    " select sc.* from playdatenow08.socialconnection sc where sc.userone = :incomingUserA and sc.usertwo = :incomingUserB "
                    + " union all "
                    + " select sc.* from playdatenow08.socialconnection sc where sc.userone = :incomingUserB and sc.usertwo = :incomingUserA "
            , nativeQuery = true)
    SocialconnectionMdl existingSoConBaby(Long incomingUserA, Long incomingUserB);


//    List<SocialconnectionMdl> existsSocialconnectionMdlByUseroneUserMdlAndUsertwoUserMdl(Long keyword);


}
