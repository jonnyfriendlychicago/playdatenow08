<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.util.Date"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<jsp:include page="/WEB-INF/include/head.jsp" />
<jsp:include page="/WEB-INF/include/bodyDesign.jsp" />
<jsp:include page="/WEB-INF/include/header.jsp" />
<jsp:include page="/WEB-INF/include/pageLayoutTop.jsp" />

<%--<div id="profileCardArray" class="container d-flex flex-wrap">--%>
<div id="profileCardArray" class="container ">
    <c:forEach var="record" items="${profileList}">
<%--        <div class="card m-1" style="width: 18rem;">--%>
        <div class="card m-1" >
            <div class="card-body">
                <h5 class="card-title">${record.userName}</h5> <a class="text-decoration-none" href="/profile/${record.id}">View Full Profile</a>
                <p class="card-text">${record.firstName}${record.lastName}</p>
                <pre style="white-space: pre-wrap; max-height: 10rem; overflow: ellipsis;">${record.aboutMe}</pre>
            </div>
                <ul class="list-group list-group-flush">

<%--                    <li class="list-group-item">--%>
<%--                        <c:choose>--%>
<%--                            <c:when test="${record.homeName.length() > 0}">--%>
<%--                                Home name: "${record.homeName}"--%>
<%--                            </c:when>--%>
<%--                            <c:otherwise>--%>
<%--                                (No home name declared.)--%>
<%--                            </c:otherwise>--%>
<%--                        </c:choose>--%>
<%--                    </li>--%>

                    <li class="list-group-item">Joined in <fmt:formatDate value="${record.createdAt}" pattern="MMMM" /> <fmt:formatDate value="${record.createdAt}" pattern="yyyy" /></li>
<%--                    <li class="list-group-item"><a class="text-decoration-none" href="/profile/${record.id}">View Full Profile</a></li>--%>
                    <li class = "list-group-item">
                        <c:choose>
                            <c:when test="${record.id == authUser.id}">
                                <p>can't friend yoself, fool!</p>
                            </c:when>
                            <c:otherwise>
                                <form:form action='/socialconnection/create' method='post' modelAttribute='soConObj'>
                                    <form:input type="hidden" path="usertwoUserMdl" value="${record.id}"/>
                                    <button type="submit" class="btn btn-primary w-100">Add Parent Circle</button>
                                </form:form>
                            </c:otherwise>
                        </c:choose>
                    </li>
                </ul>
        </div>
    </c:forEach>
</div> <!-- end profileCardArray -->
<%--    begin boilerplate for form copied from rsvp--%>


<%--        WORKING ON THIS FORM NOW!!--%>

<div class = "container m-3 p-3">
    <div class="row m-0 p-0">
        <div class="form-floating col-sm me-5 p-0 ">

            <form:form action='/socialconnection/create' method='post' modelAttribute='soConObj'>

                <form:select
                        path="usertwoUserMdl"
                        class="form-control"
                        id="usertwoUserMdl"
                        placeholder="usertwoUserMdl">
    <%--                        <form:option value="0" path="stateterritoryMdl">(none)</form:option>--%>
                    <c:forEach items="${profileList}" var="record">
    <%--                            <c:choose>--%>
    <%--                                <c:when test="${record == playdate.stateterritoryMdl}">--%>
    <%--                                    &lt;%&ndash;                            <c:when test="${record == intendedStateTerritoryObj}">&ndash;%&gt;--%>
    <%--                                    &lt;%&ndash;                                above replaced by below&ndash;%&gt;--%>
    <%--                                    &lt;%&ndash;                                <c:when test="${record == userProfileObj.stateterritoryMdl  }">&ndash;%&gt;--%>
    <%--                                    <form:option value="${record.id}" path="stateterritoryMdl" selected="true">${record.abbreviation}</form:option>--%>
    <%--                                </c:when>--%>
    <%--                                <c:otherwise>--%>
                                <form:option value="${record.id}" path="usertwoUserMdl">${record.userName}</form:option>
    <%--                                </c:otherwise>--%>
    <%--                            </c:choose>--%>
                    </c:forEach>
                </form:select>

                <div>
                    <button type="submit" class="btn btn-primary w-100">Save soCon</button>
                </div>

            </form:form>
        </div>
    </div> <!-- end row -->
</div> <!-- end container -->




<jsp:include page="/WEB-INF/include/pageLayoutBottom.jsp" />