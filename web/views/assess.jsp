<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html lang="en">
    <head>
        <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0">
        <title>Assessment</title>
        <link rel="shortcut icon" href="./assets/img/favicon.png">
        <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Poppins:ital,wght@0,500;0,600;0,700;1,400&display=swap">
        <link rel="stylesheet" href="./assets/plugins/bootstrap/css/bootstrap.min.css">
        <link rel="stylesheet" href="./assets/plugins/fontawesome/css/fontawesome.min.css">
        <link rel="stylesheet" href="./assets/plugins/fontawesome/css/all.min.css">
        <link rel="stylesheet" href="./assets/plugins/simple-calendar/simple-calendar.css">
        <link rel="stylesheet" href="./assets/css/style.css">
    </head>
    <body>
        <c:set var="contextPath" value="${pageContext.request.contextPath}" />

        <!-- Notification User -->
        <c:if test="${sessionScope.notification != null}">
            <div class="alert ${sessionScope.typeNoti} alert-dismissible fade show" role="alert" style=" position: fixed; z-index: 15 ; margin-left: 80%; margin-top: 5%;">
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
                                <h3 class="page-title">Assessment</h3>
                                <ul class="breadcrumb">
                                    <li class="breadcrumb-item"><a href="#">Dashboard</a></li>
                                    <li class="breadcrumb-item active">Assess for <span style="color:blue">${c.getClassId()} - ${c.getClassName()} - ${c.getYear()}</span> </li>
                                </ul>
                            </div>
                            <div class="col"><button class="btn btn-border"> <a href="${contextPath}/assessment?action=list">Back</a></button></div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-sm-12">
                            <div class="card card-table">
                                <div class="card-body">
                                    <div class="table-responsive">
                                        <c:if test="${c.getStudents() != null}">
                                            <form method="post" action="${contextPath}/assessment">
                                                <input type="hidden" name="yearB" value="${c.getYear()}"/>
                                                <input type="hidden" name="classId" value="${c.getClassId()}"/>
                                                <table id="teacherTable1" class="table table-hover table-center mb-0">
                                                    <thead>
                                                        <tr>
                                                            <th>ID</th>
                                                            <th>Name</th>
                                                            <th>Dob</th>
                                                            <th>Overall Score</th>
                                                            <th>Status</th>
                                                            <th>Evaluate</th>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        <c:forEach items="${c.getStudents()}" var="t">
                                                            <tr>
                                                                <td>${t.getUserId()}</td>
                                                                <td>
                                                                    <h2 class="table-avatar">
                                                                        <a href="#" class="avatar avatar-sm mr-2">
                                                                            <img class="avatar-img rounded-circle" src="${t.getAvatar()}" 
                                                                                 onerror="this.onerror=null; this.src='${contextPath}/images/user.png';"
                                                                                 alt="User Image">
                                                                        </a>
                                                                        <a href="#">${t.getFullName()}</a>
                                                                    </h2>
                                                                </td>
                                                                <td>${t.getDob()}</td>
                                                                <td>
                                                                    <c:choose>
                                                                        <c:when test="${not empty assessments}">
                                                                            <c:forEach items="${assessments}" var="a">
                                                                                <c:if test="${a.getStudentId() == t.getUserId()}">
                                                                                    ${a.getAverageScore()}
                                                                                </c:if>
                                                                            </c:forEach>
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            Not yet
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </td>
                                                                <td>
                                                                    <select name="status${t.getUserId()}">
                                                                        <c:choose>
                                                                            <c:when test="${not empty assessments}">
                                                                                <c:forEach items="${assessments}" var="a">
                                                                                    <c:if test="${a.getStudentId() == t.getUserId()}">
                                                                                        <option value="Pass" ${a.status == 'Pass' ? 'selected' : ''}>Passed</option>
                                                                                        <option value="Not Pass" ${a.status == 'Not Pass' ? 'selected' : ''}>Not Passed</option>
                                                                                    </c:if>
                                                                                </c:forEach>
                                                                            </c:when>
                                                                            <c:otherwise>
                                                                                <option value="Passed">Pass</option>
                                                                                <option value="Not Pass">Not Pass</option>
                                                                            </c:otherwise>
                                                                        </c:choose>
                                                                    </select>
                                                                </td>
                                                                <td>
                                                                    <c:choose>
                                                                        <c:when test="${not empty assessments}">
                                                                            <c:forEach items="${assessments}" var="a">
                                                                                <c:if test="${a.getStudentId() == t.getUserId()}">
                                                                                    <input name="note${t.getUserId()}" value="${a.getNote()}"/>
                                                                                </c:if>
                                                                            </c:forEach>
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <input name="note${t.getUserId()}" value=""/>
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </td>
                                                            </tr>
                                                        </c:forEach>
                                                    </tbody>
                                                </table>
                                                <div class="form-group text-right">
                                                    <button type="submit" class="btn btn-primary">Submit Assessment</button>
                                                </div>
                                            </form>
                                        </c:if>
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
            $(document).ready(function () {
                $('#teacherTable1').DataTable({
                    "paging": true, // Enable pagination
                    "ordering": true, // Enable sorting
                    "searching": true, // Enable search
                    "info": true, // Display information about record count
                    "lengthChange": true, // Enable changing the number of records per page
                    "language": {
                        "search": "Search:", // Change "Search:" text
                        "paginate": {
                            "previous": "Previous", // Change "Previous" text
                            "next": "Next" // Change "Next" text
                        }
                    }
                });
            });
        </script>

        <script>
            $(document).ready(function () {
                $(document).on('click', '.deleteBtn', function (e) {
                    e.preventDefault();
                    var userId = $(this).data('userid');

                    // Display confirmation dialog
                    if (confirm("Are you sure you want to delete this item?")) {
                        // Redirect to delete URL if confirmed
                        window.location.href = "${contextPath}/student?action=delete&userId=" + userId;
                    } else {
                        // Do nothing if not confirmed
                        return false;
                    }
                });
            });
        </script>

        <script>
            if (document.querySelector('.alert')) {
                document.querySelectorAll('.alert').forEach(function ($el) {
                    setTimeout(() => {
                        $el.classList.remove('show');
                    }, 3000);
                });
            }
        </script>
    </body>
</html>
