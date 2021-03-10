package com.toby.proxy;

import java.lang.reflect.Proxy;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProxyTest {

  @Test
  public void simpleProxy() {
    Hello hello = new HelloTarget();
    assertThat(hello.sayHello("Toby")).isEqualTo("Hello Toby");
    assertThat(hello.sayHi("Toby")).isEqualTo("Hi Toby");
    assertThat(hello.sayThankYou("Toby")).isEqualTo("Thank You Toby");

    Hello proxiedHello = new HelloUppercase(new HelloTarget());
    assertThat(proxiedHello.sayHello("Toby")).isEqualTo("HELLO TOBY");
    assertThat(proxiedHello.sayHi("Toby")).isEqualTo("HI TOBY");
    assertThat(proxiedHello.sayThankYou("Toby")).isEqualTo("THANK YOU TOBY");
  }

  @Test
  public void proxyInit(){
    // 프록시 생성
    Hello proxiedHello = (Hello) Proxy.newProxyInstance(
        getClass().getClassLoader(),
        new Class[] {Hello.class},
        new UppercaseHandler(new HelloTarget()));
  }
}
