/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yihaodian.performance.perf4junit;

import com.yihaodian.performance.perf4junit.annotation.Benchmark;
import com.yihaodian.performance.perf4junit.report.PerfReporter;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Ignore;
import org.junit.internal.AssumptionViolatedException;
import org.junit.internal.runners.model.EachTestNotifier;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.perf4j.GroupedTimingStatistics;
import org.perf4j.StopWatch;
import org.perf4j.helpers.GroupedTimingStatisticsCsvFormatter;

/**
 *
 * @author heyin
 */
public class BenchRunner extends BlockJUnit4ClassRunner {

    private static PerfReporter reporter = new PerfReporter();
    private static Set<RunNotifier> notifierCache = new HashSet<RunNotifier>();
    private GroupedTimingStatistics currentStatistics;
    private TestRunStats currentStats;

    public BenchRunner(Class<?> klass)
            throws InitializationError, ClassNotFoundException {
        super(klass);
        System.out.println("new BenchRunner");
    }

    @Override
    protected void runChild(final FrameworkMethod method, RunNotifier notifier) {
        if (!notifierCache.contains(notifier)) {
            notifierCache.add(notifier);
            notifier.addListener(reporter);
        }
        Description description = describeChild(method);
        if (method.getAnnotation(Ignore.class) != null) {
            notifier.fireTestIgnored(description);
        } else {
            runBenchLeaf(methodBlock(method), description, notifier);
        }
    }

    @Override
    protected Object createTest() throws Exception {
        return super.createTest();
    }

    private void runBenchLeaf(final Statement statement, Description description,
            RunNotifier notifier) {
        EachTestNotifier eachNotifier = new EachTestNotifier(notifier, description);
        eachNotifier.fireTestStarted();
        final Benchmark b = (Benchmark) description.getAnnotation(Benchmark.class);
        currentStats = new TestRunStats(b.assertion());
        currentStatistics = new GroupedTimingStatistics();
        try {
            for (int i = 0; i < b.warmup(); i++) {
//                System.out.println("warmup:"+i);
                try{
                    statement.evaluate();
                }catch(Throwable e){}
            }
            ExecutorService parallel = Executors.newFixedThreadPool(b.concurrency());
            currentStatistics.setStartTime(System.currentTimeMillis());
            for (int i = 0; i < b.concurrency(); i++) {
                parallel.submit(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < b.loop(); i++) {
                            int rs = measuredRun(statement, b.assertion().responseThreshold());
                            if (rs == 0) {
                                currentStats.addSuccess();
                            } else if (rs == -1) {
                                currentStats.addFailure();
                            } else if (rs == -2) {
                                currentStats.addTimeout();
                            }
                        }
                    }
                });
            }
            parallel.shutdown();
            parallel.awaitTermination(b.timeout(), TimeUnit.SECONDS);
            if (!currentStats.isPass()) {
                throw new AssumptionViolatedException(null);
            }
        } catch (InterruptedException e) {
            currentStats.setBlocked(true);
            eachNotifier.addFailure(e);
        } finally {
            currentStatistics.setStopTime(System.currentTimeMillis());
            reporter.testFinished(description, currentStatistics, currentStats);
//            genReport();
            eachNotifier.fireTestFinished();
        }
    }

    private void genReport() {
        GroupedTimingStatisticsCsvFormatter formater = new GroupedTimingStatisticsCsvFormatter();
        System.out.println(GroupedTimingStatisticsCsvFormatter.DEFAULT_FORMAT_STRING);
        System.out.println(formater.format(currentStatistics));
    }

    /*
     * @return 0:成功,-1:失败,-2:响应时间超过阈值
     */
    private int measuredRun(Statement statement, long rspThreshold) {
        try {
            StopWatch stopWatch = new StopWatch("real");
            CpuStopWatch cpuStopWatch = new CpuStopWatch("cpu", CpuStopWatch.Measure.cpu);
            CpuStopWatch systemCpuStopWatch = new CpuStopWatch("sys", CpuStopWatch.Measure.system);
            CpuStopWatch userCpuStopWatch = new CpuStopWatch("usr", CpuStopWatch.Measure.user);

            statement.evaluate();

            stopWatch.stop();
            cpuStopWatch.stop();
            systemCpuStopWatch.stop();
            userCpuStopWatch.stop();
            synchronized (this) {
                currentStatistics.addStopWatch(stopWatch);
                currentStatistics.addStopWatch(cpuStopWatch);
                currentStatistics.addStopWatch(systemCpuStopWatch);
                currentStatistics.addStopWatch(userCpuStopWatch);
            }
            if (stopWatch.getElapsedTime() > rspThreshold) {
                return -2;
            } else {
                return 0;
            }
        } catch (Throwable ex) {
            Logger.getLogger(BenchRunner.class
                    .getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
    }

    public GroupedTimingStatistics getGroupedTimingStatistics() {
        return currentStatistics;
    }

    public void setGroupedTimingStatistics(GroupedTimingStatistics groupedTimingStatistics) {
        this.currentStatistics = groupedTimingStatistics;
    }
}
