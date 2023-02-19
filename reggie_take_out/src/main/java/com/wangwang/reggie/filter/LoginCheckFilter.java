package com.wangwang.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.wangwang.reggie.common.BaseContext;
import com.wangwang.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 拦截是否登录
 */
@WebFilter(filterName = "LoginCheckFilter", urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {
    //路径匹配器
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //1、获取本次请求的UR木
        String requestURI = request.getRequestURI();
        //log.info("拦截到请求: {}", requestURI);
        //定义不需要拦截的请求
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**",
                "/user/sendMsg",
                "/user/login"
        };

        //2、判断本次请求是否需要处理
        boolean check = check(urls, requestURI);
        //3、如果不需要处理，则直接放行
        if (check) {
            //log.info("不需要处理的请求:{}",requestURI);
            filterChain.doFilter(request,response);
            return;
        }
        //4.1、判断登录状态，如果已登录，则直接放行
        if (request.getSession().getAttribute("employee") != null){
            //log.info("用户已登录,用户id为:{}",request.getSession().getAttribute("employee"));
            //long id = Thread.currentThread().getId();
            //log.info("线程id为:{}",id);
            Long empId = (Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empId);

            filterChain.doFilter(request,response);
            return;
        }

        //4.2、判断登录状态，如果已登录，则直接放行
        if (request.getSession().getAttribute("user") != null){
            //log.info("用户已登录,用户id为:{}",request.getSession().getAttribute("employee"));
            //long id = Thread.currentThread().getId();
            //log.info("线程id为:{}",id);
            Long userId = (Long) request.getSession().getAttribute("user");
            BaseContext.setCurrentId(userId);

            filterChain.doFilter(request,response);
            return;
        }
        //5、如果未登录则返回未登录结果
        log.info("用户未登录!");
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;

        //log.info("拦截到请求: {}", request.getRequestURL());
    }

    /**
     * 路径匹配，检查是否放行
     *
     * @param urls
     * @param requestURI
     * @return
     */
    public boolean check(String[] urls, String requestURI) {
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if (match) {
                return true;
            }
        }
        return false;
    }
}
