package What2Do.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

public class LoginInterceptor implements HandlerInterceptor {
    //Controller 실행 요청 전에 수행되는 메소드
    //return false일 경우 controller 실행 x
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws IOException{
        System.out.println("인터셉터 동작: " + request.getRequestURI());
        if (request.getSession().getAttribute("user")==null){
            response.sendRedirect(request.getContextPath()+
                    "/login2");
            return false;
        }
        return true;
    }

    //view단으로 forward 되기전에 수행
    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response, Object handler, ModelAndView modelAndView) {
    }
    //view까지 처리 끝난 후 수행
    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object handler, Exception ex){
    }
}
