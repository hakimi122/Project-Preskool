<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<html lang="en">
    <head>
        <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0">
        <title>Edit session</title>
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
                                <h3 class="page-title">Sessions</h3>
                                <ul class="breadcrumb">
                                    <li class="breadcrumb-item"><a href="#">Dashboard</a></li>
                                    <li class="breadcrumb-item active">Edit Sessions</li>
                                </ul>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-sm-12">
                            <div class="card">
                                <div class="card-body">
                                    <form id="editClassForm" action="${contextPath}/schedule-manage" method="post">
                                        <input type="hidden" name="action" value="editsession"/>
                                        <input type="hidden" name="sessionId" value="${ss.getSessionId()}"/>
                                        <input type="hidden" name="classId" value="${ss.getClassRoom().getClassId()}"/>
                                        <div class="row">
                                            <div class="col-12">
                                                <h5 class="form-title"><span>Session Information</span></h5>
                                            </div>
                                            <div class="col-12 col-sm-6">
                                                <div class="form-group">
                                                    <label>Class: ${ss.getClassRoom().getClassId()} - ${ss.getClassRoom().getClassName()} - ${ss.getClassRoom().getYear()}</label>
                                                </div>
                                            </div>
                                            <div class="col-12 col-sm-6">
                                                <div class="form-group">
                                                    <label>Subject</label>
                                                    <select name="subjectId" id="subjectSelect" onchange="updateTeachers()">
                                                        <c:forEach items="${subjects}" var="subject">
                                                            <option value="${subject.subjectId}" ${subject.subjectId == ss.getSubject().subjectId ? 'selected' : ''}>${subject.subjectName}</option>
                                                        </c:forEach>
                                                    </select>
                                                </div>
                                            </div>

                                            <div class="col-12 col-sm-6">
                                                <div class="form-group">
                                                    <label>Supervisor</label>
                                                    <select class="form-control" id="supervisorSelect" name="supervisorId" onchange="updateSlots()">
                                                        <option value="${ss.getTeacher().getUserId()}">${ss.getTeacher().getFullName()}</option>
                                                    </select>
                                                </div>
                                            </div>
                                            <div class="col-12 col-sm-6">
                                                <div class="form-group">
                                                    <label>Date</label>
                                                    <input type="date" name="date" id="dateSelect" value="${ss.getDate()}" onchange="updateSlots()"/>
                                                </div>
                                            </div>
                                            <div class="col-12 col-sm-6">
                                                <div class="form-group">
                                                    <label>Slot</label>
                                                    <select class="form-control" id="slotSelect" name="slotId" >
                                                        <option value="${ss.getTimeSlot().getSlotId()}">${ss.getTimeSlot().getDescription()}</option>
                                                    </select>
                                                </div>
                                            </div>
                                            <div class="col-12 col-sm-6">
                                                <div class="form-group">
                                                    <label>Room</label>
                                                    <input type="text" name="room" value="${ss.getRoom()}"/>
                                                </div>
                                            </div>
                                            <div class="col-12 col-sm-6">
                                                <div class="form-group">
                                                    <label for="statusId">Is Attended?</label>
                                                    <div id="statusId" style="display: flex">
                                                        <input type="radio" name="isActive" value="true" ${ss.isAttended ? 'checked' : ''}> True &nbsp;&nbsp;&nbsp;
                                                        <input type="radio" name="isActive" value="false" ${!ss.isAttended ? 'checked' : ''}> False
                                                    </div>
                                                </div>
                                            </div>


                                            <div class="col-12">
                                                <button type="submit" class="btn btn-primary">Submit</button>
                                            </div>
                                            <div class="col-12">
                                                <button  class="btn btn-block"><a href="${contextPath}/schedule-manage?action=getsessions&classId=${ss.getClassRoom().getClassId()}">Cancle</a></button>
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
                var subjectId = document.getElementById("subjectSelect").value;

                $.ajax({
                    url: "${contextPath}/schedule-manage",
                    type: "GET",
                    data: {
                        action: "getTeachers",
                        subjectId: subjectId
                    },
                    success: function (response) {
                        console.log(response);  // Log the whole response to inspect it
                        var supervisorSelect = document.getElementById("supervisorSelect");
                        supervisorSelect.innerHTML = "";

                        response.forEach(function (teacher) {
                            var option = document.createElement("option");
                            option.value = teacher.userId;
                            option.text = teacher.fullName;
                            supervisorSelect.appendChild(option);
                        });
                        updateSlots();
                    },
                    error: function (xhr, status, error) {
                        console.error("AJAX Error: ", status, error);
                    }
                });
            }

            function updateSlots() {
                var supervisorId = document.getElementById("supervisorSelect").value;
                var date = document.getElementById("dateSelect").value;

                if (!supervisorId || !date) {
                    return; // Không làm gì n?u ch?a ch?n giáo viên ho?c ngày
                }

                $.ajax({
                    url: "${contextPath}/schedule-manage",
                    type: "GET",
                    data: {
                        action: "getSlots",
                        supervisorId: supervisorId,
                        date: date
                    },
                    success: function (response) {
                        console.log(response);  // Log the whole response to inspect it
                        var slotSelect = document.getElementById("slotSelect");
                        slotSelect.innerHTML = "";

                        response.forEach(function (slot) {
                            var option = document.createElement("option");
                            option.value = slot.slotId;
                            option.text = slot.description;
                            slotSelect.appendChild(option);
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