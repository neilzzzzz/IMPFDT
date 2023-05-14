package com.example.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.example.common.Result;
import com.example.entity.Budget;
import com.example.mapper.BudgetMapper;
import com.example.mapper.ConsumptionMapper;
import com.example.service.BudgetService;
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
@RequestMapping("/api/budget")
public class BudgetController {
    @Resource
    private BudgetService budgetService;
    @Resource
    private HttpServletRequest request;
    @Resource
    private UserService userService;
    @Autowired
    private BudgetMapper budgetMapper;


    public User getUser() {
        String token = request.getHeader("token");
        String username = JWT.decode(token).getAudience().get(0);
        return userService.getOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, username));
    }

    @PostMapping
    public Result<?> save(@RequestBody Budget budget) {
        budgetService.save(budget);
        return Result.success();
    }

    @PutMapping
    public Result<?> update(@RequestBody Budget budget) {
        budgetService.updateById(budget);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        budgetService.removeById(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<?> findById(@PathVariable Long id) {
        return Result.success(budgetService.getById(id));
    }

    /**
     * 统计总预算
     * @return
     */
    @GetMapping("/sumbgt")
    public Result<?> sumBgt() {
        return Result.success(budgetMapper.sumBgt());
    }

    @GetMapping
    public Result<?> findAll() {
        List<Budget> list = budgetService.list();
        return Result.success(list);
    }

    @GetMapping("/page")
    public Result<?> findPage(@RequestParam(required = false, defaultValue = "") String name,
                                                @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                                @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        LambdaQueryWrapper<Budget> query = Wrappers.<Budget>lambdaQuery().orderByDesc(Budget::getId);
        if (StrUtil.isNotBlank(name)) {
            query.like(Budget::getBudgetName, name);
        }
        IPage<Budget> page = budgetService.page(new Page<>(pageNum, pageSize), query);
        return Result.success(page);
    }

    @GetMapping("/export")
    public void export(HttpServletResponse response) throws IOException {

        List<Map<String, Object>> list = CollUtil.newArrayList();

        List<Budget> all = budgetService.list();
        for (Budget obj : all) {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("预算编号", obj.getId());
            row.put("预算名称", obj.getBudgetName());
            row.put("预算金额", obj.getBgtamount());
            row.put("预算项目", obj.getProjectRel());
            row.put("创建时间", obj.getCreateTime());

            list.add(row);
        }

        // 2. 写excel
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.write(list, true);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("预算信息", "UTF-8");
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
        List<Budget> saveList = new ArrayList<>();
        for (List<Object> row : lists) {
            Budget obj = new Budget();
            obj.setBudgetName((String) row.get(1));
            obj.setBgtamount(new BigDecimal((String) row.get(2)));
            obj.setProjectRel((String) row.get(3));
            obj.setCreateTime((String) row.get(4));

            saveList.add(obj);
        }
        budgetService.saveBatch(saveList);
        return Result.success();
    }

}
