package com.example.backend.dto;
// 用于接收分组统计结果
public class BookStatsDTO {
    private String groupName;  // 分组名称（如状态、分类名）
    private Long count;        // 数量

    public BookStatsDTO() {}

    public BookStatsDTO(String groupName, Long count) {
        this.groupName = groupName;
        this.count = count;
    }

    public String getGroupName() { return groupName; }
    public void setGroupName(String groupName) { this.groupName = groupName; }
    public Long getCount() { return count; }
    public void setCount(Long count) { this.count = count; }
}