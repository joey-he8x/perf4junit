/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yihaodian.performance.perf4junit.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author heyin
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Benchmark {

    int loop();
    int concurrency();
    int warmup() default 10;
    int timeout() default 900;
    String description() default "";
    BenchmarkAssertion assertion() default @BenchmarkAssertion();
    
}
