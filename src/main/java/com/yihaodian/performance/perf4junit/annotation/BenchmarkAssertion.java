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
public @interface BenchmarkAssertion {
    /*
     * 表示当错误率大于多少时将改测试标记为失败.
     */
    float errorThreshold() default 1;
    /*
     * 表示当测试执行时间大于多少ms时将该次执行标记为失败.
     */
    long responseThreshold() default 2000;
    
}
