<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.util.Date"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<jsp:include page="/WEB-INF/include/headCommon.jsp" />

<script src="https://polyfill.io/v3/polyfill.min.js?features=default"></script> <%--without this polyfill, the map won't load.--%>
<script src="https://unpkg.com/axios@1.1.2/dist/axios.min.js"></script> <%-- this enables the geocode that's embedded in initMap function--%>

<script>
    function initMap(callback) {

    // (1) instantiate internal js object composed of kvp, with numbers in the var position, for consumption in downstream activities.
    const latLongObj = {lat: 1, lng: 1}; //

    axios
            // (2) get geocoded values from incoming java variable addy
            .get('https://maps.googleapis.com/maps/api/geocode/json', {
              params: {
                address: "${homeAddy}",
                key: 'AIzaSyBcebr3h87oaEoYNm0ix80FMxuoBzh7nMI'
              }
            }) // end get

            .then(function(response) {
              console.log(response) // log full gma results for analysis

              // (3) display geocode values on the page

              const addyComp = response.data.results[0].address_components[0].long_name; // start of additional analysis/dev we're gonna do in a little bit
              // console.log ("addyComp: ", addyComp);

              const formattedAddy = response.data.results[0].formatted_address;

              const formattedAddyOutput =
                      "<h5 class='card-title text-center'>Home</h5>" +
                      "<p class = 'm-0 text-center'>" + formattedAddy + "</p>" // this works, yay!

              document.getElementById('cardBody1').innerHTML =  formattedAddyOutput;


              // (4) update the js object (creatd in step 1 above), then execute function that builds the map, passing in the js object.  The full function is below axios, and contains the display-on-page functionality.
              latLongObj.lat = response.data.results[0].geometry.location.lat;
              latLongObj.lng = response.data.results[0].geometry.location.lng;

              // (5) run the function that builds the map
              buildMapFromGeo(latLongObj, formattedAddy);

            }) // end then

            .catch(function(error) {
              console.log(error)
            }) // end catch

    function buildMapFromGeo(latLong, formattedAddy, formattedAddyOutput) {


      // (5a) instantiate new map object
      const map1 = new google.maps.Map(document.getElementById(
        "mapCard1"),
        {zoom: 12, center: latLong,}
      );

      // (5b) instantiate/populate array of marker objects
      const markerObjList = [
        {coords: latLong, content: "<p class = 'm-0 text-success text-center'>" + formattedAddy + "</p>" },
        // put more items in here as needed.
      ];

      // (5c) run the addMarker function on all objects in the marker object array
      for (let i= 0; i < markerObjList.length; i++ ) {
        addMarkers(markerObjList[i])
      };

      function addMarkers (props) {
        const marker = new google.maps.Marker ( {
          map: map1,
          position: props.coords,
          content: props.content,
        })

        if(props.content) {
          const infoWindow = new google.maps.InfoWindow({
            content: props.content
          });

          marker.addListener('click', function () {
            infoWindow.open(map1, marker);
          });
        } // end if-props
      };  // end function addMarkers
    } // end function: buildMapFromGeo
  }  // end function: initMap
</script>
</head>

<jsp:include page="/WEB-INF/include/bodyDesign.jsp" />
<jsp:include page="/WEB-INF/include/header.jsp" />
<jsp:include page="/WEB-INF/include/pageLayoutTop.jsp" />

<c:if test="${permissionErrorMsg != null}">
    <div class="alert alert-warning" role="alert">${permissionErrorMsg}</div>
</c:if>

<div id="profileCard" class="card p-3 d-md-flex justify-content-start mt-3">
    <div id="joinedMgmtButtons" class="d-flex justify-content-between">
        <div class="card p-2 border-0">
            <p class="m-0 text-secondary" style="font-size: 0.8rem;">Joined <fmt:formatDate value="${userProfile.createdAt}" pattern="EEEE" />, <fmt:formatDate value="${userProfile.createdAt}" pattern="MMMM dd" />, <fmt:formatDate value="${userProfile.createdAt}" pattern="yyyy" />, <fmt:formatDate value="${userProfile.createdAt}" pattern="h:mm a" /></p>
