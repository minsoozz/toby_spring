package com.toby.config;

import com.toby.bean.MessageFactoryBean;
import com.toby.domain.Message;
import com.toby.service.TxProxyFactoryBean;
import com.toby.service.UserService;
import com.toby.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class FactoryBeanConfig {

  @Autowired
  private UserServiceImpl userServiceImpl;
  @Autowired
  private PlatformTransactionManager transactionManager;

  @Bean(name = "message")
  public MessageFactoryBean messageFactoryBean() {
    MessageFactoryBean factoryBean = new MessageFactoryBean();
    factoryBean.setText("Factory Bean");
    return factoryBean;
  }

  @Bean
  public Message message() throws Exception {
    return messageFactoryBean().getObject();
  }

  @Bean(name = "txProxy")
  public TxProxyFactoryBean txProxyFactoryBean() {
    TxProxyFactoryBean factoryBean = new TxProxyFactoryBean();
    factoryBean.setTarget(userServiceImpl);
    factoryBean.setTransactionManager(transactionManager);
    factoryBean.setPattern("upgradeLevels");
    factoryBean.setServiceInterface(UserService.class);
    return factoryBean;
  }

}
