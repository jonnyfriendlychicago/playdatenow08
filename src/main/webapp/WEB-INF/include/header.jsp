<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<nav class="navbar navbar-expand-sm navbar-dark bg-dark" aria-label="Third navbar example">
    <div class="container-fluid">
        <a class="navbar-brand" href="/playdate">
            <img src="/img/pdnWhiteLogoNoBg.png" alt="playDateNOW logo all pages" height="32" >
        </a>

        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarsExample03" aria-controls="navbarsExample03" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarsExample03">
            <ul class="navbar-nav me-auto mb-2 mb-sm-0">
                <!--
                 <li class="nav-item">
                   <a class="nav-link active" aria-current="page" href="#">Home</a>
                 </li>
                  -->
                <li class="nav-item">
                    <a class="nav-link" href="/playdate">Playdates</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/connection">Relates</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/profile">Members</a>
                </li>
                <!--  <li class="nav-item">
                   <a class="nav-link disabled">Disabled</a>
                 </li> -->

                <!--           <li class="nav-item dropdown">
                            <a class="nav-link dropdown-toggle" href="#" data-bs-toggle="dropdown" aria-expanded="false">Dropdown</a>
                            <ul class="dropdown-menu">
                              <li><a class="dropdown-item" href="#">Action</a></li>
                              <li><a class="dropdown-item" href="#">Another action</a></li>
                              <li><a class="dropdown-item" href="#">Something else here</a></li>
                            </ul>
                          </li> -->

            </ul>
            <!--
                    <form role="search">
                      <input class="form-control" type="search" placeholder="Search" aria-label="Search">
                    </form>
             -->
            <!--          <div class="btn-group">
                      <button type="button" class="btn btn-secondary dropdown-toggle" data-bs-toggle="dropdown" aria-expanded="false">
                        Right-aligned menu
                      </button>
                      <ul class="dropdown-menu dropdown-menu-end">
                        <li><a class="dropdown-item" href="#">Menu item</a></li>
                        <li><a class="dropdown-item" href="#">Menu item</a></li>
                        <li><a class="dropdown-item" href="#">Menu item</a></li>
                      </ul>
                    </div> -->

            <!-- 		<div class="btn-group">
                      <button type="button" class="btn btn-secondary dropdown-toggle" data-bs-toggle="dropdown" data-bs-display="static" aria-expanded="false">
                      <button type="button" class="btn btn-link dropdown-toggle" data-bs-toggle="dropdown" data-bs-display="static" aria-expanded="false">
                        Left-aligned, right-aligned lg
                      </button>
                      <ul class="dropdown-menu dropdown-menu-lg-end">
                        <li><a class="dropdown-item" href="#">Menu item</a></li>
                        <li><a class="dropdown-item" href="#">Menu item</a></li>
                        <li><a class="dropdown-item" href="#">Menu item</a></li>
                      </ul>
                    </div> -->

            <div class="nav-item dropdown ml-0">
                <a class="nav-link dropdown-toggle text-light" href="#" data-bs-toggle="dropdown" aria-expanded="false">${authUserName}</a>
                <ul class="dropdown-menu dropdown-menu-lg-end">
                    <li><a class="dropdown-item" href="/profile/${authUser.id}">Profile</a></li>
                    <li><hr class="dropdown-divider"></li>
                    <li>
                        <form id="logoutForm" method="POST" action="/logout">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                            <%--                            <button type="submit" class="btn btn-light">Signout</button>--%>
                            <button type="submit" class="btn">Signout</button>
                        </form>
                    </li>
                </ul>
            </div>

        </div>
    </div>
</nav>

<%--<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>--%>
<%--<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>--%>



<%--<nav class="navbar navbar-expand-sm navbar-dark bg-dark" aria-label="Third navbar example">--%>
<%--    <div class="container-fluid">--%>
<%--        <a class="navbar-brand" href="/playdate">--%>
<%--            <img src="/img/pdnWhiteLogoNoBg.png" alt="playDateNOW logo all pages" height="32" >--%>
<%--        </a>--%>

<%--        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarsExample03" aria-controls="navbarsExample03" aria-expanded="false" aria-label="Toggle navigation">--%>
<%--            <span class="navbar-toggler-icon"></span>--%>
<%--        </button>--%>

