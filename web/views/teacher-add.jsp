<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html lang="en">
    <head>
        <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0">
        <title>Headmaster - Dashboard</title>
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
            <%@include file="header.jsp" %>
            <%@include file="headmasterLeftboard.jsp" %>

            <div class="page-wrapper">
                <div class="content container-fluid">
                    <div class="page-header">
                        <div class="row align-items-center">
                            <div class="col">
                                <h3 class="page-title">Teachers</h3>
                                <ul class="breadcrumb">
                                    <li class="breadcrumb-item"><a href="#">Dashboard</a></li>
                                    <li class="breadcrumb-item active">Add Teachers</li>
                                </ul>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-sm-12">
                            <div class="card">
                                <div class="card-body">
                                    <form action="${contextPath}/teacher" method="post" onsubmit="return validateForm()">
                                        <input type="hidden" name="type" value="add" class="form-control">
                                        <div class="row">
                                            <div class="col-12">
                                                <h5 class="form-title"><span>Basic Details</span></h5>
                                            </div>
                                            <div class="col-12 col-sm-6">
                                                <div class="form-group">
                                                    <label>Name</label>
                                                    <input type="text" name="fullName" class="form-control" value="${sessionScope.userSavet.fullName}" required="">
                                                </div>
                                            </div>
                                            <div class="col-12 col-sm-6">
                                                <div class="form-group">
                                                    <label>Gender</label>
                                                    <select class="form-control" name="gender">
                                                        <option value="Male" <c:if test="${sessionScope.userSavet.gender == 'Male'}">selected</c:if>>Male</option>
                                                        <option value="Female" <c:if test="${sessionScope.userSavet.gender == 'Female'}">selected</c:if>>Female</option>
                                                        <option value="Other" <c:if test="${sessionScope.userSavet.gender == 'Other'}">selected</c:if>>Others</option>
                                                        </select>
                                                    </div>
                                                </div>
                                                <div class="col-12 col-sm-6">
                                                    <div class="form-group">
                                                        <label>Date of Birth</label>
                                                        <input type="date" name="dob" class="form-control" value="${sessionScope.userSavet.dob}" required="">
                                                </div>
                                            </div>
                                            <div class="col-12 col-sm-6">
                                                <div class="form-group">
                                                    <label>Phone</label>
                                                    <input type="text" name="phone" class="form-control" value="${sessionScope.userSavet.phone}" required="">
                                                </div>
                                            </div>
                                            <div class="col-12 col-sm-6">
                                                <div class="form-group">
                                                    <label>Address</label>
                                                    <input class="form-control" name="address" type="text" value="${sessionScope.userSavet.address}" required="">
                                                </div>
                                            </div>
                                            <div class="col-12">
                                                <h5 class="form-title"><span>Login Details</span></h5>
                                            </div>
                                            <div class="col-12 col-sm-6">
                                                <div class="form-group">
                                                    <label>Username</label>
                                                    <input type="text" name="username" class="form-control" value="${sessionScope.userSavet.username}" required="">
                                                </div>
                                            </div>
                                            <div class="col-12 col-sm-6">
                                                <div class="form-group">
                                                    <label>Email</label>
                                                    <input type="email" name="email" class="form-control" value="${sessionScope.userSavet.email}" required="">
                                                </div>
                                            </div>
                                            <div class="col-12 col-sm-6">
                                                <div class="form-group">
                                                    <label>Password</label>
                                                    <input type="password" id="newPassword" name="password" class="form-control" required="">
                                                </div>
                                            </div>
                                            <div class="col-12 col-sm-6">
                                                <div class="form-group">
                                                    <label>Repeat Password</label>
                                                    <input type="password" id="newPasswordCf" class="form-control" required="">
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
        <script src="./assets/js/circle-progress.min.js"></script>
        <script src="./assets/plugins/datatables/datatables.min.js"></script>

        <script src="./assets/js/script.js"></script>
        <script>
                                        function validateForm() {
                                            var newPassword = document.getElementById("newPassword").value;
                                            var confirmPassword = document.getElementById("newPasswordCf").value;
                                            var userFullName = "${sessionScope.user.fullName}"; // L?y giá tr? t? sessionScope
                                            var userName = "${sessionScope.user.username}"; // L?y giá tr? t? sessionScope
                                            var phone = document.querySelector('input[name="phone"]').value; // L?y giá tr? s? ?i?n tho?i

                                            // Ki?m tra m?t kh?u m?i và xác nh?n m?t kh?u kh?p nhau
                                            if (newPassword !== confirmPassword) {
                                                alert("New password and confirm password do not match");
                                                return false;
                                            }

                                            // Ki?m tra ?? dài c?a m?t kh?u
                                            if (newPassword.length < 12) {
                                                alert("Password must be at least 12 characters long");
                                                return false;
                                            }

                                            // Ki?m tra các ký t? t? ba nhóm tr? lên
                                            var hasUppercase = /[A-Z]/.test(newPassword);
                                            var hasLowercase = /[a-z]/.test(newPassword);
                                            var hasDigit = /[0-9]/.test(newPassword);
                                            var hasSpecialChar = /[~!@#$%^&*()_+\-={}|\[\]\\:";'<>?,./]/.test(newPassword);

                                            var categoriesCount = [hasUppercase, hasLowercase, hasDigit, hasSpecialChar].filter(Boolean).length;
                                            if (categoriesCount < 3) {
                                                alert("Password must contain characters from at least 3 of the 4 categories: uppercase, lowercase, digits, and special characters");
                                                return false;
                                            }

                                            const dob = document.querySelector('input[name="dob"]').value;
                                            const dobDate = new Date(dob);
                                            const today = new Date();
                                            if (dobDate >= today) {
                                                alert('Date of birth must be before the current date.');
                                                return false;
                                            }

                                            // Ki?m tra m?t kh?u không ch?a tên tài kho?n ho?c các ph?n c?a tên ??y ?? dài h?n 2 ký t? liên ti?p
                                            var fullNameParts = userFullName.split(" ");
                                            for (var i = 0; i < fullNameParts.length; i++) {
                                                if (fullNameParts[i].length > 2 && newPassword.includes(fullNameParts[i])) {
                                                    alert("Password must not contain parts of your full name longer than 2 consecutive characters");
                                                    return false;
                                                }
                                            }
                                            if (newPassword.includes(userName)) {
                                                alert("Password must not contain your username");
                                                return false;
                                            }


// Ki?m tra s? ?i?n tho?i g?m 11 ch? s? và b?t ??u b?ng ch? s? 0
                                            var phonePattern = /^0\d{9}$/;
                                            if (!phonePattern.test(phone)) {
                                                alert("Phone number must be 11 digits long and start with a 0");
                                                return false;
                                            }
                                            return true;
                                        }

        </script>

    </body>
</html>