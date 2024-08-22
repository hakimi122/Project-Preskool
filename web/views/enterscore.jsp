<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html lang="en">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0">
    <title>Enter score</title>
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
    <c:set var="contextPath" value="${pageContext.request.contextPath}" />

    <!-- Notification User -->
    <c:if test="${sessionScope.notification != null}">
        <div class="alert ${sessionScope.typeNoti} alert-dismissible fade show" role="alert" style="position: fixed; z-index: 15; margin-left: 80%; margin-top: 5%;">
            <strong>${sessionScope.notification}</strong>
        </div>
        <% 
          session.removeAttribute("notification");
          session.removeAttribute("typeNoti");
        %>
    </c:if>

    <div class="main-wrapper">
        <%@ include file="header.jsp" %>
        <c:if test="${sessionScope.user.getRole() == 'teacher'}">
            <%@ include file="teacherLeftboard.jsp" %>
        </c:if>

        <div class="page-wrapper">
            <div class="content container-fluid">
                <div class="page-header">
                    <div class="row align-items-center">
                        <div class="col">
                            <h3 class="page-title">Classes</h3>
                            <ul class="breadcrumb">
                                <li class="breadcrumb-item"><a href="#">Dashboard</a></li>
                                <li class="breadcrumb-item active">Enter Score <span style="color:blue;">&nbsp; ${c.classId} - ${c.className} - ${c.year}</span></li>
                            </ul>
                        </div>
                        <div class="col"><button class="btn btn-border"><a href="${contextPath}/classincharge?action=list">Back</a></button></div>
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
                                    <form action="${contextPath}/studentinclass" method="post">
                                        <input type="hidden" name="type" value="enterscore" />
                                        <input type="hidden" name="classId" value="${c.classId}" />
                                        <input type="hidden" name="year" value="${c.year}" />

                                        <table id="teacherTable" class="table table-hover table-center mb-0">
                                            <thead>
                                                <tr>
                                                    <th>Student ID</th>
                                                    <th>Student Name</th>
                                                    <th>Dob</th>
                                                    <th>Email</th>
                                                    <th>Mark Of Semester 1</th>
                                                    <th>Mark Of Semester 2</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:if test="${c != null}">
                                                    <c:forEach items="${c.getStudents()}" var="t">
                                                        <tr>
                                                            <td>${t.getUserId()}</td>
                                                            <td>${t.getFullName()}</td>
                                                            <td>${t.getDob()}</td>
                                                            <td>${t.getEmail()}</td>
                                                            <td>
                                                                <c:set var="scoreSemester1" value="" />
                                                                <c:forEach items="${scs}" var="sc">
                                                                    <c:if test="${sc.student.userId == t.userId}">
                                                                        <c:set var="scoreSemester1" value="${sc.scoreSemester1}" />
                                                                    </c:if>
                                                                </c:forEach>
                                                                <input type="number" step="0.01" name="scoreSemester1_${t.userId}" value="${scoreSemester1}" min="0" max="10" />
                                                            </td>
                                                            <td>
                                                                <c:set var="scoreSemester2" value="" />
                                                                <c:forEach items="${scs}" var="sc">
                                                                    <c:if test="${sc.student.userId == t.userId}">
                                                                        <c:set var="scoreSemester2" value="${sc.scoreSemester2}" />
                                                                    </c:if>
                                                                </c:forEach>
                                                                <input type="number" step="0.01" name="scoreSemester2_${t.userId}" value="${scoreSemester2}" min="0" max="10" />
                                                            </td>
                                                        </tr>
                                                    </c:forEach>
                                                </c:if>
                                            </tbody>
                                        </table>

                                        <div class="text-right mt-3">
                                            <input type="submit" value="Save" class="btn btn-primary" />
                                        </div>
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

        if (document.querySelector('.alert')) {
            document.querySelectorAll('.alert').forEach(function ($el) {
                setTimeout(() => {
                    $el.classList.remove('show');
                }, 3000);
            });
        }

        function exportToExcel() {
            const table = document.getElementById('teacherTable');
            const wb = XLSX.utils.book_new();
            const ws = XLSX.utils.table_to_sheet(table, {raw: true});

            // Update the values from input fields
            const tableRows = table.querySelectorAll('tbody tr');
            tableRows.forEach((row, rowIndex) => {
                const inputs = row.querySelectorAll('input');
                inputs.forEach((input, inputIndex) => {
                    const cellRef = XLSX.utils.encode_cell({r: rowIndex + 1, c: inputIndex + 4}); // Adjust column index for input fields
                    ws[cellRef] = {t: 'n', v: parseFloat(input.value) || 0};
                });
            });

            XLSX.utils.book_append_sheet(wb, ws, "Student list");

          
            const filename = `${c.classId}_${c.className}_${c.year}.xlsx`;
            XLSX.writeFile(wb, filename);
        }
    </script>
</body>
</html>
