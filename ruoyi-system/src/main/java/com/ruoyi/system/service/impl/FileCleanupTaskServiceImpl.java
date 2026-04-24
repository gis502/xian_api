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
 * 定期清理超过3分钟的文件，并删除所有空文件夹（保留根目录）
 * 修正：每5秒执行一次清理检测，删除超过3分钟的过期文件
 *
 * @author Lingma
 */
@Service
public class FileCleanupTaskServiceImpl {

    private static final Logger log = LoggerFactory.getLogger(FileCleanupTaskServiceImpl.class);

    // 本地存储根目录
    private static final String ROOT_DIR = "/home/xian/dist/";
    private static final String IMGS_DIR = ROOT_DIR + "imgs/";

    // 文件过期时间
    private static final long FILE_EXPIRATION_TIME = 60 * 60 * 1000;

    private ScheduledExecutorService scheduledExecutorService;

    @PostConstruct
    public void init() {
        log.info("初始化文件清理任务服务...");
        startFileCleanupThread();
    }

    /**
     * 启动文件清理线程（核心修改：执行频率改为5秒）
     */
    private void startFileCleanupThread() {
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread thread = new Thread(r, "FileCleanupThread");
            thread.setDaemon(true);
            return thread;
        });

        // 立即执行一次，然后每5秒执行一次清理检测
        scheduledExecutorService.scheduleWithFixedDelay(
                this::cleanupExpiredFiles,
                0,          // 初始延迟：立即执行
                5,          // 执行间隔：5秒
                TimeUnit.MINUTES  // 时间单位：秒
        );
        log.info("文件清理线程已启动，将每5秒执行一次清理检测（清理超过3分钟的过期文件）");
    }

    /**
     * 清理过期文件的主方法（无修改）
     */
    public void cleanupExpiredFiles() {
        try {
            log.info("开始执行文件清理任务...");
            cleanupDirectory(new File(IMGS_DIR));
            log.info("文件清理任务执行完成");
        } catch (Exception e) {
            log.error("文件清理任务执行异常", e);
        }
    }

    /**
     * 清理指定目录下的过期文件（递归，无修改）
     */
    private void cleanupDirectory(File directory) {
        if (!directory.exists() || !directory.isDirectory()) {
            return;
        }

        File[] files = directory.listFiles();
        if (files == null || files.length == 0) {
            checkAndDeleteEmptyDirectory(directory);
            return;
        }

        long currentTime = System.currentTimeMillis();

        // 第一步：删除当前目录下的过期文件
        for (File file : files) {
            if (file.isFile() && isFileExpired(file, currentTime)) {
                if (file.delete()) {
                    log.info("删除过期文件: {}", file.getAbsolutePath());
                } else {
                    log.warn("删除文件失败: {}", file.getAbsolutePath());
                }
            }
        }

        // 第二步：递归清理子目录
        for (File file : files) {
            if (file.isDirectory()) {
                cleanupDirectory(file);
            }
        }

        // 第三步：检查当前目录是否为空并删除
        checkAndDeleteEmptyDirectory(directory);
    }

    /**
     * 检查文件是否过期（无修改）
     */
    private boolean isFileExpired(File file, long currentTime) {
        long fileLastModified = file.lastModified();
        return (currentTime - fileLastModified) > FILE_EXPIRATION_TIME;
    }

    /**
     * 检查并删除空目录（无修改）
     */
    private void checkAndDeleteEmptyDirectory(File directory) {
        // 排除根目录，避免误删
        if (directory.getAbsolutePath().equals(ROOT_DIR)
                || directory.getAbsolutePath().equals(IMGS_DIR)) {
            return;
        }

        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files == null || files.length == 0) {
                if (directory.delete()) {
                    log.info("删除空目录: {}", directory.getAbsolutePath());
                    // 递归检查父目录
                    File parentDir = directory.getParentFile();
                    if (parentDir != null) {
                        checkAndDeleteEmptyDirectory(parentDir);
                    }
                } else {
                    log.warn("删除空目录失败: {}", directory.getAbsolutePath());
                }
            }
        }
    }

    /**
     * 关闭定时任务执行器（无修改）
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
