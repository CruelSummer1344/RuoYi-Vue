package com.ruoyi.projects.controller;

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
import com.ruoyi.projects.domain.TourismProjects;
import com.ruoyi.projects.service.ITourismProjectsService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 旅游项目Controller
 * 
 * @author ruoyi
 * @date 2025-04-04
 */
@RestController
@RequestMapping("/projects/projects")
public class TourismProjectsController extends BaseController
{
    @Autowired
    private ITourismProjectsService tourismProjectsService;

    /**
     * 查询旅游项目列表
     */
    @PreAuthorize("@ss.hasPermi('projects:projects:list')")
    @GetMapping("/list")
    public TableDataInfo list(TourismProjects tourismProjects)
    {
        startPage();
        List<TourismProjects> list = tourismProjectsService.selectTourismProjectsList(tourismProjects);
        return getDataTable(list);
    }

    /**
     * 导出旅游项目列表
     */
    @PreAuthorize("@ss.hasPermi('projects:projects:export')")
    @Log(title = "旅游项目", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TourismProjects tourismProjects)
    {
        List<TourismProjects> list = tourismProjectsService.selectTourismProjectsList(tourismProjects);
        ExcelUtil<TourismProjects> util = new ExcelUtil<TourismProjects>(TourismProjects.class);
        util.exportExcel(response, list, "旅游项目数据");
    }

    /**
     * 获取旅游项目详细信息
     */
    @PreAuthorize("@ss.hasPermi('projects:projects:query')")
    @GetMapping(value = "/{projectId}")
    public AjaxResult getInfo(@PathVariable("projectId") Long projectId)
    {
        return success(tourismProjectsService.selectTourismProjectsByProjectId(projectId));
    }

    /**
     * 新增旅游项目
     */
    @PreAuthorize("@ss.hasPermi('projects:projects:add')")
    @Log(title = "旅游项目", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TourismProjects tourismProjects)
    {
        return toAjax(tourismProjectsService.insertTourismProjects(tourismProjects));
    }

    /**
     * 修改旅游项目
     */
    @PreAuthorize("@ss.hasPermi('projects:projects:edit')")
    @Log(title = "旅游项目", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TourismProjects tourismProjects)
    {
        return toAjax(tourismProjectsService.updateTourismProjects(tourismProjects));
    }

    /**
     * 删除旅游项目
     */
    @PreAuthorize("@ss.hasPermi('projects:projects:remove')")
    @Log(title = "旅游项目", businessType = BusinessType.DELETE)
	@DeleteMapping("/{projectIds}")
    public AjaxResult remove(@PathVariable Long[] projectIds)
    {
        return toAjax(tourismProjectsService.deleteTourismProjectsByProjectIds(projectIds));
    }
}
