package com.tour.records.mapper;

import java.util.List;
import com.tour.records.domain.AttendanceRecords;

/**
 * 考勤记录Mapper接口
 * 
 * @author ruoyi
 * @date 2025-04-04
 */
public interface AttendanceRecordsMapper 
{
    /**
     * 查询考勤记录
     * 
     * @param recordId 考勤记录主键
     * @return 考勤记录
     */
    public AttendanceRecords selectAttendanceRecordsByRecordId(Long recordId);

    /**
     * 查询考勤记录列表
     * 
     * @param attendanceRecords 考勤记录
     * @return 考勤记录集合
     */
    public List<AttendanceRecords> selectAttendanceRecordsList(AttendanceRecords attendanceRecords);

    /**
     * 新增考勤记录
     * 
     * @param attendanceRecords 考勤记录
     * @return 结果
     */
    public int insertAttendanceRecords(AttendanceRecords attendanceRecords);

    /**
     * 修改考勤记录
     * 
     * @param attendanceRecords 考勤记录
     * @return 结果
     */
    public int updateAttendanceRecords(AttendanceRecords attendanceRecords);

    /**
     * 删除考勤记录
     * 
     * @param recordId 考勤记录主键
     * @return 结果
     */
    public int deleteAttendanceRecordsByRecordId(Long recordId);

    /**
     * 批量删除考勤记录
     * 
     * @param recordIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAttendanceRecordsByRecordIds(Long[] recordIds);
}
