package com.toby.bean;

import com.toby.domain.Message;
import org.springframework.beans.factory.FactoryBean;

public class MessageFactoryBean implements FactoryBean<Message> {

  String text;

  public void setText(String text) {
    this.text = text;
  }

  @Override
  public Message getObject() throws Exception {
    return Message.newMessage(this.text);
  }

  @Override
  public Class<? extends Message> getObjectType() {
    return Message.class;
  }

  public boolean isSingleton() {
    return false;
  }
}
