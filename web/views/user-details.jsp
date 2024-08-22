<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0">
        <title>User Details</title>
        <link rel="shortcut icon" href="../assets/img/favicon.png">
        <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Poppins:ital,wght@0,500;0,600;0,700;1,400&amp;display=swap">
        <link rel="stylesheet" href="./assets/plugins/bootstrap/css/bootstrap.min.css">
        <link rel="stylesheet" href="./assets/plugins/fontawesome/css/fontawesome.min.css">
        <link rel="stylesheet" href="./assets/plugins/fontawesome/css/all.min.css">
        <link rel="stylesheet" href="./assets/plugins/simple-calendar/simple-calendar.css">
        <link rel="stylesheet" href="./assets/css/style.css">
    </head>
    <body>
        <%
    String contextPath = request.getContextPath();
%>
        <!-- Notification User -->
        <c:if test="${sessionScope.notification !=null}">
            <div class="alert ${sessionScope.typeNoti} alert-dismissible fade show " role="alert" style=" position: fixed; z-index: 15 ; margin-left: 80%; margin-top: 5%;">
                <strong>${sessionScope.notification}</strong>
            </div>
            <% 
              session.removeAttribute("notification");
              session.removeAttribute("typeNoti");
            %>
        </c:if>
        <div class="main-wrapper">
            <%@include file="header.jsp" %>
            <%@include file="headmasterLeftboard.jsp" %>

            <div class="page-wrapper">
                <div class="content container-fluid">
                    <div class="page-header">
                        <div class="row align-items-center">
                            <div class="col">
                                <c:if test="${u.role eq 'teacher'}">
                                    <h3 class="page-title">Teachers</h3>
                                    <ul class="breadcrumb">
                                        <li class="breadcrumb-item"><a href="#">Dashboard</a></li>
                                        <li class="breadcrumb-item active">Teacher Details</li>
                                    </ul>
                                </c:if>
                                <c:if test="${u.role eq 'student'}">
                                    <h3 class="page-title">Students</h3>
                                    <ul class="breadcrumb">
                                        <li class="breadcrumb-item"><a href="#">Dashboard</a></li>
                                        <li class="breadcrumb-item active">Student Details</li>
                                    </ul>
                                </c:if>
                            </div>
                        </div>
                    </div>
                    <div class="card">
                        <div class="card-body">
                            <div class="row">
                                <div class="col-md-12">
                                    <div class="about-info">
                                        <h4>About Me</h4>
                                        <div class="media mt-3">
                                            <img src="${t.avatar}" class="mr-3" alt="..."
                                                 onerror="this.onerror=null; this.src='./images/user.png';"
                                                 >
                                            <div class="media-body">
                                                <ul>
                                                    <li>
                                                        <span class="title-span">Full Name : </span>
                                                        <span class="info-span">${u.fullName}</span>
                                                    </li>
                                                    <li>
                                                        <span class="title-span">Role : </span>
                                                        <span class="info-span">${u.role}</span>
                                                    </li>
                                                    <li>
                                                        <span class="title-span">Mobile : </span>
                                                        <span class="info-span">${u.phone}</span>
                                                    </li>
                                                    <li>
                                                        <span class="title-span">Email : </span>
                                                        <span class="info-span"> ${u.email}   </span>

                                                    </li>
                                                    <li>
                                                        <span class="title-span">Gender : </span>
                                                        <span class="info-span">${u.gender}</span>
                                                    </li>
                                                    <li>
                                                        <span class="title-span">DOB : </span>
                                                        <span class="info-span">${u.dob}</span>
                                                    </li>
                                                    <li>
                                                        <span class="title-span">Address : </span>
                                                        <span class="info-span">${u.address}</span>
                                                    </li>
                                                </ul>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <script src="./assets/js/jquery-3.6.0.min.js"></script>
        <script src="./assets/js/popper.min.js"></script>
        <script src="./assets/plugins/bootstrap/js/bootstrap.min.js"></script>
        <script src="./assets/plugins/slimscroll/jquery.slimscroll.min.js"></script>
        <script src="./assets/plugins/datatables/datatables.min.js"></script>
        <script src="./assets/js/script.js"></script>

    </body>
</html>