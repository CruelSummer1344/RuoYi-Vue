package com.ruoyi.sample.controller;

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
import com.ruoyi.sample.domain.WaterSample;
import com.ruoyi.sample.service.IWaterSampleService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 水样Controller
 * 
 * @author ruoyi
 * @date 2025-04-04
 */
@RestController
@RequestMapping("/sample/sample")
public class WaterSampleController extends BaseController
{
    @Autowired
    private IWaterSampleService waterSampleService;

    /**
     * 查询水样列表
     */
    @PreAuthorize("@ss.hasPermi('sample:sample:list')")
    @GetMapping("/list")
    public TableDataInfo list(WaterSample waterSample)
    {
//        startPage();
        List<WaterSample> list = waterSampleService.selectWaterSampleList(waterSample);
        return getDataTable(list);
    }

    /**
     * 导出水样列表
     */
    @PreAuthorize("@ss.hasPermi('sample:sample:export')")
    @Log(title = "水样", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WaterSample waterSample)
    {
        List<WaterSample> list = waterSampleService.selectWaterSampleList(waterSample);
        ExcelUtil<WaterSample> util = new ExcelUtil<WaterSample>(WaterSample.class);
        util.exportExcel(response, list, "水样数据");
    }

    /**
     * 获取水样详细信息
     */
    @PreAuthorize("@ss.hasPermi('sample:sample:query')")
    @GetMapping(value = "/{sampleId}")
    public AjaxResult getInfo(@PathVariable("sampleId") Long sampleId)
    {
        return success(waterSampleService.selectWaterSampleBySampleId(sampleId));
    }

    /**
     * 新增水样
     */
    @PreAuthorize("@ss.hasPermi('sample:sample:add')")
    @Log(title = "水样", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WaterSample waterSample)
    {
        return toAjax(waterSampleService.insertWaterSample(waterSample));
    }

    /**
     * 修改水样
     */
    @PreAuthorize("@ss.hasPermi('sample:sample:edit')")
    @Log(title = "水样", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WaterSample waterSample)
    {
        return toAjax(waterSampleService.updateWaterSample(waterSample));
    }

    /**
     * 删除水样
     */
    @PreAuthorize("@ss.hasPermi('sample:sample:remove')")
    @Log(title = "水样", businessType = BusinessType.DELETE)
	@DeleteMapping("/{sampleIds}")
    public AjaxResult remove(@PathVariable Long[] sampleIds)
    {
        return toAjax(waterSampleService.deleteWaterSampleBySampleIds(sampleIds));
    }
}
