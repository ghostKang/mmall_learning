package com.mmall.Interceptor;

import com.mmall.common.Const;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 拦截器
 * Created by Yuk on 2018/2/5.
 */
public class BackManagerInterceptor implements HandlerInterceptor {
    @Autowired
    private IUserService iUserService;

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("执行到了afterCompletion");
    }

    // ModelAndView：改变显示的视图或修改发往视图的目标
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        //modelAndView.addObject("msg","被拦截器修改后的消息！");
        System.out.println("执行到了postHandle");
    }

    // 返回值：是否需要将当前的额请求拦截下来，false:请求终止；true:请求继续
    // Object：被拦截的请求的目标对象
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("执行到了preHandle");
        // 设置编码
       /* request.setCharacterEncoding("utf-8");

        // 判断用户是否登录
        if(request.getSession().getAttribute("user")==null){
            // 跳到登录页面
            //request.getRequestDispatcher("/login.jsp").forward(request,response);// 转发
            response.sendRedirect("/login.jsp");// 重定向
            return false;
        }*/
        User user = (User)request.getSession().getAttribute(Const.CURRENT_USER);
        if(user == null){
            // 未登录
            request.getRequestDispatcher("/NotLogin.jsp").forward(request,response);// 转发
            return false;
        }
        if(!iUserService.checkAdminRole(user).isSuccess()){
            // 无管理员权限
            request.getRequestDispatcher("/Non-administrator.jsp").forward(request,response);// 转发
            return false;
        }
        return true;
    }
}
