package com.example.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.example.common.Result;
import com.example.entity.Scheme;
import com.example.service.SchemeService;
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
@RequestMapping("/api/scheme")
public class SchemeController {
    @Resource
    private SchemeService schemeService;
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
    public Result<?> save(@RequestBody Scheme scheme) {
        schemeService.save(scheme);
        return Result.success();
    }

    @PutMapping
    public Result<?> update(@RequestBody Scheme scheme) {
        schemeService.updateById(scheme);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        schemeService.removeById(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<?> findById(@PathVariable Long id) {
        return Result.success(schemeService.getById(id));
    }

    @GetMapping
    public Result<?> findAll() {
        List<Scheme> list = schemeService.list();
        return Result.success(list);
    }

    @GetMapping("/page")
    public Result<?> findPage(@RequestParam(required = false, defaultValue = "") String name,
                                                @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                                @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        LambdaQueryWrapper<Scheme> query = Wrappers.<Scheme>lambdaQuery().orderByDesc(Scheme::getId);
        if (StrUtil.isNotBlank(name)) {
            query.like(Scheme::getSchemeName, name);
        }
        IPage<Scheme> page = schemeService.page(new Page<>(pageNum, pageSize), query);
        return Result.success(page);
    }

    @GetMapping("/export")
    public void export(HttpServletResponse response) throws IOException {

        List<Map<String, Object>> list = CollUtil.newArrayList();

        List<Scheme> all = schemeService.list();
        for (Scheme obj : all) {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("方案编号", obj.getId());
            row.put("方案名称", obj.getSchemeName());
            row.put("所属项目", obj.getProjectRel());
            row.put("方案附件", obj.getFile());
            row.put("方案简介", obj.getSchemeInfo());
            row.put("方案发起人", obj.getUserRel());

            list.add(row);
        }

        // 2. 写excel
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.write(list, true);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("方案信息", "UTF-8");
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
        List<Scheme> saveList = new ArrayList<>();
        for (List<Object> row : lists) {
            Scheme obj = new Scheme();
            obj.setSchemeName((String) row.get(1));
            obj.setProjectRel((String) row.get(2));
            obj.setFile((String) row.get(3));
            obj.setSchemeInfo((String) row.get(4));
            obj.setUserRel((String) row.get(5));

            saveList.add(obj);
        }
        schemeService.saveBatch(saveList);
        return Result.success();
    }

}
