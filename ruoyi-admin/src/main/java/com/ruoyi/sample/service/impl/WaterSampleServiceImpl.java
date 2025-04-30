package com.ruoyi.sample.service.impl;

import java.util.List;

import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.sample.mapper.WaterSampleMapper;
import com.ruoyi.sample.domain.WaterSample;
import com.ruoyi.sample.service.IWaterSampleService;

/**
 * 水样Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-04-04
 */
@Service
public class WaterSampleServiceImpl implements IWaterSampleService 
{
    @Autowired
    private WaterSampleMapper waterSampleMapper;

    /**
     * 查询水样
     * 
     * @param sampleId 水样主键
     * @return 水样
     */
    @Override
    public WaterSample selectWaterSampleBySampleId(Long sampleId)
    {
        return waterSampleMapper.selectWaterSampleBySampleId(sampleId);
    }

    /**
     * 查询水样列表
     * 
     * @param waterSample 水样
     * @return 水样
     */
    @Override
    public List<WaterSample> selectWaterSampleList(WaterSample waterSample)
    {
        return waterSampleMapper.selectWaterSampleList(waterSample);
    }

    /**
     * 新增水样
     * 
     * @param waterSample 水样
     * @return 结果
     */
    @Override
    public int insertWaterSample(WaterSample waterSample)
    {
        waterSample.setUserId(SecurityUtils.getUserId());
        return waterSampleMapper.insertWaterSample(waterSample);
    }

    /**
     * 修改水样
     * 
     * @param waterSample 水样
     * @return 结果
     */
    @Override
    public int updateWaterSample(WaterSample waterSample)
    {
        return waterSampleMapper.updateWaterSample(waterSample);
    }

    /**
     * 批量删除水样
     * 
     * @param sampleIds 需要删除的水样主键
     * @return 结果
     */
    @Override
    public int deleteWaterSampleBySampleIds(Long[] sampleIds)
    {
        return waterSampleMapper.deleteWaterSampleBySampleIds(sampleIds);
    }

    /**
     * 删除水样信息
     * 
     * @param sampleId 水样主键
     * @return 结果
     */
    @Override
    public int deleteWaterSampleBySampleId(Long sampleId)
    {
        return waterSampleMapper.deleteWaterSampleBySampleId(sampleId);
    }
}
