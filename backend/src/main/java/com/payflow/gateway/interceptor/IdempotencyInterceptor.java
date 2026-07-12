package com.payflow.gateway.interceptor;

import com.payflow.gateway.service.IdempotencyService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class IdempotencyInterceptor implements HandlerInterceptor{
    private final IdempotencyService idempotencyService;

    @Override
    public boolean preHandle(HttpServletRequest req,HttpServletResponse res,Object handler)throws Exception{
        if(!"POST".equals(req.getMethod()))return true;

        String key=req.getHeader("Idempotency-Key");
        if(key==null)return true;

        Optional<String> cached=idempotencyService.getCachedResponse(key);
        if(cached.isPresent()){
            res.setContentType("application/json");
            res.getWriter().write(cached.get());
            return false;
        }
        return true;
    }
}