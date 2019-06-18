package en.ubb.networkconfiguration.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.springframework.aop.interceptor.AbstractMonitoringInterceptor;

import java.util.Date;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomPerformanceMonitorInterterceptor extends AbstractMonitoringInterceptor {

    private float MAX_TIME = 2.0f;

    public CustomPerformanceMonitorInterterceptor(boolean useDynamicLogger) {
        setUseDynamicLogger(useDynamicLogger);
    }

    @Override
    protected Object invokeUnderTrace(MethodInvocation invocation, Log log) throws Throwable {
        String name = createInvocationTraceName(invocation);
        long start = System.currentTimeMillis();
        log.info("Method " + name + " execution started at:" + new Date());
        try {
            return invocation.proceed();
        } finally {
            long end = System.currentTimeMillis();
            float timeSeconds = (end - start)/1000f;
            log.info("Method " + name + " execution lasted:" + timeSeconds + " seconds");
            log.info("Method " + name + " execution ended at:" + new Date());

            if (timeSeconds > MAX_TIME) {
                log.warn("Method execution longer than " + MAX_TIME + " seconds!");
            }
        }
    }

}