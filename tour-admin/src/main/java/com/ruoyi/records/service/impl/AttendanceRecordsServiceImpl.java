package com.ruoyi.records.service.impl;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.records.domain.AttendanceRecords;
import com.ruoyi.records.mapper.AttendanceRecordsMapper;
import com.ruoyi.records.service.IAttendanceRecordsService;
import com.ruoyi.system.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 考勤记录Service业务层处理
 *
 * @author ruoyi
 * @date 2025-04-04
 */
@Service
public class AttendanceRecordsServiceImpl implements IAttendanceRecordsService {
    @Autowired
    private AttendanceRecordsMapper attendanceRecordsMapper;
    @Autowired
    private SysUserMapper sysUserMapper;

    /**
     * 查询考勤记录
     *
     * @param recordId 考勤记录主键
     * @return 考勤记录
     */
    @Override
    public AttendanceRecords selectAttendanceRecordsByRecordId(Long recordId) {
        AttendanceRecords attendanceRecords = attendanceRecordsMapper.selectAttendanceRecordsByRecordId(recordId);
        SysUser sysUser = sysUserMapper.selectUserById(SecurityUtils.getUserId());
        attendanceRecords.setUserName(sysUser.getUserName());
        return attendanceRecords;
    }

    /**
     * 查询考勤记录列表
     *
     * @param attendanceRecords 考勤记录
     * @return 考勤记录
     */
    @Override
    public List<AttendanceRecords> selectAttendanceRecordsList(AttendanceRecords attendanceRecords) {
        Map<Long, String> collect = sysUserMapper.selectUserList(new SysUser()).stream().collect(Collectors.toMap(SysUser::getUserId, SysUser::getUserName));
        List<AttendanceRecords> list = attendanceRecordsMapper.selectAttendanceRecordsList(attendanceRecords);
        list.forEach(item -> item.setUserName(collect.get(item.getUserId())));
        return list;
    }

    /**
     * 新增考勤记录
     *
     * @param attendanceRecords 考勤记录
     * @return 结果
     */
    @Override
    public int insertAttendanceRecords(AttendanceRecords attendanceRecords) {
        return attendanceRecordsMapper.insertAttendanceRecords(attendanceRecords);
    }

    /**
     * 修改考勤记录
     *
     * @param attendanceRecords 考勤记录
     * @return 结果
     */
    @Override
    public int updateAttendanceRecords(AttendanceRecords attendanceRecords) {
        return attendanceRecordsMapper.updateAttendanceRecords(attendanceRecords);
    }

    /**
     * 批量删除考勤记录
     *
     * @param recordIds 需要删除的考勤记录主键
     * @return 结果
     */
    @Override
    public int deleteAttendanceRecordsByRecordIds(Long[] recordIds) {
        return attendanceRecordsMapper.deleteAttendanceRecordsByRecordIds(recordIds);
    }

    /**
     * 删除考勤记录信息
     *
     * @param recordId 考勤记录主键
     * @return 结果
     */
    @Override
    public int deleteAttendanceRecordsByRecordId(Long recordId) {
        return attendanceRecordsMapper.deleteAttendanceRecordsByRecordId(recordId);
    }

    @Override
    public int clickSubmit() {
        AttendanceRecords attendanceRecords = new AttendanceRecords();
        attendanceRecords.setUserId(SecurityUtils.getUserId());
        List<AttendanceRecords> list = attendanceRecordsMapper.selectAttendanceRecordsList(attendanceRecords);

        attendanceRecords = list.get(0);
        boolean isToday = isToday(attendanceRecords.getCreatedAt());

        if (isToday) {
            attendanceRecords.setClockOutTime(new Date());
            return attendanceRecordsMapper.updateAttendanceRecords(attendanceRecords);
        }

        attendanceRecords = new AttendanceRecords();
        attendanceRecords.setUserId(SecurityUtils.getUserId());
        attendanceRecords.setClockInTime(new Date());
        return attendanceRecordsMapper.insertAttendanceRecords(attendanceRecords);
    }

    private boolean isToday(Date createdAt) {
        LocalDate today = LocalDate.now();
        LocalDate createdDate = createdAt.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        return today.isEqual(createdDate);
    }

}
