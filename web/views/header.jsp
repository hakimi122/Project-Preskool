<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<div class="header">
    <div class="header-left">
        <a id="logoid" href="#" class="logo">
            <img src="../assets/img/logo.png" alt="Logo"
                 onerror="this.onerror=null; this.src='./assets/img/logo.png';"
                 >
        </a>

    </div>

    <a class="mobile_btn" id="mobile_btn">
        <i class="fas fa-bars"></i>
    </a>

    <ul class="nav user-menu">

        <li class="nav-item dropdown has-arrow">
            <a href="#" class="dropdown-toggle nav-link" data-toggle="dropdown">
                <span class="user-img">
                    <img class="rounded-circle" src="${sessionScope.user.avatar}" 
                         onerror="this.onerror=null; this.src='https://w7.pngwing.com/pngs/205/731/png-transparent-default-avatar-thumbnail.png';"
                         width="31"></span>
            </a>
            <div class="dropdown-menu">
                <div class="user-header">
                    <div class="avatar avatar-sm">
                        <img src="${sessionScope.user.avatar}" 
                         onerror="this.onerror=null; this.src='https://w7.pngwing.com/pngs/205/731/png-transparent-default-avatar-thumbnail.png';"
                             class="avatar-img rounded-circle">
                    </div>
                    <div class="user-text">
                        <h6>${sessionScope.user.fullName}</h6>
                        <p class="text-muted mb-0">${sessionScope.user.getRole()}</p>
                    </div>
                </div>
                <c:if test="${sessionScope.user != null && sessionScope.user.avatar ne 'headmaster'}">
                    <a class="dropdown-item" href="${contextPath}/profile">My Profile</a>
                </c:if>
                <a class="dropdown-item" href="${contextPath}/logout">Logout</a>
            </div>
        </li>

    </ul>
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
        }
        else if (userRole === "student") {
            window.location.href = contextPath + "/student/dashboard";
        }
    });
</script>
