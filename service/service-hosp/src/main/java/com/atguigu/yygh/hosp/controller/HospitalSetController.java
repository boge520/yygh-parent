package com.atguigu.yygh.hosp.controller;

import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.common.util.MD5;
import com.atguigu.yygh.hosp.service.HospitalSetService;
import com.atguigu.yygh.model.hosp.HospitalSet;
import com.atguigu.yygh.vo.hosp.HospitalSetQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

@Api(tags = "医院设置管理")
@RestController
@RequestMapping("/admin/hosp/hospitalSet")
@CrossOrigin
public class HospitalSetController {

    @Autowired
    private HospitalSetService hospitalSetService;

    //nihao
    /**
     * 1 查询医院设置表所有信息
     *
     * @return
     */
    @ApiOperation("获取所有医院设置")
    @GetMapping("/findAll")
    public Result findAll() {
        /*try {
            int i = 10 / 0;
        } catch (Exception e) {
            throw new YyghException("失败", 201);
        }*/
        List<HospitalSet> hospitalSetList = hospitalSetService.list();
        return Result.ok(hospitalSetList);
    }

    /**
     * 2.逻辑删除医院设置
     *
     * @param id:根据id删除对应的医院设置信息
     * @return
     */
    @ApiOperation("逻辑删除医院设置")
    @DeleteMapping("/{id}")
    public Result deleteById(@PathVariable("id") Long id) {
        boolean flag = hospitalSetService.removeById(id);
        if (flag) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    /**
     * 3 条件查询带分页
     *
     * @param current:当前页
     * @param limit：每页显示的条数
     * @param hospitalSetQueryVo：查询条件封装的vo
     * @return
     */
    @PostMapping("/findPageHospSet/{current}/{limit}")
    public Result findPageHospSet(@PathVariable("current") Long current,
                                  @PathVariable("limit") Long limit,
                                  @RequestBody HospitalSetQueryVo hospitalSetQueryVo) {
        Page<HospitalSet> hospitalSetPage = new Page<>(current, limit);
        //获取医院编号
        String hoscode = hospitalSetQueryVo.getHoscode();
        //获取医院名称
        String hosname = hospitalSetQueryVo.getHosname();
        QueryWrapper<HospitalSet> wrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(hoscode)) {
            wrapper.eq("hoscode", hoscode);
        }
        if (StringUtils.isNotEmpty(hosname)) {
            wrapper.like("hosname", hosname);
        }
        Page<HospitalSet> result = hospitalSetService.page(hospitalSetPage, wrapper);
        return Result.ok(result);
    }

    /**
     * 4.添加医院设置
     *
     * @param hospitalSet
     * @return
     */
    @PostMapping("/saveHospitalSet")
    public Result saveHospitalSet(@RequestBody HospitalSet hospitalSet) {
        //设置状态 1 使用 0 不能使用
        hospitalSet.setStatus(1);
        //签名秘钥
        Random random = new Random();
        hospitalSet.setSignKey(MD5.encrypt(System.currentTimeMillis() + "" + random.nextInt(1000)));
        boolean flag = hospitalSetService.save(hospitalSet);
        if (flag) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    /**
     * 5.根据id获取医院设置
     *
     * @param id:医院设置的id
     * @return
     */
    @GetMapping("/getHospSet/{id}")
    public Result getHospitalSetById(@PathVariable("id") Long id) {
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        return Result.ok(hospitalSet);
    }

    /**
     * 6.修改医院设置
     *
     * @param hospitalSet
     * @return
     */
    @PostMapping("/updateHospitalSet")
    public Result updateHospitalSetById(@RequestBody HospitalSet hospitalSet) {
        hospitalSetService.updateById(hospitalSet);
        return Result.ok();
    }

    /**
     * 7.批量删除医院设置
     *
     * @param ids:医院ids,List<Long>
     * @return
     */
    @DeleteMapping("/batchRemove")
    public Result batchDeleteByIds(@RequestBody List<Long> ids) {
        boolean flag = hospitalSetService.removeByIds(ids);
        if (flag) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    //8 医院设置锁定和解锁
    @PutMapping("/lockHospitalSet/{id}/{status}")
    public Result setStatus(@PathVariable("status") Integer status,
                            @PathVariable("id") Long id) {
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        hospitalSet.setStatus(status);
        hospitalSetService.updateById(hospitalSet);
        return Result.ok();
    }

    //9 发送签名秘钥
    @PutMapping("/sendKey/{id}")
    public Result sendKey(@PathVariable("id") Long id) {
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        String signKey = hospitalSet.getSignKey();
        String hoscode = hospitalSet.getHoscode();
        //发送短信
        return Result.ok();
    }

}
