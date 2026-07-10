<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>정보수정</title>
		<link rel="stylesheet" href="../resources/css/EditInfo.css"/>
		<link rel="stylesheet" href="../resources/css/Header.css"/>
		<script type="text/javascript" src="../resources/js/jquery-4.0.0.min.js"></script>
		<script type="text/javascript" src="../resources/js/EditInfo.js"></script>
		<script type="text/javascript" src="../resources/js/HeaderPlain.js"></script>
	</head>
	<body>
		<%@ include file="HeaderPlain.jsp"%>
		<div>계정관리</div>
		<div id="basicInfo" class="bs">
			<div>기본정보</div>
			<div>
				<img class="bs" src="https://programmers.co.kr/assets/profiles/img-profile-default-0-2b5f71cab782bc04ba248a9739013724dd9e36fbf882e5622ab4ea0cdc7f57e6.png"/>
				<svg class="bs" xmlns="http://www.w3.org/2000/svg" width="36" height="36" fill="none" viewBox="0 0 36 36" class="g_js3qful0GWzHh6xoQP"><circle cx="18" cy="18" r="18" fill="#000"></circle><path fill="#fff" d="M11.375 22.658v2.969h2.969l8.756-8.756-2.97-2.969zm14.02-8.083a.79.79 0 0 0 0-1.116l-1.852-1.852a.79.79 0 0 0-1.116 0l-1.45 1.448 2.97 2.97z"></path></svg>
				<div>
					<div>이름</div> 
					<input class="bs" type="text" value="${nickName}"/>
				</div>
				<div>
					<div>이메일</div> 
					<input class="bs" type="email" value="${email}"/>
					<div>기존 이메일과 동일합니다. 새 이메일 주소를 입력하세요.</div>
					<button class="fr bs">이메일 변경</button>
					<button class="fr bs">비밀번호 변경</button>
				</div>
				<div class="fr">
					<button class="bs">취소</button>
					<button class="bs">저장</button>
				</div>
			</div>
		</div>
		<!-- <div id="pw" class="bs">
			<div>비밀번호</div>
			<div>
				<div>비밀번호</div>
				<button class="bs">비밀번호 변경</button>
			</div>
		</div> -->
		<div id="connect" class="bs">
			<div>계정연동</div>
			<div>
				<div>
					<img class="RyPGm2iMntl88a2Z6XYZ" src="https://hera-client.grepp.co/02beb1329f49c1e1642a.png">
					<span>Google</span>
					<button class="fr bs">연결하기</button>
				</div>
				<div></div>
				<div>
					<img class="RyPGm2iMntl88a2Z6XYZ" src="https://hera-client.grepp.co/a9bd352b867488cd2b2b.png"/>
					<span class="fr">Kakao</span>
					<button class="fr bs">연결하기</button>
				</div>
			</div>
		</div>
		<div class="popupContainer">
			<div class="popupContent bs">
				<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6">
					<path stroke-linecap="round" stroke-linejoin="round" d="M6 18 18 6M6 6l12 12" />
				</svg>
				<div>
					<div class="popupBigTitle">인증번호 입력</div>
					<div class="popupSmallTitle">
						<span>zxton@naver.com</span><span>(으)로</span>
					</div>
					<div class="popupSmallTitle">
						<span>전송된 4자리 숫자를 입력하세요.</span>
					</div>
					<div class="popupBtn">0000</div> <!-- 인증번호받는곳 -->
					<div class="popupBtn">인증 완료</div>
					<div class="popupBtn">인증번호 재전송</div>
				</div>
			</div>
		</div>
	</body>
</html>