package com.tour.product.service;

import java.util.List;
import com.tour.product.domain.SpecialProduct;

/**
 * 特色产品Service接口
 * 
 * @author ruoyi
 * @date 2025-04-04
 */
public interface ISpecialProductService 
{
    /**
     * 查询特色产品
     * 
     * @param productId 特色产品主键
     * @return 特色产品
     */
    public SpecialProduct selectSpecialProductByProductId(Long productId);

    /**
     * 查询特色产品列表
     * 
     * @param specialProduct 特色产品
     * @return 特色产品集合
     */
    public List<SpecialProduct> selectSpecialProductList(SpecialProduct specialProduct);

    /**
     * 新增特色产品
     * 
     * @param specialProduct 特色产品
     * @return 结果
     */
    public int insertSpecialProduct(SpecialProduct specialProduct);

    /**
     * 修改特色产品
     * 
     * @param specialProduct 特色产品
     * @return 结果
     */
    public int updateSpecialProduct(SpecialProduct specialProduct);

    /**
     * 批量删除特色产品
     * 
     * @param productIds 需要删除的特色产品主键集合
     * @return 结果
     */
    public int deleteSpecialProductByProductIds(Long[] productIds);

    /**
     * 删除特色产品信息
     * 
     * @param productId 特色产品主键
     * @return 结果
     */
    public int deleteSpecialProductByProductId(Long productId);
}
