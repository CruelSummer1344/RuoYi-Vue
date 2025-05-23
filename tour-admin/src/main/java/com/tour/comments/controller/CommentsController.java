package com.tour.comments.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import com.tour.hotel.service.IHotelService;
import com.tour.spot.domain.ScenicSpot;
import com.tour.spot.service.IScenicSpotService;
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
import com.tour.comments.domain.Comments;
import com.tour.comments.service.ICommentsService;
import com.tour.common.utils.poi.ExcelUtil;
import com.tour.common.core.page.TableDataInfo;
import com.tour.common.utils.SecurityUtils;

/**
 * 评论Controller
 * 
 * @author ruoyi
 * @date 2025-04-04
 */
@RestController
@RequestMapping("/comments/comments")
public class CommentsController extends BaseController
{
    @Autowired
    private ICommentsService commentsService;
    @Resource
    private IScenicSpotService scenicSpotService;
    @Resource
    private IHotelService hotelService;

    /**
     * 查询评论列表
     */
//    @PreAuthorize("@ss.hasPermi('comments:comments:list')")
    @GetMapping("/list")
    public TableDataInfo list(Comments comments)
    {
        startPage();
        List<Comments> list = commentsService.selectCommentsList(comments);
        Map<Long, String> collect = scenicSpotService.selectScenicSpotList(null).stream().collect(Collectors.toMap(ScenicSpot::getSpotId, ScenicSpot::getName));
        list.forEach(comment -> comment.setProjectName(collect.get(comment.getProjectId())));
        return getDataTable(list);
    }

    /**
     * 导出评论列表
     */
//    @PreAuthorize("@ss.hasPermi('comments:comments:export')")
    @Log(title = "评论", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Comments comments)
    {
        List<Comments> list = commentsService.selectCommentsList(comments);
        ExcelUtil<Comments> util = new ExcelUtil<Comments>(Comments.class);
        util.exportExcel(response, list, "评论数据");
    }

    /**
     * 获取评论详细信息
     */
//    @PreAuthorize("@ss.hasPermi('comments:comments:query')")
    @GetMapping(value = "/{commentId}")
    public AjaxResult getInfo(@PathVariable("commentId") Long commentId)
    {
        return success(commentsService.selectCommentsByCommentId(commentId));
    }

    /**
     * 新增评论
     */
//    @PreAuthorize("@ss.hasPermi('comments:comments:add')")
    @Log(title = "评论", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Comments comments)
    {
        comments.setUserId(SecurityUtils.getUserId());
        return toAjax(commentsService.insertComments(comments));
    }

    /**
     * 修改评论
     */
//    @PreAuthorize("@ss.hasPermi('comments:comments:edit')")
    @Log(title = "评论", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Comments comments)
    {
        return toAjax(commentsService.updateComments(comments));
    }

    /**
     * 删除评论
     */
//    @PreAuthorize("@ss.hasPermi('comments:comments:remove')")
    @Log(title = "评论", businessType = BusinessType.DELETE)
	@DeleteMapping("/{commentIds}")
    public AjaxResult remove(@PathVariable Long[] commentIds)
    {
        return toAjax(commentsService.deleteCommentsByCommentIds(commentIds));
    }
}
