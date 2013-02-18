/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yihaodian.performance.perf4junit.measure;

import org.perf4j.TimingStatistics;

/**
 *
 * @author heyin
 */
public class MyTimingStatistics extends TimingStatistics implements Cloneable {

    private long sum;

    @Override
    public TimingStatistics addSampleTime(long elapsedTime) {
        sum += elapsedTime;
        return super.addSampleTime(elapsedTime);
    }

    @Override
    public MyTimingStatistics clone() {
        return (MyTimingStatistics) super.clone();
    }

    public long getSum() {
        return sum;
    }

    public void setSum(long sum) {
        this.sum = sum;
    }
}
