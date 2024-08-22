
<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0">
        <title>Edit class</title>
        <link rel="shortcut icon" href="./assets/img/favicon.png">
        <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Poppins:ital,wght@0,500;0,600;0,700;1,400&amp;display=swap">
        <link rel="stylesheet" href="./assets/plugins/bootstrap/css/bootstrap.min.css">
        <link rel="stylesheet" href="./assets/plugins/fontawesome/css/fontawesome.min.css">
        <link rel="stylesheet" href="./assets/plugins/fontawesome/css/all.min.css">
        <link rel="stylesheet" href="./assets/plugins/simple-calendar/simple-calendar.css">
        <link rel="stylesheet" href="./assets/css/style.css">
    </head>
    <body>
       <c:set var="contextPath" value="${pageContext.request.contextPath}" />

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
                                <h3 class="page-title">Classes</h3>
                                <ul class="breadcrumb">
                                    <li class="breadcrumb-item"><a href="#">Dashboard</a></li>
                                    <li class="breadcrumb-item active">Edit Class</li>
                                </ul>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-sm-12">
                            <div class="card">
                                <div class="card-body">
                                    <form id="editClassForm" action="${contextPath}/class" method="post">
                                        <input type="hidden" name="currentNOS" value="${c.getStudents().size()}"/>
                                        <div class="row">
                                            <div class="col-12">
                                                <h5 class="form-title"><span>Class Information</span></h5>
                                            </div>
                                            <div class="col-12 col-sm-6">
                                                <div class="form-group">
                                                    <label>Class ID</label>
                                                    <input type="text" class="form-control" name="classId" value="${c.classId}" readonly="">
                                                </div>
                                            </div>
                                            <div class="col-12 col-sm-6">
                                                <div class="form-group">
                                                    <label>Class Name</label>
                                                    <input type="text" class="form-control" name="className" value="${c.className}">
                                                </div>
                                            </div>
                                            <div class="col-12 col-sm-6">
                                                <div class="form-group">
                                                    <label>Level</label>
                                                    <input type="text" class="form-control" name="level" value="${c.level}">
                                                </div>
                                            </div>
                                            <div class="col-12 col-sm-6">
                                                <div class="form-group">
                                                    <label>Teacher In Charge</label>
                                                    <select class="form-control" id="chargeTeacher" name="chargeTeacherId">
                                                        <option value="">Select Teacher</option>
                                                        <c:forEach var="teacher" items="${regularTeachers}">
                                                            <option value="${teacher.userId}" <c:if test="${teacher.userId eq c.getTeacherInCharge().userId}">selected</c:if>>${teacher.userId} - ${teacher.fullName}</option>
                                                        </c:forEach>
                                                    </select>
                                                </div>
                                            </div>
                                            <div class="col-12 col-sm-6">
                                                <div class="form-group">
                                                    <label>Year</label>
                                                    <input type="number" min="1900"  class="form-control" name="year" value="${c.year}">
                                                </div>
                                            </div>
                                            <div class="col-12 col-sm-6">
                                                <div class="form-group">
                                                    <label>Limit number of Student</label>
                                                    <input type="number" min="0" class="form-control" name="limitNumberOfStudent" value="${c.limitNumberOfStudent}">
                                                </div>
                                            </div>
                                            <div class="col-12 col-sm-6">
                                                <div class="form-group">
                                                    <label for="statusId">Status</label>
                                                    <div id="statusId" style="display: flex">
                                                        <input type="radio"  name="isActive" value="true" <c:if test="${c.IsActive() == true}">checked</c:if>> Active &nbsp;&nbsp;&nbsp;
                                                        <input type="radio"  name="isActive" value="false" <c:if test="${c.IsActive() == false}">checked</c:if>> Deactived
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-12">
                                                <button type="submit" class="btn btn-primary">Submit</button>
                                            </div>
                                        </div>
                                    </form>
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
                    var userId = $(this).data('userid');

                    // Hi?n th? h?p tho?i xác nh?n
                    if (confirm("Are you sure you want to delete this item?")) {
                        // N?u ng??i dùng ch?p nh?n, chuy?n h??ng ??n URL xóa
                        window.location.href = "${contextPath}/teacher?action=delete&userId=" + userId;
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