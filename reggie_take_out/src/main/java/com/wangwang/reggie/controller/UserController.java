package com.wangwang.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wangwang.reggie.common.R;
import com.wangwang.reggie.entity.User;
import com.wangwang.reggie.service.UserService;
import com.wangwang.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {


    @Autowired
    private UserService userService;

    /**
     * 发送短信验证码
     * @param phone
     * @return
     */
    @GetMapping("/code")
    public R getCode(String phone , HttpSession session){

        //1. 生成验证码
        String code = ValidateCodeUtils.generateValidateCode4String(4);

        //2. 给这个手机号发短信
        //SMSUtils.sendMessage("签名", "模板" , phone , code);

        //3. 把验证码保存到session作用域，以便一会登录，可以判断验证码对不对！
        session.setAttribute(phone, code);

        //真实情况：
        //return R.success("短信验证码已发送！");

        //把验证码返回给前端，前端，把验证码放到输入框里面去！
        return R.success(code);
    }


    /**
     *  登录
     * @param map
     * @return
     */
    @PostMapping("/login")
    public R login(@RequestBody Map<String , String> map, HttpSession session){
        //0. 获取数据
        String phone = map.get("phone");
        String code = map.get("code");

        //1. 获取到以前保存的验证码
        String saveCode = (String) session.getAttribute(phone);

        //2. 和现在传递上来的验证码进行比较
        //if(saveCode.equalsIgnoreCase(code))
        if (true)
        {
            //登录成功

            /*
                1. 需要把用户的信息保存到数据库里面去。以便在后续的操作当中可以识别该用户。
                2. 需要先判断这个用户有没有在数据库里里面，如果有，就把他的信息查询出来，如果没有就表明
                    这个用户是第一次来登录，此时就要做保存的工作！
             */
            //1. 根据电话号码查询用户
            LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
            lqw.eq(User::getPhone , phone);
            User user = userService.getOne(lqw);

            //2. 判断是要做添加还是不添加
            if(user == null ){
                // 第一次来！
                User u = new User();
                u.setPhone(phone);
                u.setName("菩提阁_"+phone);

                //保存用户
                userService.save(u);

                session.setAttribute("user", u.getId());
            }else{
                //不是第一次来。
                session.setAttribute("user", user.getId());
            }
            return R.success("登录成功！");
        }
        //登录失败
        return R.error("登录失败！");
    }
}
