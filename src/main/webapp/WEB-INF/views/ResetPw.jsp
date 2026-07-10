<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>비밀번호 재설정</title>
		<link rel="stylesheet" href="resources/css/ResetPw.css"/>
		<script type="text/javascript" src="resources/js/jquery-4.0.0.min.js"></script>
		<script type="text/javascript" src="resources/js/ResetPw.js"></script>
		<script type="text/javascript" src="resources/js/HeaderPlain.js"></script>
	</head>
	<body>
		<%@ include file="HeaderPlain.jsp"%>
		<div id="main" class="bs">
			<svg class="bs" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" class="size-6">
  				<path fill-rule="evenodd" d="M12 1.5a5.25 5.25 0 0 0-5.25 5.25v3a3 3 0 0 0-3 3v6.75a3 3 0 0 0 3 3h10.5a3 3 0 0 0 3-3v-6.75a3 3 0 0 0-3-3v-3c0-2.9-2.35-5.25-5.25-5.25Zm3.75 8.25v-3a3.75 3.75 0 1 0-7.5 0v3h7.5Z" clip-rule="evenodd" />
			</svg>
			<h3>비밀번호 재설정</h3>
			<div>가입 시 등록한 이메일 주소를 입력해주세요.<br/> 비밀번호 재설정 링크가 포함된 메일을 발송합니다. <br/>메일이 보이지 않는 경우 스팸함을 확인해주세요.</div>
			<div>비밀번호를 재설정 할 이메일</div>
			<input class="bs" type="email" placeholder="이메일을 입력해 주세요."/>
			<svg xmlns="http://www.w3.org/2000/svg" width="18" height="15" fill="none" viewBox="0 0 18 15"><g stroke="#000" opacity=".2"><rect width="16.659" height="13.263" x=".5" y=".5" rx="1.5"></rect><path d="m.679.68 8.15 5.433L16.98.68"></path></g></svg>
			<div>가입 시 등록한 이메일이 아닙니다.</div>
			<button class="bs" >비밀번호 재설정 메일 보내기</button>
		</div>
	</body>
	
</html>