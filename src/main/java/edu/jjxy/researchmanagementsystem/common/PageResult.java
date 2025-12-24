package edu.jjxy.researchmanagementsystem.common;

import java.util.List;

public class PageResult<T> {
    private List<T> records;
    private long total;

    public PageResult() {}

    // ✅ 你项目当前正确顺序：records 在前，total 在后
    public PageResult(List<T> records, long total) {
        this.records = records;
        this.total = total;
    }

    // ✅ 新增：兼容旧/写反的用法：total 在前，records 在后
    public PageResult(long total, List<T> records) {
        this.records = records;
        this.total = total;
    }

    public List<T> getRecords() { return records; }
    public void setRecords(List<T> records) { this.records = records; }

    public long getTotal() { return total; }
    public void setTotal(long total) { this.total = total; }
}
