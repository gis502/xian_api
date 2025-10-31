package com.ruoyi.web.controller.system;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.entity.PlotIconmanagement;
import com.ruoyi.system.service.PlotIconmanagementService;
import org.apache.commons.io.FileUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Base64;
import java.util.List;


@RestController
@RequestMapping("/admins/ploticon")
public class PlotIconmanagementController {

    @Resource
    private PlotIconmanagementService plotIconmanagementService;

    // 定义允许的文件根目录（固定为项目下的logistics/uploads/PlotsPic/）
    private static final String RELATIVE_ROOT_DIR = "/logistics/uploads/PlotsPic/";


    /**
     * 工具方法：获取安全的文件对象（过滤危险文件名+校验路径在根目录内）
     * @param rawFileName 用户输入的原始文件名（不含后缀）
     * @param suffix 文件后缀（如".png"）
     * @return 安全的File对象
     * @throws IOException 路径解析异常
     * @throws SecurityException 路径越界时抛出
     */
    private File getSafeFile(String rawFileName, String suffix) throws IOException, SecurityException {
        // 1. 过滤文件名中的危险字符（只保留字母、数字、下划线、横线、点）
        String safeFileName = rawFileName.replaceAll("[^a-zA-Z0-9_\\-\\.]", "");
        if (safeFileName.isEmpty()) {
            throw new IllegalArgumentException("无效的文件名（包含非法字符或为空）");
        }

        // 2. 构建完整根目录（项目路径+相对根目录）
        String projectPath = System.getProperty("user.dir");
        File rootDir = new File(projectPath + RELATIVE_ROOT_DIR);
        // 获取根目录的规范路径（解析所有../和符号链接，确保唯一）
        String rootCanonicalPath = rootDir.getCanonicalPath();

        // 3. 安全拼接文件路径（使用File构造函数，自动处理系统路径分隔符）
        String fileNameWithSuffix = safeFileName + suffix;
        File targetFile = new File(rootDir, fileNameWithSuffix);

        // 4. 校验目标文件是否在根目录内（核心防越界逻辑）
        String targetCanonicalPath = targetFile.getCanonicalPath();
        if (!targetCanonicalPath.startsWith(rootCanonicalPath)) {
            throw new SecurityException("检测到路径越界风险，拒绝操作");
        }

        return targetFile;
    }


    @PostMapping("/getploticon")
    public AjaxResult getploticon() {
        return AjaxResult.success(plotIconmanagementService.list());
    }


    @PostMapping("/deleteploticon/{uuid}")
    @Log(title = "标会图片管理", businessType = BusinessType.DELETE)
    public AjaxResult deletePlotIcon(@PathVariable("uuid") String id) {
        PlotIconmanagement plotIcon = plotIconmanagementService.getById(id);
        if (plotIcon == null) {
            return AjaxResult.error("记录不存在");
        }

        try {
            // 安全获取待删除文件（文件名是plotIcon.getName()，后缀为.png）
            File file = getSafeFile(plotIcon.getName(), ".png");

            // 删除文件和数据库记录
            if (file.exists() && file.delete()) {
                plotIconmanagementService.removeById(id);
                return AjaxResult.success("文件和记录删除成功");
            } else {
                return AjaxResult.error("文件删除失败（文件不存在或无法删除）");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error("删除失败：" + e.getMessage());
        }
    }


    @PostMapping("/updataploticon")
    @Log(title = "标会图片管理", businessType = BusinessType.UPDATE)
    public AjaxResult updataPlotIcon(@RequestBody PlotIconmanagement plotIcon) {
        PlotIconmanagement existingPlotIcon = plotIconmanagementService.getById((Serializable) plotIcon.getUuid());
        if (existingPlotIcon == null) {
            return AjaxResult.error("记录不存在");
        }

        // 图片未变化时直接更新数据库
        boolean isBase64 = plotIcon.getImg() != null && plotIcon.getImg().startsWith("data:image/jpeg;base64");
        if (!isBase64) {
            plotIcon.setImg(null);
            plotIconmanagementService.updateById(plotIcon);
            return AjaxResult.success("记录更新成功（图片未变更）");
        }

        try {
            // 1. 删除旧图片
            File oldFile = getSafeFile(existingPlotIcon.getName(), ""); // 旧文件路径可能不带后缀，按实际存储处理
            if (oldFile.exists()) {
                oldFile.delete();
            }

            // 2. 处理新图片Base64数据
            String base64Data = plotIcon.getImg();
            if (base64Data.contains(",")) {
                base64Data = base64Data.split(",")[1];
            }
            byte[] imageBytes = Base64.getMimeDecoder().decode(base64Data);

            // 3. 安全保存新图片
            File newFile = getSafeFile(plotIcon.getName(), ".png");
            newFile.getParentFile().mkdirs(); // 确保父目录存在
            FileUtils.writeByteArrayToFile(newFile, imageBytes);

            // 4. 更新数据库
            plotIcon.setImg(plotIcon.getName());
            plotIconmanagementService.updateById(plotIcon);
            return AjaxResult.success("记录和图片更新成功");

        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error("更新失败：" + e.getMessage());
        }
    }


    @PostMapping("/searchploticon")
    public List<PlotIconmanagement> searchPloticon(@RequestParam("menuName") String menuName) {
        QueryWrapper<PlotIconmanagement> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name", menuName)
                .or().like("describe", menuName)
                .or().like("type", menuName);
        return plotIconmanagementService.list(queryWrapper);
    }


    @PostMapping("/addploticon")
    @Log(title = "标会图片管理", businessType = BusinessType.INSERT)
    public AjaxResult addPlotIcon(@RequestBody PlotIconmanagement plotIcon) {
        String base64Data = plotIcon.getImg();
        String rawImageName = plotIcon.getName(); // 用户输入的文件名（不含后缀）

        if (base64Data == null || rawImageName == null) {
            return AjaxResult.error("图片数据或文件名不能为空");
        }

        try {
            // 1. 处理Base64数据
            if (base64Data.contains(",")) {
                base64Data = base64Data.split(",")[1];
            }
            byte[] imageBytes = Base64.getMimeDecoder().decode(base64Data);

            // 2. 安全保存图片
            File outputFile = getSafeFile(rawImageName, ".png");
            outputFile.getParentFile().mkdirs(); // 确保目录存在
            FileUtils.writeByteArrayToFile(outputFile, imageBytes);

            // 3. 保存数据库记录
            plotIcon.setImg(rawImageName);
            plotIconmanagementService.save(plotIcon);
            return AjaxResult.success("图片和记录保存成功：" + rawImageName);

        } catch (IllegalArgumentException e) {
            return AjaxResult.error("Base64解码失败：" + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error("保存失败：" + e.getMessage());
        }
    }

}