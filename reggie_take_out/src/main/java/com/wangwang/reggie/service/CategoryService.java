package com.wangwang.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wangwang.reggie.common.R;
import com.wangwang.reggie.entity.Category;


public interface CategoryService extends IService<Category> {
    R<String> remove(Long id);
}
