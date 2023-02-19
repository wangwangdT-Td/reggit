package com.wangwang.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wangwang.reggie.common.CustomException;
import com.wangwang.reggie.common.R;
import com.wangwang.reggie.entity.Category;
import com.wangwang.reggie.entity.Dish;
import com.wangwang.reggie.entity.Setmeal;
import com.wangwang.reggie.mapper.CategoryMapper;
import com.wangwang.reggie.service.CategoryService;
import com.wangwang.reggie.service.DishService;
import com.wangwang.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    @Override
    public R<String> remove(Long id) {
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //添加查询条件，根据分类id查询
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,id);
        int count1 = dishService.count(dishLambdaQueryWrapper);
        //查询当前分类是否关联了菜品，如果已经关联，抛出一个业务异常
        if (count1 > 0){
            //已经关联菜品，抛出业务异常
            throw new CustomException("当前分类下关联了菜品,不能删除!");
        }

        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //添加查询条件，根据分类id查询
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        int count2 = setmealService.count(setmealLambdaQueryWrapper);
        //查询当前分类是否关联了套餐，如果已经关联，抛出一个业务异常
        if (count1 > 0){
            //已经关联套餐，抛出业务异常
            throw new CustomException("当前分类下关联了套餐,不能删除!!");
        }

        //正常删除分类
        super.removeById(id);
        return R.success("菜品分类删除成功!!");
    }
}
