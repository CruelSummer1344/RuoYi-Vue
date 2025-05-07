package com.tour.spot.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.tour.common.annotation.Log;
import com.tour.common.core.controller.BaseController;
import com.tour.common.core.domain.AjaxResult;
import com.tour.common.enums.BusinessType;
import com.tour.spot.domain.ScenicSpot;
import com.tour.spot.service.IScenicSpotService;
import com.tour.common.utils.poi.ExcelUtil;
import com.tour.common.core.page.TableDataInfo;

/**
 * 景点Controller
 * 
 * @author ruoyi
 * @date 2025-04-04
 */
@RestController
@RequestMapping("/spot/spot")
public class ScenicSpotController extends BaseController
{
    @Autowired
    private IScenicSpotService scenicSpotService;

    /**
     * 查询景点列表
     */
//    @PreAuthorize("@ss.hasPermi('spot:spot:list')")
    @GetMapping("/list")
    public TableDataInfo list(ScenicSpot scenicSpot)
    {
        startPage();
        List<ScenicSpot> list = scenicSpotService.selectScenicSpotList(scenicSpot);
        return getDataTable(list);
    }

    /**
     * 导出景点列表
     */
//    @PreAuthorize("@ss.hasPermi('spot:spot:export')")
    @Log(title = "景点", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ScenicSpot scenicSpot)
    {
        List<ScenicSpot> list = scenicSpotService.selectScenicSpotList(scenicSpot);
        ExcelUtil<ScenicSpot> util = new ExcelUtil<ScenicSpot>(ScenicSpot.class);
        util.exportExcel(response, list, "景点数据");
    }

    /**
     * 获取景点详细信息
     */
//    @PreAuthorize("@ss.hasPermi('spot:spot:query')")
    @GetMapping(value = "/{spotId}")
    public AjaxResult getInfo(@PathVariable("spotId") Long spotId)
    {
        return success(scenicSpotService.selectScenicSpotBySpotId(spotId));
    }

    /**
     * 新增景点
     */
//    @PreAuthorize("@ss.hasPermi('spot:spot:add')")
    @Log(title = "景点", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ScenicSpot scenicSpot)
    {
        return toAjax(scenicSpotService.insertScenicSpot(scenicSpot));
    }

    /**
     * 修改景点
     */
//    @PreAuthorize("@ss.hasPermi('spot:spot:edit')")
    @Log(title = "景点", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ScenicSpot scenicSpot)
    {
        return toAjax(scenicSpotService.updateScenicSpot(scenicSpot));
    }

    /**
     * 删除景点
     */
//    @PreAuthorize("@ss.hasPermi('spot:spot:remove')")
    @Log(title = "景点", businessType = BusinessType.DELETE)
	@DeleteMapping("/{spotIds}")
    public AjaxResult remove(@PathVariable Long[] spotIds)
    {
        return toAjax(scenicSpotService.deleteScenicSpotBySpotIds(spotIds));
    }
}
