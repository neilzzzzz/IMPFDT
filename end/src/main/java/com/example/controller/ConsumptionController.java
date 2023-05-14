package com.example.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.example.common.Result;
import com.example.entity.Consumption;
import com.example.mapper.BudgetMapper;
import com.example.mapper.ConsumptionMapper;
import com.example.service.ConsumptionService;
import com.example.entity.User;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.exception.CustomException;
import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.example.service.UserService;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/consumption")
public class ConsumptionController {
    @Resource
    private ConsumptionService consumptionService;
    @Resource
    private HttpServletRequest request;
    @Resource
    private UserService userService;
    @Autowired
    private ConsumptionMapper consumptionMapper;
    @Autowired
    private BudgetMapper budgetMapper;

    public User getUser() {
        String token = request.getHeader("token");
        String username = JWT.decode(token).getAudience().get(0);
        return userService.getOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, username));
    }

    @PostMapping
    public Result<?> save(@RequestBody Consumption consumption) {

        consumptionService.save(consumption);
        return Result.success();
    }

    @PutMapping
    public Result<?> update(@RequestBody Consumption consumption) {
        consumptionService.updateById(consumption);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        consumptionService.removeById(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<?> findById(@PathVariable Long id) {
        return Result.success(consumptionService.getById(id));
    }

    /**
     * 查询所有成本累计
     * @return
     */
    @GetMapping("/sumcsm")
    public Result<?> sumCsm() {
        return Result.success(consumptionMapper.sumCsm());
    }

    @GetMapping("/getnamebyrel/{name}")
    public Result<?> getNameByRel(@PathVariable String name) {
        String nameByRel = consumptionMapper.getNameByRel(name);
        return Result.success(nameByRel);
    }

    /**
     * 查看成本是否超过预算
     * @return
     */
    @GetMapping("/checksum/{name}/{cost}")
    public Result<?> checkSum(@PathVariable String name,@PathVariable Double cost) {
        Double oneCsmSum = consumptionMapper.getOneCsmSum(name);
        Double oneBgtSum = budgetMapper.getOneBgtSum(name);
//        System.out.println(oneBgtSum);
//        System.out.println(oneCsmSum);
//        System.out.println(cost);
        Double res = oneCsmSum-oneBgtSum-cost;
        System.out.println(res);
        return Result.success(res);
    }

    @GetMapping
    public Result<?> findAll() {
        List<Consumption> list = consumptionService.list();
        return Result.success(list);
    }

    @GetMapping("/page")
    public Result<?> findPage(@RequestParam(required = false, defaultValue = "") String name,
                                                @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                                @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        LambdaQueryWrapper<Consumption> query = Wrappers.<Consumption>lambdaQuery().orderByDesc(Consumption::getId);
        if (StrUtil.isNotBlank(name)) {
            query.like(Consumption::getConsumeName, name);
        }
        IPage<Consumption> page = consumptionService.page(new Page<>(pageNum, pageSize), query);
        return Result.success(page);
    }

    @GetMapping("/export")
    public void export(HttpServletResponse response) throws IOException {

        List<Map<String, Object>> list = CollUtil.newArrayList();

        List<Consumption> all = consumptionService.list();
        for (Consumption obj : all) {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("成本编号", obj.getId());
            row.put("成本名称", obj.getConsumeName());
            row.put("花费金额", obj.getConsumeSpend());
            row.put("关联预算", obj.getBudgetRel());
            row.put("关联项目", obj.getProjectRel());

            list.add(row);
        }

        // 2. 写excel
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.write(list, true);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("成本信息", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        writer.close();
        IoUtil.close(System.out);
    }

    @GetMapping("/upload/{fileId}")
    public Result<?> upload(@PathVariable String fileId) {
        String basePath = System.getProperty("user.dir") + "/src/main/resources/static/file/";
        List<String> fileNames = FileUtil.listFileNames(basePath);
        String file = fileNames.stream().filter(name -> name.contains(fileId)).findAny().orElse("");
        List<List<Object>> lists = ExcelUtil.getReader(basePath + file).read(1);
        List<Consumption> saveList = new ArrayList<>();
        for (List<Object> row : lists) {
            Consumption obj = new Consumption();
            obj.setConsumeName((String) row.get(1));
            obj.setConsumeSpend(new BigDecimal((String) row.get(2)));
            obj.setBudgetRel((String) row.get(3));
            obj.setProjectRel((String) row.get(4));

            saveList.add(obj);
        }
        consumptionService.saveBatch(saveList);
        return Result.success();
    }

}
