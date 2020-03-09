package ca.uqtr.fitbit.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;

@Aspect
public class FitBitNotificationDataAspect {

    @AfterReturning(
            pointcut = "execution(* ca.uqtr.fitbit.controller.DeviceController.getFitBitNotificationData(..))",
            returning= "result")
    public void afterGetFitBitNotificationData(JoinPoint joinPoint, Object result) {
        System.out.println("****afterGetFitBitNotificationData" + joinPoint.getSignature().getName());
    }
}
