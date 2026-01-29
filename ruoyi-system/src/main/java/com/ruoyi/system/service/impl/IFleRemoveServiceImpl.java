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
     * 下载单个HTTP资源到本地对应目录
     * @param httpUrl HTTP资源链接
     */
    public void removeFile(String httpUrl) {
        try{
            httpUrl = smartDecodeUrl(httpUrl);
            // 1. 从URL中提取文件名（处理中文和特殊字符）
            String fileName = extractFileNameFromUrl(httpUrl);
            if (fileName == null || fileName.isEmpty()) {
                System.err.println("无法提取文件名：" + httpUrl);
                return;
            }

            // 2. 根据文件后缀确定目标存储目录
            String targetDir = getTargetDirByFileExt(fileName);
            // 创建目标目录（不存在则自动创建，包括多级目录）
            Files.createDirectories(Paths.get(targetDir));

            // 3. 构建本地文件完整路径
            String localFilePath = targetDir + File.separator + fileName;
            File localFile = new File(localFilePath);

            // 4. 检查文件是否已存在，存在则直接返回成功
            if (localFile.exists()) {
                System.out.println("📁 文件已存在，跳过下载：" + localFilePath);
                return;
            }

            // 5. 发起HTTP请求下载资源
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

            // 6. 读取HTTP响应流，写入本地文件
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

            System.out.println("📥 下载成功，保存路径：" + localFilePath);
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
