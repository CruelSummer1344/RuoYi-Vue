package com.tour.promotions.service;

import java.util.List;
import com.tour.promotions.domain.CulturalPromotions;

/**
 * 文化Service接口
 * 
 * @author ruoyi
 * @date 2025-04-04
 */
public interface ICulturalPromotionsService 
{
    /**
     * 查询文化
     * 
     * @param promotionId 文化主键
     * @return 文化
     */
    public CulturalPromotions selectCulturalPromotionsByPromotionId(Long promotionId);

    /**
     * 查询文化列表
     * 
     * @param culturalPromotions 文化
     * @return 文化集合
     */
    public List<CulturalPromotions> selectCulturalPromotionsList(CulturalPromotions culturalPromotions);

    /**
     * 新增文化
     * 
     * @param culturalPromotions 文化
     * @return 结果
     */
    public int insertCulturalPromotions(CulturalPromotions culturalPromotions);

    /**
     * 修改文化
     * 
     * @param culturalPromotions 文化
     * @return 结果
     */
    public int updateCulturalPromotions(CulturalPromotions culturalPromotions);

    /**
     * 批量删除文化
     * 
     * @param promotionIds 需要删除的文化主键集合
     * @return 结果
     */
    public int deleteCulturalPromotionsByPromotionIds(Long[] promotionIds);

    /**
     * 删除文化信息
     * 
     * @param promotionId 文化主键
     * @return 结果
     */
    public int deleteCulturalPromotionsByPromotionId(Long promotionId);
}
