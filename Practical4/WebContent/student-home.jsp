<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

<h2>Dear student choose from below to proceed further</h2>

<a href="<c:url value="enroll-student.html"/>">Enroll to a course</a>
<a href="<%=response.encodeURL("email-page.html") %>" style="color:red">Email Service</a>
<a href="<%=response.encodeURL("show-my-marks.html") %>" style="color:brown">Show my marks</a>
<%= request.getAttribute("user-name") %>
</body>
</html>