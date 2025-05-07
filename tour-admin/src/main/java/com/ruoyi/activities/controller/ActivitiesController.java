package com.ruoyi.activities.controller;

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
import com.ruoyi.activities.domain.Activities;
import com.ruoyi.activities.service.IActivitiesService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 活动Controller
 * 
 * @author ruoyi
 * @date 2025-04-04
 */
@RestController
@RequestMapping("/activities/activities")
public class ActivitiesController extends BaseController
{
    @Autowired
    private IActivitiesService activitiesService;

    /**
     * 查询活动列表
     */
    @PreAuthorize("@ss.hasPermi('activities:activities:list')")
    @GetMapping("/list")
    public TableDataInfo list(Activities activities)
    {
        startPage();
        List<Activities> list = activitiesService.selectActivitiesList(activities);
        return getDataTable(list);
    }

    /**
     * 导出活动列表
     */
    @PreAuthorize("@ss.hasPermi('activities:activities:export')")
    @Log(title = "活动", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Activities activities)
    {
        List<Activities> list = activitiesService.selectActivitiesList(activities);
        ExcelUtil<Activities> util = new ExcelUtil<Activities>(Activities.class);
        util.exportExcel(response, list, "活动数据");
    }

    /**
     * 获取活动详细信息
     */
    @PreAuthorize("@ss.hasPermi('activities:activities:query')")
    @GetMapping(value = "/{activityId}")
    public AjaxResult getInfo(@PathVariable("activityId") Long activityId)
    {
        return success(activitiesService.selectActivitiesByActivityId(activityId));
    }

    /**
     * 新增活动
     */
    @PreAuthorize("@ss.hasPermi('activities:activities:add')")
    @Log(title = "活动", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Activities activities)
    {
        return toAjax(activitiesService.insertActivities(activities));
    }

    /**
     * 修改活动
     */
    @PreAuthorize("@ss.hasPermi('activities:activities:edit')")
    @Log(title = "活动", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Activities activities)
    {
        return toAjax(activitiesService.updateActivities(activities));
    }

    /**
     * 删除活动
     */
    @PreAuthorize("@ss.hasPermi('activities:activities:remove')")
    @Log(title = "活动", businessType = BusinessType.DELETE)
	@DeleteMapping("/{activityIds}")
    public AjaxResult remove(@PathVariable Long[] activityIds)
    {
        return toAjax(activitiesService.deleteActivitiesByActivityIds(activityIds));
    }
}
