<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0">
        <title>teacher list</title>
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
            <%@include file="headmasterLeftboard.jsp" %>

            <div class="page-wrapper">
                <div class="content container-fluid">
                    <div class="page-header">
                        <div class="row align-items-center">
                            <div class="col">
                                <h3 class="page-title">Teachers</h3>
                                <ul class="breadcrumb">
                                    <li class="breadcrumb-item"><a href="#">Dashboard</a></li>
                                    <li class="breadcrumb-item active">Teachers</li>
                                </ul>
                            </div>
                            <div class="col-auto text-right float-right ml-auto">
                                <a href="${contextPath}/teacher?action=add" class="btn btn-primary"><i class="fas fa-plus"></i></a>
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
                                                    <th>ID</th>
                                                    <th>Name</th>
                                                    <th>Dob</th>
                                                    <th>Gender</th>
                                                    <th>Phone</th>
                                                    <th>Email</th>
                                                    <th>Address</th>
                                                    <th class="text-right">Action</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:if test="${teachers != null}">
                                                    <c:forEach items="${teachers}" var="t">
                                                        <tr id="row_${t.userId}">
                                                            <td>${t.userId}</td>
                                                            <td>
                                                                <h2 class="table-avatar">

                                                                    <a href="${contextPath}/teacher?action=details&userId=${t.userId}" class="avatar avatar-sm mr-2">
                                                                        <img class="avatar-img rounded-circle" src="${t.avatar}" 
                                                                             onerror="this.onerror=null; this.src='./images/user.png';"
                                                                             alt="User Image">
                                                                    </a>
                                                                    <a href="${contextPath}/teacher?action=details&userId=${t.userId}">${t.fullName}</a>
                                                                </h2>
                                                            </td>
                                                            <td>${t.dob}</td>
                                                            <td>${t.gender}</td>
                                                            <td>${t.phone}</td>
                                                            <td>${t.email}</td>
                                                            <td>${t.address}</td>
                                                            <td class="text-right">
                                                                <div class="actions">
                                                                    <a href="${contextPath}/teacher?action=assignsubject&teacherId=${t.userId}" class="btn btn-outline-primary ml-2"><i class="fas fa-user-edit"></i>Assign</a>

                                                                    <a href="#" class="btn btn-sm bg-success-light mr-2 editBtn"
                                                                       data-userid="${t.userId}"
                                                                       data-fullname="${t.fullName}"
                                                                       data-dob="${t.dob}"
                                                                       data-gender="${t.gender}"
                                                                       data-phone="${t.phone}"
                                                                       data-email="${t.email}"
                                                                       data-address="${t.address}">
                                                                        <i class="fas fa-pen"></i>
                                                                    </a>
                                                                    <a href="#" class="btn btn-sm bg-danger-light deleteBtn" data-userid="${t.userId}">
                                                                        <i class="fas fa-trash"></i>
                                                                    </a>
                                                                </div>
                                                            </td>
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
        <div class="modal fade" id="editUserModal" tabindex="-1" role="dialog" aria-labelledby="editUserModalLabel" aria-hidden="true">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="editUserModalLabel">Edit Teacher Information</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <form id="editUserForm" action="${contextPath}/teacher" method="post" onsubmit="return validateForm()">
                            <input type="hidden" class="form-control" name="type" value="edit">
                            <div class="form-group">
                                <label for="editFullName">Name:</label>
                                <input type="text" class="form-control" id="nameEdit" name="fullName" placeholder="Enter name">
                            </div>
                            <div class="form-group">
                                <label for="editDob">Dob</label>
                                <input type="date" class="form-control" id="dobEdit" name="dob" placeholder="Enter name">
                            </div>
                            <div class="form-group">
                                <label for="editGender">Gender</label>
                                <select id="genderEdit" name="gender" class="form-control">
                                    <option value="Male">Male</option>
                                    <option value="Female">Female</option>
                                    <option value="Other">Other</option>
                                </select>
                            </div>
                            <div class="form-group">
                                <label for="phoneEdit">Phone</label>
                                <input type="text" class="form-control" id="editPhone" name="phone">
                            </div>
                            <div class="form-group">
                                <label for="editEmail">Email</label>
                                <input type="text" class="form-control" id="emailEdit" name="email" placeholder="Enter email">
                            </div>
                            <div class="form-group">
                                <label for="editAddress">Address</label>
                                <input type="text" class="form-control" id="addressEdit" name="address" placeholder="Enter address">
                            </div>
                            <input type="hidden" id="editUserId" name="userId">
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                        <button type="button" class="btn btn-primary" id="saveChangesBtn">Save changes</button>
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
            });
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
            $(document).ready(function ()
            {
                $(document).on('click', '.editBtn', function (e) {
                    e.preventDefault();
                    var userId = $(this).data('userid');
                    var fullName = $(this).data('fullname');
                    var dob = $(this).data('dob');
                    var gender = $(this).data('gender');
                    var phone = $(this).data('phone');
                    var email = $(this).data('email');
                    var address = $(this).data('address');
                    console.log(gender);

                    // Populate the modal with user information
                    $('#editUserId').val(userId);
                    $('#nameEdit').val(fullName);
                    $('#dobEdit').val(dob);
                    $('#genderEdit').val(gender); // Select the corresponding gender value
                    $('#editPhone').val(phone);
                    $('#emailEdit').val(email);
                    $('#addressEdit').val(address);

                    // Display the modal
                    $('#editUserModal').modal('show');
                });

                $(document).on('click', '#saveChangesBtn', function (e) {
                    e.preventDefault();

                    $('#editUserForm').submit(); // Submit the form
                });
            });

        </script>

        <script>
            function validateForm() {
                var fullName = document.getElementById("nameEdit").value;
                var dob = document.getElementById("dobEdit").value;
                var gender = document.getElementById("genderEdit").value;
                var phone = document.getElementById("editPhone").value;
                var email = document.getElementById("emailEdit").value;
                var address = document.getElementById("addressEdit").value;

                // Validate Full Name
                if (!fullName || fullName.length < 2) {
                    alert("Full Name must be at least 2 characters long");
                    return false;
                }

                // Validate Date of Birth
                if (!dob) {
                    alert("Date of Birth is required");
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

                const dobDate = new Date(dob);
                const today = new Date();
                if (dobDate >= today) {
                    alert('Date of birth must be before the current date.');
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
        </script>

    </body>
</html>