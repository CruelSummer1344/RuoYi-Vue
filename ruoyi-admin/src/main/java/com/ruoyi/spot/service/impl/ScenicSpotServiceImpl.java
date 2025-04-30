package com.ruoyi.spot.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.spot.mapper.ScenicSpotMapper;
import com.ruoyi.spot.domain.ScenicSpot;
import com.ruoyi.spot.service.IScenicSpotService;

/**
 * 景点Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-04-04
 */
@Service
public class ScenicSpotServiceImpl implements IScenicSpotService 
{
    @Autowired
    private ScenicSpotMapper scenicSpotMapper;

    /**
     * 查询景点
     * 
     * @param spotId 景点主键
     * @return 景点
     */
    @Override
    public ScenicSpot selectScenicSpotBySpotId(Long spotId)
    {
        return scenicSpotMapper.selectScenicSpotBySpotId(spotId);
    }

    /**
     * 查询景点列表
     * 
     * @param scenicSpot 景点
     * @return 景点
     */
    @Override
    public List<ScenicSpot> selectScenicSpotList(ScenicSpot scenicSpot)
    {
        return scenicSpotMapper.selectScenicSpotList(scenicSpot);
    }

    /**
     * 新增景点
     * 
     * @param scenicSpot 景点
     * @return 结果
     */
    @Override
    public int insertScenicSpot(ScenicSpot scenicSpot)
    {
        return scenicSpotMapper.insertScenicSpot(scenicSpot);
    }

    /**
     * 修改景点
     * 
     * @param scenicSpot 景点
     * @return 结果
     */
    @Override
    public int updateScenicSpot(ScenicSpot scenicSpot)
    {
        return scenicSpotMapper.updateScenicSpot(scenicSpot);
    }

    /**
     * 批量删除景点
     * 
     * @param spotIds 需要删除的景点主键
     * @return 结果
     */
    @Override
    public int deleteScenicSpotBySpotIds(Long[] spotIds)
    {
        return scenicSpotMapper.deleteScenicSpotBySpotIds(spotIds);
    }

    /**
     * 删除景点信息
     * 
     * @param spotId 景点主键
     * @return 结果
     */
    @Override
    public int deleteScenicSpotBySpotId(Long spotId)
    {
        return scenicSpotMapper.deleteScenicSpotBySpotId(spotId);
    }
}
