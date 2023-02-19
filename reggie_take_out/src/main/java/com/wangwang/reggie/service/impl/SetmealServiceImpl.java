package com.wangwang.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wangwang.reggie.common.CustomException;
import com.wangwang.reggie.dto.SetmealDto;
import com.wangwang.reggie.entity.DishFlavor;
import com.wangwang.reggie.entity.Setmeal;
import com.wangwang.reggie.entity.SetmealDish;
import com.wangwang.reggie.mapper.SetmealMapper;
import com.wangwang.reggie.service.SetmealDishService;
import com.wangwang.reggie.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Resource
    private SetmealDishService setmealDishService;

    @Override
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {

        //保存套餐信息
        super.save(setmealDto);

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().map((item) ->{
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        //保存关联信息
        setmealDishService.saveBatch(setmealDishes);
    }

    @Override
    public SetmealDto getByIdWithDish(Long id) {
        //根据id查询菜品基本信息
        Setmeal setmeal = getById(id);

        //拷贝信息到dto
        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(setmeal,setmealDto);

        //查询套餐对应的菜品信息
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId,setmeal.getId());
        List<SetmealDish> flavors = setmealDishService.list(queryWrapper);

        setmealDto.setSetmealDishes(flavors);
        return setmealDto;
    }

    @Override
    public void updateWithDish(SetmealDto setmealDto) {
        //更新基本信息
        this.updateById(setmealDto);

        //删除当前套餐的菜品信息
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId,setmealDto.getId());
        setmealDishService.remove(queryWrapper);

        //添加菜品信息
        List<SetmealDish> flavors = setmealDto.getSetmealDishes();

        flavors.stream().map((item) ->{
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(flavors);
    }

    @Override
    @Transactional
    public void deleteWithDish(List<Long> ids) {
        //根据id查询查询套餐是否在售
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId,ids);
        queryWrapper.eq(Setmeal::getStatus,1);

        int count = count(queryWrapper);

        if (count >0){
            throw new CustomException("套餐正在售卖，不能删除");
        }

        //删除套餐信息
        this.removeByIds(ids);

        //根据套餐id查询关联表的菜品信息
        LambdaQueryWrapper<SetmealDish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.in(SetmealDish::getSetmealId,ids);

        //删除关联表的菜品信息
        setmealDishService.remove(dishLambdaQueryWrapper);
    }


}
