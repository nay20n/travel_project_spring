<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Weekly</title>
	<link rel="stylesheet" href="../../resources/css/MainWeek.css"/>
	<link rel="stylesheet" href="../../resources/css/Header.css"/>
	<link rel="stylesheet" href="../../resources/css/BoardTitle.css"/>
	<script type="text/javascript" src="../../resources/js/jquery-4.0.0.min.js"></script>
	<script type="text/javascript" src="../../resources/js/MainPlan.js"></script>
	<script type="text/javascript" src="../../resources/js/MainWeek.js"></script>
	<script type="text/javascript" src="../../resources/js/HeaderEditBoard.js"></script>
	<script type="text/javascript" src="../../resources/js/BoardTitle.js"></script>
</head>
<body>
	<%@ include file="HeaderEditBoard.jsp"%>
	<%@ include file="BoardTitle.jsp"%>
	<div id="main">
		<div class="bs">
			<div class="inputBdDiv">
				<label>
					<input type="search" placeholder="음식점, 카페, 명소" />
					<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6">
	  					<path stroke-linecap="round" stroke-linejoin="round" d="m21 21-5.197-5.197m0 0A7.5 7.5 0 1 0 5.196 5.196a7.5 7.5 0 0 0 10.607 10.607Z" />
					</svg>
				</label>
			</div>
			<div>
				<div class="bs">내 일정</div>
				<div class="bs">
					My
					<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" class="size-6">
						<path fill-rule="evenodd" d="M10.788 3.21c.448-1.077 1.976-1.077 2.424 0l2.082 5.006 5.404.434c1.164.093 1.636 1.545.749 2.305l-4.117 3.527 1.257 5.273c.271 1.136-.964 2.033-1.96 1.425L12 18.354 7.373 21.18c-.996.608-2.231-.29-1.96-1.425l1.257-5.273-4.117-3.527c-.887-.76-.415-2.212.749-2.305l5.404-.434 2.082-5.005Z" clip-rule="evenodd" />
					</svg>
				</div>
			</div>
			<c:forEach var="places" items="${placesList}">
				<div class="place" data-placeId="${places.placeId}">
		            <div>
		                <img src="../../resources/img/장소예시이미지.png"/>
		            </div>
					<div class="placeInfo">
						<div class="placeTitle">
							<div>
								<a>${places.name}</a><span>${places.category}</span>
							</div>
							<div>
								<c:choose>
									<c:when test="${places.isLiked==1}">
										<svg  class="fillStar" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6">
											<path stroke-linecap="round" stroke-linejoin="round" d="M11.48 3.499a.562.562 0 0 1 1.04 0l2.125 5.111a.563.563 0 0 0 .475.345l5.518.442c.499.04.701.663.321.988l-4.204 3.602a.563.563 0 0 0-.182.557l1.285 5.385a.562.562 0 0 1-.84.61l-4.725-2.885a.562.562 0 0 0-.586 0L6.982 20.54a.562.562 0 0 1-.84-.61l1.285-5.386a.562.562 0 0 0-.182-.557l-4.204-3.602a.562.562 0 0 1 .321-.988l5.518-.442a.563.563 0 0 0 .475-.345L11.48 3.5Z" />
										</svg>
									</c:when>
									<c:otherwise>
										<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6">
											<path stroke-linecap="round" stroke-linejoin="round" d="M11.48 3.499a.562.562 0 0 1 1.04 0l2.125 5.111a.563.563 0 0 0 .475.345l5.518.442c.499.04.701.663.321.988l-4.204 3.602a.563.563 0 0 0-.182.557l1.285 5.385a.562.562 0 0 1-.84.61l-4.725-2.885a.562.562 0 0 0-.586 0L6.982 20.54a.562.562 0 0 1-.84-.61l1.285-5.386a.562.562 0 0 0-.182-.557l-4.204-3.602a.562.562 0 0 1 .321-.988l5.518-.442a.563.563 0 0 0 .475-.345L11.48 3.5Z" />
										</svg>
									</c:otherwise>
								</c:choose>
							</div>
						</div>
						<div>
							<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" class="size-6">
								<path fill-rule="evenodd" d="M10.788 3.21c.448-1.077 1.976-1.077 2.424 0l2.082 5.006 5.404.434c1.164.093 1.636 1.545.749 2.305l-4.117 3.527 1.257 5.273c.271 1.136-.964 2.033-1.96 1.425L12 18.354 7.373 21.18c-.996.608-2.231-.29-1.96-1.425l1.257-5.273-4.117-3.527c-.887-.76-.415-2.212.749-2.305l5.404-.434 2.082-5.005Z" clip-rule="evenodd" />
							</svg>
							<span>${places.avgRating}</span><span>(${places.reviewCnt})</span>
						</div>
						<div>${places.address}</div>
					</div>
				</div>
			</c:forEach>
		</div>
		<div>
			<div>
				<h2 class="date fl">2026년 5월</h2>
				<div class="changeView fl bs" >
					<span>일</span><span>주</span><span>월</span>
				</div>
				<span class="fr moveDate">></span> <span class="fr moveDate">오늘</span>
				<span class="fr moveDate">&lt;</span>
				<div style="clear: both;"></div>
			</div>
			<div class="calendar">
				<!-- 일정표 -->
			</div>
			<button class="bs">일정 확정하기</button>
		</div>
	</div>
	<div class="popupContainer hide">
		<div class="popupContent bs hide">
			<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6">
  				<path stroke-linecap="round" stroke-linejoin="round" d="M6 18 18 6M6 6l12 12" />
			</svg>
			<div>
				<div>
	                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6">
					  <path stroke-linecap="round" stroke-linejoin="round" d="M15.75 19.5 8.25 12l7.5-7.5" />
					</svg>
					<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6">
					  <path stroke-linecap="round" stroke-linejoin="round" d="m8.25 4.5 7.5 7.5-7.5 7.5" />
					</svg>
	            </div>
				<div class="popupPlace">
					<div>
						<div>
							<a></a> <!-- 장소이름 -->
						</div>
						<div>
							<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6">
		  						<path stroke-linecap="round" stroke-linejoin="round" d="M11.48 3.499a.562.562 0 0 1 1.04 0l2.125 5.111a.563.563 0 0 0 .475.345l5.518.442c.499.04.701.663.321.988l-4.204 3.602a.563.563 0 0 0-.182.557l1.285 5.385a.562.562 0 0 1-.84.61l-4.725-2.885a.562.562 0 0 0-.586 0L6.982 20.54a.562.562 0 0 1-.84-.61l1.285-5.386a.562.562 0 0 0-.182-.557l-4.204-3.602a.562.562 0 0 1 .321-.988l5.518-.442a.563.563 0 0 0 .475-.345L11.48 3.5Z" />
							</svg>
						</div>
					</div>
					<div>
						<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"
							fill="currentColor" class="size-6">
							<path fill-rule="evenodd" d="M10.788 3.21c.448-1.077 1.976-1.077 2.424 0l2.082 5.006 5.404.434c1.164.093 1.636 1.545.749 2.305l-4.117 3.527 1.257 5.273c.271 1.136-.964 2.033-1.96 1.425L12 18.354 7.373 21.18c-.996.608-2.231-.29-1.96-1.425l1.257-5.273-4.117-3.527c-.887-.76-.415-2.212.749-2.305l5.404-.434 2.082-5.005Z" clip-rule="evenodd" />
						</svg>
						<span></span><span></span> <!-- 댓글 수 -->
					</div>
					<div></div>
					<div class="placeDetail">
						<div>
							<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6">
								<path stroke-linecap="round" stroke-linejoin="round" d="M15 10.5a3 3 0 1 1-6 0 3 3 0 0 1 6 0Z" />
								<path stroke-linecap="round" stroke-linejoin="round" d="M19.5 10.5c0 7.142-7.5 11.25-7.5 11.25S4.5 17.642 4.5 10.5a7.5 7.5 0 1 1 15 0Z" />
							</svg>
							<span></span> <!-- 주소 -->
						</div>
						<div>
							<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6">
								<path stroke-linecap="round" stroke-linejoin="round" d="M12 6v6h4.5m4.5 0a9 9 0 1 1-18 0 9 9 0 0 1 18 0Z" />
							</svg>
							영업시간
							<svg class="hide" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6">
								<path stroke-linecap="round" stroke-linejoin="round" d="m19.5 8.25-7.5 7.5-7.5-7.5" />
							</svg>
							<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6">
								<path stroke-linecap="round" stroke-linejoin="round" d="m4.5 15.75 7.5-7.5 7.5 7.5" />
							</svg>
						</div>
						<div>
							<div></div>
							<div></div>
							<div></div>
							<div></div>
							<div></div>
							<div></div>
							<div></div>
						</div>
						<div>
							<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6">
								<path stroke-linecap="round" stroke-linejoin="round" d="M13.19 8.688a4.5 4.5 0 0 1 1.242 7.244l-4.5 4.5a4.5 4.5 0 0 1-6.364-6.364l1.757-1.757m13.35-.622 1.757-1.757a4.5 4.5 0 0 0-6.364-6.364l-4.5 4.5a4.5 4.5 0 0 0 1.242 7.244" />
							</svg>
							<div></div> <!-- url -->
						</div>
						<div>
							<!-- 별점박스 -->
							<div>
								<div>
									<span>5</span>
									<div>
										<div class="graph"></div>
									</div>
								</div>
								<div>
									<span>4</span>
									<div>
										<div class="graph"></div>
									</div>
								</div>
								<div>
									<span>3</span>
									<div>
										<div class="graph"></div>
									</div>
								</div>
								<div>
									<span>2</span>
									<div>
										<div class="graph"></div>
									</div>
								</div>
								<div>
									<span>1</span>
									<div>
										<div class="graph"></div>
									</div>
								</div>
								<div>
									<span>0</span>
									<div>
										<div class="graph"></div>
									</div>
								</div>
							</div>
							<div>
								<div></div> <!-- 평균 평점 -->
								<div>
									<div></div> <!-- 평균 평점 그리기 -->
								</div>
								<div></div> <!-- 리뷰 수 -->
							</div>
						</div>
					</div>
					<div class=reviewInput>
						<div>
							<svg class="inputStar" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6">
  								<path stroke-linecap="round" stroke-linejoin="round" d="M11.48 3.499a.562.562 0 0 1 1.04 0l2.125 5.111a.563.563 0 0 0 .475.345l5.518.442c.499.04.701.663.321.988l-4.204 3.602a.563.563 0 0 0-.182.557l1.285 5.385a.562.562 0 0 1-.84.61l-4.725-2.885a.562.562 0 0 0-.586 0L6.982 20.54a.562.562 0 0 1-.84-.61l1.285-5.386a.562.562 0 0 0-.182-.557l-4.204-3.602a.562.562 0 0 1 .321-.988l5.518-.442a.563.563 0 0 0 .475-.345L11.48 3.5Z" />
							</svg>
							<svg class="inputStar" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6">
  								<path stroke-linecap="round" stroke-linejoin="round" d="M11.48 3.499a.562.562 0 0 1 1.04 0l2.125 5.111a.563.563 0 0 0 .475.345l5.518.442c.499.04.701.663.321.988l-4.204 3.602a.563.563 0 0 0-.182.557l1.285 5.385a.562.562 0 0 1-.84.61l-4.725-2.885a.562.562 0 0 0-.586 0L6.982 20.54a.562.562 0 0 1-.84-.61l1.285-5.386a.562.562 0 0 0-.182-.557l-4.204-3.602a.562.562 0 0 1 .321-.988l5.518-.442a.563.563 0 0 0 .475-.345L11.48 3.5Z" />
							</svg>
							<svg class="inputStar" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6">
  								<path stroke-linecap="round" stroke-linejoin="round" d="M11.48 3.499a.562.562 0 0 1 1.04 0l2.125 5.111a.563.563 0 0 0 .475.345l5.518.442c.499.04.701.663.321.988l-4.204 3.602a.563.563 0 0 0-.182.557l1.285 5.385a.562.562 0 0 1-.84.61l-4.725-2.885a.562.562 0 0 0-.586 0L6.982 20.54a.562.562 0 0 1-.84-.61l1.285-5.386a.562.562 0 0 0-.182-.557l-4.204-3.602a.562.562 0 0 1 .321-.988l5.518-.442a.563.563 0 0 0 .475-.345L11.48 3.5Z" />
							</svg>
							<svg class="inputStar" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6">
  								<path stroke-linecap="round" stroke-linejoin="round" d="M11.48 3.499a.562.562 0 0 1 1.04 0l2.125 5.111a.563.563 0 0 0 .475.345l5.518.442c.499.04.701.663.321.988l-4.204 3.602a.563.563 0 0 0-.182.557l1.285 5.385a.562.562 0 0 1-.84.61l-4.725-2.885a.562.562 0 0 0-.586 0L6.982 20.54a.562.562 0 0 1-.84-.61l1.285-5.386a.562.562 0 0 0-.182-.557l-4.204-3.602a.562.562 0 0 1 .321-.988l5.518-.442a.563.563 0 0 0 .475-.345L11.48 3.5Z" />
							</svg>
							<svg class="inputStar" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6">
  								<path stroke-linecap="round" stroke-linejoin="round" d="M11.48 3.499a.562.562 0 0 1 1.04 0l2.125 5.111a.563.563 0 0 0 .475.345l5.518.442c.499.04.701.663.321.988l-4.204 3.602a.563.563 0 0 0-.182.557l1.285 5.385a.562.562 0 0 1-.84.61l-4.725-2.885a.562.562 0 0 0-.586 0L6.982 20.54a.562.562 0 0 1-.84-.61l1.285-5.386a.562.562 0 0 0-.182-.557l-4.204-3.602a.562.562 0 0 1 .321-.988l5.518-.442a.563.563 0 0 0 .475-.345L11.48 3.5Z" />
							</svg>
						</div>
						<div class="inputBdDiv" id="textarea">
							<textarea></textarea>
						</div>
						<div>
							<div class="inputBdDiv">사진 추가</div>
							<div class="inputBdDiv">댓글 등록</div>
						</div>
					</div>
					<div>
						
					</div>
				</div>
			</div>
		</div>
		<div class="popupContent bs hide">
			<svg class="bs" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6">
	  			<path stroke-linecap="round" stroke-linejoin="round" d="M6 18 18 6M6 6l12 12" />
			</svg>
			<div>
				<div>
					<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6">
						<path stroke-linecap="round" stroke-linejoin="round" d="M12 6v6h4.5m4.5 0a9 9 0 1 1-18 0 9 9 0 0 1 18 0Z" />
					</svg>
					<span>16:00 ~ 18:00</span>
					<span class="setBlockColor">●</span>
				</div>
				<div>
					<input class="checkBox" type="checkbox" id="agree_check" value="0"/>
					<span>AI 반영</span>
				</div>
			</div>
			<div>
				<div class="map">
					<!-- 장소좌표 -->
				</div>
				<div>
					<div>로우앤스윗 로스터리 (Raw&Sweet Roastery Cafe)</div>
					<div>카페</div>
					<div>대한민국 부산광역시 해운대구 우동1로20번길 19 1층</div>
				</div>
			</div>
		</div>
		<div class="popupContent bs hide">
			<span class="blockColor">●</span>
			<span class="blockColor">●</span>
			<span class="blockColor">●</span>
			<span class="blockColor">●</span>
			<span class="blockColor">●</span>
		</div>
		<div class="popupContent bs hide">
			<span></span>
			<span></span>
			<span></span>
			<span></span>
			<span></span>
		</div>
	</div>
</body>
</html>