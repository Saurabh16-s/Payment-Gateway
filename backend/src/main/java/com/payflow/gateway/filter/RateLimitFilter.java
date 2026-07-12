package com.payflow.gateway.filter;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitFilter extends OncePerRequestFilter{
    private final Map<String,Bucket> buckets=new ConcurrentHashMap<>();

    private Bucket newBucket(){
        Bandwidth limit=Bandwidth.classic(10,Refill.greedy(10,Duration.ofMinutes(1)));
        return Bucket.builder().addLimit(limit).build();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,HttpServletResponse res,FilterChain chain)throws ServletException,IOException{
        String apiKey=req.getHeader("X-API-Key");
        if(apiKey==null)apiKey="anonymous";
        Bucket bucket=buckets.computeIfAbsent(apiKey,k->newBucket());
        if(bucket.tryConsume(1)){
            chain.doFilter(req,res);
        }else{
            res.setStatus(429);
            res.setContentType("application/json");
            res.getWriter().write("{\"error\":\"rate limit exceeded\"}");
        }
    }
}