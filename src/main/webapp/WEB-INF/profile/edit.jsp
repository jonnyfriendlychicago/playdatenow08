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

<c:if test="${validationErrorMsg != null}">
    <div class="alert alert-danger" role="alert">${validationErrorMsg}</div>
</c:if>
<div id="profileCard" class="card p-3 d-md-flex justify-content-start mt-3">
    <div id="joinedMgmtButtons" class="d-flex justify-content-between">
        <div class="card mb-3 p-2 border-0">
            <p class="m-0 text-secondary" style="font-size: 0.8rem;">Joined <fmt:formatDate value="${userProfileCreatedAt}" pattern="EEEE" />, <fmt:formatDate value="${userProfileCreatedAt}" pattern="MMMM dd" />, <fmt:formatDate value="${userProfileCreatedAt}" pattern="yyyy" />, <fmt:formatDate value="${userProfileCreatedAt}" pattern="h:mm a" /></p>
        </div>
        <div>
            <a href="/profile/${userProfileId}"><button class="btn btn-secondary">Cancel</button></a>
        </div>
    </div> <!-- end joinedMgmtButtons -->

    <form:form action='/profile/edit' method='post' modelAttribute='userProfileTobe'>
        <div id="eventDeetsAndRsvpRow" class="row mt-3">
            <div id="playdateInfoCol" class="col">

                <div class="form-floating mb-3">
                    <form:input
                            path="userName"
                            type="text"
                            class="form-control"
                            id="userName"
                            placeholder="userName" />
                    <form:label
                            path="userName"
                            for="userName">Username</form:label>
                    <p class="text-danger"><form:errors path="userName" />
                </div>

                <div class="form-floating mb-3">
                    <form:input
                            path="email"
                            type="email"
                            class="form-control"
                            id="email"
                            placeholder="name@example.com"
                            readonly="true"
                    />
                    <!-- note: the readonly="true" stuff above shall be whacked once we figure out how to make update email work with authentication -->
                    <form:label path="email" for="email">Email</form:label>
                    <p class="text-danger"><form:errors path="email" />
                    <div id="emailHelp" class="form-text">Change email function coming soon! We're working on it, I promise!</div>
                </div>

                <div class="form-floating mb-3">
                    <form:input
                            path="firstName"
                            type="text"
                            class="form-control"
                            id="firstName"
                            placeholder="firstName" />
                    <form:label path="firstName" for="firstName">First Name</form:label>
                    <p class="text-danger"><form:errors path="firstName" />
                </div>

                <div class="form-floating mb-3">
                    <form:input
                            path="lastName"
                            type="text"
                            class="form-control"
                            id="lastName"
                            placeholder="lastName" />
                    <form:label path="lastName" for="lastName">Last Name</form:label>
                    <p class="text-danger"><form:errors path="lastName" />
                </div>

                <div class="form-floating mb-3">
                    <form:textarea
                            path="aboutMe"
                            type="text"
                            class="form-control"
                            id="aboutMe"
                            placeholder="aboutMe"
                            style="height: 10rem;" />
                    <form:label path="aboutMe" for="aboutMe">About me</form:label>
                    <p class="text-danger"><form:errors path="aboutMe" />
                </div>


            </div><!-- end playdateInfoCol -->

            <div id="rsvpEtcCol" class="col">

                <div class="form-floating mb-3">
                    <form:input
                            path="addressLine1"
                            type="text"
                            class="form-control"
                            id="addressLine1"
                            placeholder="addressLine1" />
                    <form:label
                            path="addressLine1"
                            for="addressLine1">addressLine1</form:label>
                    <p class="text-danger"><form:errors path="addressLine1" />
                </div>

                <div class="form-floating mb-3">
                    <form:input
                            path="addressLine2"
                            type="text"
                            class="form-control"
                            id="addressLine2"
                            placeholder="addressLine2" />
                    <form:label
                            path="addressLine2"
                            for="addressLine2">addressLine2</form:label>
                    <p class="text-danger"><form:errors path="addressLine2" />
                </div>

                <div class="form-floating mb-3">
                    <form:input
                            path="city"
                            type="text"
                            class="form-control"
                            id="city"
                            placeholder="city" />
                    <form:label
                            path="city"
                            for="city">City</form:label>
                    <p class="text-danger"><form:errors path="city" />
                </div>

                <div class="form-floating mb-3">
                    <form:select
                            path="stateterritoryMdl"
                            class="form-control"
                            id="stateterritoryMdl"
                            placeholder="stateterritoryMdl">
                        <form:option value="0" path="stateterritoryMdl">(none)</form:option>
                        <c:forEach items="${stateterritoryList}" var="record">
                            <c:choose>
                                <c:when test="${record == asIsStateTerritoryObj}">
                                    <form:option value="${record.id}" path="stateterritoryMdl"
                                                 selected="true">${record.abbreviation}</form:option>
                                </c:when>
                                <c:otherwise>
                                    <form:option value="${record.id}" path="stateterritoryMdl">${record.abbreviation}</form:option>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </form:select>
                    <form:label path="stateterritoryMdl" for="stateterritoryMdl">stateterritoryMdl:</form:label>
                    <p class="text-danger"><form:errors path="stateterritoryMdl" />
                </div>



                <div class="form-floating mb-3">
                    <form:input
                            path="zipCode"
                            type="text"
                            class="form-control"
                            id="zipCode"
                            placeholder="zipCode" />
                    <form:label path="zipCode" for="zipCode">ZIP code</form:label>
                    <p class="text-danger"><form:errors path="zipCode" />
                </div>

            </div> <!-- end rsvpEtcCol-->
        </div><!-- end eventDeetsAndRsvpRow -->

        <div id="bottomButtonTray" class="d-flex justify-content-center">
            <div>
                <button type="submit" class="btn btn-primary">Update</button>
            </div>
        </div><!-- end bottomButtonTray -->

<%--                <div>--%>
<%--                    <button type="submit" class="btn btn-primary w-100">Update</button>--%>
<%--                </div>--%>
    </form:form>
</div><!-- end profileCard -->
<%--<jsp:include page="/WEB-INF/include/pageLayoutBottom.jsp" />--%>
<jsp:include page="/WEB-INF/include/pageLayoutBottomCommon.jsp" />



</body>
</html>