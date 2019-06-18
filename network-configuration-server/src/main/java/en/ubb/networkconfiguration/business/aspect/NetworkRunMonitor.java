package en.ubb.networkconfiguration.business.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@EnableAspectJAutoProxy
@Aspect
public class NetworkRunMonitor {

    @Pointcut("execution(public * en.ubb.networkconfiguration.business.service.impl.NetworkServiceImpl.run(..))")
    public void monitor() {
    }

    @Around("monitor()")
    public Object logNetworkRun(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        String className = proceedingJoinPoint.getTarget().getClass().getCanonicalName();
        String methodName = proceedingJoinPoint.getSignature().getName();
        log.info("Entering method: " + className + "." + methodName);

        Object obj;
        try {
            obj = proceedingJoinPoint.proceed();
            long endTime = System.currentTimeMillis();
            log.info("Method " + className + "." + methodName + " execution lasted: " + ((endTime - startTime) / 1000f) + " seconds");
            return obj;
        } finally {
            log.info("Exiting method : " + className + "." + methodName);
        }
    }
}
