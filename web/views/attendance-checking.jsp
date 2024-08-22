
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="helper" class="utils.DateTimeHelper"/>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Attendance Checking</title>
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
            <c:if test="${sessionScope.user.getRole() == 'headmaster'}">
                <%@include file="headmasterLeftboard.jsp" %>
            </c:if>
            <c:if test="${sessionScope.user.getRole() == 'teacher'}">
                <%@include file="teacherLeftboard.jsp" %>
            </c:if>
            <div class="page-wrapper">
                <div class="content container-fluid">
                    <div class="page-header">
                        <div class="row align-items-center">
                            <div class="col">
                                <h3 class="page-title">Attendance Checking</h3>
                                <ul class="breadcrumb">
                                    <li class="breadcrumb-item"><a href="${contextPath}/schedule">Dashboard</a></li>
                                    <li class="breadcrumb-item active">Attendance Checking</li>
                                </ul>
                                <h2 style="text-align: center">Take Attendance</h2>          
                                <b>Class: </b> ${requestScope.ses.getClassRoom().className} <br>
                                <b>Subject: </b> ${requestScope.ses.subject.subjectName} <br>
                                <b>Supervisor: </b> ${requestScope.ses.teacher.fullName} <br>
                                <b>Date: </b> ${requestScope.ses.date} <br>
                                <b>Time:</b> ${requestScope.ses.timeSlot.description}<br/>
                                <b>Room: </b> ${requestScope.ses.getRoom()}<br/>
                                <b>Attended: </b>    
                                <c:if test="${requestScope.ses.isAttended}">
                                    <span style="color:green"><b>YES</b></span>
                                </c:if>
                                <c:if test="${!requestScope.ses.isAttended}">
                                    <span style="color:red"><b>NO</b></span>
                                </c:if>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-sm-12">
                            <div class="card card-table">
                                <div class="card-body">
                                    <div class="table-responsive">
                                        <form action="${contextPath}/attendance" method="POST">
                                            <input type="hidden" name="sesid" value="${ses.sessionId}"/>
                                            <table class="table table-bordered table-striped">
                                                <thead style="background-color: #6b90da">
                                                    <tr>
                                                        <th scope="col" style="text-align: center">No.</th>                      
                                                        <th scope="col" style="text-align: center" >STUDENT ID</th>
                                                        <th scope="col" style="text-align: center">FULL NAME</th>
                                                        <th scope="col" style="text-align: center">ABSENT</th>
                                                        <th scope="col" style="text-align: center">PRESENT</th>
                                                        <th scope="col" style="text-align: center">NOTE</th>  
                                                        <th scope="col" style="text-align: center">Record Time</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <c:if test="${(requestScope.ses.attendances eq null) or (requestScope.ses.attendances.size() == 0)}">
                                                        <tr>
                                                            <th scope="col" colspan="6">
                                                                <h5 style="color:chocolate">This session does not have students!</h5>
                                                            </th>
                                                        </tr>                  
                                                    </c:if>

                                                    <c:if test="${(requestScope.ses.attendances ne null) and (requestScope.ses.attendances.size() > 0)}">
                                                        <c:forEach items="${requestScope.ses.attendances}" var="att" varStatus="loop">
                                                            <tr>
                                                                <th scope="col" style="text-align: center">${loop.index + 1} </th>
                                                                <th scope="col" style="text-align: center">
                                                                    ${att.student.userId} <input type="hidden" name="stdid" value="${att.student.userId}"/>
                                                                </th>
                                                                <td style="text-align: center">${att.student.fullName} </td>
                                                                <th scope="col" style="text-align: center">
                                                                    <input type="radio" 
                                                                           <c:if test="${att.isPresent()}">  checked="checked"  </c:if> 
                                                                           name="present${att.student.userId}" value="present" />  
                                                                </th>

                                                                <th scope="col" style="text-align: center">
                                                                    <input type="radio"
                                                                           <c:if test="${!att.isPresent()}"> checked="checked" </c:if>
                                                                           name="present${att.student.userId}" value="absent" /> 
                                                                </th>
                                                                <th scope="col" style="text-align: center">
                                                                    <input type="text" name="description${att.student.userId}" value="${att.note}" /> 
                                                                </th>
                                                                <td style="text-align: center">
                                                                    ${att.recordedTime}
                                                                </td>
                                                            </tr>
                                                        </c:forEach>
                                                    </c:if>
                                                </tbody>
                                            </table>
                                            <input type="hidden" name="flag" value="1" />
                                            <c:if test="${requestScope.flag eq 1}">
                                                <div class="add"> <button type="submit"> <b>SAVE </b><i class="fa fa-save"></i> </button> </div> <br>
                                            </c:if>
                                        </form>
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
