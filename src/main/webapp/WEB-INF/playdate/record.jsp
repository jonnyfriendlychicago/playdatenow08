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
    <div class="alert alert-warning mt-3" role="alert">${permissionErrorMsg}</div>
</c:if>

<c:if test="${successMsg != null}">
    <div class="alert alert-success mt-3" role="success">${successMsg}</div>
</c:if>

<div id="playdateCard" class="card p-3 d-md-flex justify-content-start mt-3">
    <div id="creationOrganizerButtons" class="d-flex justify-content-between"> <!-- this should be a row, get back to it -->

        <div id="creatorOrganizer" class="card p-2 border-0">
            <p class="m-0 text-secondary" style="font-size: 0.8rem;">
                Created by
                <c:choose>
                    <c:when test="${playdate.userMdl.id == authUser.id}">you (${playdate.userMdl.userName})</c:when>
                    <c:otherwise>${playdate.userMdl.userName}</c:otherwise>
                </c:choose>
                on <fmt:formatDate value="${playdate.createdAt}" pattern="EEEE" />, <fmt:formatDate value="${playdate.createdAt}" pattern="MMMM dd" />, <fmt:formatDate value="${playdate.createdAt}" pattern="yyyy" />, <fmt:formatDate value="${playdate.createdAt}" pattern="h:mm a" />
            </p>
            <c:if test="${playdate.userMdl.id == authUser.id}">
                <p class="m-0 text-secondary">You are the organizer of this event.</p>
            </c:if>
        </div>

        <div>
            <c:if test="${authUserIsAdmin == 1}">
                <a href="/playdate/${playdate.id}/edit"><button class="btn btn-info mb-2">Playdate Edit(Admin)</button></a>
            </c:if>

            <c:if test="${playdate.userMdl.id == authUser.id}">
                <a href="/playdate/${playdate.id}/edit"><button class="btn btn-primary mb-2">Edit Playdate</button></a>
            </c:if>
        </div>

    </div><!-- end creationOrganizerButtons -->

    <div class="row mt-3" id="playdateInfoRow">
        <div id="playdateInfoCol" class="col">

            <c:if test="${playdate.playdateStatus.code == 'pending' || playdate.playdateStatus.code == 'cancelled'}" >
                <div class="card p-2 m-0 border-0
                <c:choose>
                    <c:when test="${playdate.playdateStatus.code == 'pending'}">
                    bg-warning
                    </c:when>
                    <c:otherwise>
                    bg-danger text-light
                    </c:otherwise>
                </c:choose>
                ">
                    <p class="m-0 ">Playdate Status: <strong>${playdate.playdateStatus.displayValue} </strong> </p>
                </div>
            </c:if>

            <div class="card p-2 m-0 border-0">
                <c:choose>
                    <c:when test="${playdate.eventName.length() > 0}">
                        <p class="m-0 display-6">${playdate.eventName} @</p>
                    </c:when>
                    <c:otherwise>
                        <p class="m-0 display-6">Playdate @</p>
                    </c:otherwise>
                </c:choose>

                <c:choose>
                    <c:when test="${playdate.locationType.code == 'otherLocation'}">
                        <p class="m-0 display-6" >${playdate.locationName}</p>
                        <p class="m-0 " >${playdate.addressLine1}</p>
                        <p class="m-0 " >${playdate.city}, ${playdate.stateterritoryMdl.abbreviation}</p>
                    </c:when>
                    <c:otherwise>
                        <p class="m-0 display-6" >
                            <c:choose>
                                <c:when test="${playdate.userMdl.homeName.length() > 0}">
                                    ${playdate.userMdl.homeName}
                                </c:when>
                                <c:otherwise>
                                    ${playdate.userMdl.userName}
                                </c:otherwise>
                            </c:choose>
                        </p>
                        <p class="m-0 " >${playdate.userMdl.addressLine1}</p>
                        <p class="m-0 " >${playdate.userMdl.city}, ${playdate.userMdl.stateterritoryMdl.abbreviation}</p>
                    </c:otherwise>
                </c:choose>
                <fmt:formatDate value="${playdate.eventDate}" pattern="EEEE" />, <fmt:formatDate value="${playdate.eventDate}" pattern="MMMM dd" />, <fmt:formatDate value="${playdate.eventDate}" pattern="yyyy" />
                <p class="m-0">${playdate.startTimeTxt}</p>
            </div>

            <c:choose>
                <c:when test="${playdate.userMdl.id != authUser.id}">
                    <div class="card p-2 m-0 border-0">
                        <p class="m-0 text-secondary" style="font-size: 0.8rem;">Organizer</p>
                        <p class="m-0">${playdate.userMdl.userName}</p>
                    </div>
                </c:when>
                <c:otherwise>
                </c:otherwise>
            </c:choose>

            <div class="card p-2 border-0">
                <p class="m-0 text-secondary" style="font-size: 0.8rem;">Details/Description</p>
                <pre style="white-space: pre-wrap">${playdate.eventDescription}</pre>
            </div>

        </div><!-- end playdateInfoCol -->

        <div id="rsvpEtcCol" class="col">

            <div id="rsvpTrackingCard" class="card p-3 d-md-flex justify-content-start mb-3">
                <p class="m-0 text-secondary text-center">RSVP Tracking</p>
                <table class="table table-responsive mt-2 table-borderless table-sm">
                    <thead class="table-light align-top">
                    <tr>
                        <th scope="col">RSVPs</th>
                        <th scope="col">RSVPed Adults</th>
                        <th scope="col">Max Kids</th>
                        <th scope="col">RSVPed Kids</th>
                        <th scope="col">Open Kid Spots</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td>${rsvpCount}</td>
                        <td>${aggAdultsCount}</td>
                        <td>${playdate.maxCountKids}</td>
                        <td>${aggKidsCount}</td>
                        <td>${openKidsSpots}</td>
                    </tr>
                    </tbody>
                </table>
            </div>

            <div class="card p-2 border-0">
                <p class="m-0 text-secondary" style="font-size: 0.8rem;">Home address</p>
                <p class="m-0">${homeAddy}</p>
            </div>

            <div class="card">
                <div id="cardBody1" class="card-body"></div> <!-- end cardBody1 -->
                <div id="mapCard1" class="card p-2 m-2 border-0" style="height: 20rem"></div> <!-- end mapCard1 -->
            </div> <!-- end card -->

        </div><!-- end rsvpEtcCol -->
    </div><!-- end row -->

    <div class="row m-1" id="rsvpRow">
        <div id="rsvpCard" class="card p-3 d-md-flex justify-content-start bg-secondary ">

            <c:choose>
                <c:when test="${authUser.id == playdate.userMdl.id}">

                    <p class="text-center text-light fs-3">Your RSVP</p>

                    <div class = "container m-0 p-0">
                        <div class="row m-0 p-0">

                            <div class="col-sm me-5 p-0 mb-3" >
                                <div class="card p-2 m-0 border-0">
                                    <p class="m-0 text-secondary" style="font-size: 0.8rem;">Status</p>
                                    <p class="m-0">${playdate.rsvpStatus}</p>
                                </div>
                            </div>

                            <div class="col-sm me-5 p-0 mb-3" >
                                <div class="card p-2 m-0 border-0">
                                    <p class="m-0 text-secondary" style="font-size: 0.8rem;">playdateOrganizerRsvpStatus</p>
                                    <p class="m-0">${playdate.playdateOrganizerRsvpStatus.displayValue}</p>
                                </div>
                            </div>

                            <div class="col-sm me-5 p-0 mb-3">
                                <div class="card p-2 border-0">
                                    <p class="m-0 text-secondary" style="font-size: 0.8rem;"># of Kids</p>
                                    <c:choose>
                                        <c:when test="${playdate.kidCount == null}">
                                            <p>we got a null here</p>
                                        </c:when>
                                        <c:otherwise>
                                            <p class="m-0">${playdate.kidCount}</p>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>

                            <div class="col-sm p-0" >
                                <div class="card p-2 border-0">
                                    <p class="m-0 text-secondary" style="font-size: 0.8rem;"># of Adults</p>
                                    <p class="m-0">${playdate.adultCount}</p>
                                </div>
                            </div>
                        </div> <!-- end row -->
                    </div> <!-- end container -->

                </c:when>

                <c:when test="${rsvpExistsCreatedByAuthUser}">
                    <div class="row mb-3">
                        <div class="col">
                        </div>

                        <div class="col">
                            <p class="m-0 text-center text-light fs-3" style="font-size: 1.25rem;">Your RSVP</p>
                        </div>

                        <div class="col d-flex justify-content-end">
                            <a href="/playdate/${playdate.id}/rsvp/${rsvpObjForAuthUser.id}/edit"><button class="btn btn-primary mb-2">Edit</button></a>
                        </div>
                    </div>

                    <div class = "container m-0 p-0">
                        <div class="row m-0 p-0">

                            <div class="col-sm me-5 p-0 mb-3" >
                                <div class="card p-2 m-0 border-0">
                                    <p class="m-0 text-secondary" style="font-size: 0.8rem;">Status</p>
                                    <p class="m-0">${rsvpObjForAuthUser.rsvpStatus}</p>
                                </div>
                            </div>

                            <div class="col-sm me-5 p-0 mb-3" >
                                <div class="card p-2 m-0 border-0">
                                    <p class="m-0 text-secondary" style="font-size: 0.8rem;">respondentRsvpStatus</p>
                                    <p class="m-0">${rsvpObjForAuthUser.respondentRsvpStatus.displayValue}</p>
                                </div>
                            </div>

                            <div class="col-sm me-5 p-0 mb-3">
                                <div class="card p-2 border-0">
                                    <p class="m-0 text-secondary" style="font-size: 0.8rem;"># of Kids</p>
                                    <p class="m-0">${rsvpObjForAuthUser.kidCount}</p>
                                </div>
                            </div>

                            <div class="col-sm p-0" >
                                <div class="card p-2 border-0">
                                    <p class="m-0 text-secondary" style="font-size: 0.8rem;"># of Adults</p>
                                    <p class="m-0">${rsvpObjForAuthUser.adultCount}</p>
                                </div>
                            </div>
                        </div> <!-- end row -->
                    </div> <!-- end container -->

                    <div class="card p-2 border-0 bg-secondary">
                        <p class="m-0 text-light text-end" style="font-size: 0.8rem;">You RSVPed on <fmt:formatDate value="${rsvpObjForAuthUser.createdAt}" pattern="EEEE" />, <fmt:formatDate value="${rsvpObjForAuthUser.createdAt}" pattern="MMMM dd" />, <fmt:formatDate value="${rsvpObjForAuthUser.createdAt}" pattern="yyyy" />, <fmt:formatDate value="${rsvpObjForAuthUser.createdAt}" pattern="h:mm a" /></p>
                    </div>

                </c:when>

                <c:otherwise>

                    <p class="text-center text-light fs-3">Add your RSVP</p>

                    <form:form action='/playdate/${playdate.id}/rsvp/create' method='post' modelAttribute='rsvp'>

                        <div class = "container m-0 p-0">
                            <div class="row m-0 p-0">
                                <div class="form-floating col-sm me-5 p-0 ">
                                    <form:select
                                            path="rsvpStatus"
                                            class="form-control"
                                            id="rsvpStatus" placeholder="rsvpStatus">
                                        <form:option value="In" path="rsvpStatus">In</form:option>
                                        <form:option value="Maybe" path="rsvpStatus">Maybe</form:option>
                                        <form:option value="Out" path="rsvpStatus">Out</form:option>
                                    </form:select>
                                    <form:label path="rsvpStatus" for="rsvpStatus">Status</form:label>
                                    <p class="text-danger bg-light"><form:errors path="rsvpStatus" />
                                </div>

                                <div class="form-floating col-sm me-5 p-0">
                                    <form:select
                                            path="respondentRsvpStatus"
                                            class="form-control"
                                            id="respondentRsvpStatus"
                                            placeholder="respondentRsvpStatus">
                                        <c:forEach items="${playdateRsvpStatusList}" var="record">
                                            <c:choose>
                                                <c:when test="${record == rsvp.respondentRsvpStatus}">
                                                    <form:option value="${record.id}" path="respondentRsvpStatus" selected="true">${record.displayValue}</form:option>
                                                </c:when>
                                                <c:otherwise>
                                                    <form:option value="${record.id}" path="respondentRsvpStatus">${record.displayValue}</form:option>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:forEach>
                                    </form:select>
                                    <form:label path="respondentRsvpStatus" for="respondentRsvpStatus">respondentRsvpStatus</form:label>
                                    <p class="text-danger bg-light"><form:errors path="respondentRsvpStatus" />
                                </div>