<%--            <p>userSocialConnectionPjo.socialconnectionId: ${userSocialConnectionPjo.socialconnectionId}</p>--%>
        </div>
        <div>
            <p>authUserIsAdmin: ${authUserIsAdmin}</p>
            <c:if test="${authUserIsAdmin == 1}">
            <a href="/profile/${userProfile.id}/edit"><button class="btn btn-info mb-2">Admin Edit</button></a>
            </c:if>

            <c:choose>
                <c:when test="${userSocialConnectionPjo.soconStatusEnhanced == 'authUserRecord'}">
                    <a href="/profile/${userProfile.id}/edit"><button class="btn btn-primary mb-2">Edit</button></a>
                </c:when>

                <c:when test="${userSocialConnectionPjo.soconStatusEnhanced == 'authUserSentRequest'}">
                    <form:form action='/socialconnection/cancel' method='post' modelAttribute='soConObjForm'>
                        <form:input type="hidden" path="id" value = "${userSocialConnectionPjo.socialconnectionId}"/>
                        <form:input type="hidden" path="objectOrigin" value="profileRecord"/>
                        <button type="submit" class="btn btn-secondary me-2">Cancel Request</button>
                    </form:form>
                </c:when>

                <c:when test="${userSocialConnectionPjo.soconStatusEnhanced == 'authUserReceivedRequest'}">

                    <form:form action='/socialconnection/accept' method='post' modelAttribute='soConObjForm'>
                        <form:input type="hidden" path="id" value = "${userSocialConnectionPjo.socialconnectionId}"/>
                        <form:input type="hidden" path="objectOrigin" value="profileRecord"/>
                        <button type="submit" class="btn btn-primary me-2">Accept</button>
                    </form:form>

                    <form:form action='/socialconnection/decline' method='post' modelAttribute='soConObjForm'>
                        <form:input type="hidden" path="id" value = "${userSocialConnectionPjo.socialconnectionId}"/>
                        <form:input type="hidden" path="objectOrigin" value="profileRecord"/>
                        <button type="submit" class="btn btn-secondary me-2">Decline</button>
                    </form:form>

                </c:when>

                <c:when test="${userSocialConnectionPjo.soconStatusEnhanced == 'friends'}">

<%--                    <form:form action='/socialconnection/unfriend' method='post' modelAttribute='soConObjForm'>--%>
<%--                        <form:input type="hidden" path="id" value = "${userSocialConnectionPjo.socialconnectionId}"/>--%>
<%--                        <button type="submit" class="btn btn-info me-2">Unfriend</button>--%>
<%--                    </form:form>--%>


                    <div class="dropdown d-flex justify-content-end">
                        <button class="btn btn-secondary dropdown-toggle my-3 me-1" type="button" data-bs-toggle="dropdown" aria-expanded="false">
                            Friends
                        </button>
                        <ul class="dropdown-menu dropdown-menu-end">
                            <li>
                                <form:form action='/socialconnection/unfriend' method='post' modelAttribute='soConObjForm'>
                                    <form:input type="hidden" path="id" value="${userSocialConnectionPjo.socialconnectionId}"/>
                                    <form:input type="hidden" path="objectOrigin" value="profileRecord"/>
                                    <button type="submit" class="dropdown-item">Unfriend</button>
                                </form:form>
                            </li>
                        </ul>
                    </div>


                </c:when>

<%--                <c:when test="${record.soconStatusEnhanced == 'blocked'}">--%>
<%--                    <form:form action='/socialconnection/unblock' method='post' modelAttribute='soConObjForm'>--%>
<%--                        <form:input type="hidden" path="id" value = "${record.socialconnectionId}"/>--%>
<%--                        <button type="submit" class="btn btn-warning me-2">Unblock</button>--%>
<%--                    </form:form>--%>
<%--                </c:when>--%>

                <c:otherwise>
                    <form:form action='/socialconnection/request' method='post' modelAttribute='soConObjForm'>
                        <form:input type="hidden" path="responderUser" value="${userSocialConnectionPjo.id}"/>
                        <form:input type="hidden" path="objectOrigin" value="profileRecord"/>
                        <button type="submit" class="btn btn-primary">Add Friend</button>
                    </form:form>
                </c:otherwise>
            </c:choose>

