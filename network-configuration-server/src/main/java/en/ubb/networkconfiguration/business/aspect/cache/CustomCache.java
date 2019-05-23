package en.ubb.networkconfiguration.business.aspect.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
@EnableAspectJAutoProxy
@Aspect
public class CustomCache {


    private final Cache<String, Object> cache;

    public CustomCache() {
        cache = CacheBuilder.newBuilder()
                .maximumSize(100)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .build();
    }

    @Pointcut("execution(@en.ubb.networkconfiguration.business.aspect.cache.CCachable * *.*(..))")
    private void cache() {
    }

    @Around("cache()")
    public Object aroundProfileMethods(ProceedingJoinPoint thisJoinPoint) throws Throwable {
        StringBuilder keyBuilder = new StringBuilder();
        keyBuilder.append(thisJoinPoint.getSignature().getName());
        Arrays.stream(thisJoinPoint.getArgs())
                .forEach(arg -> keyBuilder.append(arg.toString()));

        String key = keyBuilder.toString();
        Object obj = cache.getIfPresent(key);
        if (obj == null) {
            obj = thisJoinPoint.proceed();
            cache.put(key, obj);
        } else {
            log.info("Object requested by method {} retrieved from cache",
                    thisJoinPoint.getTarget().getClass() + "." + thisJoinPoint.getSignature().getName());
        }
        return obj;
    }


}
