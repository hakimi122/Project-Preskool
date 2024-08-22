<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<html lang="en">
    <head>
        <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0">
        <title>Class list</title>
        <link rel="shortcut icon" href="./assets/img/favicon.png">
        <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Poppins:ital,wght@0,500;0,600;0,700;1,400&amp;display=swap">
        <link rel="stylesheet" href="./assets/plugins/bootstrap/css/bootstrap.min.css">
        <link rel="stylesheet" href="./assets/plugins/fontawesome/css/fontawesome.min.css">
        <link rel="stylesheet" href="./assets/plugins/fontawesome/css/all.min.css">
        <link rel="stylesheet" href="./assets/plugins/simple-calendar/simple-calendar.css">
        <link rel="stylesheet" href="./assets/css/style.css">
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
            <%@include file="headmasterLeftboard.jsp" %>

            <div class="page-wrapper">
                <div class="content container-fluid">
                    <div class="page-header">
                        <div class="row align-items-center">
                            <div class="col">
                                <h3 class="page-title">Schedule</h3>
                                <ul class="breadcrumb">
                                    <li class="breadcrumb-item"><a href="#">Dashboard</a></li>
                                    <li class="breadcrumb-item active">Schedule</li>
                                </ul>
                            </div>
                            <div class="col">
                                <h3 class="page-title">Year</h3>
                                <form id="yearForm" action="${contextPath}/schedule-manage" method="get">
                                    <input type="hidden" name="action" value="filteryear">
                                    <select name="year" id="yearSelect" class="form-control" onchange="submitYearForm()">
                                        <option value="-1" ${year == -1 ? 'selected' : ''}>All</option>
                                        <c:forEach items="${uniqueYears}" var="year">
                                            <option value="${year}" ${year == requestScope.year ? 'selected' : ''}>${year}</option>
                                        </c:forEach>
                                    </select>
                                </form>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-sm-12">
                            <div class="card card-table">
                                <div class="card-body">
                                    <div class="table-responsive">
                                        <table id="teacherTable" class="table table-hover table-center mb-0">
                                            <thead>
                                                <tr>
                                                    <th>ID</th>
                                                    <th>Name</th>
                                                    <th>Level</th>
                                                    <th>Teacher in charge</th>
                                                    <th>Year</th>
                                                    <th>Is Active?</th>
                                                    <th>Action</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:if test="${classes != null}">
                                                    <c:forEach items="${classes}" var="t">
                                                        <tr id="row_${t.classId}">
                                                            <td>${t.classId}</td>
                                                            <td>${t.className}</td>
                                                            <td>${t.level}</td>
                                                            <td>${t.getTeacherInCharge().userId} - ${t.getTeacherInCharge().fullName}</td>
                                                            <td>${t.year}</td>
                                                            <td>${t.IsActive()}</td>
                                                            <td>
                                                                <div class="actions">
                                                                    <a href="${contextPath}/schedule-manage?action=getsessions&classId=${t.classId}" class="btn btn-outline-primary ml-2">
                                                                        <i class="fas fa-user-edit"></i>Schedules</a>
                                                                </div>
                                                            </td>
                                                        </tr>
                                                    </c:forEach>
                                                </c:if>
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
                                    $(document).ready(function () {
                                        $('#teacherTable').DataTable({
                                            "paging": true, // Cho phép phân trang
                                            "ordering": true, // Cho phép s?p x?p
                                            "searching": true, // Cho phép tìm ki?m
                                            "info": true, // Hi?n th? thông tin s? l??ng b?n ghi
                                            "lengthChange": true, // Cho phép thay ??i s? l??ng b?n ghi m?i trang
                                            "language": {
                                                "search": "Search:", // Thay ??i t? 'Search:' thành ti?ng Vi?t ho?c ngôn ng? b?n mu?n
                                                "paginate": {
                                                    "previous": "Previous", // Thay ??i t? 'Previous' thành ti?ng Vi?t ho?c ngôn ng? b?n mu?n
                                                    "next": "Next" // Thay ??i t? 'Next' thành ti?ng Vi?t ho?c ngôn ng? b?n mu?n
                                                }
                                            }
                                        });
                                    });
        </script>

        <script>

            if (document.querySelector('.alert'))
            {
                document.querySelectorAll('.alert').forEach(function ($el) {
                    setTimeout(() => {
                        $el.classList.remove('show');
                    }, 3000);
                });
            }

            function submitYearForm() {
                document.getElementById('yearForm').submit();
            }
        </script>
    </body>
</html>