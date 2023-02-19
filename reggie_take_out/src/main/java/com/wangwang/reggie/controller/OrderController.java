package com.wangwang.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wangwang.reggie.common.R;
import com.wangwang.reggie.entity.Orders;
import com.wangwang.reggie.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {

    @Resource
    private OrderService orderService;

    /**
     * 用户下单
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders) {
        log.info("提交的订单信息: " + orders);
        orderService.submit(orders);
        return R.success("用户下单成功");
    }

    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @param number
     * @param beginTime
     * @param endTime
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, Long number, String beginTime, String endTime) {
        log.info("page = {},pageSize = {},name = {}", page, pageSize, number);
        log.info("beginTime = {} ,endTime = {} ",beginTime,endTime);

        //具有转换功能的对象

        //构造分页构造器
        Page pageInfo = new Page(page, pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper();
        //添加过滤条件
        queryWrapper.like(number != null, Orders::getNumber, number);
        queryWrapper.ge(beginTime != null,Orders::getOrderTime,beginTime )//大于开始时间
                .le(endTime != null,Orders::getOrderTime,endTime);//小于等于结束时间
        //添加排序条件
        queryWrapper.orderByDesc(Orders::getOrderTime);
        //执行查询
        orderService.page(pageInfo, queryWrapper);
        return R.success(pageInfo);
    }

    /**
     * 订单状态修改
     * @param orders
     * @return
     */
    @PutMapping
    public R<String> order(@RequestBody Orders orders){
        //log.info("orders:{}", orders);
        Orders order = orderService.getById(orders.getId());
        if (order.getStatus() == 2){
            orders.setStatus(3);
            orderService.updateById(orders);
            return R.success("订单派送成功");
        }else {
            orders.setStatus(4);
            orderService.updateById(orders);
            return R.success("订单已完成");
        }
    }

}
