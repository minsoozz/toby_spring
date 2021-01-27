package com.toby;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class JUnitTest {

  static Set<JUnitTest> testObjects = new HashSet<JUnitTest>();

  @Test
  public void test1() {
    assertThat(testObjects, not(hasItem(this)));
    testObjects.add(this);
  }

  @Test
  public void test2() {
    assertThat(testObjects, not(hasItem(this)));
    testObjects.add(this);
  }

  @Test
  public void test3() {
    assertThat(testObjects, not(hasItem(this)));
    testObjects.add(this);
  }
}
