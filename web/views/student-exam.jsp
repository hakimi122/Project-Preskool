<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <title>Danh sách kỳ thi</title>
        <!-- Bootstrap CSS -->
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    </head>
    <body>

        <div class="container">
            <h1 class="mt-4 mb-4">Danh sách kỳ thi của: ${sessionScope.user.userId} - ${sessionScope.user.fullName}</h1>

            <!-- Dropdown for sorting -->
            <div class="mb-3">
                <label for="sortDropdown" class="form-label">Sắp xếp theo ngày thi:</label>
                <select id="sortDropdown" class="form-control" onchange="sortExams()">
                    <option value="desc" <c:if test="${sortDate == 'desc'}">selected</c:if>>Cũ nhất</option>
                    <option value="asc" <c:if test="${sortDate == 'asc'}">selected</c:if>>Mới nhất</option>
                    </select>
                </div>

                <!-- Exam Table -->
                <table class="table table-bordered table-striped">
                    <thead class="thead-dark">
                        <tr>
                            <th scope="col">Ngày thi</th>
                            <th scope="col">Môn thi</th>
                            <th scope="col">Slot</th>
                            <th scope="col">Room</th>
                            <th scope="col">Loại kỳ thi</th>
                        </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="exam" items="${exams}">
                        <tr>
                            <td>${exam.examDate}</td>
                            <td>${exam.subject.subjectName}</td>
                            <td>${exam.slot.description}</td>
                            <td>${exam.room}</td>
                            <td>${exam.examType}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
        <div style="text-align: center">        
            <a class="btn btn-primary" href="${contextPath}/student/dashboard">Back</a>
        </div>

        <!-- Bootstrap JS and dependencies -->
        <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.3/dist/umd/popper.min.js"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>

        <script>
                    function sortExams() {
                        var sortValue = document.getElementById('sortDropdown').value;
                        window.location.href = '${contextPath}/exam-student?sortDate=' + sortValue;
                    }
        </script>
    </body>
</html>
