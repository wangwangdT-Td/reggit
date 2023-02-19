package com.wangwang.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wangwang.reggie.common.R;
import com.wangwang.reggie.entity.Employee;

import javax.servlet.http.HttpServletRequest;

public interface EmployeeService extends IService<Employee> {
    R<Employee> login(HttpServletRequest request, Employee employee);
}
