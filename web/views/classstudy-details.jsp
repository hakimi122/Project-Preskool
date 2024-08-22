<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<html lang="en">
    <head>
        <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0">
        <title>Class Study Details</title>
        <link rel="shortcut icon" href="./assets/img/favicon.png">
        <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Poppins:ital,wght@0,500;0,600;0,700;1,400&amp;display=swap">
        <link rel="stylesheet" href="./assets/plugins/bootstrap/css/bootstrap.min.css">
        <link rel="stylesheet" href="./assets/plugins/fontawesome/css/fontawesome.min.css">
        <link rel="stylesheet" href="./assets/plugins/fontawesome/css/all.min.css">
        <link rel="stylesheet" href="./assets/plugins/simple-calendar/simple-calendar.css">
        <link rel="stylesheet" href="./assets/css/style.css">
        <script src="https://cdnjs.cloudflare.com/ajax/libs/xlsx/0.17.5/xlsx.full.min.js"></script>

    </head>
    <body>

        <!-- Notification User -->
        <c:if test="${sessionScope.notification !=null}">
            <div class="alert ${sessionScope.typeNoti} alert-dismissible fade show " role="alert" style="position: fixed; z-index: 15; margin-left: 80%; margin-top: 5%;">
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
                                <h2>Class Details of <span style="color:blue">${classroom.classId} - ${classroom.className}</span></h2>
                                <h4>Teacher In Charge: <span style="color:blue">${classroom.getTeacherInCharge().getUserId()} - ${classroom.getTeacherInCharge().getFullName()}</span></h4>
                                <h4>Year: <span style="color:blue">${classroom.getYear()} </span></h4>
                                <h4>Number of Student:  <span style="color:blue">${classroom.getStudents().size()} </span></h4>
                            </div>
                            <div class="col">
                                <button class="btn-link"> <a href="${contextPath}/classstudy?action=list">Go back</a></button>
                            </div>

                            <div class="col">
                                <button onclick="exportToExcel()">Export as Excel</button>
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
                                                    <th>Student ID</th>
                                                    <th>Student Name</th>
                                                    <th>Final Score</th>
                                                    <th>Status</th>
                                                    <th>Teacher Evaluation</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:forEach items="${classroom.students}" var="student" varStatus="status">
                                                    <tr>
                                                        <td>${student.userId}</td>
                                                        <td>${student.fullName}</td>
                                                        <c:if test="${status.index < ass.size()}">
                                                            <td>${ass[status.index].averageScore}</td>
                                                            <td>${ass[status.index].status}</td>
                                                            <td>${ass[status.index].note}</td>
                                                        </c:if>
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
                                    $(document).ready(function () {
                                        $('#teacherTable').DataTable({
                                            "paging": true,
                                            "ordering": true,
                                            "searching": true,
                                            "info": true,
                                            "lengthChange": true,
                                            "language": {
                                                "search": "Search:",
                                                "paginate": {
                                                    "previous": "Previous",
                                                    "next": "Next"
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

                    if (confirm("Are you sure you want to delete this item?")) {
                        window.location.href = "${contextPath}/class?action=delete&classId=" + userId;
                    } else {
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
        <script>
            function exportToExcel() {
                const table = document.getElementById('teacherTable');
                const wb = XLSX.utils.table_to_book(table, {sheet: "Student list"});

                // Set filename
                const filename = `${classroom.getClassId()}_${classroom.getClassName()}_${classroom.getYear()}.xlsx`;

                // Customize sheet properties
                const ws = wb.Sheets['Student list'];

                // Define style object for bold font
                const boldStyle = {font: {bold: true}};

                // Apply style to each header cell
                ['A1', 'B1', 'C1', 'D1', 'E1'].forEach(cell => {
                    if (ws[cell]) {
                        ws[cell].s = boldStyle;
                    }
                });

                // Apply style to all cells in the header row
                const headerRow = ws['1'];
                if (headerRow) {
                    Object.keys(headerRow).forEach(key => {
                        if (key !== '!ref') {
                            headerRow[key].s = boldStyle;
                        }
                    });
                }

                // Write file with customized name
                XLSX.writeFile(wb, filename);
            }
        </script>

    </body>
</html>
