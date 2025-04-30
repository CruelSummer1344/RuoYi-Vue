package com.ruoyi.spot.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 景点对象 scenic_spot
 * 
 * @author ruoyi
 * @date 2025-04-04
 */
public class ScenicSpot extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 景点ID */
    private Long spotId;

    /** 景点名称 */
    @Excel(name = "景点名称")
    private String name;

    /** 景点描述 */
    @Excel(name = "景点描述")
    private String description;

    /** 价格 */
    @Excel(name = "价格")
    private BigDecimal price;

    /** 位置 */
    @Excel(name = "位置")
    private String location;

    /** 开放时间 */
    @JsonFormat(pattern = "HH:mm:ss")
    @Excel(name = "开放时间", width = 30, dateFormat = "HH:mm:ss")
    private Date startTime;

    /** 关闭时间 */
    @JsonFormat(pattern = "HH:mm:ss")
    @Excel(name = "关闭时间", width = 30, dateFormat = "HH:mm:ss")
    private Date endTime;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;

    public void setSpotId(Long spotId) 
    {
        this.spotId = spotId;
    }

    public Long getSpotId() 
    {
        return spotId;
    }

    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }

    public void setDescription(String description) 
    {
        this.description = description;
    }

    public String getDescription() 
    {
        return description;
    }

    public void setPrice(BigDecimal price) 
    {
        this.price = price;
    }

    public BigDecimal getPrice() 
    {
        return price;
    }

    public void setLocation(String location) 
    {
        this.location = location;
    }

    public String getLocation() 
    {
        return location;
    }

    public void setStartTime(Date startTime) 
    {
        this.startTime = startTime;
    }

    public Date getStartTime() 
    {
        return startTime;
    }

    public void setEndTime(Date endTime) 
    {
        this.endTime = endTime;
    }

    public Date getEndTime() 
    {
        return endTime;
    }

    public void setCreatedAt(Date createdAt) 
    {
        this.createdAt = createdAt;
    }

    public Date getCreatedAt() 
    {
        return createdAt;
    }

    public void setUpdatedAt(Date updatedAt) 
    {
        this.updatedAt = updatedAt;
    }

    public Date getUpdatedAt() 
    {
        return updatedAt;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("spotId", getSpotId())
            .append("name", getName())
            .append("description", getDescription())
            .append("price", getPrice())
            .append("location", getLocation())
            .append("startTime", getStartTime())
            .append("endTime", getEndTime())
            .append("createdAt", getCreatedAt())
            .append("updatedAt", getUpdatedAt())
            .toString();
    }
}
