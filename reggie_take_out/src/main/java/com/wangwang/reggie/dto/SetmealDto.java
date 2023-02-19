package com.wangwang.reggie.dto;


import com.wangwang.reggie.entity.Setmeal;
import com.wangwang.reggie.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
