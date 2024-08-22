<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0">
        <title>Edit Students</title>
        <link rel="shortcut icon" href="./assets/img/favicon.png">
        <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Poppins:ital,wght@0,500;0,600;0,700;1,400&amp;display=swap">
        <link rel="stylesheet" href="./assets/plugins/bootstrap/css/bootstrap.min.css">
        <link rel="stylesheet" href="./assets/plugins/fontawesome/css/fontawesome.min.css">
        <link rel="stylesheet" href="./assets/plugins/fontawesome/css/all.min.css">
        <link rel="stylesheet" href="./assets/plugins/simple-calendar/simple-calendar.css">
        <link rel="stylesheet" href="./assets/css/style.css">
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
                                <h3 class="page-title">Students</h3>
                                <ul class="breadcrumb">
                                    <li class="breadcrumb-item"><a href="#">Dashboard</a></li>
                                    <li class="breadcrumb-item active">Edit Student</li>
                                </ul>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-sm-12">
                            <div class="card">
                                <div class="card-body">
                                    <form id="editClassForm" action="${contextPath}/student" method="post" onsubmit="return validateForm()">
                                        <input type="hidden" name="userId" value="${userEdit.userId}"/>
                                        <div class="row">
                                            <div class="col-12">
                                                <h5 class="form-title"><span>Student Information</span></h5>
                                            </div>
                                            <div class="col-12 col-sm-6">
                                                <div class="form-group">
                                                    <label for="nameEdit">Name</label>
                                                    <input type="text" class="form-control" id="nameEdit" name="fullName" value="${userEdit.fullName}">
                                                </div>
                                            </div>
                                            <div class="col-12 col-sm-6">
                                                <div class="form-group">
                                                    <label for="dobEdit">Dob</label>
                                                    <input type="date" class="form-control" id="dobEdit" name="dob" value="${userEdit.dob}">
                                                </div>
                                            </div>
                                            <div class="col-12 col-sm-6">
                                                <div class="form-group">
                                                    <label for="genderEdit">Gender</label>
                                                    <select name="gender" id="genderEdit" class="form-control">
                                                        <option value="Male" <c:if test="${userEdit.gender == 'Male'}">selected</c:if>>Male</option>
                                                        <option value="Female" <c:if test="${userEdit.gender == 'Female'}">selected</c:if>>Female</option>
                                                        <option value="Other" <c:if test="${userEdit.gender == 'Other'}">selected</c:if>>Other</option>
                                                        </select>
                                                    </div>
                                                </div>
                                                <div class="col-12 col-sm-6">
                                                    <div class="form-group">
                                                        <label for="phoneEdit">Phone</label>
                                                        <input type="text" id="phoneEdit" class="form-control" name="phone" value="${userEdit.phone}">
                                                </div>
                                            </div>
                                            <div class="col-12 col-sm-6">
                                                <div class="form-group">
                                                    <label for="emailEdit">Email</label>
                                                    <input type="email" id="emailEdit" class="form-control" name="email" value="${userEdit.email}">
                                                </div>
                                            </div>
                                            <div class="col-12 col-sm-6">
                                                <div class="form-group">
                                                    <label for="addressEdit">Address</label>
                                                    <input type="text" id="addressEdit" class="form-control" name="address" value="${userEdit.address}">
                                                </div>
                                            </div>
                                            <div class="col-12 col-sm-6">
                                                <div class="form-group">
                                                    <label for="statusId">Status</label>
                                                    <div id="statusId" style="display: flex">
                                                        <input type="radio" name="active" value="true" <c:if test="${userEdit.active == true}">checked</c:if>> Active &nbsp;&nbsp;&nbsp;
                                                        <input type="radio" name="active" value="false" <c:if test="${userEdit.active == false}">checked</c:if>> Deactived
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-12">
                                                    <button type="submit" class="btn btn-primary">Submit</button>
                                                </div>
                                            </div>
                                        </form>
                                    <%
                          session.removeAttribute("loadTime");
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
        <script src="./assets/plugins/datatables/datatables.min.js"></script>
        <script src="./assets/js/script.js"></script>

        <script>
                                        function validateForm() {
                                            var fullName = document.getElementById("nameEdit").value;
                                            var dob = document.getElementById("dobEdit").value;
                                            var gender = document.getElementById("genderEdit").value;
                                            var phone = document.getElementById("phoneEdit").value;
                                            var email = document.getElementById("emailEdit").value;
                                            var address = document.getElementById("addressEdit").value;

                                            // Validate Full Name
                                            if (!fullName || fullName.length < 2) {
                                                alert("Full Name must be at least 2 characters long");
                                                return false;
                                            }

                                            // Validate Date of Birth
                                            if (!dob) {
                                                alert("Date of birth is required");
                                                return false;
                                            }
                                            const dobDate = new Date(dob);
                                            const today = new Date();
                                            if (dobDate >= today) {
                                                alert('Date of birth must be before the current date.');
                                                return false;
                                            }

                                            // Validate Gender
                                            if (!gender) {
                                                alert("Gender is required");
                                                return false;
                                            }

                                            // Validate Phone Number
                                            var phonePattern = /^0\d{9}$/;
                                            if (!phonePattern.test(phone)) {
                                                alert("Phone number must be 10 digits long and start with a 0");
                                                return false;
                                            }

                                            // Validate Email
                                            var emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
                                            if (!emailPattern.test(email)) {
                                                alert("Invalid email format");
                                                return false;
                                            }


                                            // Validate Address
                                            if (!address || address.length < 5) {
                                                alert("Address must be at least 5 characters long");
                                                return false;
                                            }

                                            // Additional validation for passwords if present
                                            var newPassword = document.getElementById("newPassword") ? document.getElementById("newPassword").value : '';
                                            var confirmPassword = document.getElementById("newPasswordCf") ? document.getElementById("newPasswordCf").value : '';
                                            var userFullName = "${sessionScope.user.fullName}";
                                            var userName = "${sessionScope.user.username}";

                                            if (newPassword && confirmPassword) {
                                                if (newPassword !== confirmPassword) {
                                                    alert("New password and confirm password do not match");
                                                    return false;
                                                }

                                                if (newPassword.length < 12) {
                                                    alert("Password must be at least 12 characters long");
                                                    return false;
                                                }

                                                var hasUppercase = /[A-Z]/.test(newPassword);
                                                var hasLowercase = /[a-z]/.test(newPassword);
                                                var hasDigit = /[0-9]/.test(newPassword);
                                                var hasSpecialChar = /[~!@#$%^&*()_+\-={}|\[\]\\:";'<>?,./]/.test(newPassword);

                                                var categoriesCount = [hasUppercase, hasLowercase, hasDigit, hasSpecialChar].filter(Boolean).length;
                                                if (categoriesCount < 3) {
                                                    alert("Password must contain characters from at least 3 of the 4 categories: uppercase, lowercase, digits, and special characters");
                                                    return false;
                                                }

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
                                            }

                                            return true;
                                        }

                                        $(document).ready(function () {
                                            $('#teacherTable').DataTable({
                                                "paging": true, // Enable paging
                                                "ordering": true, // Enable sorting
                                                "searching": true, // Enable searching
                                                "info": true, // Show information
                                                "lengthChange": true, // Allow changing number of records per page
                                                "language": {
                                                    "search": "Search:",
                                                    "paginate": {
                                                        "previous": "Previous",
                                                        "next": "Next"
                                                    }
                                                }
                                            });
                                        });

                                        $(document).ready(function () {
                                            $(document).on('click', '.deleteBtn', function (e) {
                                                e.preventDefault();
                                                var userId = $(this).data('userid');

                                                if (confirm("Are you sure you want to delete this item?")) {
                                                    window.location.href = "${contextPath}/student?action=delete&userId=" + userId;
                                                } else {
                                                    return false;
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
        </script>
    </body>
</html>
