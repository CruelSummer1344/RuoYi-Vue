package com.tour.activities.service;

import java.util.List;
import com.tour.activities.domain.Activities;

/**
 * 活动Service接口
 * 
 * @author ruoyi
 * @date 2025-04-04
 */
public interface IActivitiesService 
{
    /**
     * 查询活动
     * 
     * @param activityId 活动主键
     * @return 活动
     */
    public Activities selectActivitiesByActivityId(Long activityId);

    /**
     * 查询活动列表
     * 
     * @param activities 活动
     * @return 活动集合
     */
    public List<Activities> selectActivitiesList(Activities activities);

    /**
     * 新增活动
     * 
     * @param activities 活动
     * @return 结果
     */
    public int insertActivities(Activities activities);

    /**
     * 修改活动
     * 
     * @param activities 活动
     * @return 结果
     */
    public int updateActivities(Activities activities);

    /**
     * 批量删除活动
     * 
     * @param activityIds 需要删除的活动主键集合
     * @return 结果
     */
    public int deleteActivitiesByActivityIds(Long[] activityIds);

    /**
     * 删除活动信息
     * 
     * @param activityId 活动主键
     * @return 结果
     */
    public int deleteActivitiesByActivityId(Long activityId);
}
