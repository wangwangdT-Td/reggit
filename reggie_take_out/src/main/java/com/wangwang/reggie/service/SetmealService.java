package com.wangwang.reggie.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.wangwang.reggie.dto.SetmealDto;
import com.wangwang.reggie.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {

    /**
     * 新增套餐，保存套餐和菜品的关联关系
     * @param setmealDto
     */
    public void saveWithDish(SetmealDto setmealDto);

    /**
     * 根据id获取套餐的菜品信息
     * @param id
     * @return
     */
    public SetmealDto getByIdWithDish(Long id);

    /**
     * 修改套餐和套餐的菜品信息
     * @param setmealDto
     */
    public void updateWithDish(SetmealDto setmealDto);

    void deleteWithDish(List<Long> ids);
}
