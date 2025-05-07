package com.tour.sample.service;

import java.util.List;
import com.tour.sample.domain.WaterSample;

/**
 * 水样Service接口
 * 
 * @author ruoyi
 * @date 2025-04-04
 */
public interface IWaterSampleService 
{
    /**
     * 查询水样
     * 
     * @param sampleId 水样主键
     * @return 水样
     */
    public WaterSample selectWaterSampleBySampleId(Long sampleId);

    /**
     * 查询水样列表
     * 
     * @param waterSample 水样
     * @return 水样集合
     */
    public List<WaterSample> selectWaterSampleList(WaterSample waterSample);

    /**
     * 新增水样
     * 
     * @param waterSample 水样
     * @return 结果
     */
    public int insertWaterSample(WaterSample waterSample);

    /**
     * 修改水样
     * 
     * @param waterSample 水样
     * @return 结果
     */
    public int updateWaterSample(WaterSample waterSample);

    /**
     * 批量删除水样
     * 
     * @param sampleIds 需要删除的水样主键集合
     * @return 结果
     */
    public int deleteWaterSampleBySampleIds(Long[] sampleIds);

    /**
     * 删除水样信息
     * 
     * @param sampleId 水样主键
     * @return 结果
     */
    public int deleteWaterSampleBySampleId(Long sampleId);
}
