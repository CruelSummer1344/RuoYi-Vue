package com.tour.order.controller;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;
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
import com.tour.order.domain.Order;
import com.tour.order.service.IOrderService;
import com.tour.common.utils.poi.ExcelUtil;
import com.tour.common.core.page.TableDataInfo;

/**
 * 订单Controller
 * 
 * @author ruoyi
 * @date 2025-04-04
 */
@RestController
@RequestMapping("/order/order")
public class OrderController extends BaseController
{
    @Autowired
    private IOrderService orderService;

    /**
     * 查询订单列表
     */
//    @PreAuthorize("@ss.hasPermi('order:order:list')")
    @GetMapping("/list")
    public TableDataInfo list(Order order)
    {
        startPage();
        List<Order> list = orderService.selectOrderList(order);
        return getDataTable(list);
    }

    /**
     * 导出订单列表
     */
//    @PreAuthorize("@ss.hasPermi('order:order:export')")
    @Log(title = "订单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Order order)
    {
        List<Order> list = orderService.selectOrderList(order);
        ExcelUtil<Order> util = new ExcelUtil<Order>(Order.class);
        util.exportExcel(response, list, "订单数据");
    }

    /**
     * 获取订单详细信息
     */
//    @PreAuthorize("@ss.hasPermi('order:order:query')")
    @GetMapping(value = "/{orderId}")
    public AjaxResult getInfo(@PathVariable("orderId") Long orderId)
    {
        return success(orderService.selectOrderByOrderId(orderId));
    }

    /**
     * 新增订单
     */
//    @PreAuthorize("@ss.hasPermi('order:order:add')")
    @Log(title = "订单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Order order)
    {
        return toAjax(orderService.insertOrder(order));
    }

    /**
     * 修改订单
     */
//    @PreAuthorize("@ss.hasPermi('order:order:edit')")
    @Log(title = "订单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Order order)
    {
        return toAjax(orderService.updateOrder(order));
    }

    /**
     * 删除订单
     */
//    @PreAuthorize("@ss.hasPermi('order:order:remove')")
    @Log(title = "订单", businessType = BusinessType.DELETE)
	@DeleteMapping("/{orderIds}")
    public AjaxResult remove(@PathVariable Long[] orderIds)
    {
        return toAjax(orderService.deleteOrderByOrderIds(orderIds));
    }

    @GetMapping("/listData")
    public AjaxResult listData(Order order)
    {
        List<Order> list = orderService.selectOrderList(order);
// 获取当前时间和三个月前的时间戳
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime threeMonthsAgo = now.minusMonths(3);

        // 将 LocalDateTime 转换为时间戳（毫秒）
        Date threeMonthsAgoDate = Date.from(threeMonthsAgo.atZone(ZoneId.systemDefault()).toInstant());
        // 获取当前时间戳
        // 过滤近三个月的数据并按 createdAt 排序
        List<Order> collect = list.stream()
                .filter(order1 -> !order1.getCreatedAt().before(threeMonthsAgoDate))
                .sorted(Comparator.comparing(Order::getCreatedAt))
                .collect(Collectors.toList());

        return AjaxResult.success(collect);
    }
}
