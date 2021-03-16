package com.toby.bean;

import static org.assertj.core.api.Assertions.assertThat;

import com.toby.domain.Message;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FactoryBeanTest {

  @Autowired
  ApplicationContext context;

  @Test
  public void getMessageFromFactoryBean() {
    Object message = context.getBean("message");
    assertThat(message.getClass()).isEqualTo(Message.class);
    assertThat(((Message) message).getText()).isEqualTo("Factory Bean");
  }

  @Test
  public void getFactoryBean() throws Exception {
    Object factory = context.getBean("&message");
    assertThat(factory).isEqualTo(MessageFactoryBean.class);
  }

  @Test
  public void getTransactionAdvisor() throws Exception {
    Object transactionAdvisor = context.getBean("transactionAdvisor");
    System.out.println("transactionAdvisor = " + transactionAdvisor.toString());
  }

}
