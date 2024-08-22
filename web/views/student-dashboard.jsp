<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="en">
    <head>
        <link rel="icon" type="image/x-icon" href="../assets/img/favicon.png">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Student Dashboard</title>
        <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Poppins:ital,wght@0,500;0,600;0,700;1,400&amp;display=swap">
        <link href="../assets/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>

        <link rel="stylesheet" href="../assets/plugins/fontawesome/css/fontawesome.min.css">
        <link rel="stylesheet" href="../assets/plugins/fontawesome/css/all.min.css">
        <link rel="stylesheet" href="../assets/css/style.css">
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
        <c:set var="contextPath" value="${pageContext.request.contextPath}" />

        <div class="main-wrapper">
            <%@include file="header.jsp" %>

            <%@include file="studentLeftboard.jsp" %>

            <div class="page-wrapper">
                <div class="content container-fluid">
                    <div class="page-header">
                        <div class="row">
                            <div class="col-sm-12">
                                <h3 class="page-title">Welcome,<span style="color:blue"> ${sessionScope.user.fullName}</span>!</h3>
                            </div>
                        </div>
                    </div>


                </div>
            </div>
        </div>

        <script src="../assets/js/jquery-3.6.0.min.js"></script>
        <script src="../assets/js/popper.min.js"></script>
        <script src="../assets/plugins/bootstrap/js/bootstrap.min.js"></script>
        <script src="../assets/plugins/slimscroll/jquery.slimscroll.min.js"></script>
        <script src="../assets/js/circle-progress.min.js"></script>
        <script src="../assets/js/script.js"></script>
    </body>
</html>