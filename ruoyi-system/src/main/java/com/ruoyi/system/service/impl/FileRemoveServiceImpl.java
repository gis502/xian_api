package com.ruoyi.system.service.impl;

import com.ruoyi.system.service.FileRemoveService;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Pattern;

/**
 * 图片下载服务实现类（保留URL目录层级下载到指定位置）
 * @author zzw
 * @date 2025/11/27 上午10:30
 */
@Service
public class FileRemoveServiceImpl implements FileRemoveService {
    // 图片存储根目录
    private static final String IMGS_ROOT_DIR = "/home/xian/dist/imgs/";
    // HTTP连接超时时间（5秒）
    private static final int CONNECT_TIMEOUT = 5000;
    // HTTP读取超时时间（10秒）
    private static final int READ_TIMEOUT = 10000;

    // 匹配URL编码的正则：% + 两位十六进制字符（0-9、a-f、A-F）
    private final Pattern URL_ENCODED_PATTERN = Pattern.compile("%[0-9a-fA-F]{2}");

    /**
     * 下载HTTP链接中的图片，保留原URL的目录层级（从/home/后开始）
     * 支持处理多次URL编码的链接（如http%253A%252F%252Fxxx...）
     * 新增：文件已存在则跳过下载
     * @param encodedUrl 多次URL编码的图片链接
     */
    public void removeFile(String encodedUrl) {
        try {
            // 1. 基础校验
            if (encodedUrl == null || encodedUrl.isEmpty()) {
                System.err.println("错误：图片链接不能为空");
                return;
            }

            // 2. 校验是否为HTTP链接（非HTTP直接返回）
            if (!encodedUrl.toLowerCase().contains("http")) {
                System.err.println("错误：仅支持HTTP/HTTPS图片链接，当前链接：" + encodedUrl);
                return;
            }

            // 3. 多次URL解码（处理多层编码的链接）
            String decodedUrl = multiDecodeUrl(encodedUrl);

            // 4. 提取图片文件名（处理中文和特殊字符）
            String fileName = extractFileNameFromUrl(decodedUrl);
            if (fileName == null || fileName.isEmpty()) {
                System.err.println("错误：无法从链接提取文件名，链接：" + decodedUrl);
                return;
            }

            // 5. 校验是否为图片文件（非图片直接返回）
            if (!isImageFile(fileName)) {
                System.err.println("错误：仅支持图片文件，当前文件：" + fileName);
                return;
            }

            // 6. 提取URL中/data/后的目录层级（如output/storm-disaster/thematic/...）
            String folderPath = extractFolderPathAfterHome(decodedUrl);

            // 7. 构建完整的本地存储目录（imgs根目录 + 提取的层级）
            String localTargetDir = IMGS_ROOT_DIR + folderPath;
            // 确保目录存在（多级目录自动创建）
            Files.createDirectories(Paths.get(localTargetDir));

            // 8. 构建本地图片保存路径
            String localImgPath = localTargetDir + File.separator + fileName;
            File localImgFile = new File(localImgPath);

            // ========== 核心新增：文件存在性检查 ==========
            if (localImgFile.exists()) {
                // 仅保留轻量提示，也可直接删除该行（完全静默跳过）
                System.err.println("提示：文件已存在，跳过下载：" + localImgPath);
                return;
            }
            // =============================================

            // 9. 下载图片到指定目录
            downloadImage(decodedUrl, localImgFile);

        } catch (Exception e) {
            System.err.println("错误：图片下载失败 - " + e.getMessage());
            // 生产环境可注释掉printStackTrace，仅保留日志输出
            // e.printStackTrace();
        }
    }

    /**
     * 多次URL解码（处理多层编码的链接，直到无编码字符）
     * @param encodedUrl 多次编码的URL
     * @return 解码后的真实URL
     */
    private String multiDecodeUrl(String encodedUrl) {
        String decoded = encodedUrl;
        // 循环解码，直到链接中无URL编码字符（%xx）
        while (URL_ENCODED_PATTERN.matcher(decoded).find()) {
            try {
                decoded = URLDecoder.decode(decoded, StandardCharsets.UTF_8.name());
            } catch (Exception e) {
                System.err.println("警告：解码中断，使用当前已解码结果");
                break;
            }
        }
        return decoded;
    }

