<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="currentUrl" value="${pageContext.request.requestURI}" />
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<div class="sidebar" id="sidebar">
    <div class="sidebar-inner slimscroll">
        <div id="sidebar-menu" class="sidebar-menu">
            <ul>
                <li class="menu-title">
                    <span>Main Menu</span>
                </li>

                <li class="submenu">
                    <!--<a href="#"><i class="fas fa-user-graduate"></i> <span id="logoid"> Commons</span> <span class="menu-arrow"></span></a>-->
                    <!--<ul>-->
                        <li><a href="${contextPath}/classincharge?action=list" class="${currentUrl.contains('class') ? 'active' : ''}">Classes</a></li>
                        <li><a href="${contextPath}/assessment?action=list" class="${currentUrl.contains('assess') ? 'active' : ''}">Assessment</a></li>
                        <li><a href="${contextPath}/material?action=list" class="${currentUrl.contains('material')   ? 'active' : ''}">Materials</a></li>
                        <li><a href="${contextPath}/schedule-teacher" class="${(currentUrl.contains('schedule') or currentUrl.contains('attendance'))  ? 'active' : ''}">Schedule</a></li>
                        <li><a href="${contextPath}/exam-teacher" class="${(currentUrl.contains('exam') or currentUrl.contains('attendance'))  ? 'active' : ''}">Exam Schedule</a></li>

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