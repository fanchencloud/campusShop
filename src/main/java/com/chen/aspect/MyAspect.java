package com.chen.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * @author hll
 */
@Aspect
@Component
public class MyAspect {
    /**
     * 日志记录器
     */
    private final static Logger logger = LoggerFactory.getLogger(MyAspect.class);

    /**
     * 这个切点的表达式需要根据自己的项目来写
     */
    @Pointcut("execution(public * com.chen.controller..*(..))")
    public void log() {

    }

    @Before("log()")
    public void doBefore(JoinPoint joinPoint) {
        logger.info("aop doBefore..");
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();

        //url
        logger.info("url={}", request.getRequestURI());

        //method
        logger.info("method={}", request.getMethod());

        //ip
        logger.info("ip={}", request.getRemoteAddr());

        //类方法
        logger.info("classMethod={}", joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());

        //参数
        Enumeration<String> parameter = request.getParameterNames();
        while (parameter.hasMoreElements()) {
            String str = parameter.nextElement();
            logger.info(str + "={}", request.getParameter(str));
        }

    }

    @After("log()")
    public void doAfter() {
        logger.info("aop doAfter");
    }
}

