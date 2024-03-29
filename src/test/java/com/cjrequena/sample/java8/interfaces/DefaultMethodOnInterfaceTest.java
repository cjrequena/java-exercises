package com.cjrequena.sample.java8.interfaces;

import org.junit.jupiter.api.Test;

/**
 *
 */
public class DefaultMethodOnInterfaceTest {

  @Test
  public void interfaceDefaultMethodTest() {
    DefaultMethodOnInterface formula = new DefaultMethodOnInterface() {
      @Override
      public double calculate(int a) {
        return sqrt(a * 100);
      }
    };

    formula.calculate(100);     // 100.0
    formula.sqrt(16);           // 4.0
  }

}
