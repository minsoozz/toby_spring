package com.toby.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class UppercaseHandler implements InvocationHandler {

  Object target;

  public UppercaseHandler(Object target) {
    this.target = target;
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    String ret = (String) method.invoke(target, args);  // 타겟으로 위임, 인터페이스의 메소드 호출에 모두 적용된다.
    if (ret instanceof String) {
      return ((String) ret).toUpperCase();
    } else {
      return ret;
    }
  }
}
