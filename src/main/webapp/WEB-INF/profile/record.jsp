<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.util.Date"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<jsp:include page="/WEB-INF/include/head.jsp" />
<jsp:include page="/WEB-INF/include/bodyDesign.jsp" />
<jsp:include page="/WEB-INF/include/header.jsp" />
<jsp:include page="/WEB-INF/include/pageLayoutTop.jsp" />

<c:if test="${permissionErrorMsg != null}">
  <div class="alert alert-warning" role="alert">
      ${permissionErrorMsg}</div>
</c:if>

<div id="profileCard" class="card p-3 d-md-flex justify-content-start mt-3">
  <div class="d-flex justify-content-between">
    <div class="card p-2 border-0">
      <p class="m-0 text-secondary" style="font-size: 0.8rem;">
        Joined <fmt:formatDate value="${userProfile.createdAt}" pattern="EEEE" />, <fmt:formatDate value="${userProfile.createdAt}" pattern="MMMM dd" />, <fmt:formatDate value="${userProfile.createdAt}" pattern="yyyy" />, <fmt:formatDate value="${userProfile.createdAt}" pattern="h:mm a" />
      </p>
    </div>
    <div>
      <c:if test="${userProfile.id == authUser.id}">
        <a href="/profile/${userProfile.id}/edit"><button class="btn btn-primary mb-2">Edit</button></a>
      </c:if>
    </div>
  </div>

  <div class="card p-2 border-0">
    <p class="m-0 text-secondary" style="font-size: 0.8rem;">Username</p>
    <p class="m-0">${userProfile.userName}</p>
  </div>

  <div class="card p-2 border-0">
    <p class="m-0 text-secondary" style="font-size: 0.8rem;">Email</p>
    <p class="m-0">${userProfile.email}</p>
  </div>

  <div class="card p-2 border-0">
    <p class="m-0 text-secondary" style="font-size: 0.8rem;">First name</p>
    <p class="m-0">${userProfile.firstName}</p>
  </div>

  <div class="card p-2 border-0">
    <p class="m-0 text-secondary" style="font-size: 0.8rem;">Last name</p>
    <p class="m-0">${userProfile.lastName}</p>
  </div>

  <div class="card p-2 border-0">
    <p class="m-0 text-secondary" style="font-size: 0.8rem;">About me</p>
    <pre class="textAreaReadOut">${userProfile.aboutMe}</pre>
  </div>

  <div class="card p-2 border-0">
    <p class="m-0 text-secondary" style="font-size: 0.8rem;">Location</p>
    <p class="m-0">${userProfile.city} -- ${userProfile.zipCode}</p>
  </div>

</div>
<!-- end profileCard -->

<div id="userEventsCard"
     class="card p-3 mt-3 d-md-flex justify-content-start">

  <h3>
    <c:choose>
      <c:when test="${authUser.id == userProfile.id}">
        My Organized Playdates
      </c:when>
      <c:otherwise>
        Organized Playdates
      </c:otherwise>
    </c:choose>
  </h3>
  <ul class="nav nav-tabs">
    <li class="nav-item"><a class="nav-link active"
                            data-bs-toggle="tab" aria-current="page" href="#userEventsUpcoming">Upcoming</a>
    </li>
    <li class="nav-item"><a class="nav-link" data-bs-toggle="tab"
                            href="#userEventsPast">Previous</a></li>
  </ul>

  <div class="tab-content">
    <div class="tab-pane container active" id="userEventsUpcoming">

      <table class="table table-striped table-hover table-responsive mt-2">
        <thead>
        <tr>
          <th scope="col">EventX</th>
          <th scope="col">Status</th>
          <th scope="col">Date/Time</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="record"
                   items="${userHostedPlaydateListCurrentPlus}">
          <tr>
            <td><a class="text-decoration-none"
                   href="/playdate/${record.id}"> <c:choose>
              <c:when test="${record.eventName.length() == 0}">
                Playdate @ ${record.locationName}
              </c:when>
              <c:otherwise>
                ${record.eventName} @ ${record.locationName}
              </c:otherwise>
            </c:choose>
            </a></td>
            <td>${record.eventStatus}
            <td><fmt:formatDate value="${record.eventDate}"
                                pattern="MMMM dd" />, <fmt:formatDate
                    value="${record.eventDate}" pattern="yyyy" /> @
                ${record.startTimeTxt}</td>
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
            <td><a class="text-decoration-none"
                   href="/playdate/${record.id}"> <c:choose>
              <c:when test="${record.eventName.length() == 0}">
                Playdate @ ${record.locationName}
              </c:when>
              <c:otherwise>
                ${record.eventName} @ ${record.locationName}
              </c:otherwise>
            </c:choose>
            </a></td>
            <td>${record.eventStatus}
            <td><fmt:formatDate value="${record.eventDate}"
                                pattern="MMMM dd" />, <fmt:formatDate
                    value="${record.eventDate}" pattern="yyyy" /> @
                ${record.startTimeTxt}</td>
          </tr>
        </c:forEach>
        </tbody>
      </table>

    </div>
  </div>
</div> <!-- end userEventsCard -->
<jsp:include page="/WEB-INF/include/pageLayoutBottom.jsp" />