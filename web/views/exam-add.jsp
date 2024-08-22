<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<html lang="en">
    <head>
        <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0">
        <title>Add new exam</title>
        <link rel="shortcut icon" href="./assets/img/favicon.png">
        <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Poppins:ital,wght@0,500;0,600;0,700;1,400&amp;display=swap">
        <link rel="stylesheet" href="./assets/plugins/bootstrap/css/bootstrap.min.css">
        <link rel="stylesheet" href="./assets/plugins/fontawesome/css/fontawesome.min.css">
        <link rel="stylesheet" href="./assets/plugins/fontawesome/css/all.min.css">
        <link rel="stylesheet" href="./assets/plugins/simple-calendar/simple-calendar.css">
        <link rel="stylesheet" href="./assets/css/style.css">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
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
                                <h3 class="page-title">Exams</h3>
                                <ul class="breadcrumb">
                                    <li class="breadcrumb-item"><a href="#">Dashboard</a></li>
                                    <li class="breadcrumb-item active">Add Exams</li>
                                </ul>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-sm-12">
                            <div class="card">
                                <div class="card-body">
                                    <form id="editClassForm" action="${contextPath}/exam" method="post">
                                        <input type="hidden" name="action" value="editexam"/>
                                        <div class="row">
                                            <div class="col-12">
                                                <h5 class="form-title"><span>Exam Information</span></h5>
                                            </div>
                                            <div class="col-12 col-sm-6">
                                                <div class="form-group">
                                                    <select class="form-control" name="examType">
                                                        <option value="Midterm Exam">Midterm Exam</option>
                                                        <option value="Final Exam">Final Exam</option>
                                                    </select>
                                                </div>
                                            </div>
                                            <div class="col-12 col-sm-6">
                                                <div class="form-group">
                                                    <label>Subject</label>
                                                    <select name="subjectId" id="subjectSelect" >
                                                        <c:forEach items="${subjects}" var="t">
                                                            <option value="${t.getSubjectId()}">${t.getSubjectName()}</option>
                                                        </c:forEach>
                                                    </select>
                                                </div>
                                            </div>
                                            <div class="col-12 col-sm-6">
                                                <div class="form-group">
                                                    <label>Slot</label>
                                                    <select class="form-control" id="slotSelect" name="slotId" onchange="updateTeachers()">
                                                        <c:forEach items="${slots}" var="t">
                                                            <option value="${t.slotId}">${t.description}</option>
                                                        </c:forEach>
                                                    </select>
                                                </div>
                                            </div>
                                            <div class="col-12 col-sm-6">
                                                <div class="form-group">
                                                    <label>Date</label>
                                                    <input type="date" name="examDate" id="dateSelect" onchange="updateTeachers()"/>
                                                </div>
                                            </div>
                                            <div class="col-12 col-sm-6">
                                                <div class="form-group">
                                                    <label>Room</label>
                                                    <input type="text" class="form-control" name="room"required="">
                                                </div>
                                            </div>
                                            <div class="col-12 col-sm-6">
                                                <div class="form-group">
                                                    <label>Teacher in charge</label>
                                                    <select class="form-control" id="supervisorSelect" name="teacherId">
                                                        <c:forEach items="${regularTeachers}" var="supervisor">
                                                            <option value="${supervisor.userId}">${supervisor.fullName}</option>
                                                        </c:forEach>
                                                    </select>
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
                                                        }
                                                        );
        </script>
        <script>
            function updateTeachers() {
                var dateSelect = document.getElementById("dateSelect").value;
                var slotId = document.getElementById("slotSelect").value;

                $.ajax({
                    url: "${contextPath}/exam",
                    type: "GET",
                    data: {
                        action: "getTeachers",
                        dateSelect: dateSelect,
                        slotId: slotId
                    },
                    success: function (response) {
                        var supervisorSelect = document.getElementById("supervisorSelect");
                        supervisorSelect.innerHTML = "";

                        response.forEach(function (teacher) {
                            var option = document.createElement("option");
                            option.value = teacher.userId;
                            option.text = teacher.fullName;
                            supervisorSelect.appendChild(option);
                        });
                    },
                    error: function (xhr, status, error) {
                        console.error("AJAX Error: ", status, error);
                    }
                });
            }
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