<%--                                            min="1"--%>
<%--                                            value="1" --%>
                                <div class="form-floating col-sm me-5 p-0">
                                    <form:input
                                            path="kidCount"
                                            type="number"
                                            class="form-control"
                                            id="kidCount"
                                            placeholder="kidCount"
                                            min="0"
                                            step="1"
                                    />
                                    <form:label path="kidCount" for="kidCount"># of Kids</form:label>
                                    <p class="text-danger bg-light"><form:errors path="kidCount" />
                                </div>

<%--                                            min="1"--%>
<%--                                            value="1" --%>
                                <div class="form-floating col-sm p-0">
                                    <form:input
                                            path="adultCount"
                                            type="number"
                                            class="form-control"
                                            id="adultCount"
                                            placeholder="adultCount"
                                            min="0"
                                            step="1"
                                    />
                                    <form:label path="adultCount" for="adultCount"># of Adults</form:label>
                                    <p class="text-danger bg-light"><form:errors path="adultCount" />
                                </div>
                            </div> <!-- end row -->
                        </div> <!-- end container -->

                        <div>
                            <button type="submit" class="btn btn-primary w-100">Save My Rsvp</button>
                        </div>

                    </form:form>

                </c:otherwise>
            </c:choose>

        </div> <!-- end rsvpCard -->
    </div > <!--end rsvpRow> -->

    <div id="rsvpListRow" class="row m-1 table-responsive">

        <table class="table table-striped table-hover table-responsive mt-2 caption-top">
            <caption class="text-dark" style="font-size: 1.5rem;">Rsvp List</caption>
            <thead class="border-top-0">
            <tr>
                <th scope="col">Name</th>
                <th scope="col">Status</th>
                <th scope="col"># of Kids</th>
                <th scope="col"># of Adults</th>
                <c:if test="${authUser.id == playdate.userMdl.id}">
                    <th scope="col">Manage</th>
                </c:if>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="record" items="${playdateRsvpList}">
                <tr>
                    <td><a class="text-decoration-none" href="/profile/${record.userId}">${record.userName}</a></td>
                    <td>${record.rsvpStatus}</td>
                    <td>${record.kidCount}</td>
                    <td>${record.adultCount}</td>
                    <c:if test="${authUser.id == playdate.userMdl.id}">
                        <c:choose>
                            <c:when test="${playdate.userMdl.id == record.userId}">
                                <td>what are you thinking?</td>
                            </c:when>
                            <c:otherwise>
                                <td>
                                    <a href="/rsvp/delete/${record.rsvpId}"><button class="btn btn-danger mb-2">Delete </button></a>
                                </td>
                            </c:otherwise>
                        </c:choose>
                    </c:if>
                </tr>
            </c:forEach>
            </tbody>
        </table>

    </div><!-- end rsvpListRow -->

