package com.tour.hotel.service;

import java.util.List;
import com.tour.hotel.domain.Hotel;

/**
 * 酒店表Service接口
 * 
 * @author ruoyi
 * @date 2025-04-04
 */
public interface IHotelService 
{
    /**
     * 查询酒店表
     * 
     * @param hotelId 酒店表主键
     * @return 酒店表
     */
    public Hotel selectHotelByHotelId(Long hotelId);

    /**
     * 查询酒店表列表
     * 
     * @param hotel 酒店表
     * @return 酒店表集合
     */
    public List<Hotel> selectHotelList(Hotel hotel);

    /**
     * 新增酒店表
     * 
     * @param hotel 酒店表
     * @return 结果
     */
    public int insertHotel(Hotel hotel);

    /**
     * 修改酒店表
     * 
     * @param hotel 酒店表
     * @return 结果
     */
    public int updateHotel(Hotel hotel);

    /**
     * 批量删除酒店表
     * 
     * @param hotelIds 需要删除的酒店表主键集合
     * @return 结果
     */
    public int deleteHotelByHotelIds(Long[] hotelIds);

    /**
     * 删除酒店表信息
     * 
     * @param hotelId 酒店表主键
     * @return 结果
     */
    public int deleteHotelByHotelId(Long hotelId);
}
