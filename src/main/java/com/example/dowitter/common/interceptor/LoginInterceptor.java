package com.example.dowitter.common.interceptor;

import com.example.dowitter.MemberVO;
import com.example.dowitter.common.constants.SessionConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {

    public static final List loginEssential =
            Arrays.asList("/**");
    public static final List loginInessential =
            Arrays.asList("/css/**", "/img/**", "/favicon.ico/**",  // 정적 리소스
                            "/login",   // 로그인
                            "/join",    // 회원가입
                            "/error");  // 에러


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        Object loginMember = session.getAttribute(SessionConstant.LOGIN_MEMBER);
        log.info(request.getRequestURI());

        if( loginMember == null ) {
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HttpSession session = request.getSession();
        // 로그인한 멤버
        MemberVO loginMember = (MemberVO) session.getAttribute(SessionConstant.LOGIN_MEMBER);

        if( loginMember != null && modelAndView != null ) {
            loginMember.setPassword(null);
            modelAndView.addObject("loginMember", loginMember);
            log.info(modelAndView.getViewName());
        }

        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

}
