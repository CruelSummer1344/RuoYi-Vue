package com.ruoyi.product.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.product.mapper.SpecialProductMapper;
import com.ruoyi.product.domain.SpecialProduct;
import com.ruoyi.product.service.ISpecialProductService;

/**
 * 特色产品Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-04-04
 */
@Service
public class SpecialProductServiceImpl implements ISpecialProductService 
{
    @Autowired
    private SpecialProductMapper specialProductMapper;

    /**
     * 查询特色产品
     * 
     * @param productId 特色产品主键
     * @return 特色产品
     */
    @Override
    public SpecialProduct selectSpecialProductByProductId(Long productId)
    {
        return specialProductMapper.selectSpecialProductByProductId(productId);
    }

    /**
     * 查询特色产品列表
     * 
     * @param specialProduct 特色产品
     * @return 特色产品
     */
    @Override
    public List<SpecialProduct> selectSpecialProductList(SpecialProduct specialProduct)
    {
        return specialProductMapper.selectSpecialProductList(specialProduct);
    }

    /**
     * 新增特色产品
     * 
     * @param specialProduct 特色产品
     * @return 结果
     */
    @Override
    public int insertSpecialProduct(SpecialProduct specialProduct)
    {
        return specialProductMapper.insertSpecialProduct(specialProduct);
    }

    /**
     * 修改特色产品
     * 
     * @param specialProduct 特色产品
     * @return 结果
     */
    @Override
    public int updateSpecialProduct(SpecialProduct specialProduct)
    {
        return specialProductMapper.updateSpecialProduct(specialProduct);
    }

    /**
     * 批量删除特色产品
     * 
     * @param productIds 需要删除的特色产品主键
     * @return 结果
     */
    @Override
    public int deleteSpecialProductByProductIds(Long[] productIds)
    {
        return specialProductMapper.deleteSpecialProductByProductIds(productIds);
    }

    /**
     * 删除特色产品信息
     * 
     * @param productId 特色产品主键
     * @return 结果
     */
    @Override
    public int deleteSpecialProductByProductId(Long productId)
    {
        return specialProductMapper.deleteSpecialProductByProductId(productId);
    }
}
