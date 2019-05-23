package en.ubb.networkconfiguration.business.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Component
@EnableAspectJAutoProxy
@Aspect
public class ServiceLogger {


    @Pointcut("execution(* en.ubb.networkconfiguration.business.service.impl.*.*(..))")
    public void monitor() {
    }

    @Before("monitor()")
    public void beforeMethod(JoinPoint joinPoint){
        log.info("Enter method {} with args {}",
                joinPoint.getTarget().getClass() + "." + joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));
    }
}
