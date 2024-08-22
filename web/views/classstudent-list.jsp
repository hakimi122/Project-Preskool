<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html lang="en">
    <head>
        <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0">
        <title>class list</title>
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
                                <h3 class="page-title">Classes</h3>
                                <ul class="breadcrumb">
                                    <li class="breadcrumb-item"><a href="#">Dashboard</a></li>
                                    <li class="breadcrumb-item active">Student In class <span style="color:blue;">&nbsp; ${c.classId} -${c.className}-${c.year}</span> </li>
                                </ul>
                            </div>
                            <div class="col-auto text-right float-right ml-auto">
                                <a href="${contextPath}/studentinclass?action=add&classId=${c.classId}" class="btn btn-primary "><i class="fas fa-plus"></i></a>
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
                                                    <th>Name</th>
                                                    <th>Dob</th>
                                                    <th>Gender</th>
                                                    <th>Email</th>
                                                    <th>Phone</th>
                                                        <c:if test="${sessionScope.user.getRole() == 'headmaster'}">
                                                        <th class="text-right">Action</th>
                                                        </c:if>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:if test="${c != null}">
                                                    <c:forEach items="${c.getStudents()}" var="t">
                                                        <tr>
                                                            <td>${t.getFullName()}</td>
                                                            <td>${t.getDob()}</td>
                                                            <td>${t.getGender()}</td>
                                                            <td>${t.getEmail()}</td>
                                                            <td>${t.getPhone()}</td>
                                                            <c:if test="${sessionScope.user.getRole() == 'headmaster'}">
                                                                <td class="text-right">
                                                                    <div class="actions">
                                                                        <a href="#" data-classid="${c.getClassId()}" data-userid="${t.getUserId()}" 
                                                                           class="btn btn-sm bg-danger-light deleteBtn">
                                                                            <i class="fas fa-trash"></i>
                                                                        </a>
                                                                    </div>
                                                                </td>
                                                            </c:if>
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


        <!-- Modal for editing user information -->
        <div class="modal fade" id="addStudentToClassModal" tabindex="-1" role="dialog" aria-labelledby="addStudentToClassModal" aria-hidden="true">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="addStudentToClassModal">Add Student To Class <span style="color:blue;">&nbsp; ${c.classId} -${c.className}-${c.year}</span></h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <form id="addStudentToClassForm" action="#" method="post">
                            <input type="hidden" class="form-control" id="year"  name="year" value="${c.year}">
                            <input type="hidden" class="form-control" id="classId"  name="classId" value="${c.classId}">     <!-- Example: -->
                            <div class="form-group">
                                <label for="studentId">ID Student</label>
                                <input type="text" class="form-control" id="studentId" name="studentId" placeholder="Enter id of student" required="">
                            </div>
                            <div class="form-group">
                                <label for="statusId">Status</label>
                                <select name="status" id="statusId">
                                    <option value="Not Passed">Not Passed</option>
                                    <option value="Passed">Passed</option>
                                </select>
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                        <button type="button" class="btn btn-primary" id="saveChangesBtn">Save changes</button>
                    </div>
                </div>
            </div>
        </div>
        <!--<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>-->

        <script src="./assets/js/jquery-3.6.0.min.js"></script>
        <script src="./assets/js/popper.min.js"></script>
        <script src="./assets/plugins/bootstrap/js/bootstrap.min.js"></script>
        <script src="./assets/plugins/slimscroll/jquery.slimscroll.min.js"></script>
        <script src="./assets/plugins/datatables/datatables.min.js"></script>
        <script src="./assets/js/script.js"></script>

        <script>
            $(document).ready(function ()
            {
                $(document).on('click', '.addBtn', function (e) {
                    e.preventDefault();
                    // Display the modal
                    $('#addStudentToClassModal').modal('show');
                });


                $(document).on('click', '#saveChangesBtn', function (e) {
                    e.preventDefault();

                    // Get values from the form
                    var classId = $('#classId').val();
                    var studentId = $('#studentId').val();
                    var year = $('#year').val();
                    var status = $('#statusId').val();
                    var contextPath = '${contextPath}';

                    // Perform an AJAX call to check if the student already exists in another class for the given year
                    $.ajax({
                        url: contextPath + '/studentinclass',
                        type: 'get',
                        data: {
                            action: 'checkExist',
                            classId: classId,
                            studentId: studentId,
                            year: year
                        },
                        success: function (response) {
                            if (response !== null) {
                                if (confirm("This student is studying in " + response.classId + " - " + response.className + " in" + response.year + " . Do you want to move the student to this class?")) {
                                    // Add the removeClassId to the form
                                    $('<input>').attr({
                                        type: 'hidden',
                                        id: 'typeId',
                                        name: 'type',
                                        value: 'edit'
                                    }).appendTo('#addStudentToClassForm');

                                    $('<input>').attr({
                                        type: 'hidden',
                                        id: 'removeClassId',
                                        name: 'removeClassId',
                                        value: response.classId
                                    }).appendTo('#addStudentToClassForm');

                                    $('#addStudentToClassForm').submit();
                                }
                            } else {
                                $('<input>').attr({
                                    type: 'hidden',
                                    id: 'typeId',
                                    name: 'type',
                                    value: 'add'
                                }).appendTo('#addStudentToClassForm');
                                $('#addStudentToClassForm').submit();
                            }
                        }
                    });
                });


            });
        </script>

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
                    var classId = $(this).data('classid');
                    var userId = $(this).data('userid');
                    // Hi?n th? h?p tho?i xác nh?n
                    if (confirm("Are you sure you want to delete student with id = " + userId + " in this class?")) {
                        // N?u ng??i dùng ch?p nh?n, chuy?n h??ng ??n URL xóa
                        window.location.href = "${contextPath}/studentinclass?action=delete&classId=" + classId + "&studentId=" + userId;
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