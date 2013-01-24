/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yihaodian.performance.perf4junit;

import com.yihaodian.performance.perf4junit.annotation.BenchmarkAssertion;

/**
 *
 * @author heyin
 */
public class TestRunStats {

    private BenchmarkAssertion assertion;
    private int failed;
    private int timeout;
    private int total;
    private boolean blocked = false;

    public TestRunStats(BenchmarkAssertion assertion) {
        this.assertion = assertion;
    }

    public boolean isPass() {
        if (blocked) return false;
        if (failed / total > assertion.errorThreshold()) {
            return false;
        } else {
            return true;
        }
    }

    public void addSuccess() {
        total++;
    }

    public void addFailure() {
        total++;
        failed++;
    }

    public void addTimeout() {
        total++;
        timeout++;
        failed++;
    }

    public int getFailed() {
        return failed;
    }

    public int getTotal() {
        return total;
    }

    public int getTimeout() {
        return timeout;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean isBlock) {
        this.blocked = isBlock;
    }
}
