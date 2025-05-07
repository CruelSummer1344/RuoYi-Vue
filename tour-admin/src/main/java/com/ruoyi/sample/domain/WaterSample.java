package com.ruoyi.sample.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 水样对象 water_sample
 * 
 * @author ruoyi
 * @date 2025-04-04
 */
public class WaterSample extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 水样id */
    private Long sampleId;

    /** 用户id */
    private Long userId;

    /** 收集时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "收集时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date collectionTime;

    /** 位置 */
    @Excel(name = "位置")
    private String location;

    /** 上传时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "上传时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date uploadTime;

    /** 备注 */
    @Excel(name = "备注")
    private String remarks;

    public void setSampleId(Long sampleId) 
    {
        this.sampleId = sampleId;
    }

    public Long getSampleId() 
    {
        return sampleId;
    }

    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }

    public void setCollectionTime(Date collectionTime) 
    {
        this.collectionTime = collectionTime;
    }

    public Date getCollectionTime() 
    {
        return collectionTime;
    }

    public void setLocation(String location) 
    {
        this.location = location;
    }

    public String getLocation() 
    {
        return location;
    }

    public void setUploadTime(Date uploadTime) 
    {
        this.uploadTime = uploadTime;
    }

    public Date getUploadTime() 
    {
        return uploadTime;
    }

    public void setRemarks(String remarks) 
    {
        this.remarks = remarks;
    }

    public String getRemarks() 
    {
        return remarks;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("sampleId", getSampleId())
            .append("userId", getUserId())
            .append("collectionTime", getCollectionTime())
            .append("location", getLocation())
            .append("uploadTime", getUploadTime())
            .append("remarks", getRemarks())
            .toString();
    }
}
