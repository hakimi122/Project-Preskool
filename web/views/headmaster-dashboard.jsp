<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>

<html lang="en">

    <!-- Mirrored from preschool.dreamguystech.com/html-template/teacher-dashboard.html by HTTrack Website Copier/3.x [XR&CO'2014], Thu, 28 Oct 2021 11:11:40 GMT -->
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0">
        <title>Headmaster - Dashboard</title>
        <link rel="shortcut icon" href="../assets/img/favicon.png">
        <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Poppins:ital,wght@0,500;0,600;0,700;1,400&amp;display=swap">
        <link rel="stylesheet" href="../assets/plugins/bootstrap/css/bootstrap.min.css">
        <link rel="stylesheet" href="../assets/plugins/fontawesome/css/fontawesome.min.css">
        <link rel="stylesheet" href="../assets/plugins/fontawesome/css/all.min.css">
        <link rel="stylesheet" href="../assets/plugins/simple-calendar/simple-calendar.css">
        <link rel="stylesheet" href="../assets/css/style.css">
    </head>
    <body>
        <c:set var="contextPath" value="${pageContext.request.contextPath}" />

        <div class="main-wrapper">
            <%@include file="header.jsp" %>
            <%@include file="headmasterLeftboard.jsp" %>

            <div class="page-wrapper">
                <div class="content container-fluid">
                    <div class="page-header">
                        <div class="row">
                            <div class="col-sm-12">
                                <h3 class="page-title">Welcome headmaster!</h3>
                                <ul class="breadcrumb">
                                    <li class="breadcrumb-item active">Dashboard</li>
                                </ul>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xl-3 col-sm-6 col-12 d-flex">
                            <div class="card bg-one w-100">
                                <div class="card-body">
                                    <div class="db-widgets d-flex justify-content-between align-items-center">
                                        <div class="db-icon">
                                            <i class="fas fa-user-graduate"></i>
                                        </div>
                                        <div class="db-info">
                                            <h3>${reporting.totalStudents}</h3>
                                            <h6>Total Students</h6>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-xl-3 col-sm-6 col-12 d-flex">
                            <div class="card bg-two w-100">
                                <div class="card-body">
                                    <div class="db-widgets d-flex justify-content-between align-items-center">
                                        <div class="db-icon">
                                            <i class="fas fa-chalkboard-teacher"></i>
                                        </div>
                                        <div class="db-info">
                                            <h3>${reporting.totalTeachers}</h3>
                                            <h6>Total Teachers</h6>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-xl-3 col-sm-6 col-12 d-flex">
                            <div class="card bg-three w-100">
                                <div class="card-body">
                                    <div class="db-widgets d-flex justify-content-between align-items-center">
                                        <div class="db-icon">
                                            <i class="fas fa-building"></i>
                                        </div>
                                        <div class="db-info">
                                            <h3>${reporting.totalClasses}</h3>
                                            <h6>Total Classes</h6>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-xl-3 col-sm-6 col-12 d-flex">
                            <div class="card bg-four w-100">
                                <div class="card-body">
                                    <div class="db-widgets d-flex justify-content-between align-items-center">
                                        <div class="db-icon">
                                            <i class="fas fa-book-open"></i>
                                        </div>
                                        <div class="db-info">
                                            <h3>${reporting.totalMaterial}</h3>
                                            <h6>Total Material</h6>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row">                  
                        <div class="col-md-12 col-lg-12">
                            <div class="card card-chart">
                                <div class="card-header">
                                    <div class="row align-items-center">
                                        <div class="col-12">
                                            <h5 class="card-title">Number of Students</h5>
                                        </div>
                                        <div class="col-12">
                                            <ul class="list-inline-group text-right mb-0 pl-0">
                                                <li class="list-inline-item">
                                                    <div class="form-group mb-0 amount-spent-select">                                                        
                                                        <select class="form-control form-control-sm" id="yearDropdown">                                                            
                                                        </select>
                                                    </div>
                                                </li>
                                            </ul>
                                        </div>
                                    </div>
                                </div>
                                <div class="card-body">
                                    <div id="bar"></div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
<!--                        <div class="col-md-6 d-flex">
                            <div class="card flex-fill">
                                <div class="card-header">
                                    <h5 class="card-title">Star Students</h5>
                                </div>
                                <div class="card-body">
                                    <div class="table-responsive">
                                        <table class="table table-hover table-center">
                                            <thead class="thead-light">
                                                <tr>
                                                    <th>ID</th>
                                                    <th>Name</th>
                                                    <th class="text-center">Rank</th>
                                                    <th class="text-center">Class</th>
                                                    <th class="text-right">Year</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:forEach items="${studentStar}" var="s">
                                                    <tr>
                                                        <td class="text-nowrap">
                                                            <div>${s.getStudentId()}</div>
                                                        </td>
                                                        <td class="text-nowrap">${s.getUsername()}</td>
                                                        <td class="text-center">${s.getRank()}</td>
                                                        <td class="text-center">${s.getClassName()}</td>
                                                        <td class="text-right">
                                                            <div>${s.getYear()}</div>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div>-->

<!--                        <div class="col-md-6 d-flex">
                            <div class="card flex-fill">
                                <div class="card-header">
                                    <h5 class="card-title">Student Activity</h5>
                                </div>
                                <div class="card-body">
                                    <ul class="activity-feed">
                                        <li class="feed-item">
                                            <div class="feed-date">Apr 13</div>
                                            <span class="feed-text"><a>Hoàng Anh Tu?n</a> won 1st place in <a>"Chess"</a></span>
                                        </li>
                                        <li class="feed-item">
                                            <div class="feed-date">Mar 21</div>
                                            <span class="feed-text"><a>Nguy?n Phúc Th?nh</a> participated in <a href="invoice.html">"Carrom"</a></span>
                                        </li>
                                        <li class="feed-item">
                                            <div class="feed-date">Feb 2</div>
                                            <span class="feed-text"><a>Ng?c ?ào Nguy?n</a>attended internation conference in <a href="profile.html">"St.John School"</a></span>
                                        </li>
                                        <li class="feed-item">
                                            <div class="feed-date">Mar 21</div>
                                            <span class="feed-text"><a>Justin Lee</a> participated in <a href="invoice.html">"Carrom"</a></span>
                                        </li>
                                    </ul>
                                </div>
                            </div>
                        </div>-->
                    </div>                 
                </div>            
            </div>
        </div>
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
        <script src="../assets/plugins/apexchart/apexcharts.min.js"></script>
        <script src="../assets/plugins/apexchart/chart-data.js"></script>
        <script src="../assets/js/jquery-3.6.0.min.js"></script>
        <script src="../assets/js/popper.min.js"></script>
        <script src="../assets/plugins/bootstrap/js/bootstrap.min.js"></script>
        <script src="../assets/plugins/slimscroll/jquery.slimscroll.min.js"></script>
        <script src="../assets/js/circle-progress.min.js"></script>

        <script src="../assets/js/script.js"></script>
    </body>
</html>