<%--            <c:if test="${userSocialConnectionPjo.soconStatusEnhanced != 'authUserRecord'}">--%>
<%--                <div class="dropdown">--%>
<%--                    <button class="btn btn-secondary dropdown-toggle" type="button" data-bs-toggle="dropdown" aria-expanded="false">Advanced Options</button>--%>
<%--                    <ul class="dropdown-menu">--%>
<%--                        <li>--%>
<%--&lt;%&ndash;                            <a class="dropdown-item" href="#">&ndash;%&gt;--%>
<%--                                <form:form action='/socialconnection/block' method='post' modelAttribute='soConObjForm'>--%>
<%--                                    <form:input type="hidden" path="responderUser" value="${userSocialConnectionPjo.id}"/>--%>
<%--                                    <button type="submit" class="dropdown-item">Block</button>--%>
<%--                                </form:form>--%>
<%--&lt;%&ndash;                            </a>&ndash;%&gt;--%>
<%--                        </li>--%>
<%--&lt;%&ndash;                        <li><a class="dropdown-item" href="#">Another action</a></li>&ndash;%&gt;--%>
<%--&lt;%&ndash;                        <li><a class="dropdown-item" href="#">Something else here</a></li>&ndash;%&gt;--%>
<%--&lt;%&ndash;                        <li><button class="dropdown-item" type="button">Another action</button></li>&ndash;%&gt;--%>
<%--                    </ul>--%>
<%--                </div>--%>
<%--            </c:if>--%>
        </div>
    </div><!-- end joinedMgmtButtons -->

    <div class="row mt-3">
        <div id="playdateInfoCol" class="col">

<%--            <div class="card p-2 border-0">--%>
<%--                <p class="m-0 text-secondary" style="font-size: 0.8rem;">soconStatusEnhanced</p>--%>
<%--                <p class="m-0">${userSocialConnectionPjo.soconStatusEnhanced}</p>--%>
<%--            </div>--%>

            <div class="card p-2 border-0">
                <p class="m-0 text-secondary" style="font-size: 0.8rem;">First name</p>
                <p class="m-0">${userProfile.firstName}</p>
            </div>

            <div class="card p-2 border-0">
                <p class="m-0 text-secondary" style="font-size: 0.8rem;">Last name</p>
                <p class="m-0">${userProfile.lastName}</p>
            </div>

            <div class="card p-2 border-0">
                <p class="m-0 text-secondary" style="font-size: 0.8rem;">Username</p>
                <p class="m-0">${userProfile.userName}</p>
            </div>

            <div class="card p-2 border-0">
                <p class="m-0 text-secondary" style="font-size: 0.8rem;">Home Name</p>
                <p class="m-0">${userProfile.homeName}</p>
            </div>

            <div class="card p-2 border-0">
                <p class="m-0 text-secondary" style="font-size: 0.8rem;">About me</p>
                <pre style="white-space: pre-wrap">${userProfile.aboutMe}</pre>
            </div>

            <div class="card p-2 border-0">
                <p class="m-0 text-secondary" style="font-size: 0.8rem;">Email</p>
                <p class="m-0">${userProfile.email}</p>
            </div>

        </div><!-- end playdateInfoCol -->

        <div id="homeEtcCol" class="col">

            <div class="card p-2 border-0">
                <p class="m-0 text-secondary" style="font-size: 0.8rem;">Home address</p>
                <p class="m-0">${homeAddy}</p>
            </div>

            <div class="card">
                <div id="cardBody1" class="card-body"></div> <!-- end cardBody1 -->
                <div id="mapCard1" class="card p-2 m-2 border-0" style="height: 20rem"></div> <!-- end mapCard1 -->
            </div> <!-- end card -->

            <c:if test="${userSocialConnectionPjo.soconStatusEnhanced != 'authUserRecord'}">
                <div class="dropdown d-flex justify-content-end">
                    <button class="btn btn-secondary dropdown-toggle my-3 me-1" type="button" data-bs-toggle="dropdown" aria-expanded="false">Advanced Options</button>
                    <ul class="dropdown-menu">
                        <li>
                            <form:form action='/socialconnection/block' method='post' modelAttribute='soConObjForm'>
                                <form:input type="hidden" path="responderUser" value="${userSocialConnectionPjo.id}"/>
                                <button type="submit" class="dropdown-item">Block</button>
                            </form:form>
                        </li>
                    </ul>
                </div>
            </c:if>

        </div><!-- end homeEtcCol -->
    </div><!-- end row -->
