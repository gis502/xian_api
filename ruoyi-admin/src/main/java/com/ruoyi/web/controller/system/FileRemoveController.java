package com.ruoyi.web.controller.system;

import com.ruoyi.system.service.FileRemoveService;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author zzw
 * @description: TODO
 * @date 2025/11/27 上午10:26
 */
@RestController
@RequestMapping("/admins/file")
public class FileRemoveController {

    @Resource
    private FileRemoveService fileRemoveService;

    @PostMapping("/remove")
    public void removeFile(@Param("url") String url){
        fileRemoveService.removeFile( url);
    }
}
