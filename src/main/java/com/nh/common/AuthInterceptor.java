package com.nh.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.support.RequestContextUtils;

public class AuthInterceptor implements HandlerInterceptor {
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		HttpSession session = request.getSession();
		if(session!=null && session.getAttribute("loginId")!=null) return true;
		
		System.out.println("인터셉터에서 막힘 ->" + request.getRequestURI());
		
		FlashMap flashMap = new FlashMap();
		flashMap.put("msg", "로그인이 필요한 서비스입니다.");

		RequestContextUtils.getFlashMapManager(request).saveOutputFlashMap(
            flashMap,
            request,
            response
        );
		
	    response.sendRedirect("/TravelPlanner");
		return false; // -> false를 리턴하면 여기서 차단되어 컨트롤러가 실행되지 않음!
	}
}
