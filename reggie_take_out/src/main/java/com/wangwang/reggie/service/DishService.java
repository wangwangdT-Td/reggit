package com.wangwang.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wangwang.reggie.dto.DishDto;
import com.wangwang.reggie.entity.Dish;

import java.util.List;

public interface DishService extends IService<Dish> {
    public void saveWithFlavor(DishDto dishDto);

    public DishDto getByIdWithFlavor(Long id);

    public void updateWithFlavor(DishDto dishDto);

    /**
     * 删除菜品的口味信息
     * @param ids
     */
    public void removeWithDish(List<Long> ids);
}
