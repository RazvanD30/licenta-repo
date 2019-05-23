package en.ubb.networkconfiguration.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
@Slf4j
@Aspect
public class MonitoringConfig {

    @Pointcut("execution(public * en.ubb.networkconfiguration.business.service.impl.NetworkServiceImpl.run(..))")
    public void monitor() {
    }

    @Bean
    public CustomPerformanceMonitorInterterceptor performanceMonitorInterceptor() {
        return new CustomPerformanceMonitorInterterceptor(true);
    }

    @Bean
    public Advisor performanceMonitorAdvisor() {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("en.ubb.networkconfiguration.config.MonitoringConfig.monitor()");
        return new DefaultPointcutAdvisor(pointcut, performanceMonitorInterceptor());
    }
}
