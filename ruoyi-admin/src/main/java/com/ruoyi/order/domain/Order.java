package com.ruoyi.order.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 订单对象 order
 * 
 * @author ruoyi
 * @date 2025-04-04
 */
public class Order extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 订单id */
    private Long orderId;

    /** 用户id */
    @Excel(name = "用户id")
    private Long userId;

    /** 景点id */
    @Excel(name = "景点id")
    private Long spotId;

    /** 酒店ID */
    @Excel(name = "酒店ID")
    private Long hotelId;

    /** 产品ID */
    @Excel(name = "产品ID")
    private Long productId;

    /** 支付状态 */
    @Excel(name = "支付状态")
    private String status;

    /** 人数 */
    @Excel(name = "人数")
    private Long peopleCount;

    /** 总价格 */
    @Excel(name = "总价格")
    private BigDecimal totalPrice;

    /** 备注 */
    @Excel(name = "备注")
    private String remarks;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createdAt;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date updatedAt;

    /** 用户名称 */
    @Excel(name = "用户名称")
    private String userName;

    /** 景点名称 */
    @Excel(name = "景点名称")
    private String spotName;

    /** 酒店名称 */
    @Excel(name = "酒店名称")
    private String hotelName;

    /** 项目名称 */
    @Excel(name = "项目名称")
    private String productName;

    public void setOrderId(Long orderId) 
    {
        this.orderId = orderId;
    }

    public Long getOrderId() 
    {
        return orderId;
    }

    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }

    public void setSpotId(Long spotId) 
    {
        this.spotId = spotId;
    }

    public Long getSpotId() 
    {
        return spotId;
    }

    public void setHotelId(Long hotelId) 
    {
        this.hotelId = hotelId;
    }

    public Long getHotelId() 
    {
        return hotelId;
    }

    public void setProductId(Long productId) 
    {
        this.productId = productId;
    }

    public Long getProductId() 
    {
        return productId;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    public void setPeopleCount(Long peopleCount) 
    {
        this.peopleCount = peopleCount;
    }

    public Long getPeopleCount() 
    {
        return peopleCount;
    }

    public void setTotalPrice(BigDecimal totalPrice) 
    {
        this.totalPrice = totalPrice;
    }

    public BigDecimal getTotalPrice() 
    {
        return totalPrice;
    }

    public void setRemarks(String remarks) 
    {
        this.remarks = remarks;
    }

    public String getRemarks() 
    {
        return remarks;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSpotName() {
        return spotName;
    }

    public void setSpotName(String spotName) {
        this.spotName = spotName;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("orderId", getOrderId())
            .append("userId", getUserId())
            .append("spotId", getSpotId())
            .append("hotelId", getHotelId())
            .append("productId", getProductId())
            .append("status", getStatus())
            .append("peopleCount", getPeopleCount())
            .append("totalPrice", getTotalPrice())
            .append("remarks", getRemarks())
            .append("createdAt", getCreatedAt())
            .append("updatedAt", getUpdatedAt())
            .toString();
    }
}
