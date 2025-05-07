package com.tour.hotel.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tour.hotel.mapper.HotelMapper;
import com.tour.hotel.domain.Hotel;
import com.tour.hotel.service.IHotelService;

/**
 * 酒店表Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-04-04
 */
@Service
public class HotelServiceImpl implements IHotelService 
{
    @Autowired
    private HotelMapper hotelMapper;

    /**
     * 查询酒店表
     * 
     * @param hotelId 酒店表主键
     * @return 酒店表
     */
    @Override
    public Hotel selectHotelByHotelId(Long hotelId)
    {
        return hotelMapper.selectHotelByHotelId(hotelId);
    }

    /**
     * 查询酒店表列表
     * 
     * @param hotel 酒店表
     * @return 酒店表
     */
    @Override
    public List<Hotel> selectHotelList(Hotel hotel)
    {
        return hotelMapper.selectHotelList(hotel);
    }

    /**
     * 新增酒店表
     * 
     * @param hotel 酒店表
     * @return 结果
     */
    @Override
    public int insertHotel(Hotel hotel)
    {
        return hotelMapper.insertHotel(hotel);
    }

    /**
     * 修改酒店表
     * 
     * @param hotel 酒店表
     * @return 结果
     */
    @Override
    public int updateHotel(Hotel hotel)
    {
        return hotelMapper.updateHotel(hotel);
    }

    /**
     * 批量删除酒店表
     * 
     * @param hotelIds 需要删除的酒店表主键
     * @return 结果
     */
    @Override
    public int deleteHotelByHotelIds(Long[] hotelIds)
    {
        return hotelMapper.deleteHotelByHotelIds(hotelIds);
    }

    /**
     * 删除酒店表信息
     * 
     * @param hotelId 酒店表主键
     * @return 结果
     */
    @Override
    public int deleteHotelByHotelId(Long hotelId)
    {
        return hotelMapper.deleteHotelByHotelId(hotelId);
    }
}
