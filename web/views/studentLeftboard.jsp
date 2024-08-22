
<div class="sidebar" id="sidebar">
    <div class="sidebar-inner slimscroll">
        <div id="sidebar-menu" class="sidebar-menu">
            <ul>
                <li class="menu-title">
                    <span>Dashboard</span>
                </li>
                <li class="submenu">
                    <!--<a href="#"><i class="fas fa-user-graduate"></i> <span id="logoid"> Dashboard</span> <span class="menu-arrow"></span></a>-->
                    <!--<ul>-->
                        <li><a href="${contextPath}/material?action=list" class="${currentUrl.contains('material')   ? 'active' : ''}">Materials</a></li>
                        <li><a href="${contextPath}/classstudy?action=list" class="${currentUrl.contains('classstudy')   ? 'active' : ''}">Your Classes</a></li>
                        <li><a href="${contextPath}/schedule-student" class="${currentUrl.contains('schedule')   ? 'active' : ''}">Study Schedules</a></li>
                        <li><a href="${contextPath}/exam-student" class="${(currentUrl.contains('exam')) ? 'active' : ''}">Exam Schedule</a></li>
                    <!--</ul>-->
                </li>
            </ul>
        </div>
    </div>
</div>

<script type="text/javascript">
    document.getElementById("logoid").addEventListener("click", function (event) {
        event.preventDefault();
        var userRole = "${sessionScope.user.role}";
        var contextPath = "${contextPath}";
        if (userRole === "headmaster") {
            window.location.href = contextPath + "/headmaster/dashboard";
        } else if (userRole === "teacher") {
            window.location.href = contextPath + "/teacher/dashboard";
        } else if (userRole === "student") {
            window.location.href = contextPath + "/student/dashboard";
        }
    });
</script>