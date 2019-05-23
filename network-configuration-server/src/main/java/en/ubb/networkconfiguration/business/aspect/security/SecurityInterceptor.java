package en.ubb.networkconfiguration.business.aspect.security;

import en.ubb.networkconfiguration.business.service.UserService;
import en.ubb.networkconfiguration.business.validation.exception.AuthorizationBussExc;
import en.ubb.networkconfiguration.persistence.domain.authentication.User;
import en.ubb.networkconfiguration.persistence.domain.authentication.enums.Role;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Arrays;
import java.util.List;

@Component
@EnableAspectJAutoProxy
@Aspect
public class SecurityInterceptor {

    private final UserService userService;

    @Autowired
    public SecurityInterceptor(UserService userService) {
        this.userService = userService;
    }


    @Pointcut("execution(* en.ubb.networkconfiguration.business.service.impl.*.*(..))")
    private void pointcut() {
    }

    @Around("pointcut()")
    public Object checkAuthorization(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] arguments = joinPoint.getArgs();
        if (arguments.length == 0) {
            return null;
        }

        Annotation annotation = checkTheAnnotation(arguments);
        boolean securityAnnotationPresent = (annotation != null);

        User currentUser = userService.getCurrentUser();

        if (currentUser == null) {
            throw new AuthorizationBussExc("Authorization failed, user unknown");
        }

        if (securityAnnotationPresent && !verifyRole(annotation, currentUser)) {
            throw new AuthorizationBussExc("Authorization failed for the user with username " + currentUser.getUsername());
        }
        return joinPoint.proceed();
    }

    /**
     * The function verifies if the current user has sufficient privilages to
     * have the component built
     */
    private boolean verifyRole(Annotation annotation, User currentUser) {
        SecurityAnnotation annotationRule = (SecurityAnnotation) annotation;
        List<Role> requiredRolesList = Arrays.asList(annotationRule.allowedRole());
        Role userRole = currentUser.getRole();
        return requiredRolesList.contains(userRole);
    }

    /**
     * Basing on the method's argument check if the class is annotataed with
     * {@link SecurityAnnotation}
     */
    private Annotation checkTheAnnotation(Object[] arguments) {
        Object concreteClass = arguments[0];
        if (concreteClass instanceof AnnotatedElement) {
            AnnotatedElement annotatedElement = (AnnotatedElement) concreteClass;
            return annotatedElement.getAnnotation(SecurityAnnotation.class);
        }
        return null;
    }

}
