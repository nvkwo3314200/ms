package com.ais.sys.advice;
import java.lang.reflect.Method;  
  
import org.springframework.aop.MethodBeforeAdvice;  
  
//前置通知  
public class WelcomeAdvice implements MethodBeforeAdvice {  
  
  public void before(Method method, Object[] args, Object obj)  
          throws Throwable {  
        
      System.out.println("Hello welcome to bye ");  
  }  
}  