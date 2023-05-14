package com.example.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.example.common.Result;
import com.example.entity.Progress;
import com.example.mapper.ProgressMapper;
import com.example.service.ProgressService;
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
@RequestMapping("/api/progress")
public class ProgressController {
    @Resource
    private ProgressService progressService;
    @Resource
    private HttpServletRequest request;
    @Resource
    private UserService userService;
    @Autowired
    private ProgressMapper progressMapper;

    public User getUser() {
        String token = request.getHeader("token");
        String username = JWT.decode(token).getAudience().get(0);
        return userService.getOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, username));
    }

    @PostMapping
    public Result<?> save(@RequestBody Progress progress) {
        progressService.save(progress);
        return Result.success();
    }

    @PutMapping
    public Result<?> update(@RequestBody Progress progress) {
        progressService.updateById(progress);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        progressService.removeById(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<?> findById(@PathVariable Long id) {
        return Result.success(progressService.getById(id));
    }

    @GetMapping("/allProgress")
    public Result<?> getAllProgress() {
        //所有进行中项目数量
        Integer ongoing = progressMapper.getOngoing()==null?0:progressMapper.getOngoing();
        //所有项目进度之和
        Integer sumProgress = progressMapper.getSumProgress()==null?0:progressMapper.getSumProgress();
        //计算整体进行中项目进度
        Double res = 0.0;
        if (ongoing != 0){
            res = (1.0 * sumProgress) / (ongoing*100);
        }
//        System.out.println(ongoing);
//        System.out.println(sumProgress);
//        System.out.println(res*1000/100);
//        System.out.println(String.format("%.1f", res * 100));
        return Result.success(String.format("%.1f", res * 100));
    }

    @GetMapping
    public Result<?> findAll() {
        List<Progress> list = progressService.list();
        return Result.success(list);
    }

    @GetMapping("/page")
    public Result<?> findPage(@RequestParam(required = false, defaultValue = "") String name,
                                                @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                                @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        LambdaQueryWrapper<Progress> query = Wrappers.<Progress>lambdaQuery().orderByDesc(Progress::getId);
        if (StrUtil.isNotBlank(name)) {
            query.like(Progress::getProgressName, name);
        }
        IPage<Progress> page = progressService.page(new Page<>(pageNum, pageSize), query);
        return Result.success(page);
    }

    @GetMapping("/export")
    public void export(HttpServletResponse response) throws IOException {

        List<Map<String, Object>> list = CollUtil.newArrayList();

        List<Progress> all = progressService.list();
        for (Progress obj : all) {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("序号", obj.getId());
            row.put("进度名称", obj.getProgressName());
            row.put("进度内容", obj.getContent());
            row.put("所属项目", obj.getProjectRel());
            row.put("现场图片", obj.getImg());
            row.put("完成时间", obj.getFinishTime());
            row.put("完成比例", obj.getProportion());

            list.add(row);
        }

        // 2. 写excel
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.write(list, true);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("进度信息", "UTF-8");
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
        List<Progress> saveList = new ArrayList<>();
        for (List<Object> row : lists) {
            Progress obj = new Progress();
            obj.setProgressName((String) row.get(1));
            obj.setContent((String) row.get(2));
            obj.setProjectRel((String) row.get(3));
            obj.setImg((String) row.get(4));
            obj.setFinishTime((String) row.get(5));
            obj.setProportion(Integer.valueOf((String) row.get(6)));

            saveList.add(obj);
        }
        progressService.saveBatch(saveList);
        return Result.success();
    }

}
