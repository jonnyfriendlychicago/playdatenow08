<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.util.Date"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<%--<jsp:include page="/WEB-INF/include/head.jsp" />--%>
<jsp:include page="/WEB-INF/include/headCommon.jsp" />

<%--<script>--%>
<%--    function myFunction() {--%>
<%--        document.getElementById("conditionalFieldContainer1").innerHTML = "Paragraph changed.";--%>
<%--    }--%>
<%--</script>--%>

<%--above works, now trying below--%>
<%--below is a mess, can't handle form:input stuff--%>

<%--<script>--%>
<%--    function myFunction(locationTypeSelector) {--%>

<%--        if (locationTypeSelector == 1){--%>
<%--            document.getElementById("conditionalFieldContainer1").innerHTML = "<h1>selector 1 clicked.</h1>";--%>
<%--        } else {--%>
<%--            document.getElementById("conditionalFieldContainer1").innerHTML =--%>
<%--            '<div class="form-floating mb-3">' +--%>
<%--            '<form:input path="locationName" type="text" class="form-control" id="locationName" placeholder="locationName" />' +--%>
<%--            '<form:label path="locationName" for="locationName">Location Name</form:label>' +--%>
<%--            '</div>';--%>
<%--        }--%>
<%--    }--%>
<%--</script>--%>

<%--below is attempt to use hidden on/off--%>
<script>
    function myFunction(locationTypeSelector) {

        if (locationTypeSelector == 1){
            document.getElementById("locationNameContainer").removeAttribute("class");
            document.getElementById("locationNameContainer").setAttribute("class", "form-floating mb-3 collapse");

            document.getElementById("locationAddyContainer").removeAttribute("class");
            document.getElementById("locationAddyContainer").setAttribute("class", "form-floating mb-3 collapse");
        } else {
            document.getElementById("locationNameContainer").removeAttribute("class");
            document.getElementById("locationNameContainer").setAttribute("class", "form-floating mb-3");

            document.getElementById("locationAddyContainer").removeAttribute("class");
            document.getElementById("locationAddyContainer").setAttribute("class", "form-floating mb-3");
        }
    }
</script>


</head>

<jsp:include page="/WEB-INF/include/bodyDesign.jsp" />
<jsp:include page="/WEB-INF/include/header.jsp" />
<jsp:include page="/WEB-INF/include/pageLayoutTop.jsp" />

<c:if test="${validationErrorMsg != null}">
  <div class="alert alert-danger" role="alert">${validationErrorMsg}</div>
</c:if>
<div id="playdateCard" class="card p-3 d-md-flex justify-content-start">
  <div id="creatorOrganizerTopButton" class="d-flex justify-content-end">
    <a href="/playdate"><button class="btn btn-secondary mb-2">Cancel</button></a>
  </div>

  <form:form action='/playdate/new' method='post' modelAttribute='playdate'>

    <div id="eventDeetsAndRsvpRow" class="row mt-3">
      <div id="playdateInfoCol" class="col">

<%--        <div class="form-floating mb-3">--%>
<%--          <form:select path="eventStatus" class="form-control" id="eventStatus" placeholder="eventStatus">--%>
<%--            <form:option value="It's on" path="eventStatus">It's on</form:option>--%>
<%--            <form:option value="Canceled" path="eventStatus">Canceled</form:option>--%>
<%--            <form:option value="Pending" path="eventStatus">Pending</form:option>--%>
<%--          </form:select>--%>
<%--          <form:label path="eventStatus" for="eventStatus">Playdate status</form:label>--%>
<%--          <p class="text-danger"><form:errors path="eventStatus" />--%>
<%--        </div>--%>

  <div class="form-floating mb-3">
    <form:select
            path="playdateStatus"
            class="form-control"
            id="playdateStatus"
            placeholder="playdateStatus">
