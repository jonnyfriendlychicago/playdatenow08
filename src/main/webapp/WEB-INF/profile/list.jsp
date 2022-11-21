<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.util.Date"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<jsp:include page="/WEB-INF/include/head.jsp" />
<jsp:include page="/WEB-INF/include/bodyDesign.jsp" />
<jsp:include page="/WEB-INF/include/header.jsp" />
<jsp:include page="/WEB-INF/include/pageLayoutTop.jsp" />

<div id="profileCardArray" class="container  d-flex flex-wrap">
    <c:forEach var="record" items="${profileList}">
        <div class="card m-1" style="width: 18rem;">
            <div class="card-body">
                <h5 class="card-title">${record.userName}</h5>
                <p class="card-text">${record.firstName}${record.lastName}</p>
                <pre style="white-space: pre-wrap; max-height: 10rem; overflow: ellipsis;">${record.aboutMe}</pre>
            </div>
                <ul class="list-group list-group-flush">

                    <li class="list-group-item">
                        <c:choose>
                            <c:when test="${record.homeName.length() > 0}">
                                Home name: "${record.homeName}"
                            </c:when>
                            <c:otherwise>
                                (No home name declared.)
                            </c:otherwise>
                        </c:choose>
                    </li>

                    <li class="list-group-item">Joined in <fmt:formatDate value="${record.createdAt}" pattern="MMMM" /> <fmt:formatDate value="${record.createdAt}" pattern="yyyy" /></li>
                    <li class="list-group-item"><a class="text-decoration-none" href="/profile/${record.id}">View Full Profile</a></li>
                </ul>
        </div>
    </c:forEach>
</div> <!-- end profileCardArray -->
<jsp:include page="/WEB-INF/include/pageLayoutBottom.jsp" />