<%--        <div class="collapse navbar-collapse" id="navbarsExample03">--%>
<%--            <ul class="navbar-nav me-auto mb-2 mb-sm-0">--%>
<%--                <!----%>
<%--                 <li class="nav-item">--%>
<%--                   <a class="nav-link active" aria-current="page" href="#">Home</a>--%>
<%--                 </li>--%>
<%--                  -->--%>
<%--                <li class="nav-item">--%>
<%--                    <a class="nav-link" href="/playdate">Dashboard</a>--%>
<%--                </li>--%>
<%--                <li class="nav-item">--%>
<%--                    <a class="nav-link" href="/profile">Members</a>--%>
<%--                </li>--%>
<%--                <!--  <li class="nav-item">--%>
<%--                   <a class="nav-link disabled">Disabled</a>--%>
<%--                 </li> -->--%>

<%--                <!--           <li class="nav-item dropdown">--%>
<%--                            <a class="nav-link dropdown-toggle" href="#" data-bs-toggle="dropdown" aria-expanded="false">Dropdown</a>--%>
<%--                            <ul class="dropdown-menu">--%>
<%--                              <li><a class="dropdown-item" href="#">Action</a></li>--%>
<%--                              <li><a class="dropdown-item" href="#">Another action</a></li>--%>
<%--                              <li><a class="dropdown-item" href="#">Something else here</a></li>--%>
<%--                            </ul>--%>
<%--                          </li> -->--%>

<%--            </ul>--%>
<%--            <!----%>
<%--                    <form role="search">--%>
<%--                      <input class="form-control" type="search" placeholder="Search" aria-label="Search">--%>
<%--                    </form>--%>
<%--             -->--%>
<%--            <!--          <div class="btn-group">--%>
<%--                      <button type="button" class="btn btn-secondary dropdown-toggle" data-bs-toggle="dropdown" aria-expanded="false">--%>
<%--                        Right-aligned menu--%>
<%--                      </button>--%>
<%--                      <ul class="dropdown-menu dropdown-menu-end">--%>
<%--                        <li><a class="dropdown-item" href="#">Menu item</a></li>--%>
<%--                        <li><a class="dropdown-item" href="#">Menu item</a></li>--%>
<%--                        <li><a class="dropdown-item" href="#">Menu item</a></li>--%>
<%--                      </ul>--%>
<%--                    </div> -->--%>

<%--            <!-- 		<div class="btn-group">--%>
<%--                      <button type="button" class="btn btn-secondary dropdown-toggle" data-bs-toggle="dropdown" data-bs-display="static" aria-expanded="false">--%>
<%--                      <button type="button" class="btn btn-link dropdown-toggle" data-bs-toggle="dropdown" data-bs-display="static" aria-expanded="false">--%>
<%--                        Left-aligned, right-aligned lg--%>
<%--                      </button>--%>
<%--                      <ul class="dropdown-menu dropdown-menu-lg-end">--%>
<%--                        <li><a class="dropdown-item" href="#">Menu item</a></li>--%>
<%--                        <li><a class="dropdown-item" href="#">Menu item</a></li>--%>
<%--                        <li><a class="dropdown-item" href="#">Menu item</a></li>--%>
<%--                      </ul>--%>
<%--                    </div> -->--%>

<%--            <div class="nav-item dropdown ml-0">--%>
<%--                <a class="nav-link dropdown-toggle text-light" href="#" data-bs-toggle="dropdown" aria-expanded="false">${authUserName}</a>--%>
<%--                <ul class="dropdown-menu dropdown-menu-lg-end">--%>
<%--                    <li><a class="dropdown-item" href="/profile/${authUser.id}">Profile</a></li>--%>
<%--                    <li><hr class="dropdown-divider"></li>--%>
<%--                    <li>--%>
<%--                        <form id="logoutForm" method="POST" action="/logout">--%>
<%--                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />--%>
<%--&lt;%&ndash;                            <button type="submit" class="btn btn-light">Signout</button>&ndash;%&gt;--%>
<%--                            <button type="submit" class="btn">Signout</button>--%>
<%--                        </form>--%>
<%--                    </li>--%>
<%--                </ul>--%>
<%--            </div>--%>

<%--        </div>--%>
<%--    </div>--%>
<%--</nav>--%>