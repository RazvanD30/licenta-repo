package en.ubb.networkconfiguration.business.aspect.security;

import en.ubb.networkconfiguration.persistence.domain.authentication.enums.Role;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface SecurityAnnotation {
    Role[] allowedRole();
}