<%--      <form:option value="0" path="playdateStatus">(none)</form:option>--%>
      <c:forEach items="${playdateStatusList}" var="record">
        <c:choose>
          <c:when test="${record == playdate.playdateStatus}">
            <form:option value="${record.id}" path="playdateStatus"
                         selected="true">${record.displayValue}</form:option>
          </c:when>
          <c:otherwise>
            <form:option value="${record.id}" path="playdateStatus">${record.displayValue}</form:option>
          </c:otherwise>
        </c:choose>
      </c:forEach>
    </form:select>
    <form:label path="playdateStatus" for="playdateStatus">Playdate Status</form:label>
    <p class="text-danger"><form:errors path="playdateStatus" />
  </div>

  <div class="form-floating mb-3">
          <form:input
                  path="eventName"
                  type="text"
                  class="form-control"
                  id="eventName"
                  placeholder="eventName" />
          <form:label path="eventName" for="playdateName">Playdate Name</form:label>
          <div id="eventNameHelp" class="form-text">If left blank, your event will be listed as "Playdate." Suggestion: enter a descriptive name (e.g., arts & crafts, bike ride, scooters, legos, etc.).</div>
          <p class="text-danger"><form:errors path="eventName" />
  </div>

<%--    <div class="form-floating mb-3">--%>
<%--        <form:select--%>
<%--                path="locationType"--%>
<%--                class="form-control"--%>
<%--                id="locationType"--%>
<%--                placeholder="locationType">--%>
<%--            <c:forEach items="${locationTypeList}" var="record">--%>
<%--                <c:choose>--%>
<%--                    <c:when test="${record == playdate.locationType}">--%>
<%--                        <form:option value="${record.id}" path="locationType"--%>
<%--                                     selected="true">${record.displayValue}</form:option>--%>
<%--                    </c:when>--%>
<%--                    <c:otherwise>--%>
<%--                        <form:option value="${record.id}" path="locationType">${record.displayValue}</form:option>--%>
<%--                    </c:otherwise>--%>
<%--                </c:choose>--%>
<%--            </c:forEach>--%>
<%--        </form:select>--%>
<%--        <form:label path="locationType" for="locationType">Location Type</form:label>--%>
<%--        <p class="text-danger"><form:errors path="locationType" />--%>
<%--    </div>--%>

<%--    above is original drop-down list--%>
<%--    <div>--%>
<%--        <form:label path="locationType">Location Type: </form:label>--%>
<%--        <form:radiobutton path="locationType" value="1" label="one redone B" />--%>
<%--        <form:radiobutton path="locationType" value="2" label="two baby" />--%>
<%--    </div>--%>

<%--    above works, but isnt' styled, not sure how--%>
<%--    below is my attempt to style above... AND IT WORKS!!!!--%>

<%--        <div id = "collectionOfRadios">--%>
<%--            <form:label path="locationType">Location Type: </form:label>--%>
<%--            <div class="form-check">--%>
<%--                <form:radiobutton--%>
<%--                        path="locationType"--%>
<%--                        class="form-check-input"--%>
<%--                        id="1"--%>
<%--                        value="1"--%>
<%--                        label="one redone"--%>
<%--                />--%>
<%--            </div>--%>
<%--            <div class="form-check">--%>
<%--                <form:radiobutton--%>
<%--                        path="locationType"--%>
<%--                        class="form-check-input"--%>
<%--                        id="2"--%>
<%--                        value="2"--%>
<%--                        label="two baby"--%>
<%--                        checked="true"--%>
<%--                />--%>
<%--            </div>--%>
<%--       </div> &lt;%&ndash; end collectionOfRadios&ndash;%&gt;--%>

<%--    NEXT STEP: make above work as a jsp loop for the values!   insert a bunch of records in the code table to see if/how this list wraps--%>

    <div id = "collectionOfRadios"  class = "mb-3">
        <form:label path="locationType">Location Type: </form:label>
        <p class="text-danger"><form:errors path="locationType" />
            <c:forEach items="${locationTypeList}" var="record">
                <c:choose>
                    <c:when test="${record == playdate.locationType}">
                        <div class="form-check form-check-inline">
                            <form:radiobutton path="locationType" class="form-check-input" id="locationTypeSelector${record.id}" value="${record.id}" label="${record.displayValue}" checked="true" onclick="myFunction(${record.id})"/>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="form-check form-check-inline">
                            <form:radiobutton path="locationType" class="form-check-input" id="locationTypeSelector${record.id}" value="${record.id}" label="${record.displayValue}" onclick="myFunction(${record.id})"/>
                        </div>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
    </div> <%-- end collectionOfRadios--%>

