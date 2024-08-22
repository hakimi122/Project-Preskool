
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="helper" class="utils.DateTimeHelper"/>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Schedule Of Student</title>
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
            <c:if test="${sessionScope.user.getRole() == 'student'}">
                <%@include file="studentLeftboard.jsp" %>
            </c:if>
            <div class="page-wrapper">
                <div class="content container-fluid">
                    <div class="page-header">
                        <div class="row align-items-center">
                            <div class="col">
                                <h3 class="page-title">Year</h3>
                                <select name="classId" id="yearSelect">
                                    <c:forEach items="${classes}" var="c">
                                        <option value="${c.getClassId()}" <c:if test="${classId == c.getClassId()}">selected</c:if>>${c.getYear()}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-sm-12">
                            <div class="card card-table">
                                <div class="card-body">
                                    <form id="form" action="${contextPath}/schedule-student" method="get" onsubmit="return check();">
                                        <h2 style="text-align: center"> Schedule Of <span style="color:blue">${sessionScope.user.userId} - ${sessionScope.user.fullName}</span>  </h2>
                                        <h4 style="text-align: center">Class: <b>${cl.getClassId()} - ${cl.getClassName()}</b> </h4>
                                        <h4 style="text-align: center">Year <b>${cl.getYear()}</b> </h4>
                                        <input type="hidden" name="classId" id="classIdValue" value="${classId}"/>
                                        <b>From:</b> <input id="from" type="date" name="from" value="${sessionScope.from}"/>               
                                        <b> To:</b> <input id="to" type="date" name="to" value="${sessionScope.to}"/>
                                        <input  type="submit" value="View"/> 
                                    </form>
                                    <div class="table-responsive">
                                        <table class="table table-bordered table-striped">
                                            <thead style="background-color: #6b90da">
                                                <tr>
                                                    <th scope="col" rowspan="2" style="text-align: center">Slot</th>
                                                        <c:forEach items="${requestScope.dates}" var="d">
                                                        <th scope="col" style="text-align: center"> ${helper.getDayNameofWeek(d)} </th>                                                                                         
                                                        </c:forEach> 
                                                </tr>    
                                                <tr>
                                                    <c:forEach items="${requestScope.dates}" var="d">
                                                        <th scope="col" style="text-align: center">  ${helper.dateFormat(d,"dd/MM/yyyy")}  </th>                                                                                     
                                                        </c:forEach>   
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:forEach items="${requestScope.slots}" var="slot">
                                                    <tr style="text-align: center">
                                                        <th style="text-align: center">${slot.slotId} <i>(${slot.description})</i></th>
                                                            <c:forEach items="${requestScope.dates}" var="d">
                                                            <th scope="col">
                                                                <c:forEach items="${requestScope.sessions}" var="ses">
                                                                    <c:if test="${helper.compare(ses.date,d) eq 0 and (ses.timeSlot.slotId eq slot.slotId)}">
                                                                        <div>
                                                                            <div class="bt">
                                                                                <a style="font-style: none;" href="${contextPath}/attendance-view?sessionId=${ses.sessionId}&studentId=${sessionScope.user.userId}">Class: ${ses.getClassRoom().getClassName()} <br>
                                                                                    Subject: ${ses.subject.subjectName}</a> <br/>
                                                                            </div>
                                                                            <div class="bt">
                                                                                <a style="font-style: none;" href="${contextPath}/attendance?sessionId=${ses.sessionId}&flag=1">Class: ${ses.getClassRoom().getClassName()} <br>
                                                                                    Room: ${ses.getRoom()}</a> <br/>
                                                                            </div>

                                                                            <c:if test="${ses.isAttended}">
                                                                                (<font  color="Green" style="text-align: center">Attended</font>)
                                                                            </c:if>
                                                                            <c:if test="${!ses.isAttended}">
                                                                                (<font  color="Red" style="text-align: center">Not yet</font>)
                                                                            </c:if>
                                                                        </div>
                                                                    </th>
                                                                </c:if>
                                                            </c:forEach>
                                                            </th>
                                                        </c:forEach>
                                                    </tr>
                                                </c:forEach>
                                            </tbody>
                                        </table>
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

                                        // Year select change event
                                        document.getElementById('yearSelect').addEventListener('change', function () {
                                            document.getElementById('classIdValue').value = this.value;
                                            document.getElementById('form').submit();
                                        });
        </script>
    </body>
</html>
