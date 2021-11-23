package solutions.desati.twatter.interceptors;

import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import solutions.desati.twatter.services.AuthService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    final AuthService authService;
    public AuthInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            var token = request.getHeader("Authorization")
                    .replace("Bearer ", "");
            var user = authService.getUserFromToken(token);
            if(user == null) throw new Error();
            request.setAttribute("user", user);
            return true;
        } catch (Throwable e) {
            // could not auth
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
    }
}
