package com.ruoyi.system.service.impl;

import com.ruoyi.system.service.FileRemoveService;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Pattern;

/**
 * @author zzw
 * @description: TODO
 * @date 2025/11/27 上午10:30
 */
@Service
public class IFleRemoveServiceImpl implements FileRemoveService {
    // 本地存储根目录（可根据需要修改）
    private static final String ROOT_DIR = "/home/xian/dist/";
    // 文档存储目录（docx、doc等）
    private static final String DOCS_DIR = ROOT_DIR + "docs/";
    // 图片存储目录（jpg、png、gif等）
    private static final String IMGS_DIR = ROOT_DIR + "imgs/";
    // HTTP连接超时时间（5秒）
    private static final int CONNECT_TIMEOUT = 5000;
    // HTTP读取超时时间（10秒）
    private static final int READ_TIMEOUT = 10000;

    // 匹配URL编码的正则：% + 两位十六进制字符（0-9、a-f、A-F）
    private final Pattern URL_ENCODED_PATTERN = Pattern.compile("%[0-9a-fA-F]{2}");



    /**
     * 移动文件到本地对应目录
     * 支持HTTP URL和本地路径两种方式
     * 根据路径自动提取文件夹路径和文件名，创建目标目录并移动文件
     * @param filePath 文件路径（可以是HTTP URL或本地路径）
     */
    public void removeFile(String filePath) {
        try{
            // 判断是HTTP URL还是本地路径
            boolean isHttpUrl = filePath.startsWith("http://") || filePath.startsWith("https://");
            
            String fileName;
            String folderPath;
            
            if (isHttpUrl) {
                // HTTP URL处理
                filePath = smartDecodeUrl(filePath);
                fileName = extractFileNameFromUrl(filePath);
                folderPath = extractFolderPathFromUrl(filePath);
            } else {
                // 本地路径处理
                fileName = extractFileNameFromLocalPath(filePath);
                folderPath = extractFolderPathFromLocalPath(filePath);
            }
            
            if (fileName == null || fileName.isEmpty()) {
                System.err.println("无法提取文件名：" + filePath);
                return;
            }
            
            // 根据文件后缀确定目标存储目录
            String baseTargetDir = getTargetDirByFileExt(fileName);
            
            // 构建完整的本地目标路径
            String localTargetDir = baseTargetDir;
            if (folderPath != null && !folderPath.isEmpty()) {
                localTargetDir = baseTargetDir + folderPath;
            }
            
            // 创建目标目录（包括多级目录）
            Files.createDirectories(Paths.get(localTargetDir));
            
            // 构建本地文件完整路径
            String localFilePath = localTargetDir + File.separator + fileName;
            File localFile = new File(localFilePath);
            File sourceFile = new File(filePath);
            
            if (isHttpUrl) {
                // HTTP下载处理
                downloadHttpFile(filePath, localFile);
            } else {
                // 本地文件复制处理
                copyLocalFile(sourceFile, localFile);
            }
            
            System.out.println("📥 文件移动成功，保存路径：" + localFilePath);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 从HTTP URL中提取文件名（处理中文和特殊字符）
     * @param url HTTP资源链接
     * @return 文件名（含后缀）
     */
    private String extractFileNameFromUrl(String url) {
        try {
            // 截取URL中最后一个"/"后的部分（即文件名）
            int lastSlashIndex = url.lastIndexOf("/");
            if (lastSlashIndex == -1 || lastSlashIndex == url.length() - 1) {
                System.err.println("无效的URL：" + url);
                return null;
            }
            String fileName = url.substring(lastSlashIndex + 1);
            // 还原中文编码（URLDecoder解码）
            return java.net.URLDecoder.decode(fileName, StandardCharsets.UTF_8.name());
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 从本地路径中提取文件名
     * @param localPath 本地文件路径
     * @return 文件名（含后缀）
     */
    private String extractFileNameFromLocalPath(String localPath) {
        try {
            // 统一路径分隔符
            String normalizedPath = localPath.replace('\\', '/');
            // 截取路径中最后一个"/"后的部分（即文件名）
            int lastSlashIndex = normalizedPath.lastIndexOf("/");
            if (lastSlashIndex == -1 || lastSlashIndex == normalizedPath.length() - 1) {
                System.err.println("无效的本地路径：" + localPath);
                return null;
            }
            return normalizedPath.substring(lastSlashIndex + 1);
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 从HTTP URL中提取文件夹路径（最后四层目录结构）
     * @param url HTTP资源链接
     * @return 文件夹路径（以/开头）
     */
    private String extractFolderPathFromUrl(String url) {
        try {
            // 移除协议部分（http:// 或 https://）
            String path = url;
            if (path.startsWith("http://")) {
                path = path.substring(7);
            } else if (path.startsWith("https://")) {
                path = path.substring(8);
            }
            
            // 找到最后一个"/"的位置（文件名位置）
            int lastSlashIndex = path.lastIndexOf("/");
            if (lastSlashIndex == -1) {
                return ""; // 没有路径信息
            }
            
            // 获取文件名之前的路径部分
            String pathPart = path.substring(0, lastSlashIndex);
            
            // 找到主机名结束位置（第一个"/"）
            int hostEndIndex = pathPart.indexOf("/");
            if (hostEndIndex == -1) {
                return ""; // 只有主机名，没有路径
            }
            
            // 提取路径部分（去掉主机名）
            String folderPath = pathPart.substring(hostEndIndex);
            
            return extractLastFourLevels(folderPath);
            
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
    
    /**
     * 从本地路径中提取文件夹路径（最后四层目录结构）
     * @param localPath 本地文件路径
     * @return 文件夹路径（以/开头）
     */
    private String extractFolderPathFromLocalPath(String localPath) {
        try {
            // 统一路径分隔符
            String normalizedPath = localPath.replace('\\', '/');
            
            // 找到最后一个"/"的位置（文件名位置）
            int lastSlashIndex = normalizedPath.lastIndexOf("/");
            if (lastSlashIndex == -1) {
                return ""; // 没有路径信息
            }
            
            // 获取文件名之前的路径部分
            String folderPath = normalizedPath.substring(0, lastSlashIndex);
            
            return extractLastFourLevels(folderPath);
            
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
    
    /**
     * 提取路径的最后四层目录结构
     * @param folderPath 文件夹路径
     * @return 最后四层目录路径（以/开头）
     */
    private String extractLastFourLevels(String folderPath) {
        try {
            // 如果路径为空或只有根目录
            if (folderPath.isEmpty() || folderPath.equals("/")) {
                return "";
            }
            
            // 确保路径以"/"开头
            if (!folderPath.startsWith("/")) {
                folderPath = "/" + folderPath;
            }
            
            // 提取最后四层目录
            String[] pathSegments = folderPath.split("/");
            
            // 过滤空字符串
            java.util.List<String> validSegments = new java.util.ArrayList<>();
            for (String segment : pathSegments) {
                if (!segment.isEmpty()) {
                    validSegments.add(segment);
                }
            }
            
            if (validSegments.isEmpty()) {
                return "";
            }
            
            // 取最后最多4个有效段
            int startIndex = Math.max(0, validSegments.size() - 4);
            java.util.List<String> lastFourSegments = validSegments.subList(startIndex, validSegments.size());
            
            // 重新组合路径
            StringBuilder result = new StringBuilder();
            for (String segment : lastFourSegments) {
                result.append("/").append(segment);
            }
            
            return result.toString();
            
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
    
    /**
     * 下载HTTP文件到本地
     * @param httpUrl HTTP资源链接
     * @param localFile 本地目标文件
     */
    private void downloadHttpFile(String httpUrl, File localFile) throws Exception {
        // 发起HTTP请求下载资源
        URL url = new URL(httpUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        
        // 配置HTTP请求参数
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(CONNECT_TIMEOUT);
        connection.setReadTimeout(READ_TIMEOUT);
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36");
        
        // 检查响应状态（200=成功）
        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            System.err.println("HTTP响应失败，状态码：" + responseCode + "，链接：" + httpUrl);
            connection.disconnect();
            return;
        }
        
        // 读取HTTP响应流，写入本地文件（同名文件会覆盖）
        try (InputStream in = connection.getInputStream();
             OutputStream out = new FileOutputStream(localFile)) {
            
            byte[] buffer = new byte[1024 * 8]; // 8KB缓冲区，提升下载效率
            int len;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            out.flush(); // 确保数据完全写入
        } finally {
            connection.disconnect(); // 关闭连接，释放资源
        }
    }
    
    /**
     * 复制本地文件到目标位置
     * @param sourceFile 源文件
     * @param targetFile 目标文件
     */
    private void copyLocalFile(File sourceFile, File targetFile) throws Exception {
        // 检查源文件是否存在
        if (!sourceFile.exists()) {
            System.err.println("源文件不存在：" + sourceFile.getAbsolutePath());
            return;
        }
        
        // 检查是否为文件
        if (!sourceFile.isFile()) {
            System.err.println("源路径不是文件：" + sourceFile.getAbsolutePath());
            return;
        }
        
        // 复制文件（同名文件会覆盖）
        try (InputStream in = new FileInputStream(sourceFile);
             OutputStream out = new FileOutputStream(targetFile)) {
            
            byte[] buffer = new byte[1024 * 8]; // 8KB缓冲区
            int len;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            out.flush();
        }
        
        System.out.println("📄 本地文件复制成功：" + sourceFile.getAbsolutePath() + " -> " + targetFile.getAbsolutePath());
    }
    
    /**
     * 智能URL解码：判断链接是否编码，编码则解码，未编码则返回原链接
     * @param url 待处理的URL（可能编码/未编码）
     * @return 处理后的正常URL
     */
    private String smartDecodeUrl(String url) {
        if (url == null || url.isEmpty()) {
            return "";
        }

        // 第一步：判断链接是否包含有效的URL编码（%+两位十六进制）
        boolean isEncoded = URL_ENCODED_PATTERN.matcher(url).find();
        if (!isEncoded) {
            System.out.println("ℹ️  链接未进行URL编码，直接返回原链接");
            return url;
        }

        // 第二步：编码链接执行解码（UTF-8标准编码）
        try {
            String decodedUrl = URLDecoder.decode(url, "UTF-8");
            System.out.println("✅ 链接已编码，解码成功");
            return decodedUrl;
        } catch (UnsupportedEncodingException e) {
            System.err.println("❌ 解码失败（编码格式异常），返回原链接：" + e.getMessage());
            return url;
        } catch (IllegalArgumentException e) {
            // 捕获无效编码（如%后不是两位十六进制），返回原链接
            System.err.println("❌ 链接包含无效URL编码，返回原链接：" + e.getMessage());
            return url;
        }
    }


    /**
     * 根据文件后缀确定目标存储目录
     * @param fileName 文件名（含后缀）
     * @return 目标目录路径
     */
    private String getTargetDirByFileExt(String fileName) {
        // 提取文件后缀（忽略大小写）
        String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();

        // 图片后缀列表（可根据需要扩展，如png、gif、bmp等）
        String[] imgExts = {"jpg", "jpeg", "png", "gif", "bmp", "tiff"};
        for (String ext : imgExts) {
            if (ext.equals(fileExt)) {
                return IMGS_DIR;
            }
        }

        // 文档后缀列表（可根据需要扩展，如doc、pdf、txt等）
        String[] docExts = {"docx", "doc", "pdf", "txt", "xls", "xlsx", "ppt", "pptx"};
        for (String ext : docExts) {
            if (ext.equals(fileExt)) {
                return DOCS_DIR;
            }
        }

        // 其他文件默认存到docs目录（可根据需要修改默认目录）
        return DOCS_DIR;
    }
}