<%--    <div id="conditionalFieldContainer1"></div>--%>

    <div id="locationNameContainer" class="form-floating mb-3 collapse" >
          <form:input
                  path="locationName"
                  type="text"
                  class="form-control"
                  id="locationName"
                  placeholder="locationName" />
          <form:label path="locationName" for="locationName">Location Name</form:label>
          <div id="locationHelp" class="form-text">Examples: Central Park or Gertie's Ice Cream, etc.</div>
          <p class="text-danger"><form:errors path="locationName" />
    </div>

    <div id="locationAddyContainer" class="form-floating mb-3 collapse">
        <form:input
                path="locationAddy"
                type="text"
                class="form-control"
                id="locationAddy"
                placeholder="locationAddy" />
        <form:label path="locationAddy" for="locationAddy">Location Address</form:label>
        <p class="text-danger"><form:errors path="locationAddy" />
    </div>

    <div class="form-floating mb-3">
      <form:input
              path="eventDate"
              type="date"
              class="form-control"
              id="eventDate"
              placeholder="eventDate" />
      <form:label path="eventDate" for="eventDate">Event Date</form:label>
      <p class="text-danger"><form:errors path="eventDate" /></p>
    </div>

        <div class="form-floating mb-3">
          <form:select
                  path="startTimeTxt"
                  class="form-control"
                  id="startTimeTxt"
                  placeholder="startTimeTxt">
              <form:option value="select" path="startTimeTxt">select...</form:option>
              <c:forEach items="${startTimeList}" var="record">
              <form:option value="${record}" path="startTimeTxt">${record}</form:option>
            </c:forEach>
          </form:select>
          <form:label path="startTimeTxt" for="startTimeTxt">Start Time:</form:label>
          <p class="text-danger"><form:errors path="startTimeTxt" />
        </div>

        <div class="form-floating mb-3">
          <form:textarea
                  path="eventDescription"
                  type="eventDescription"
                  class="form-control"
                  id="eventDescription"
                  placeholder="eventDescription"
                  style="height: 10rem;" />
          <form:label path="eventDescription" for="eventDescription">Playdate Description:</form:label>
          <p class="text-danger">
              <form:errors path="eventDescription" />
        </div>

        <div class="form-floating mb-3">
          <form:input
                  path="maxCountKids"
                  type="number"
                  class="form-control"
                  id="maxCountKids"
                  placeholder="maxCountKids"
                  min="1"
                  step="1"
                  value="1" />
          <form:label path="maxCountKids" for="maxCountKids">Max. Number Kids</form:label>
          <p class="text-danger"><form:errors path="maxCountKids" />
        </div>

      </div><!-- end playdateInfoCol -->

      <div id="rsvpEtcCol" class="col">

        <div id="rsvpCard" class="card p-3 d-md-flex justify-content-start">
          <p class="m-0 text-center" style="font-size: 1.25rem;">Your RSVP</p>

          <div class="form-floating mb-3">
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

          <div class="form-floating mb-3">
            <form:input
                    path="kidCount"
                    type="number"
                    class="form-control"
                    id="kidCount"
                    placeholder="kidCount"
                    min="1"
                    step="1"
                    value="1"
            />
            <form:label path="kidCount" for="kidCount"># of Kids</form:label>
            <p class="text-danger"><form:errors path="kidCount" />
          </div>

          <div class="form-floating mb-3">
            <form:input
                    path="adultCount"
                    type="number"
                    class="form-control"
                    id="adultCount"
                    placeholder="adultCount"
                    min="1"
                    step="1"
                    value="1"
            />
            <form:label path="adultCount" for="adultCount"># of Adults</form:label>
            <p class="text-danger"><form:errors path="adultCount" />
          </div>

          <div class="form-floating mb-3">
            <form:textarea
                    path="comment"
                    type="text"
                    class="form-control"
                    id="comment"
                    placeholder="comment"
                    style="height: 10rem;" />
            <form:label path="comment" for="comment">Comment</form:label>
            <p class="text-danger"><form:errors path="comment" />
          </div>
        </div><!-- end rsvpCard -->
      </div><!-- end rsvpEtcCol-->
    </div><!-- end eventDeetsAndRsvpRow -->
    <div id="bottomButtonTray" class="d-flex justify-content-center">
      <div>
        <button type="submit" class="btn btn-primary w-100">Create New Playdate</button>
      </div>
    </div><!-- end bottomButtonTray -->
  </form:form><!-- end playdateEditForm -->
</div> <!-- end playdateCard -->
<%--<jsp:include page="/WEB-INF/include/pageLayoutBottom.jsp" />--%>
<jsp:include page="/WEB-INF/include/pageLayoutBottomCommon.jsp" />




</body>
</html>