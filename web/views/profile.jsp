<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html lang="en">
    <!-- Mirrored from preschool.dreamguystech.com/html-template/profile.html by HTTrack Website Copier/3.x [XR&CO'2014], Thu, 28 Oct 2021 11:11:38 GMT -->
    <head>
        <link rel="icon" type="image/x-icon" href="./assets/img/favicon.png">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>User - Profile</title>
        <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Poppins:ital,wght@0,500;0,600;0,700;1,400&amp;display=swap">
        <link rel="stylesheet" href="./assets/plugins/bootstrap/css/bootstrap.min.css">
        <link rel="stylesheet" href="./assets/plugins/fontawesome/css/fontawesome.min.css">
        <link rel="stylesheet" href="./assets/plugins/fontawesome/css/all.min.css">
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
            <c:if test="${sessionScope.user.role eq 'headmaster'}">
                <%@include file="headmasterLeftboard.jsp" %>
            </c:if>
            <c:if test="${sessionScope.user.role eq 'student'}">
                <%@include file="studentLeftboard.jsp" %>
            </c:if>
            <c:if test="${sessionScope.user.role eq 'teacher'}">
                <%@include file="teacherLeftboard.jsp" %>
            </c:if>
            <div class="page-wrapper">
                <div class="content container-fluid">
                    <div class="page-header">
                        <div class="row">
                            <div class="col">
                                <h3 class="page-title">Profile</h3>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-12">
                            <div class="profile-header">
                                <div class="row align-items-center">
                                    <div class="col-auto profile-image">
                                        <a href="#">
                                            <img class="rounded-circle" alt="User Image" 
                                                 src="${sessionScope.user.avatar}"
                                                 onerror="this.onerror=null; this.src='~/${sessionScope.user.avatar}';"
                                                 >
                                        </a>
                                    </div>
                                    <div class="col ml-md-n2 profile-user-info">
                                        <h4 class="user-name mb-0">${sessionScope.user.fullName}</h4>
                                        <h6 class="text-muted">${sessionScope.user.role}</h6>
                                        <div class="user-Location"><i class="fas fa-map-marker-alt"></i> ${sessionScope.user.address}</div>
                                    </div>

                                </div>
                            </div>
                            <div class="profile-menu">
                                <ul class="nav nav-tabs nav-tabs-solid">
                                    <li class="nav-item">
                                        <a class="nav-link active" data-toggle="tab" href="#per_details_tab">About</a>
                                    </li>
                                    <li class="nav-item">
                                        <a class="nav-link" data-toggle="tab" href="#password_tab">Password</a>
                                    </li>
                                </ul>
                            </div>
                            <div class="tab-content profile-tab-cont">
                                <div class="tab-pane fade show active" id="per_details_tab">
                                    <div class="row">
                                        <div class="col-lg-9">
                                            <div class="card">
                                                <div class="card-body">
                                                    <h5 class="card-title d-flex justify-content-between">
                                                        <span>Personal Details</span>
                                                        <a class="edit-link" data-toggle="modal" href="#edit_personal_details">
                                                            <i class="far fa-edit mr-1"></i>Edit</a>
                                                    </h5>
                                                    <div class="row">
                                                        <p class="col-sm-3 text-muted text-sm-right mb-0 mb-sm-3">Full name</p>
                                                        <p class="col-sm-9">${sessionScope.user.fullName}</p>
                                                    </div>
                                                    <div class="row">
                                                        <p class="col-sm-3 text-muted text-sm-right mb-0 mb-sm-3">Date of Birth</p>
                                                        <p class="col-sm-9">${sessionScope.user.dob}</p>
                                                    </div>
                                                    <div class="row">
                                                        <p class="col-sm-3 text-muted text-sm-right mb-0 mb-sm-3">Gender</p>
                                                        <p class="col-sm-9">${sessionScope.user.gender}</p>    
                                                    </div>
                                                    <div class="row">
                                                        <p class="col-sm-3 text-muted text-sm-right mb-0 mb-sm-3">Email</p>
                                                        <p class="col-sm-9">${sessionScope.user.email}</p>    
                                                    </div>
                                                    <div class="row">
                                                        <p class="col-sm-3 text-muted text-sm-right mb-0 mb-sm-3">Mobile</p>
                                                        <p class="col-sm-9">${sessionScope.user.phone}</p>
                                                    </div>
                                                    <div class="row">
                                                        <p class="col-sm-3 text-muted text-sm-right mb-0">Address</p>
                                                        <p class="col-sm-9 mb-0">${sessionScope.user.address}</p>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div id="password_tab" class="tab-pane fade">
                                    <div class="card">
                                        <div class="card-body">
                                            <h5 class="card-title">Change Password</h5>
                                            <div class="row">
                                                <div class="col-md-10 col-lg-6">
                                                    <form action="profile" method="post" onsubmit="return validateForm()">
                                                        <input type="hidden" name="updatePart" value="password">
                                                        <div class="form-group">
                                                            <label>Old Password</label>
                                                            <input type="password" name="oldPassword" class="form-control">
                                                        </div>
                                                        <div class="form-group">
                                                            <label>New Password</label>
                                                            <input type="password" id="newPassword" name="newPassword" class="form-control">
                                                        </div>
                                                        <div class="form-group">
                                                            <label>Confirm Password</label>
                                                            <input type="password" id="newPasswordCf" class="form-control">
                                                        </div>
                                                        <button class="btn btn-primary" type="submit">Save Changes</button>
                                                    </form>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Modal -->
        <div id="edit_personal_details" class="modal fade" role="dialog">
            <div class="modal-dialog">
                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        <h4 class="modal-title">Edit Personal Details</h4>
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                    </div>
                    <div class="modal-body">
                        <form id="editPersonalDetailsForm" enctype="multipart/form-data" action="profile" method="post" onsubmit="return validateForm1()">
                            <input type="hidden" name="updatePart" value="about">
                            <div class="row">
                                <div class="col-md-6">
                                    <c:if test="${sessionScope.user.avatar != null && sessionScope.user.avatar ne ''}">
                                        <div class="align-items-center">
                                            <img class="rounded-circle mt-5" width="100px" height="100px" 
                                                 src="${sessionScope.user.avatar}"
                                                 onerror="this.onerror=null; this.src='~/${sessionScope.user.avatar}';
                                                 ">
                                        </div>
                                    </c:if>
                                    <c:if test="${sessionScope.user.avatar == null || sessionScope.user.avatar eq ''}">
                                        <div class="d-flex flex-column align-items-center text-center p-3 py-5">
                                            <img class="rounded-circle mt-5" width="100px" height="100px" 
                                                 src="~/../images/user.png"
                                                 >
                                        </div>
                                    </c:if>
                                </div>
                                <div class="col-md-6">
                                    <div class="d-flex flex-column align-items-center text-center p-3 py-5">
                                        <label for="imageFile" class="form-label">Choose image</label>
                                        <input name="avatar" class="form-control form-control-lg" id="imageFile" type="file" accept="image/png, image/jpeg, image/jpg">
                                    </div>
                                </div>
                            </div>
                            <div class="row form-row">
                                <div class="col-12 col-sm-6">
                                    <div class="form-group">
                                        <label>Full Name</label>
                                        <input type="text" name="fullName" class="form-control" value="${sessionScope.user.fullName}" required="">
                                    </div>
                                </div>
                                <div class="col-12 col-sm-6">
                                    <div class="form-group">
                                        <label>Date of Birth</label>
                                        <input type="date" name="dob" class="form-control" value="${sessionScope.user.dob}">
                                    </div>
                                </div>
                                <div class="col-12 col-sm-6">
                                    <div class="form-group">
                                        <label>Email</label>
                                        <input type="email" name="email" class="form-control" value="${sessionScope.user.email}" required="">
                                    </div>
                                </div>
                                <div class="col-12 col-sm-6">
                                    <div class="form-group">
                                        <label>Mobile</label>
                                        <input type="text" id="phone" name="phone" class="form-control" value="${sessionScope.user.phone}" required="">
                                    </div>
                                </div>
                                <div class="col-12">
                                    <div class="form-group">
                                        <label>Address</label>
                                        <input type="text" name="address" class="form-control" value="${sessionScope.user.address}" required="">
                                    </div>
                                </div>
                            </div>
                            <button type="submit" class="btn btn-primary btn-block">Save Changes</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
        <script data-cfasync="false" src="cdn-cgi/scripts/5c5dd728/cloudflare-static/email-decode.min.js"></script><script src="assets/js/jquery-3.6.0.min.js"></script>
        <script src="assets/js/popper.min.js"></script>
        <script src="assets/plugins/bootstrap/js/bootstrap.min.js"></script>
        <script src="assets/plugins/slimscroll/jquery.slimscroll.min.js"></script>
        <script src="assets/js/script.js"></script>
        <script>
                            function validatePhone() {
                                const phone = document.getElementById('phone').value;
                                var phonePattern = /^0\d{9}$/;
                                if (!phonePattern.test(phone)) {
                                    alert('Please enter a valid phone number.');
                                    return false;
                                }
                                return true;
                            }

                            function validateDOB() {
                                const dob = document.querySelector('input[name="dob"]').value;
                                const dobDate = new Date(dob);
                                const today = new Date();
                                if (dobDate >= today) {
                                    alert('Date of birth must be before the current date.');
                                    return false;
                                }
                                return true;
                            }

                            function validateForm1() {
                                return validatePhone() && validateDOB();
                            }
        </script>
        <script>


            function validateForm() {
                var newPassword = document.getElementById("newPassword").value;
                var confirmPassword = document.getElementById("newPasswordCf").value;
                var userFullName = "${sessionScope.user.fullName}"; // L?y giá tr? t? sessionScope
                var userName = "${sessionScope.user.username}"; // L?y giá tr? t? sessionScope
                var historyPasswords = '${sessionScope.user.historyPassword}'; // L?y l?ch s? m?t kh?u t? sessionScope

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

                // Ki?m tra m?t kh?u không trùng v?i 5 m?t kh?u g?n nh?t
                if (historyPasswords) {
                    var passwordHistory = JSON.parse(historyPasswords);
                    for (var i = 0; i < passwordHistory.length; i++) {
                        if (passwordHistory[i] === newPassword) {
                            alert("Password must not be the same as any of the last 5 passwords");
                            return false;
                        }
                    }
                }

                return true;
            }

        </script>
    </body>
</html>