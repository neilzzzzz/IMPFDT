package com.example.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.example.common.Result;
import com.example.entity.Dailyreport;
import com.example.service.DailyreportService;
import com.example.entity.User;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;
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
@RequestMapping("/api/dailyreport")
public class DailyreportController {
    @Resource
    private DailyreportService dailyreportService;
    @Resource
    private HttpServletRequest request;
    @Resource
    private UserService userService;

    public User getUser() {
        String token = request.getHeader("token");
        String username = JWT.decode(token).getAudience().get(0);
        return userService.getOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, username));
    }

    @PostMapping
    public Result<?> save(@RequestBody Dailyreport dailyreport) {
        dailyreportService.save(dailyreport);
        return Result.success();
    }

    @PutMapping
    public Result<?> update(@RequestBody Dailyreport dailyreport) {
        dailyreportService.updateById(dailyreport);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        dailyreportService.removeById(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<?> findById(@PathVariable Long id) {
        return Result.success(dailyreportService.getById(id));
    }

    @GetMapping
    public Result<?> findAll() {
        List<Dailyreport> list = dailyreportService.list();
        return Result.success(list);
    }

    @GetMapping("/page")
    public Result<?> findPage(@RequestParam(required = false, defaultValue = "") String name,
                                                @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                                @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        LambdaQueryWrapper<Dailyreport> query = Wrappers.<Dailyreport>lambdaQuery().orderByDesc(Dailyreport::getId);
        if (StrUtil.isNotBlank(name)) {
            query.like(Dailyreport::getDailyName, name);
        }
        IPage<Dailyreport> page = dailyreportService.page(new Page<>(pageNum, pageSize), query);
        return Result.success(page);
    }

    @GetMapping("/export")
    public void export(HttpServletResponse response) throws IOException {

        List<Map<String, Object>> list = CollUtil.newArrayList();

        List<Dailyreport> all = dailyreportService.list();
        for (Dailyreport obj : all) {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("日报编号", obj.getId());
            row.put("日报标题", obj.getDailyName());
            row.put("日报内容", obj.getDailyContent());
            row.put("所属项目", obj.getProjectRel());
            row.put("现场情况", obj.getImg());
            row.put("填写日期", obj.getCreateDate());
            row.put("填写人", obj.getUserRel());

            list.add(row);
        }

        // 2. 写excel
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.write(list, true);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("施工日报信息", "UTF-8");
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
        List<Dailyreport> saveList = new ArrayList<>();
        for (List<Object> row : lists) {
            Dailyreport obj = new Dailyreport();
            obj.setDailyName((String) row.get(1));
            obj.setDailyContent((String) row.get(2));
            obj.setProjectRel((String) row.get(3));
            obj.setImg((String) row.get(4));
            obj.setCreateDate((String) row.get(5));
            obj.setUserRel((String) row.get(6));

            saveList.add(obj);
        }
        dailyreportService.saveBatch(saveList);
        return Result.success();
    }

}
