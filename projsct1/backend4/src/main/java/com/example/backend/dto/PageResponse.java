package com.example.backend.dto;

import java.util.List;

public class PageResponse<T> {
    private List<T> content;       // 当前页的数据列表
    private int currentPage;       // 当前页码（从 0 开始）
    private int pageSize;          // 每页大小
    private long totalElements;    // 总记录数
    private int totalPages;        // 总页数
    private boolean first;         // 是否是第一页
    private boolean last;          // 是否是最后一页

    // 无参构造
    public PageResponse() {}

    // 全参构造
    public PageResponse(List<T> content, int currentPage, int pageSize, long totalElements, int totalPages) {
        this.content = content;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.first = currentPage == 0;
        this.last = currentPage >= totalPages - 1;
    }

    // Getter 和 Setter
    public List<T> getContent() { return content; }
    public void setContent(List<T> content) { this.content = content; }
    public int getCurrentPage() { return currentPage; }
    public void setCurrentPage(int currentPage) { this.currentPage = currentPage; }
    public int getPageSize() { return pageSize; }
    public void setPageSize(int pageSize) { this.pageSize = pageSize; }
    public long getTotalElements() { return totalElements; }
    public void setTotalElements(long totalElements) { this.totalElements = totalElements; }
    public int getTotalPages() { return totalPages; }
    public void setTotalPages(int totalPages) { this.totalPages = totalPages; }
    public boolean isFirst() { return first; }
    public void setFirst(boolean first) { this.first = first; }
    public boolean isLast() { return last; }
    public void setLast(boolean last) { this.last = last; }
}