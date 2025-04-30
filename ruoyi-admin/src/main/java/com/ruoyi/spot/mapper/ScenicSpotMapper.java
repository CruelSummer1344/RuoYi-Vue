package com.ruoyi.spot.mapper;

import java.util.List;
import com.ruoyi.spot.domain.ScenicSpot;

/**
 * 景点Mapper接口
 * 
 * @author ruoyi
 * @date 2025-04-04
 */
public interface ScenicSpotMapper 
{
    /**
     * 查询景点
     * 
     * @param spotId 景点主键
     * @return 景点
     */
    public ScenicSpot selectScenicSpotBySpotId(Long spotId);

    /**
     * 查询景点列表
     * 
     * @param scenicSpot 景点
     * @return 景点集合
     */
    public List<ScenicSpot> selectScenicSpotList(ScenicSpot scenicSpot);

    /**
     * 新增景点
     * 
     * @param scenicSpot 景点
     * @return 结果
     */
    public int insertScenicSpot(ScenicSpot scenicSpot);

    /**
     * 修改景点
     * 
     * @param scenicSpot 景点
     * @return 结果
     */
    public int updateScenicSpot(ScenicSpot scenicSpot);

    /**
     * 删除景点
     * 
     * @param spotId 景点主键
     * @return 结果
     */
    public int deleteScenicSpotBySpotId(Long spotId);

    /**
     * 批量删除景点
     * 
     * @param spotIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteScenicSpotBySpotIds(Long[] spotIds);
}
