
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="helper" class="utils.DateTimeHelper"/>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Attendance Viewing</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0-beta1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-0evHe/X+R7YkIZDRvuzKMRqM+OrBnVFBL6DOitfPri4tjfHxaWutUpFmBp4vmVor" crossorigin="anonymous">
        <link rel="shortcut icon" href="./assets/img/favicon.png">
        <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Poppins:ital,wght@0,500;0,600;0,700;1,400&amp;display=swap">
        <link rel="stylesheet" href="./assets/plugins/bootstrap/css/bootstrap.min.css">
        <link rel="stylesheet" href="./assets/plugins/fontawesome/css/fontawesome.min.css">
        <link rel="stylesheet" href="./assets/plugins/fontawesome/css/all.min.css">
        <link rel="stylesheet" href="./assets/plugins/simple-calendar/simple-calendar.css">
        <link rel="stylesheet" href="./assets/css/style.css">

        <style>
            .table-responsive {
                overflow-x: auto;
            }
        </style>
    </head>
    <body>
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
            <%@include file="studentLeftboard.jsp" %>
            <div class="page-wrapper">
                <div class="content container-fluid">
                    <div class="page-header">
                        <div class="row align-items-center">
                            <div class="col">
                                <h2 style="text-align: center"> Attendance Viewing</h2>          
                                <b>Class: </b> ${requestScope.a.getSession().getClassRoom().getClassName()} <br>
                                <b>Subject: </b> ${requestScope.a.getSession().getSubject().getSubjectName()} <br>
                                <b>Supervisor: </b> ${requestScope.a.getSession().getTeacher().getUserId()} -  ${requestScope.a.getSession().getTeacher().getFullName()} <br>
                                <b>Date: </b> ${requestScope.a.getSession().getDate()} <br>
                                <b>Time:</b> ${requestScope.a.getSession().getTimeSlot().getDescription()}<br/>
                                 <b>Room: </b> ${requestScope.ses.getRoom()}<br/>
                                <b>Attended: </b>    
                                <c:if test="${requestScope.a.getSession().isAttended()}">
                                    <span style="color:green"><b>YES</b></span>
                                </c:if>
                                <c:if test="${!requestScope.a.getSession().isAttended()}">
                                    <span style="color:red"><b>NO</b></span>
                                </c:if>
                                <br>
                                <c:if test="${requestScope.a.getSession().isAttended()}">
                                    <b>Status: </b>
                                    <c:choose>
                                        <c:when test="${a.isPresent()}">
                                            <span style="color:green"><b> Attended </b></span>
                                        </c:when>
                                        <c:otherwise>
                                            <span style="color:red"><b> Absent</b></span>
                                        </c:otherwise>
                                    </c:choose>
                                    <br>
                                    <b>Note: </b> <i>${a.getNote()}</i>
                                </c:if>
                            </div>
                            <div class="col"><button class="btn btn-link"><a href="${contextPath}/schedule-student">Go back</a></button></div>
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
        <script>

            if (document.querySelector('.alert'))
            {
                document.querySelectorAll('.alert').forEach(function ($el) {
                    setTimeout(() => {
                        $el.classList.remove('show');
                    }, 3000);
                });
            }


        </script>
    </body>
</html>