<%--    this is for exploration only--%>

    <div id="userFriendList" class="row m-1 table-responsive">

        <table class="table table-striped table-hover table-responsive mt-2 caption-top">
            <caption class="text-dark" style="font-size: 1.5rem;">userFriendForPlaydateInviteDropdownList</caption>
            <thead class="border-top-0">
            <tr>
                <th scope="col">Name</th>
                <th scope="col">UserId</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="record" items="${userFriendForPlaydateInviteDropdownList}">
                <tr>
                    <td><a class="text-decoration-none" href="/profile/${record.id}">${record.userName}</a></td>
                    <td>${record.id}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>

    </div><!-- end userFriendList -->

    <div class="row m-1" id="rsvpRowTwo">
        <div id="rsvpCard" class="card p-3 d-md-flex justify-content-start bg-secondary ">

            <p class="text-center text-light fs-3">Create RSVP for someone else</p>

            <form:form action='/playdate/${playdate.id}/rsvp/createforuser' method='post' modelAttribute='rsvp'>

                <div class = "container m-0 p-0">
                    <div class="row m-0 p-0">
                        <div class="form-floating col-sm me-5 p-0 ">
                            <form:select
                                    path="rsvpStatus"
                                    class="form-control"
                                    id="rsvpStatus"
                                    placeholder="rsvpStatus">
                                <form:option value="In" path="rsvpStatus">In</form:option>
                                <form:option value="Maybe" path="rsvpStatus">Maybe</form:option>
                                <form:option value="Out" path="rsvpStatus">Out</form:option>
                            </form:select>
                            <form:label path="rsvpStatus" for="rsvpStatus">Status</form:label>
                            <p class="text-danger"><form:errors path="rsvpStatus" />
                        </div>

                        <div class="form-floating col-sm me-5 p-0 ">
                            <form:select
                                    path="userMdl"
                                    class="form-control"
                                    id="userMdl"
                                    placeholder="userMdl">
                                <c:forEach items="${userFriendForPlaydateInviteDropdownList}" var="record">
                                    <form:option value="${record.id}" path="userMdl">${record.userName}</form:option>
                                </c:forEach>
                            </form:select>
                            <form:label path="userMdl" for="userMdl">userMdl</form:label>
                            <p class="text-danger"><form:errors path="userMdl" />
                        </div>

                        <div class="form-floating col-sm me-5 p-0">
                            <form:input
                                    path="kidCount"
                                    type="number"
                                    class="form-control"
                                    id="kidCount"
                                    placeholder="kidCount"
                                    min="0"
                                    step="1"
                                    value="1" />
                            <form:label path="kidCount" for="kidCount"># of Kids</form:label>
                            <p class="text-danger"><form:errors path="kidCount" />
                        </div>

                        <div class="form-floating col-sm p-0">
                            <form:input
                                    path="adultCount"
                                    type="number"
                                    class="form-control"
                                    id="adultCount"
                                    placeholder="adultCount"
                                    min="0"
                                    step="1"
                                    value="1" />
                            <form:label path="adultCount" for="adultCount"># of Adults</form:label>
                            <p class="text-danger"><form:errors path="adultCount" />
                        </div>
                    </div> <!-- end row -->
                </div> <!-- end container -->

                <div>
                    <button type="submit" class="btn btn-primary w-100">Invite</button>
                </div>

            </form:form>

        </div> <!-- end rsvpCard -->
    </div > <!--end rsvpRowTwo> -->



</div><!-- end playdateCard -->

<jsp:include page="/WEB-INF/include/pageLayoutBottomCommon.jsp" />

<script
        src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBcebr3h87oaEoYNm0ix80FMxuoBzh7nMI&callback=initMap"
        defer
></script>

</body>
</html>
