<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>header</title>
<link rel="stylesheet" type="text/css" href="resources/css/common.css">
</head>
<body>
	<header>
		<div>
			<h1 class="logo">
				<a href="index.do"><img src="picture/logo.png"></a>
			</h1>
			<ul class="login-menu">
				<c:choose>
					<c:when
						test="${sessionScope.userId == null || sessionScope.userId == '' }">
						<li><a href="#">로그인&nbsp;</a></li>
						<li><a href="#">회원가입&nbsp;</a></li>
					</c:when>
				</c:choose>
			</ul>
			<nav>
			<ul class = "headmenu">
				<li ><a href="movie.do">영화</a></li>
				<li ><a href="show_date.do">예매</a></li>
			</ul>
		</nav>
		</div>
		
	</header>
</body>
</html>