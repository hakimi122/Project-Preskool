<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0">
        <title>Add Student</title>
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
                                <h3 class="page-title">Students</h3>
                                <ul class="breadcrumb">
                                    <li class="breadcrumb-item"><a href="#">Dashboard</a></li>
                                    <li class="breadcrumb-item active">Add Student</li>
                                </ul>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-sm-12">
                            <div class="card">
                                <div class="card-body">
                                    <form action="${contextPath}/student" method="post" onsubmit="return validateForm()">
                                        <div class="row">
                                            <div class="col-12">
                                                <h5 class="form-title"><span>Basic Details</span></h5>
                                            </div>
                                            <div class="col-12 col-sm-6">
                                                <div class="form-group">
                                                    <label>Name</label>
                                                    <input type="text" id="fullName" name="fullName" class="form-control" required value="${sessionScope.userSave.fullName}">
                                                </div>
                                            </div>
                                            <div class="col-12 col-sm-6">
                                                <div class="form-group">
                                                    <label>Gender</label>
                                                    <select class="form-control" id="gender" name="gender">
                                                        <option value="Male" ${sessionScope.userSave.gender == 'Male' ? 'selected' : ''}>Male</option>
                                                        <option value="Female" ${sessionScope.userSave.gender == 'Female' ? 'selected' : ''}>Female</option>
                                                        <option value="Other" ${sessionScope.userSave.gender == 'Other' ? 'selected' : ''}>Others</option>
                                                    </select>
                                                </div>
                                            </div>
                                            <div class="col-12 col-sm-6">
                                                <div class="form-group">
                                                    <label>Date of Birth</label>
                                                    <input type="date" id="dob" name="dob" class="form-control" required value="${sessionScope.userSave.dob}">
                                                </div>
                                            </div>
                                            <div class="col-12 col-sm-6">
                                                <div class="form-group">
                                                    <label>Phone</label>
                                                    <input type="text" id="phone" name="phone" class="form-control" required value="${sessionScope.userSave.phone}">
                                                </div>
                                            </div>
                                            <div class="col-12 col-sm-6">
                                                <div class="form-group">
                                                    <label>Address</label>
                                                    <input class="form-control" id="address" name="address" type="text" required value="${sessionScope.userSave.address}">
                                                </div>
                                            </div>
                                            <div class="col-12 col-sm-6">
                                                <div class="form-group">
                                                    <label>Email</label>
                                                    <input type="email" id="email" name="email" class="form-control" required value="${sessionScope.userSave.email}">
                                                </div>
                                            </div>
                                            <div class="col-12 col-sm-6">
                                                <div class="form-group">
                                                    <label>Username</label>
                                                    <input type="text" id="username" name="username" class="form-control" required value="${sessionScope.userSave.username}">
                                                </div>
                                            </div>
                                            <div class="col-12 col-sm-6">
                                                <div class="form-group">
                                                    <label>Password</label>
                                                    <input type="password" id="password" name="password" class="form-control" required>
                                                </div>
                                            </div>
                                            <div class="col-12 col-sm-6">
                                                <div class="form-group">
                                                    <label>Confirm Password</label>
                                                    <input type="password" id="confirmPassword" name="confirmPassword" class="form-control" required>
                                                </div>
                                            </div>
                                            <div class="col-12">
                                                <button type="submit" class="btn btn-primary">Submit</button>
                                            </div>
                                        </div>
                                    </form>
                                    <%
                                        session.removeAttribute("userSave");
                                    %>
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
                                            var password = document.getElementById("password").value;
                                            var confirmPassword = document.getElementById("confirmPassword").value;
                                            var userFullName = "${sessionScope.user.fullName}";
                                            var userName = "${sessionScope.user.username}";
                                            var phone = document.getElementById("phone").value;

                                            // Validate password and confirm password match
                                            if (password !== confirmPassword) {
                                                alert("New password and confirm password do not match");
                                                return false;
                                            }

                                            // Validate password length
                                            if (password.length < 12) {
                                                alert("Password must be at least 12 characters long");
                                                return false;
                                            }

                                            // Validate password categories
                                            var hasUppercase = /[A-Z]/.test(password);
                                            var hasLowercase = /[a-z]/.test(password);
                                            var hasDigit = /[0-9]/.test(password);
                                            var hasSpecialChar = /[~!@#$%^&*()_+\-={}|\[\]\\:";'<>?,./]/.test(password);

                                            var categoriesCount = [hasUppercase, hasLowercase, hasDigit, hasSpecialChar].filter(Boolean).length;
                                            if (categoriesCount < 3) {
                                                alert("Password must contain characters from at least 3 of the 4 categories: uppercase, lowercase, digits, and special characters");
                                                return false;
                                            }

                                            // Validate password not containing parts of full name or username
                                            var fullNameParts = userFullName.split(" ");
                                            for (var i = 0; i < fullNameParts.length; i++) {
                                                if (fullNameParts[i].length > 2 && password.includes(fullNameParts[i])) {
                                                    alert("Password must not contain parts of your full name longer than 2 consecutive characters");
                                                    return false;
                                                }
                                            }
                                            if (password.includes(userName)) {
                                                alert("Password must not contain your username");
                                                return false;
                                            }
                                            
                                            const dob = document.querySelector('input[name="dob"]').value;
                                            const dobDate = new Date(dob);
                                            const today = new Date();
                                            if (dobDate >= today) {
                                                alert('Date of birth must be before the current date.');
                                                return false;
                                            }

                                            // Validate phone number pattern
                                            var phonePattern = /^0\d{9}$/;
                                            if (!phonePattern.test(phone)) {
                                                alert("Phone number must be 10 digits long and start with a 0");
                                                return false;
                                            }

                                            return true;
                                        }


                                        if (document.querySelector('.alert')) {
                                            document.querySelectorAll('.alert').forEach(function ($el) {
                                                setTimeout(() => {
                                                    $el.classList.remove('show');
                                                }, 3000);
                                            });
                                        }
        </script>
    </body>
</html>
