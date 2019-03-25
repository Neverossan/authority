package com.mmall.common;

import com.mmall.util.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.mail.Session;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @Auther: HP
 * @Date: 2019/3/21 19:21
 * @Description:
 */
@Slf4j
public class HttpInterceptor extends HandlerInterceptorAdapter {

    private static final String START_TIME = "requestStartTime";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String url = request.getRequestURI().toString();
        Map map = request.getParameterMap();
        log.info("request start. url:{}, params:{}", url, JsonMapper.obj2String(map));
        Long start = System.currentTimeMillis();
        request.getSession().setAttribute(START_TIME,start);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//        String url = request.getRequestURI().toString();
//        Map map = request.getParameterMap();
//        Long start = (Long) request.getSession().getAttribute(START_TIME);
//        Long end = System.currentTimeMillis();
//        log.info("request finished. url:{}, cost:{}", url, end - start);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String url = request.getRequestURI().toString();
        Long start = (Long) request.getSession().getAttribute(START_TIME);
        Long end = System.currentTimeMillis();
        log.info("request complete. url:{}, cost:{}", url, end - start);
    }
}
