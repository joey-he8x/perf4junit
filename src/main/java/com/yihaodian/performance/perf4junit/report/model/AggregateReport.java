/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yihaodian.performance.perf4junit.report.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author heyin
 */
@XStreamAlias("report")
public class AggregateReport {

    @XStreamAsAttribute
    private Date start;
    @XStreamAsAttribute
    private Date end;
    @XStreamAsAttribute
    private int total;
    @XStreamAsAttribute
    private int failed;
    private Target target;
    private Platform platform = new Platform();
    @XStreamImplicit(itemFieldName = "test")
    private List<Test> tests = new ArrayList<Test>();

    public AggregateReport(){}
    
    public AggregateReport(Date start, Date end, Target target, List<Test> tests) {
        this.start = start;
        this.end = end;
        this.target = target;
        this.tests = tests;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public Platform getPlatform() {
        return platform;
    }

    public List<Test> getTests() {
        return tests;
    }

    public void setTests(List<Test> tests) {
        this.tests = tests;
    }

    public Target getTarget() {
        return target;
    }

    public void setTarget(Target target) {
        this.target = target;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getFailed() {
        return failed;
    }

    public void setFailed(int failed) {
        this.failed = failed;
    }

    public static class Target {

        private String groupId;
        private String artifactId;
        private String version;
        private String revision;

        public String getRevision() {
            return revision;
        }

        public void setRevision(String revision) {
            this.revision = revision;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public String getArtifactId() {
            return artifactId;
        }

        public void setArtifactId(String artifactId) {
            this.artifactId = artifactId;
        }
    }

    public static class Test {

        @XStreamAsAttribute
        private String methodName;
        @XStreamAsAttribute
        private String className;
        @XStreamAsAttribute
        private boolean executed;
        @XStreamAsAttribute
        private Date start;
        @XStreamAsAttribute
        private Date end;
        private String description;
        private Load load;
        private Result result;

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Load getLoad() {
            return load;
        }

        public void setLoad(Load load) {
            this.load = load;
        }

        public Result getResult() {
            return result;
        }

        public void setResult(Result result) {
            this.result = result;
        }

        public void setExecuted(boolean executed) {
            this.executed = executed;
        }

        public Date getStart() {
            return start;
        }

        public void setStart(Date start) {
            this.start = start;
        }

        public Date getEnd() {
            return end;
        }

        public void setEnd(Date end) {
            this.end = end;
        }

        public String getMethodName() {
            return methodName;
        }

        public void setMethodName(String methodName) {
            this.methodName = methodName;
        }

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public boolean isExecuted() {
            return executed;
        }
    }

    public static class Load {

        @XStreamAsAttribute
        private int concurrency;
        @XStreamAsAttribute
        private int loop;

        public Load(int concurrency, int loop) {
            this.concurrency = concurrency;
            this.loop = loop;
        }

        public int getConcurrency() {
            return concurrency;
        }

        public void setConcurrency(int concurrency) {
            this.concurrency = concurrency;
        }

        public int getLoop() {
            return loop;
        }

        public void setLoop(int loop) {
            this.loop = loop;
        }
    }

    public static class Platform {

        @XStreamAlias("os.arc")
        private String osArc;
        @XStreamAlias("os.name")
        private String osName;
        @XStreamAlias("os.version")
        private String osVersion;
        @XStreamAlias("vm.name")
        private String vmName;
        @XStreamAlias("vm.version")
        private String vmVersion;
        private Integer processors;

        public Platform() {
            this.osArc = ManagementFactory.getOperatingSystemMXBean().getArch();
            this.osName = ManagementFactory.getOperatingSystemMXBean().getName();
            this.osVersion = ManagementFactory.getOperatingSystemMXBean().getVersion();
            this.processors = Runtime.getRuntime().availableProcessors();
            this.vmName = ManagementFactory.getRuntimeMXBean().getVmName();
            this.vmVersion = System.getProperty("java.version");
        }

        public String getOsArc() {
            return osArc;
        }

        public void setOsArc(String osArc) {
            this.osArc = osArc;
        }

        public String getOsName() {
            return osName;
        }

        public void setOsName(String osName) {
            this.osName = osName;
        }

        public String getOsVersion() {
            return osVersion;
        }

        public void setOsVersion(String osVersion) {
            this.osVersion = osVersion;
        }

        public String getVmName() {
            return vmName;
        }

        public void setVmName(String vmName) {
            this.vmName = vmName;
        }

        public String getVmVersion() {
            return vmVersion;
        }

        public void setVmVersion(String vmVersion) {
            this.vmVersion = vmVersion;
        }

        public Integer getProcessors() {
            return processors;
        }

        public void setProcessors(Integer processors) {
            this.processors = processors;
        }
    }

    public static class Result {

        @XStreamAsAttribute
        private int total;
        @XStreamAsAttribute
        private int passed;
        @XStreamAsAttribute
        private int failed;
        @XStreamAsAttribute
        private int timeout;
        @XStreamAsAttribute
        private long escaped;
        @XStreamAlias("real")
        private ResultTimingStats realStats;
        @XStreamAlias("cpu")
        private ResultTimingStats cpuStats;
        @XStreamAlias("sys")
        private ResultTimingStats sysStats;
        @XStreamAlias("usr")
        private ResultTimingStats usrStats;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getPassed() {
            return passed;
        }

        public void setPassed(int passed) {
            this.passed = passed;
        }

        public int getFailed() {
            return failed;
        }

        public void setFailed(int failed) {
            this.failed = failed;
        }

        public int getTimeout() {
            return timeout;
        }

        public void setTimeout(int timeout) {
            this.timeout = timeout;
        }

        public long getEscaped() {
            return escaped;
        }

        public void setEscaped(long escaped) {
            this.escaped = escaped;
        }

        public ResultTimingStats getRealStats() {
            return realStats;
        }

        public void setRealStats(ResultTimingStats realStats) {
            this.realStats = realStats;
        }

        public ResultTimingStats getCpuStats() {
            return cpuStats;
        }

        public void setCpuStats(ResultTimingStats cpuStats) {
            this.cpuStats = cpuStats;
        }

        public ResultTimingStats getSysStats() {
            return sysStats;
        }

        public void setSysStats(ResultTimingStats sysStats) {
            this.sysStats = sysStats;
        }

        public ResultTimingStats getUsrStats() {
            return usrStats;
        }

        public void setUsrStats(ResultTimingStats usrStats) {
            this.usrStats = usrStats;
        }
    }

    public static class ResultTimingStats {

        @XStreamAsAttribute
        private float avg;
        @XStreamAsAttribute
        private float min;
        @XStreamAsAttribute
        private float max;
        @XStreamAsAttribute
        private int count;

        public float getAvg() {
            return avg;
        }

        public void setAvg(float avg) {
            this.avg = avg;
        }

        public float getMin() {
            return min;
        }

        public void setMin(float min) {
            this.min = min;
        }

        public float getMax() {
            return max;
        }

        public void setMax(float max) {
            this.max = max;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }
}
