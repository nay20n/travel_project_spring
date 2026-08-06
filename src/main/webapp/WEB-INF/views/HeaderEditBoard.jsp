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
		<img src="<c:url value='/resources/img/logo.png'/>"/>
		<a>트래블 플래너</a>
	</div>
	<div>
		<div data-bno="${bno}" id="shareBtn" class="bs">
			<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6">
			  <path stroke-linecap="round" stroke-linejoin="round" d="M7.217 10.907a2.25 2.25 0 1 0 0 2.186m0-2.186c.18.324.283.696.283 1.093s-.103.77-.283 1.093m0-2.186 9.566-5.314m-9.566 7.5 9.566 5.314m0 0a2.25 2.25 0 1 0 3.935 2.186 2.25 2.25 0 0 0-3.935-2.186Zm0-12.814a2.25 2.25 0 1 0 3.933-2.185 2.25 2.25 0 0 0-3.933 2.185Z" />
			</svg>
			<div>공유</div>
		</div>
		<div class="headerBtn bs">
			<div>로그아웃</div>
		</div>
		<div id="profileImg" class="bs">
			<div>마이페이지</div>
		</div>
	</div>
</div>