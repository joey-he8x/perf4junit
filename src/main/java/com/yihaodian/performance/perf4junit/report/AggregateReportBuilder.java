/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yihaodian.performance.perf4junit.report;

import com.yihaodian.performance.perf4junit.TestRunStats;
import com.yihaodian.performance.perf4junit.annotation.Benchmark;
import com.yihaodian.performance.perf4junit.annotation.BenchmarkAssertion;
import com.yihaodian.performance.perf4junit.report.model.AggregateReport;
import com.yihaodian.performance.perf4junit.report.model.AggregateReport.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.junit.runner.Description;
import org.perf4j.GroupedTimingStatistics;
import org.perf4j.TimingStatistics;

/**
 *
 * @author heyin
 */
public class AggregateReportBuilder {
    private Date start;
    private Date end;
    private List<Test> tests = new ArrayList<Test>();
    public AggregateReport buildAggregateReport(long start, long end, Map<Description, GroupedTimingStatistics> statistics, Map<Description, TestRunStats> detailMap){
        this.start = new Date(start);
        this.end = new Date(end);
        Iterator it = statistics.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry) it.next();
            Description d = (Description) pairs.getKey();
            GroupedTimingStatistics g = (GroupedTimingStatistics) pairs.getValue();
            TestRunStats s = detailMap.get(d);
            tests.add(buildTest(d,g,s));
        }
        return new AggregateReport(this.start, this.end, this.tests);
    }
    
    private Test buildTest(Description d, GroupedTimingStatistics gs, TestRunStats detail){
        Test test = new Test();
        Benchmark b = (Benchmark) d.getAnnotation(Benchmark.class);
        test.setDescription(b.description());
        test.setStart(new Date(gs.getStartTime()));
        test.setEnd(new Date(gs.getStopTime()));
        test.setLoad(new Load(b.concurrency(),b.loop()));
        test.setExecuted(detail.isPass());
        test.setName(d.getMethodName());
        test.setResult(buildResult(gs, detail));
        return test;
    }
    
    private Result buildResult(GroupedTimingStatistics gs, TestRunStats detail){
        Result result = new Result();
        result.setEscaped(gs.getStopTime()-gs.getStartTime());
        result.setFailed(detail.getFailed());
        result.setPassed(detail.getTotal()-detail.getFailed());
        result.setTimeout(detail.getTimeout());
        result.setTotal(detail.getTotal());
        Iterator it = gs.getStatisticsByTag().entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry) it.next();
            String tag = (String) pairs.getKey();
            TimingStatistics ts = (TimingStatistics) pairs.getValue();
            ResultTimingStats rts = new ResultTimingStats();
            rts.setAvg(new Float(ts.getMean()));
            rts.setCount(ts.getCount());
            rts.setMax(new Float(ts.getMax()));
            rts.setMin(new Float(ts.getMin()));
            if("real".equals(tag)){
                result.setRealStats(rts);
            }else if(("cpu").equals(tag)){
                result.setCpuStats(rts);
            }else if(("sys").equals(tag)){
                result.setSysStats(rts);
            }else if(("usr").equals(tag)){
                result.setUsrStats(rts);
            }
        }
        return result;
    }
}
