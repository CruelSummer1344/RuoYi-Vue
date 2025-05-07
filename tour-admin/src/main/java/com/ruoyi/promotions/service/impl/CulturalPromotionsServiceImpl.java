package com.ruoyi.promotions.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.promotions.mapper.CulturalPromotionsMapper;
import com.ruoyi.promotions.domain.CulturalPromotions;
import com.ruoyi.promotions.service.ICulturalPromotionsService;

/**
 * 文化Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-04-04
 */
@Service
public class CulturalPromotionsServiceImpl implements ICulturalPromotionsService 
{
    @Autowired
    private CulturalPromotionsMapper culturalPromotionsMapper;

    /**
     * 查询文化
     * 
     * @param promotionId 文化主键
     * @return 文化
     */
    @Override
    public CulturalPromotions selectCulturalPromotionsByPromotionId(Long promotionId)
    {
        return culturalPromotionsMapper.selectCulturalPromotionsByPromotionId(promotionId);
    }

    /**
     * 查询文化列表
     * 
     * @param culturalPromotions 文化
     * @return 文化
     */
    @Override
    public List<CulturalPromotions> selectCulturalPromotionsList(CulturalPromotions culturalPromotions)
    {
        return culturalPromotionsMapper.selectCulturalPromotionsList(culturalPromotions);
    }

    /**
     * 新增文化
     * 
     * @param culturalPromotions 文化
     * @return 结果
     */
    @Override
    public int insertCulturalPromotions(CulturalPromotions culturalPromotions)
    {
        return culturalPromotionsMapper.insertCulturalPromotions(culturalPromotions);
    }

    /**
     * 修改文化
     * 
     * @param culturalPromotions 文化
     * @return 结果
     */
    @Override
    public int updateCulturalPromotions(CulturalPromotions culturalPromotions)
    {
        return culturalPromotionsMapper.updateCulturalPromotions(culturalPromotions);
    }

    /**
     * 批量删除文化
     * 
     * @param promotionIds 需要删除的文化主键
     * @return 结果
     */
    @Override
    public int deleteCulturalPromotionsByPromotionIds(Long[] promotionIds)
    {
        return culturalPromotionsMapper.deleteCulturalPromotionsByPromotionIds(promotionIds);
    }

    /**
     * 删除文化信息
     * 
     * @param promotionId 文化主键
     * @return 结果
     */
    @Override
    public int deleteCulturalPromotionsByPromotionId(Long promotionId)
    {
        return culturalPromotionsMapper.deleteCulturalPromotionsByPromotionId(promotionId);
    }
}
