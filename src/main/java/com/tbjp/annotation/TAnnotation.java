package com.tbjp.annotation;

import com.tbjp.profiler.CallerInfo;
import com.tbjp.profiler.Profiler;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.InitializingBean;

import java.lang.reflect.Method;

/**
 * Created with IntelliJ IDEA.
 * User: tubingbing
 * Date: 16-2-25
 * Time: 下午1:58
 * To change this template use File | Settings | File Templates.
 */
@Aspect
public class TAnnotation implements InitializingBean {
    private String systemKey;
    private String jvmKey;

    @Pointcut("@annotation(com.tbjp.annotation.TProfiler)")
    public void AnnotationPoint(){
    }

    @Around("AnnotationPoint()")
    public Object execAnnotation(ProceedingJoinPoint jp)throws Throwable {
        Method method = getMethod(jp) ;
        CallerInfo callerInfo = null;
        try
        {
            TProfiler anno = (TProfiler)method.getAnnotation(TProfiler.class);
            String methodKey;
            if (anno != null){
                methodKey = anno.methodKey();
                if (!isBlank(methodKey)){
                    callerInfo = Profiler.start(methodKey, true, true);
                }
            }
            return jp.proceed();
        }catch (Throwable e){
            if (callerInfo != null){
                Profiler.error(callerInfo);
            }
            throw e;
        } finally {
            if (callerInfo != null){
                Profiler.end(callerInfo);
            }
        }
    }

    private Method getMethod(JoinPoint jp) throws Exception {
        MethodSignature msig = (MethodSignature)jp.getSignature();
        Method method = msig.getMethod();
        return method;
    }

    public void setSystemKey(String systemKey) {
        this.systemKey = systemKey;
    }

    @Override
    public void afterPropertiesSet()throws Exception{
        if (!isBlank(this.systemKey)) {
            Profiler.scopeAlive(this.systemKey);
        }

        if (!isBlank(this.jvmKey)){
            Profiler.registerJvmData(this.jvmKey);
        }
    }

    private boolean isBlank(String value){
        if ((null != value) && (!"".equals(value.trim()))) {
            return false;
        }
        return true;
    }

    public void setJvmKey(String jvmKey) {
        this.jvmKey = jvmKey;
    }
}
