package com.wuhao.email.interceptor;

import com.wuhao.email.domain.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 自定拦截器
 */
@Configuration
public class MyInterceptor implements WebMvcConfigurer {
    @Value("${myConfig.VERIFY_ERROR_URL}")
    private String VERIFY_ERROR_URL;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        HandlerInterceptor handlerInterceptor = new HandlerInterceptor() {
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
                HttpSession session = request.getSession();
                User user =(User) session.getAttribute("user");
                if (user==null){
                    response.sendRedirect(response.encodeRedirectURL(VERIFY_ERROR_URL));
                    return false;
                }
                return true;
            }

            @Override
            public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

            }

            @Override
            public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

            }
        };
//        registry.addInterceptor(handlerInterceptor).addPathPatterns("/**").excludePathPatterns("/user/userIndex.html").excludePathPatterns("/user/doLogin").excludePathPatterns("/register/*").excludePathPatterns("/user/product/listAllProduct");
    }
}
