<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>비밀번호 재설정</title>
	<link rel="stylesheet" href="resources/css/SetPw.css"/>
	<script type="text/javascript" src="resources/js/jquery-4.0.0.min.js"></script>
	<script type="text/javascript" src="resources/js/SetPw.js"></script>
	<script type="text/javascript" src="resources/js/HeaderPlain.js"></script>
</head>
<body>
	<%@ include file="HeaderPlain.jsp"%>
	<div id="main" class="bs">
		<svg class="bs" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" class="size-6">
 				<path fill-rule="evenodd" d="M12 1.5a5.25 5.25 0 0 0-5.25 5.25v3a3 3 0 0 0-3 3v6.75a3 3 0 0 0 3 3h10.5a3 3 0 0 0 3-3v-6.75a3 3 0 0 0-3-3v-3c0-2.9-2.35-5.25-5.25-5.25Zm3.75 8.25v-3a3.75 3.75 0 1 0-7.5 0v3h7.5Z" clip-rule="evenodd" />
		</svg>
		<h3>비밀번호 재설정</h3>
		<div id="pwInput">
			<div>
				<div>비밀번호</div>
				<input type="password"  class="bs" placeholder="비밀번호를 입력하세요."/>
				<div>비밀번호 (영문자, 숫자 특수문자 포함 최소 8~20자)</div>
			</div>
			<div>
				<div>비밀번호 확인</div>
				<input type="password"  class="bs" placeholder="비밀번호를 한 번 더 입력하세요."/>
				<div>입력하신 비밀번호와 다릅니다.</div>
			</div>
		</div>
		<div>
			<button class="bs fr" >저장</button>
		</div>
	</div>
</body>
</html>