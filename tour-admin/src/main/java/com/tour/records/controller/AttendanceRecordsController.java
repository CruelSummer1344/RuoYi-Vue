package com.tour.records.controller;

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
import com.tour.records.domain.AttendanceRecords;
import com.tour.records.service.IAttendanceRecordsService;
import com.tour.common.utils.poi.ExcelUtil;
import com.tour.common.core.page.TableDataInfo;

/**
 * 考勤记录Controller
 * 
 * @author ruoyi
 * @date 2025-04-04
 */
@RestController
@RequestMapping("/records/records")
public class AttendanceRecordsController extends BaseController
{
    @Autowired
    private IAttendanceRecordsService attendanceRecordsService;

    /**
     * 查询考勤记录列表
     */
//    @PreAuthorize("@ss.hasPermi('records:records:list')")
    @GetMapping("/list")
    public TableDataInfo list(AttendanceRecords attendanceRecords)
    {
        startPage();
        List<AttendanceRecords> list = attendanceRecordsService.selectAttendanceRecordsList(attendanceRecords);
        return getDataTable(list);
    }

    /**
     * 导出考勤记录列表
     */
//    @PreAuthorize("@ss.hasPermi('records:records:export')")
    @Log(title = "考勤记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AttendanceRecords attendanceRecords)
    {
        List<AttendanceRecords> list = attendanceRecordsService.selectAttendanceRecordsList(attendanceRecords);
        ExcelUtil<AttendanceRecords> util = new ExcelUtil<AttendanceRecords>(AttendanceRecords.class);
        util.exportExcel(response, list, "考勤记录数据");
    }

    /**
     * 获取考勤记录详细信息
     */
//    @PreAuthorize("@ss.hasPermi('records:records:query')")
    @GetMapping(value = "/{recordId}")
    public AjaxResult getInfo(@PathVariable("recordId") Long recordId)
    {
        return success(attendanceRecordsService.selectAttendanceRecordsByRecordId(recordId));
    }

    /**
     * 新增考勤记录
     */
//    @PreAuthorize("@ss.hasPermi('records:records:add')")
    @Log(title = "考勤记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AttendanceRecords attendanceRecords)
    {
        return toAjax(attendanceRecordsService.insertAttendanceRecords(attendanceRecords));
    }

    /**
     * 修改考勤记录
     */
//    @PreAuthorize("@ss.hasPermi('records:records:edit')")
    @Log(title = "考勤记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AttendanceRecords attendanceRecords)
    {
        return toAjax(attendanceRecordsService.updateAttendanceRecords(attendanceRecords));
    }

    /**
     * 删除考勤记录
     */
//    @PreAuthorize("@ss.hasPermi('records:records:remove')")
    @Log(title = "考勤记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{recordIds}")
    public AjaxResult remove(@PathVariable Long[] recordIds)
    {
        return toAjax(attendanceRecordsService.deleteAttendanceRecordsByRecordIds(recordIds));
    }

    @PostMapping("/clickSubmit")
    public AjaxResult clickSubmit(){
        return toAjax(attendanceRecordsService.clickSubmit());
    }
}
