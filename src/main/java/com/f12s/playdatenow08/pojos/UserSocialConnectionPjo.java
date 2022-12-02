package com.f12s.playdatenow08.pojos;

import java.util.Date;

public interface UserSocialConnectionPjo {
//    Integer getStatusRelationToAuthUser();
//    String getSoconStatus();
//    Integer getSoconStatusCodeId();
    String getSoconStatusEnhanced();
    Integer getSocialconnectionId();
    Integer getRelationInitiator();
    // below are all of the userMdl fields
    Integer getId();
    String getAboutMe();
    String getAddressLine1();
    String getAddressLine2();
    String getCity();
    String getStateName();
    String getZipCode();
    Date getCreatedAt();
    String getEmail();
    String getFirstName();
    String getLastName();
    String getUserName();
    String getHomeName();

} // end pojo


//package com.f12s.playdatenow08.pojos;
//
//public interface UserSocialConnectionPjo {
//}
