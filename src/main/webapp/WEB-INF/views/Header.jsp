<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<link rel="stylesheet" type="text/css" href="https://cdn.jsdelivr.net/npm/toastify-js/src/toastify.min.css">
<script type="text/javascript" src="https://cdn.jsdelivr.net/npm/toastify-js"></script>
<c:if test="${not empty msg}">
	<script>
		Toastify({
		  text: "${msg}",
		  duration: 3000,
		  newWindow: true,
		  close: true,
		  gravity: "top",
		  position: "center",
		  stopOnFocus: true,
		  style: {
		    background: "linear-gradient(to left, #E3D4FF, #925DE8)",
		  }
		}).showToast();
	</script>
</c:if>
<div id="header" class="bs">
	<div>
		<img src="resources/img/logo.png"/>
		<a>트래블 플래너</a>
	</div>
	<div>
		<div class="hide"></div>
		<div class="headerBtn bs">
			<div>로그아웃</div>
		</div>
		<div id="profileImg" class="bs">
			<div>마이페이지</div>
		</div>
	</div>
</div>