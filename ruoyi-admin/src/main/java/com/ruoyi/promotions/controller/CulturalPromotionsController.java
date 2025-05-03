package com.ruoyi.promotions.controller;

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
import com.ruoyi.promotions.domain.CulturalPromotions;
import com.ruoyi.promotions.service.ICulturalPromotionsService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 文化Controller
 * 
 * @author ruoyi
 * @date 2025-04-04
 */
@RestController
@RequestMapping("/promotions/promotions")
public class CulturalPromotionsController extends BaseController
{
    @Autowired
    private ICulturalPromotionsService culturalPromotionsService;

    /**
     * 查询文化列表
     */
//    @PreAuthorize("@ss.hasPermi('promotions:promotions:list')")
    @GetMapping("/list")
    public TableDataInfo list(CulturalPromotions culturalPromotions)
    {
        startPage();
        List<CulturalPromotions> list = culturalPromotionsService.selectCulturalPromotionsList(culturalPromotions);
        return getDataTable(list);
    }

    /**
     * 导出文化列表
     */
//    @PreAuthorize("@ss.hasPermi('promotions:promotions:export')")
    @Log(title = "文化", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CulturalPromotions culturalPromotions)
    {
        List<CulturalPromotions> list = culturalPromotionsService.selectCulturalPromotionsList(culturalPromotions);
        ExcelUtil<CulturalPromotions> util = new ExcelUtil<CulturalPromotions>(CulturalPromotions.class);
        util.exportExcel(response, list, "文化数据");
    }

    /**
     * 获取文化详细信息
     */
//    @PreAuthorize("@ss.hasPermi('promotions:promotions:query')")
    @GetMapping(value = "/{promotionId}")
    public AjaxResult getInfo(@PathVariable("promotionId") Long promotionId)
    {
        return success(culturalPromotionsService.selectCulturalPromotionsByPromotionId(promotionId));
    }

    /**
     * 新增文化
     */
//    @PreAuthorize("@ss.hasPermi('promotions:promotions:add')")
    @Log(title = "文化", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CulturalPromotions culturalPromotions)
    {
        return toAjax(culturalPromotionsService.insertCulturalPromotions(culturalPromotions));
    }

    /**
     * 修改文化
     */
//    @PreAuthorize("@ss.hasPermi('promotions:promotions:edit')")
    @Log(title = "文化", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CulturalPromotions culturalPromotions)
    {
        return toAjax(culturalPromotionsService.updateCulturalPromotions(culturalPromotions));
    }

    /**
     * 删除文化
     */
//    @PreAuthorize("@ss.hasPermi('promotions:promotions:remove')")
    @Log(title = "文化", businessType = BusinessType.DELETE)
	@DeleteMapping("/{promotionIds}")
    public AjaxResult remove(@PathVariable Long[] promotionIds)
    {
        return toAjax(culturalPromotionsService.deleteCulturalPromotionsByPromotionIds(promotionIds));
    }
}
