package com.ruoyi.product.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.product.domain.SpecialProduct;
import com.ruoyi.product.service.ISpecialProductService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 特色产品Controller
 * 
 * @author ruoyi
 * @date 2025-04-04
 */
@RestController
@RequestMapping("/product/product")
public class SpecialProductController extends BaseController
{
    @Autowired
    private ISpecialProductService specialProductService;

    /**
     * 查询特色产品列表
     */
//    @PreAuthorize("@ss.hasPermi('product:product:list')")
    @GetMapping("/list")
    public TableDataInfo list(SpecialProduct specialProduct)
    {
        startPage();
        List<SpecialProduct> list = specialProductService.selectSpecialProductList(specialProduct);
        return getDataTable(list);
    }

    /**
     * 导出特色产品列表
     */
//    @PreAuthorize("@ss.hasPermi('product:product:export')")
    @Log(title = "特色产品", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SpecialProduct specialProduct)
    {
        List<SpecialProduct> list = specialProductService.selectSpecialProductList(specialProduct);
        ExcelUtil<SpecialProduct> util = new ExcelUtil<SpecialProduct>(SpecialProduct.class);
        util.exportExcel(response, list, "特色产品数据");
    }

    /**
     * 获取特色产品详细信息
     */
//    @PreAuthorize("@ss.hasPermi('product:product:query')")
    @GetMapping(value = "/{productId}")
    public AjaxResult getInfo(@PathVariable("productId") Long productId)
    {
        return success(specialProductService.selectSpecialProductByProductId(productId));
    }

    /**
     * 新增特色产品
     */
//    @PreAuthorize("@ss.hasPermi('product:product:add')")
    @Log(title = "特色产品", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SpecialProduct specialProduct)
    {
        return toAjax(specialProductService.insertSpecialProduct(specialProduct));
    }

    /**
     * 修改特色产品
     */
//    @PreAuthorize("@ss.hasPermi('product:product:edit')")
    @Log(title = "特色产品", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SpecialProduct specialProduct)
    {
        return toAjax(specialProductService.updateSpecialProduct(specialProduct));
    }

    /**
     * 删除特色产品
     */
//    @PreAuthorize("@ss.hasPermi('product:product:remove')")
    @Log(title = "特色产品", businessType = BusinessType.DELETE)
	@DeleteMapping("/{productIds}")
    public AjaxResult remove(@PathVariable Long[] productIds)
    {
        return toAjax(specialProductService.deleteSpecialProductByProductIds(productIds));
    }
}
