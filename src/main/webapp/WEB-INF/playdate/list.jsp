<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.util.Date"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%--<jsp:include page="/WEB-INF/include/head.jsp" />--%>
<jsp:include page="/WEB-INF/include/headCommon.jsp" />
</head>
<jsp:include page="/WEB-INF/include/bodyDesign.jsp" />
<jsp:include page="/WEB-INF/include/header.jsp" />
<jsp:include page="/WEB-INF/include/pageLayoutTop.jsp" />


<div id="playdateList" class="container-sm my-5 table-responsive">
  <c:if test="${successMsg != null}">
    <div class="alert alert-success alert-dismissible fade show" role="alert">
        ${successMsg}
      <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
  </c:if>

  <h3>Playdate List</h3>
  <a href="/playdate/new"><button class="btn btn-primary">Create New Playdate</button></a>
  <table class="table table-striped table-hover table-bordered">
    <thead>
    <tr>
      <th scope="col">Event</th>
      <th scope="col">Organizer</th>
      <th scope="col">Status</th>
      <th scope="col">Date/Time</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="record" items="${playdateList}">
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
        <td><a class="text-decoration-none"
               href="/profile/${record.userMdl.id}">${record.userMdl.userName}</a></td>
        <td>${record.eventStatus}
        <td><fmt:formatDate value="${record.eventDate}" pattern="MMMM dd" />, <fmt:formatDate value="${record.eventDate}" pattern="yyyy" /> @ ${record.startTimeTxt}</td>
      </tr>
    </c:forEach>
    </tbody>
  </table>

</div> <!-- end playdateList -->
<%--<jsp:include page="/WEB-INF/include/pageLayoutBottom.jsp" />--%>

<jsp:include page="/WEB-INF/include/pageLayoutBottomCommon.jsp" />

</body>
</html>