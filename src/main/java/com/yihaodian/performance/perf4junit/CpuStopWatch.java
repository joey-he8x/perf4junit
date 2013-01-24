/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yihaodian.performance.perf4junit;

import java.lang.management.ManagementFactory;
import org.perf4j.StopWatch;

/**
 *
 * @author heyin
 */
public class CpuStopWatch extends StopWatch {

    public enum Measure {

        user, system, cpu //, real
    }
    private static final long NANOS_IN_A_MILLI = 1000000L;
    private Measure measure;
    private long measureTime;
    private long elapsedTime;

    public CpuStopWatch(String tag, Measure m) {
        super(System.currentTimeMillis(), -1L, tag, null);
        this.measure = m;
        this.measureTime = getMeasureTime(m);
    }

    private long getMeasureTime(Measure m) {
        if (Measure.user.equals(m)) {
            return ManagementFactory.getThreadMXBean().getCurrentThreadUserTime();
        } else if (Measure.cpu.equals(m)) {
            return ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime();
//        } else if (Measure.real.equals(m)) {
//            return System.currentTimeMillis();
        } else if (Measure.system.equals(m)) {
            return ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime() - ManagementFactory.getThreadMXBean().getCurrentThreadUserTime();
        }
        return 0l;
    }

    @Override
    public long getElapsedTime() {
        if (elapsedTime == -1L) {
//            if (Measure.real.equals(measure)) {
//                return super.getElapsedTime();
//            } else {
            return (getMeasureTime(measure) - measureTime) / NANOS_IN_A_MILLI;
//            }
        } else {
            return elapsedTime;
        }
    }

    @Override
    public String stop() {
//        if (Measure.real.equals(measure)) {
//            super.stop();
//            elapsedTime = super.getElapsedTime();
//        } else {
        elapsedTime = (getMeasureTime(measure) - measureTime) / NANOS_IN_A_MILLI;
//        }
        return this.toString();
    }

    @Override
    public String toString() {
        String message = getMessage();
        return "start[" + getStartTime()
                + "] time[" + getElapsedTime()
                + "] tag[" + getTag()
                + ((message == null) ? "]" : "] message[" + message + "]");
    }
}
