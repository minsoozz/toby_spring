package com.toby.config;

import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.interceptor.BeanFactoryTransactionAttributeSourceAdvisor;

@Configuration
public class TransactionAdvice {

  @Bean(name = "transactionAdvice")
  public TransactionAdvice transactionAdvice() {
    return new TransactionAdvice();
  }

  @Bean(name = "transactionPointcut")
  public NameMatchMethodPointcut nameMatchMethodPointcut() {
    NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
    pointcut.setMappedName("upgrade*");
    return pointcut;
  }

  @Bean(name = "transactionAdvisor")
  public BeanFactoryTransactionAttributeSourceAdvisor transactionAdvisor() {
    BeanFactoryTransactionAttributeSourceAdvisor attributeSourceAdvisor = new BeanFactoryTransactionAttributeSourceAdvisor();
    return attributeSourceAdvisor;
  }
}
