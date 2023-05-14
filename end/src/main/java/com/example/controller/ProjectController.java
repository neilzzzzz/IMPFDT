package com.example.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.example.common.Result;
import com.example.entity.Project;
import com.example.mapper.ProjectMapper;
import com.example.service.ProjectService;
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
import javax.xml.transform.Source;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/project")
public class ProjectController {
    @Resource
    private ProjectService projectService;
    @Resource
    private HttpServletRequest request;
    @Resource
    private UserService userService;
    @Autowired
    private ProjectMapper projectMapper;

    public User getUser() {
        String token = request.getHeader("token");
        String username = JWT.decode(token).getAudience().get(0);
        return userService.getOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, username));
    }

    @PostMapping
    public Result<?> save(@RequestBody Project project) {
//        重名验证
        int check = projectMapper.checkName(project.getProjectName());
        if(check != 0){
            return Result.error("400","项目名称不能重复");
        }else {
            projectService.save(project);
            return Result.success();
        }

    }

    @PutMapping
    public Result<?> update(@RequestBody Project project) {

        //通过id找到原来未修改的项目
        Project oldproject = projectService.getById(project.getId());
        //获取原来项目的名称
        String oldName = oldproject.getProjectName();
        //重名验证>0表示重复
        Integer check = projectMapper.checkUpdate(oldName, project.getProjectName());
        if (check == 0){
            projectService.updateById(project);
            return Result.success();
        }else {
            return Result.error("400","和已有项目名称重复");
        }

    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        projectService.removeById(id);
        return Result.success();
    }

    @GetMapping("/cnt")
    public Result<?> findCnt(){
//        System.out.println(projectMapper.findCnt());
        return Result.success(projectMapper.findCnt());
    }

    @GetMapping("/allonproject")
    public Result<?> findAllOnProject(){
        return Result.success(projectMapper.onProjectEntity());
    }

    /**
     * 统计进行中项目
     * @return
     */
    @GetMapping("/onproject")
    public Result<?> findOnproject(){
        return Result.success(projectMapper.onProject());
    }

    @GetMapping("/{id}")
    public Result<?> findById(@PathVariable Long id) {
        return Result.success(projectService.getById(id));
    }

    @GetMapping
    public Result<?> findAll() {
        List<Project> list = projectService.list();
        return Result.success(list);
    }

    @GetMapping("/page")
    public Result<?> findPage(@RequestParam(required = false, defaultValue = "") String name,
                                                @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                                @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        LambdaQueryWrapper<Project> query = Wrappers.<Project>lambdaQuery().orderByDesc(Project::getId);
        if (StrUtil.isNotBlank(name)) {
            query.like(Project::getProjectName, name);
//            query.like(Project::getUserRel,name);
        }
        IPage<Project> page = projectService.page(new Page<>(pageNum, pageSize), query);
        return Result.success(page);
    }

    @GetMapping("/export")
    public void export(HttpServletResponse response) throws IOException {

        List<Map<String, Object>> list = CollUtil.newArrayList();

        List<Project> all = projectService.list();
        for (Project obj : all) {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("项目编号", obj.getId());
            row.put("项目名称", obj.getProjectName());
            row.put("创建时间", obj.getCreateTime());
            row.put("完成日期", obj.getFinishDate());
            row.put("项目负责人", obj.getUserRel());
            row.put("未开始,进行中,项目中止,完成", obj.getStatusRadio());
            row.put("项目预览", obj.getImg());

            list.add(row);
        }

        // 2. 写excel
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.write(list, true);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("工程项目信息", "UTF-8");
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
        List<Project> saveList = new ArrayList<>();
        for (List<Object> row : lists) {
            Project obj = new Project();
            obj.setProjectName((String) row.get(1));
            obj.setCreateTime((String) row.get(2));
            obj.setFinishDate((String) row.get(3));
            obj.setUserRel((String) row.get(4));
            obj.setStatusRadio((String) row.get(5));
            obj.setImg((String) row.get(6));

            saveList.add(obj);
        }
        System.out.println(saveList);
        projectService.saveBatch(saveList);
        return Result.success();
    }

}
