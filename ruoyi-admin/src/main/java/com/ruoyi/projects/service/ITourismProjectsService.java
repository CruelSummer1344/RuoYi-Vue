package com.ruoyi.projects.service;

import java.util.List;
import com.ruoyi.projects.domain.TourismProjects;

/**
 * 旅游项目Service接口
 * 
 * @author ruoyi
 * @date 2025-04-04
 */
public interface ITourismProjectsService 
{
    /**
     * 查询旅游项目
     * 
     * @param projectId 旅游项目主键
     * @return 旅游项目
     */
    public TourismProjects selectTourismProjectsByProjectId(Long projectId);

    /**
     * 查询旅游项目列表
     * 
     * @param tourismProjects 旅游项目
     * @return 旅游项目集合
     */
    public List<TourismProjects> selectTourismProjectsList(TourismProjects tourismProjects);

    /**
     * 新增旅游项目
     * 
     * @param tourismProjects 旅游项目
     * @return 结果
     */
    public int insertTourismProjects(TourismProjects tourismProjects);

    /**
     * 修改旅游项目
     * 
     * @param tourismProjects 旅游项目
     * @return 结果
     */
    public int updateTourismProjects(TourismProjects tourismProjects);

    /**
     * 批量删除旅游项目
     * 
     * @param projectIds 需要删除的旅游项目主键集合
     * @return 结果
     */
    public int deleteTourismProjectsByProjectIds(Long[] projectIds);

    /**
     * 删除旅游项目信息
     * 
     * @param projectId 旅游项目主键
     * @return 结果
     */
    public int deleteTourismProjectsByProjectId(Long projectId);
}