    /**
     * 从URL中提取图片文件名（处理中文和特殊字符）
     * @param url 解码后的HTTP图片链接
     * @return 图片文件名（含后缀）
     */
    private String extractFileNameFromUrl(String url) {
        try {
            // 截取URL最后一个"/"后的部分作为文件名
            int lastSlashIndex = url.lastIndexOf("/");
            if (lastSlashIndex == -1 || lastSlashIndex == url.length() - 1) {
                return null;
            }
            String fileName = url.substring(lastSlashIndex + 1);
            // 最终解码确保中文正常显示
            return URLDecoder.decode(fileName, StandardCharsets.UTF_8.name());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 提取URL中/home/后的目录层级（到文件名前）
     * 示例：http://10.22.245.247/home/output/storm/1/xxx.jpg → output/storm/1/
     * @param url 解码后的真实URL
     * @return /home/后的目录路径（无则返回空字符串）
     */
    private String extractFolderPathAfterHome(String url) {
        try {
            // 第一步：移除协议和主机部分（如http://10.22.245.247/）
            String path = url;
            if (path.startsWith("http://")) {
                path = path.substring(7);
            } else if (path.startsWith("https://")) {
                path = path.substring(8);
            }
            // 找到第一个"/"（主机名结束位置）
            int hostEndIndex = path.indexOf("/");
            if (hostEndIndex == -1) {
                return "";
            }
            // 截取主机名后的路径部分
            String fullPath = path.substring(hostEndIndex + 1);

            // 第二步：找到/home/的位置
            String homeFlag = "data/";
            int homeIndex = fullPath.indexOf(homeFlag);
            if (homeIndex == -1) {
                return ""; // 无/home/目录则返回空
            }

            // 第三步：截取/home/后的部分（到文件名前）
            String pathAfterHome = fullPath.substring(homeIndex + homeFlag.length());
            // 找到最后一个"/"（文件名前的位置）
            int lastSlashIndex = pathAfterHome.lastIndexOf("/");
            if (lastSlashIndex == -1) {
                return "";
            }

            // 返回/home/后到文件名前的目录路径
            return pathAfterHome.substring(0, lastSlashIndex);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 校验是否为图片文件（通过后缀判断）
     * @param fileName 文件名（含后缀）
     * @return true=是图片，false=非图片
     */
    private boolean isImageFile(String fileName) {
        // 支持的图片后缀（可按需扩展）
        String[] imgExts = {"jpg", "jpeg", "png", "gif", "bmp", "tiff", "webp"};
        // 提取后缀（忽略大小写）
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex == -1 || dotIndex == fileName.length() - 1) {
            return false;
        }
        String ext = fileName.substring(dotIndex + 1).toLowerCase();
        // 校验后缀是否在图片列表中
        for (String imgExt : imgExts) {
            if (imgExt.equals(ext)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 下载图片到本地指定文件
     * @param imgUrl 解码后的图片链接
     * @param localFile 本地保存的图片文件
     * @throws Exception 下载异常
     */
    private void downloadImage(String imgUrl, File localFile) throws Exception {
        URL url = new URL(imgUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // 配置HTTP请求参数
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(CONNECT_TIMEOUT);
        connection.setReadTimeout(READ_TIMEOUT);
        // 设置User-Agent，避免部分服务器拒绝请求
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36");

        // 校验响应状态（200=成功）
        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new RuntimeException("HTTP响应失败，状态码：" + responseCode);
        }

        // 读取图片流并写入本地文件
        try (InputStream in = connection.getInputStream();
             OutputStream out = new FileOutputStream(localFile)) {

            byte[] buffer = new byte[1024 * 8]; // 8KB缓冲区提升下载效率
            int len;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            out.flush(); // 确保数据完全写入
        } finally {
            connection.disconnect(); // 关闭连接释放资源
        }
    }
}
