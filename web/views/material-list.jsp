<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<html lang="en">
    <head>
        <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0">
        <title>Material list</title>
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
            <c:if test="${sessionScope.user.getRole() == 'headmaster'}">
                <%@include file="headmasterLeftboard.jsp" %>
            </c:if>
            <c:if test="${sessionScope.user.getRole() == 'teacher'}">
                <%@include file="teacherLeftboard.jsp" %>
            </c:if>
            <c:if test="${sessionScope.user.getRole() == 'student'}">
                <%@include file="studentLeftboard.jsp" %>
            </c:if>

            <div class="page-wrapper">
                <div class="content container-fluid">
                    <div class="page-header">
                        <div class="row align-items-center">
                            <div class="col">
                                <h3 class="page-title">Materials</h3>
                                <ul class="breadcrumb">
                                    <li class="breadcrumb-item"><a href="${contextPath}/material?action=list">Dashboard</a></li>
                                    <li class="breadcrumb-item active">Materials</li>
                                </ul>
                            </div>
                            <c:if test="${sessionScope.user.getRole() != 'student'}">
                                <div class="col-auto text-right float-right ml-auto">
                                    <a href="${contextPath}/material?action=add" class="btn btn-primary"><i class="fas fa-plus"></i></a>
                                </div>
                            </c:if>

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
                                                    <th>Material ID</th>
                                                    <th>Material Name</th>
                                                    <th>Material Description</th>
                                                    <th>Subject</th>
                                                    <th>Material For Role</th>
                                                    <th>Created by</th>
                                                    <th class="text-right">Action</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:if test="${materials != null}">
                                                    <c:forEach items="${materials}" var="t">
                                                        <tr >
                                                            <td>${t.getMaterialId()}</td>
                                                            <td>${t.getMaterialName()}</td>
                                                            <td>${t.getMaterialDescription()}</td>
                                                            <td>${t.getSubject().getSubjectName()}</td>
                                                            <td>${t.getMaterialForRole()}</td>
                                                            <td>${t.getCreatedByUser().getUserId()} - ${t.getCreatedByUser().getFullName()}</td>
                                                            <td class="text-right">
                                                                <a href="${contextPath}/material?action=download&fileName=${t.fileName}" >Download </a>
                                                                <c:if test="${sessionScope.user.getRole() == 'headmaster'}">
                                                                    <div class="actions">
                                                                        <a href="${contextPath}/material?action=edit&materialId=${t.getMaterialId()}" class="btn btn-sm bg-success-light mr-2 editBtn">
                                                                            <i class="fas fa-pen"></i>
                                                                        </a>
                                                                        <a href="#" class="btn btn-sm bg-danger-light deleteBtn" data-subjectid="${t.getMaterialId()}">
                                                                            <i class="fas fa-trash"></i>
                                                                        </a>
                                                                    </div>
                                                                </c:if>
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
            $(document).ready(function () {
                $(document).on('click', '.deleteBtn', function (e) {
                    e.preventDefault();
                    var userId = $(this).data('subjectid');

                    // Hi?n th? h?p tho?i xác nh?n
                    if (confirm("Are you sure you want to delete this item?")) {
                        // N?u ng??i dùng ch?p nh?n, chuy?n h??ng ??n URL xóa
                        window.location.href = "${contextPath}/material?action=delete&materialId=" + userId;
                    } else {
                        // N?u ng??i dùng không ch?p nh?n, không làm gì c?
                        return false;
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


        </script>
    </body>
</html>
</html>
