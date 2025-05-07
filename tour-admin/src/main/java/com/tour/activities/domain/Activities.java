package com.tour.activities.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.tour.common.annotation.Excel;
import com.tour.common.core.domain.BaseEntity;

/**
 * 活动对象 activities
 * 
 * @author ruoyi
 * @date 2025-04-04
 */
public class Activities extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 活动ID */
    private Long activityId;

    /** 活动标题 */
    @Excel(name = "活动标题")
    private String title;

    /** 活动日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "活动日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date date;

    /** 活动图片 */
    @Excel(name = "活动图片")
    private String imageUrl;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createdAt;

    public void setActivityId(Long activityId) 
    {
        this.activityId = activityId;
    }

    public Long getActivityId() 
    {
        return activityId;
    }

    public void setTitle(String title) 
    {
        this.title = title;
    }

    public String getTitle() 
    {
        return title;
    }

    public void setDate(Date date) 
    {
        this.date = date;
    }

    public Date getDate() 
    {
        return date;
    }

    public void setImageUrl(String imageUrl) 
    {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() 
    {
        return imageUrl;
    }

    public void setCreatedAt(Date createdAt) 
    {
        this.createdAt = createdAt;
    }

    public Date getCreatedAt() 
    {
        return createdAt;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("activityId", getActivityId())
            .append("title", getTitle())
            .append("date", getDate())
            .append("imageUrl", getImageUrl())
            .append("createdAt", getCreatedAt())
            .toString();
    }
}
