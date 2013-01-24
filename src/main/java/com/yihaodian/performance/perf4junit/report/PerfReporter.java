/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yihaodian.performance.perf4junit.report;

import com.thoughtworks.xstream.XStream;
import com.yihaodian.performance.perf4junit.TestRunStats;
import com.yihaodian.performance.perf4junit.report.model.AggregateReport;
import java.util.HashMap;
import java.util.Map;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.RunListener;
import org.perf4j.GroupedTimingStatistics;

/**
 *
 * @author heyin
 */
public class PerfReporter extends RunListener {

    private Long start=null;
    private Long end;
    private Map<Description, GroupedTimingStatistics> timingStatsMap = new HashMap<Description, GroupedTimingStatistics>();
    private Map<Description, TestRunStats> detailMap = new HashMap<Description, TestRunStats>();
//    private Map<Description, Boo
    @Override
    public void testRunStarted(Description description) throws Exception {
        System.out.println("testRunStarted");
//        this.start = System.currentTimeMillis();
    }
    
    @Override
    public void testStarted(Description description) throws Exception {
        if(start==null)
            this.start = System.currentTimeMillis();
        System.out.println("testStarted");
        //this.start = System.currentTimeMillis();
    }

//    public void testStarted(Description description) throws Exception {
//    }
//
//    public void testFinished(Description description) throws Exception {
//    }

    @Override
    public void testRunFinished(Result result) throws Exception {
        this.end = System.currentTimeMillis();
        AggregateReportBuilder b = new AggregateReportBuilder();
        AggregateReport report = b.buildAggregateReport(start, end, timingStatsMap, detailMap);

        System.out.println("testRunFinished");
        XStream stream = new XStream();
        stream.processAnnotations(AggregateReport.class);
        System.out.println(stream.toXML(report));
    }

    public void testFinished(Description description, GroupedTimingStatistics timingStats, TestRunStats detail) {
        timingStatsMap.put(description, timingStats);
        detailMap.put(description, detail);
    }
}