</div><!-- end profileCard -->

<div id="userEventsCard" class="card p-3 mt-3 d-md-flex justify-content-start">

    <h3>
        <c:choose>
            <c:when test="${authUser.id == userProfile.id}">My Organized Playdates</c:when>
            <c:otherwise>Organized Playdates</c:otherwise>
        </c:choose>
    </h3>
    <ul class="nav nav-tabs">
        <li class="nav-item"><a class="nav-link active" data-bs-toggle="tab" aria-current="page" href="#userEventsUpcoming">Upcoming</a></li>
        <li class="nav-item"><a class="nav-link" data-bs-toggle="tab" href="#userEventsPast">Previous</a></li>
    </ul>

    <div class="tab-content">
        <div class="tab-pane container active" id="userEventsUpcoming">

        <table class="table table-striped table-hover table-responsive mt-2">
            <thead>
                <tr>
                    <th scope="col">Event</th>
                    <th scope="col">Status</th>
                    <th scope="col">Date/Time</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="record" items="${userHostedPlaydateListCurrentPlus}">
                <tr>
                    <td>
                        <a class="text-decoration-none" href="/playdate/${record.id}">
                        <c:choose>
                            <c:when test="${record.eventName.length() == 0}">Playdate @ ${record.locationName}</c:when>
                            <c:otherwise>${record.eventName} @ ${record.locationName}</c:otherwise>
                        </c:choose>
                        </a>
                    </td>
<%--                    <td>${record.eventStatuslookup.displayValue}--%>
                    <td>${record.playdateStatus.displayValue}
                    <td><fmt:formatDate value="${record.eventDate}" pattern="MMMM dd" />, <fmt:formatDate value="${record.eventDate}" pattern="yyyy" /> @${record.startTimeTxt}</td>
                </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>

    <div class="tab-pane container fade" id="userEventsPast">
        <table class="table table-striped table-hover table-responsive mt-2">
            <thead>
                <tr>
                    <th scope="col">Event</th>
                    <th scope="col">Status</th>
                    <th scope="col">Date/Time</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="record" items="${userHostedPlaydateListPast}">
                    <tr>
                        <td>
                            <a class="text-decoration-none" href="/playdate/${record.id}">
                            <c:choose>
                            <c:when test="${record.eventName.length() == 0}">
                            Playdate @ ${record.locationName}
                            </c:when>
                            <c:otherwise>
                            ${record.eventName} @ ${record.locationName}
                            </c:otherwise>
                            </c:choose>
                            </a>
                        </td>
                        <td>${record.playdateStatus.displayValue}
                        <td><fmt:formatDate value="${record.eventDate}" pattern="MMMM dd" />, <fmt:formatDate value="${record.eventDate}" pattern="yyyy" /> @${record.startTimeTxt}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

    </div>
    </div>
</div> <!-- end userEventsCard -->

<jsp:include page="/WEB-INF/include/pageLayoutBottomCommon.jsp" />

<script
  src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBcebr3h87oaEoYNm0ix80FMxuoBzh7nMI&callback=initMap"
  defer
></script>


</body>
</html>