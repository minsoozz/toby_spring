package com.toby.bean;

import static org.assertj.core.api.Assertions.*;

import com.toby.domain.Message;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FactoryBeanTest {

  @Autowired
  ApplicationContext context;

  @Test
  public void getMessageFromFactoryBean() {
    Object message = context.getBean("message");
    assertThat(message).isEqualTo(Message.class);
    assertThat(((Message) message).getText()).isEqualTo("Factory Bean");
  }
}
