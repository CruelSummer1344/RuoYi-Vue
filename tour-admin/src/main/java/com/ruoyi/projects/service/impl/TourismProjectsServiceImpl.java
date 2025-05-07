package com.ruoyi.projects.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.projects.mapper.TourismProjectsMapper;
import com.ruoyi.projects.domain.TourismProjects;
import com.ruoyi.projects.service.ITourismProjectsService;

/**
 * 旅游项目Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-04-04
 */
@Service
public class TourismProjectsServiceImpl implements ITourismProjectsService 
{
    @Autowired
    private TourismProjectsMapper tourismProjectsMapper;

    /**
     * 查询旅游项目
     * 
     * @param projectId 旅游项目主键
     * @return 旅游项目
     */
    @Override
    public TourismProjects selectTourismProjectsByProjectId(Long projectId)
    {
        return tourismProjectsMapper.selectTourismProjectsByProjectId(projectId);
    }

    /**
     * 查询旅游项目列表
     * 
     * @param tourismProjects 旅游项目
     * @return 旅游项目
     */
    @Override
    public List<TourismProjects> selectTourismProjectsList(TourismProjects tourismProjects)
    {
        return tourismProjectsMapper.selectTourismProjectsList(tourismProjects);
    }

    /**
     * 新增旅游项目
     * 
     * @param tourismProjects 旅游项目
     * @return 结果
     */
    @Override
    public int insertTourismProjects(TourismProjects tourismProjects)
    {
        return tourismProjectsMapper.insertTourismProjects(tourismProjects);
    }

    /**
     * 修改旅游项目
     * 
     * @param tourismProjects 旅游项目
     * @return 结果
     */
    @Override
    public int updateTourismProjects(TourismProjects tourismProjects)
    {
        return tourismProjectsMapper.updateTourismProjects(tourismProjects);
    }

    /**
     * 批量删除旅游项目
     * 
     * @param projectIds 需要删除的旅游项目主键
     * @return 结果
     */
    @Override
    public int deleteTourismProjectsByProjectIds(Long[] projectIds)
    {
        return tourismProjectsMapper.deleteTourismProjectsByProjectIds(projectIds);
    }

    /**
     * 删除旅游项目信息
     * 
     * @param projectId 旅游项目主键
     * @return 结果
     */
    @Override
    public int deleteTourismProjectsByProjectId(Long projectId)
    {
        return tourismProjectsMapper.deleteTourismProjectsByProjectId(projectId);
    }
}
