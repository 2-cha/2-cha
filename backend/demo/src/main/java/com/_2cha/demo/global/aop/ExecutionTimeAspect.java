package com._2cha.demo.global.aop;

import com._2cha.demo.global.annotation.Stopwatch;
import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;


@Aspect
@Component
@Slf4j
public class ExecutionTimeAspect {

  @Pointcut("@annotation(com._2cha.demo.global.annotation.Stopwatch)")
  public void stopWatchMethod() {

  }

  @Around(value = "stopWatchMethod()")
  public Object runStopWatch(ProceedingJoinPoint pjp) throws Throwable {
    Object retVal = pjp.proceed();
    
    if (log.isDebugEnabled()) {
      MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
      Method method = methodSignature.getMethod();

      StopWatch stopWatch = new StopWatch();
      Stopwatch annotation = method.getAnnotation(Stopwatch.class);

      stopWatch.start();
      stopWatch.stop();
      String executionTime = switch (annotation.value()) {
        case SECONDS -> stopWatch.getTotalTimeSeconds() + " s";
        case MILLISECONDS -> stopWatch.getTotalTimeMillis() + " ms";
        case NANOSECONDS -> stopWatch.getTotalTimeNanos() + " ns";
      };

      log.debug("[{}#{}] Took {}",
                pjp.getTarget().getClass().getSimpleName(),
                method.getName(),
                executionTime);
    }

    return retVal;
  }
}
