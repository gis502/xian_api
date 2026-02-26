package com.ruoyi.system.service.impl;

import com.ruoyi.common.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 文件清理任务服务实现类
 * 定期清理超过1小时的文件，并删除空文件夹
 * 
 * @author Lingma
 */
@Service
public class FileCleanupTaskServiceImpl {

    private static final Logger log = LoggerFactory.getLogger(FileCleanupTaskServiceImpl.class);
    
    // 本地存储根目录（可根据需要修改）
    private static final String ROOT_DIR = "/home/xian/dist/";
    // 文档存储目录
    private static final String DOCS_DIR = ROOT_DIR + "docs/";
    // 图片存储目录
    private static final String IMGS_DIR = ROOT_DIR + "imgs/";
    
    // 文件过期时间（1小时 = 3600000毫秒）
    private static final long FILE_EXPIRATION_TIME = 3600000L;
    
    // 定时任务执行器
    private ScheduledExecutorService scheduledExecutorService;
    
    @PostConstruct
    public void init() {
        log.info("初始化文件清理任务服务...");
        startFileCleanupThread();
    }
    
    /**
     * 启动文件清理线程
     */
    private void startFileCleanupThread() {
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread thread = new Thread(r, "FileCleanupThread");
            thread.setDaemon(true);
            return thread;
        });
        
        // 立即执行一次，然后每小时执行一次
        scheduledExecutorService.scheduleWithFixedDelay(this::cleanupExpiredFiles, 0, 1, TimeUnit.HOURS);
        log.info("文件清理线程已启动，将每小时执行一次清理任务");
    }
    
    /**
     * 清理过期文件的主方法
     */
    public void cleanupExpiredFiles() {
        try {
            log.info("开始执行文件清理任务...");
            
            // 清理文档目录
            cleanupDirectory(new File(DOCS_DIR));
            
            // 清理图片目录
            cleanupDirectory(new File(IMGS_DIR));
            
            log.info("文件清理任务执行完成");
        } catch (Exception e) {
            log.error("文件清理任务执行异常", e);
        }
    }
    
    /**
     * 清理指定目录下的过期文件
     * @param directory 目标目录
     */
    private void cleanupDirectory(File directory) {
        if (!directory.exists() || !directory.isDirectory()) {
            log.warn("目录不存在或不是目录: {}", directory.getAbsolutePath());
            return;
        }
        
        File[] files = directory.listFiles();
        if (files == null) {
            return;
        }
        
        long currentTime = System.currentTimeMillis();
        boolean hasFilesDeleted = false;
        
        for (File file : files) {
            if (file.isFile()) {
                // 检查文件是否过期
                if (isFileExpired(file, currentTime)) {
                    if (file.delete()) {
                        log.info("删除过期文件: {}", file.getAbsolutePath());
                        hasFilesDeleted = true;
                    } else {
                        log.warn("删除文件失败: {}", file.getAbsolutePath());
                    }
                }
            } else if (file.isDirectory()) {
                // 递归清理子目录
                cleanupDirectory(file);
            }
        }
        
        // 如果删除了文件，检查当前目录是否为空
        if (hasFilesDeleted) {
            checkAndDeleteEmptyDirectory(directory);
        }
    }
    
    /**
     * 检查文件是否过期（创建时间超过1小时）
     * @param file 文件对象
     * @param currentTime 当前时间戳
     * @return 是否过期
     */
    private boolean isFileExpired(File file, long currentTime) {
        long fileTime = file.lastModified();
        return (currentTime - fileTime) > FILE_EXPIRATION_TIME;
    }
    
    /**
     * 检查并删除空目录
     * @param directory 目录对象
     */
    private void checkAndDeleteEmptyDirectory(File directory) {
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            // 如果目录为空，则删除
            if (files == null || files.length == 0) {
                if (directory.delete()) {
                    log.info("删除空目录: {}", directory.getAbsolutePath());
                    // 递归检查父目录是否也变为空目录
                    File parentDir = directory.getParentFile();
                    if (parentDir != null && !parentDir.getAbsolutePath().equals(ROOT_DIR)) {
                        checkAndDeleteEmptyDirectory(parentDir);
                    }
                } else {
                    log.warn("删除空目录失败: {}", directory.getAbsolutePath());
                }
            }
        }
    }
    
    /**
     * 关闭定时任务执行器
     */
    public void shutdown() {
        if (scheduledExecutorService != null && !scheduledExecutorService.isShutdown()) {
            scheduledExecutorService.shutdown();
            try {
                if (!scheduledExecutorService.awaitTermination(5, TimeUnit.SECONDS)) {
                    scheduledExecutorService.shutdownNow();
                }
            } catch (InterruptedException e) {
                scheduledExecutorService.shutdownNow();
                Thread.currentThread().interrupt();
            }
            log.info("文件清理任务服务已关闭");
        }
    }
}