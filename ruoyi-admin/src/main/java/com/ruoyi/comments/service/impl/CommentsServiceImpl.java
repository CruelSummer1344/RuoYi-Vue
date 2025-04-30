package com.ruoyi.comments.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.comments.mapper.CommentsMapper;
import com.ruoyi.comments.domain.Comments;
import com.ruoyi.comments.service.ICommentsService;

/**
 * 评论Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-04-04
 */
@Service
public class CommentsServiceImpl implements ICommentsService 
{
    @Autowired
    private CommentsMapper commentsMapper;

    /**
     * 查询评论
     * 
     * @param commentId 评论主键
     * @return 评论
     */
    @Override
    public Comments selectCommentsByCommentId(Long commentId)
    {
        return commentsMapper.selectCommentsByCommentId(commentId);
    }

    /**
     * 查询评论列表
     * 
     * @param comments 评论
     * @return 评论
     */
    @Override
    public List<Comments> selectCommentsList(Comments comments)
    {
        return commentsMapper.selectCommentsList(comments);
    }

    /**
     * 新增评论
     * 
     * @param comments 评论
     * @return 结果
     */
    @Override
    public int insertComments(Comments comments)
    {
        return commentsMapper.insertComments(comments);
    }

    /**
     * 修改评论
     * 
     * @param comments 评论
     * @return 结果
     */
    @Override
    public int updateComments(Comments comments)
    {
        return commentsMapper.updateComments(comments);
    }

    /**
     * 批量删除评论
     * 
     * @param commentIds 需要删除的评论主键
     * @return 结果
     */
    @Override
    public int deleteCommentsByCommentIds(Long[] commentIds)
    {
        return commentsMapper.deleteCommentsByCommentIds(commentIds);
    }

    /**
     * 删除评论信息
     * 
     * @param commentId 评论主键
     * @return 结果
     */
    @Override
    public int deleteCommentsByCommentId(Long commentId)
    {
        return commentsMapper.deleteCommentsByCommentId(commentId);
    }
}
