package cn.linkedcare.springboot.portal.annotation.permission;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Permission {
	String value(); //权限表达式（需要支持括号，与，或符号运算）
	boolean isLogin() default true; //是否需要用户登录
}
