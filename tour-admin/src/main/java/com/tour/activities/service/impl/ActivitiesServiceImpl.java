package com.tour.activities.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tour.activities.mapper.ActivitiesMapper;
import com.tour.activities.domain.Activities;
import com.tour.activities.service.IActivitiesService;

/**
 * 活动Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-04-04
 */
@Service
public class ActivitiesServiceImpl implements IActivitiesService 
{
    @Autowired
    private ActivitiesMapper activitiesMapper;

    /**
     * 查询活动
     * 
     * @param activityId 活动主键
     * @return 活动
     */
    @Override
    public Activities selectActivitiesByActivityId(Long activityId)
    {
        return activitiesMapper.selectActivitiesByActivityId(activityId);
    }

    /**
     * 查询活动列表
     * 
     * @param activities 活动
     * @return 活动
     */
    @Override
    public List<Activities> selectActivitiesList(Activities activities)
    {
        return activitiesMapper.selectActivitiesList(activities);
    }

    /**
     * 新增活动
     * 
     * @param activities 活动
     * @return 结果
     */
    @Override
    public int insertActivities(Activities activities)
    {
        return activitiesMapper.insertActivities(activities);
    }

    /**
     * 修改活动
     * 
     * @param activities 活动
     * @return 结果
     */
    @Override
    public int updateActivities(Activities activities)
    {
        return activitiesMapper.updateActivities(activities);
    }

    /**
     * 批量删除活动
     * 
     * @param activityIds 需要删除的活动主键
     * @return 结果
     */
    @Override
    public int deleteActivitiesByActivityIds(Long[] activityIds)
    {
        return activitiesMapper.deleteActivitiesByActivityIds(activityIds);
    }

    /**
     * 删除活动信息
     * 
     * @param activityId 活动主键
     * @return 结果
     */
    @Override
    public int deleteActivitiesByActivityId(Long activityId)
    {
        return activitiesMapper.deleteActivitiesByActivityId(activityId);
    }
}
