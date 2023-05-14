package com.example.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.example.common.Result;
import com.example.entity.Material;
import com.example.service.MaterialService;
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
@RequestMapping("/api/material")
public class MaterialController {
    @Resource
    private MaterialService materialService;
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
    public Result<?> save(@RequestBody Material material) {
        materialService.save(material);
        return Result.success();
    }

    @PutMapping
    public Result<?> update(@RequestBody Material material) {
        materialService.updateById(material);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        materialService.removeById(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<?> findById(@PathVariable Long id) {
        return Result.success(materialService.getById(id));
    }

    @GetMapping
    public Result<?> findAll() {
        List<Material> list = materialService.list();
        return Result.success(list);
    }

    @GetMapping("/page")
    public Result<?> findPage(@RequestParam(required = false, defaultValue = "") String name,
                                                @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                                @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        LambdaQueryWrapper<Material> query = Wrappers.<Material>lambdaQuery().orderByDesc(Material::getId);
        if (StrUtil.isNotBlank(name)) {
            query.like(Material::getMaterialName, name);
        }
        IPage<Material> page = materialService.page(new Page<>(pageNum, pageSize), query);
        return Result.success(page);
    }

    @GetMapping("/export")
    public void export(HttpServletResponse response) throws IOException {

        List<Map<String, Object>> list = CollUtil.newArrayList();

        List<Material> all = materialService.list();
        for (Material obj : all) {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("物资编号", obj.getId());
            row.put("物资名称", obj.getMaterialName());
            row.put("物品单价", obj.getPrice());
            row.put("物品数量", obj.getQuantity());
            row.put("进出货时间", obj.getTime());
            row.put("负责人", obj.getUserRel());
            row.put("所属仓库", obj.getDepositoryRel());
            row.put("办公用材,建筑耗材,装修耗材,其他材料", obj.getTypeRadio());
            row.put("入库,出库", obj.getStatusRadio());

            list.add(row);
        }

        // 2. 写excel
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.write(list, true);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("物资信息", "UTF-8");
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
        List<Material> saveList = new ArrayList<>();
        for (List<Object> row : lists) {
            Material obj = new Material();
            obj.setMaterialName((String) row.get(1));
            obj.setPrice(new BigDecimal((String) row.get(2)));
            obj.setQuantity(Integer.valueOf((String) row.get(3)));
            obj.setTime((String) row.get(4));
            obj.setUserRel((String) row.get(5));
            obj.setDepositoryRel((String) row.get(6));
            obj.setTypeRadio((String) row.get(7));
            obj.setStatusRadio((String) row.get(8));

            saveList.add(obj);
        }
        materialService.saveBatch(saveList);
        return Result.success();
    }

}
