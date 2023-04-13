package com.example.mwhbs.controller;

import com.example.mwhbs.enums.ErrorMsg;
import com.example.mwhbs.entity.IdleItem;
import com.example.mwhbs.service.IdleItemService;
import com.example.mwhbs.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 商品管理
 */
@RestController
@RequestMapping("idle")
public class IdleItemController {

    @Autowired
    private IdleItemService idleItemService;

    @PostMapping("add")
    public ResultVo addIdleItem(@CookieValue("shUserId")
                                    @NotNull(message = "登录异常 请重新登录")
                                    @NotEmpty(message = "登录异常 请重新登录") String shUserId,
                                @RequestBody IdleItem idleItem){
        idleItem.setUserId(Long.valueOf(shUserId));
        idleItem.setIdleStatus((byte) 1);
        idleItem.setReleaseTime(new Date());
        if(idleItemService.addIdleItem(idleItem)){
            return ResultVo.success(idleItem);
        }
        return ResultVo.fail(ErrorMsg.SYSTEM_ERROR);
    }

    @GetMapping("info")
    public ResultVo getIdleItem(@RequestParam Long id){
        return ResultVo.success(idleItemService.getIdleItem(id));
    }

    @GetMapping("all")
    public ResultVo getAllIdleItem(@CookieValue("shUserId")
                                       @NotNull(message = "登录异常 请重新登录")
                                       @NotEmpty(message = "登录异常 请重新登录") String shUserId){
        return ResultVo.success(idleItemService.getAllIdelItem(Long.valueOf(shUserId)));
    }

    @GetMapping("find")
    public ResultVo findIdleItem(@RequestParam(value = "findValue",required = false) String findValue,
                                 @RequestParam(value = "page",required = false) Integer page,
                                 @RequestParam(value = "nums",required = false) Integer nums){
        if(null==findValue){
            findValue="";
        }
        int p=1;
        int n=8;
        if(null!=page){
            p=page>0?page:1;
        }
        if(null!=nums){
            n=nums>0?nums:8;
        }
        return ResultVo.success(idleItemService.findIdleItem(findValue,p,n));
    }

    @GetMapping("lable")
    public ResultVo findIdleItemByLable(@RequestParam(value = "idleLabel",required = true) Integer idleLabel,
                                 @RequestParam(value = "page",required = false) Integer page,
                                 @RequestParam(value = "nums",required = false) Integer nums){
        int p=1;
        int n=8;
        if(null!=page){
            p=page>0?page:1;
        }
        if(null!=nums){
            n=nums>0?nums:8;
        }
        return ResultVo.success(idleItemService.findIdleItemByLable(idleLabel,p,n));
    }

    @PostMapping("update")
    public ResultVo updateIdleItem(@CookieValue("shUserId")
                                       @NotNull(message = "登录异常 请重新登录")
                                       @NotEmpty(message = "登录异常 请重新登录") String shUserId,
                                   @RequestBody IdleItem idleItem){
        idleItem.setUserId(Long.valueOf(shUserId));
        if(idleItemService.updateIdleItem(idleItem)){
            return ResultVo.success();
        }
        return ResultVo.fail(ErrorMsg.SYSTEM_ERROR);
    }
}
