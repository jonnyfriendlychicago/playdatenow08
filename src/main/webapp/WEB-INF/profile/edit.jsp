<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.util.Date"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<jsp:include page="/WEB-INF/include/head.jsp" />
<jsp:include page="/WEB-INF/include/bodyDesign.jsp" />
<jsp:include page="/WEB-INF/include/header.jsp" />
<jsp:include page="/WEB-INF/include/pageLayoutTop.jsp" />

<c:if test="${validationErrorMsg != null}">
    <div class="alert alert-danger" role="alert">${validationErrorMsg}</div>
</c:if>
<div id="profileCard" class="card p-3 d-md-flex justify-content-start">
    <div class="d-flex justify-content-between">
        <div class="card mb-3 p-2 border-0">
            <p class="m-0 text-secondary" style="font-size: 0.8rem;">Joined <fmt:formatDate value="${userProfileCreatedAt}" pattern="EEEE" />, <fmt:formatDate value="${userProfileCreatedAt}" pattern="MMMM dd" />, <fmt:formatDate value="${userProfileCreatedAt}" pattern="yyyy" />, <fmt:formatDate value="${userProfileCreatedAt}" pattern="h:mm a" /></p>
        </div>
        <div>
            <a href="/profile/${userProfileId}"><button class="btn btn-secondary">Cancel</button></a>
        </div>
    </div>

    <form:form action='/profile/edit' method='post' modelAttribute='userProfileTobe'>

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
            <div id="emailHelp" class="form-text">Change email addy function... coming soon!  (We're working on it, I promise!)</div>
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
            <form:input
                    path="zipCode"
                    type="text"
                    class="form-control"
                    id="zipCode"
                    placeholder="zipCode" />
            <form:label path="zipCode" for="zipCode">ZIP code</form:label>
            <p class="text-danger"><form:errors path="zipCode" />
        </div>

        <div>
            <button type="submit" class="btn btn-primary w-100">Update</button>
        </div>
    </form:form>
</div>
<!-- end profileCard -->
<jsp:include page="/WEB-INF/include/pageLayoutBottom.jsp" />