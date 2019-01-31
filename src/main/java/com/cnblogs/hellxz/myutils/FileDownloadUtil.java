package com.cnblogs.hellxz.myutils;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

/**
 * <b>类名</b>: FileDownloadUtil
 * <p><b>描    述</b> 文件下载工具类 </p>
 *
 * <p><b>创建日期</b>: 2019/1/9 18:38 </p>
 *
 * @author HELLXZ 张
 * @version 1.0
 * @since jdk 1.8
 */
public class FileDownloadUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileDownloadUtil.class);

    /**
     * 下载文件
     *
     * @param filePathName 文件名全路径名,classpath下
     * @param downloadName 文件下载显示的名称
     * @param request      请求
     * @param response     响应
     */
    public static void downloadFile(String filePathName,
                                    String downloadName, HttpServletRequest request, HttpServletResponse response) {
        InputStream input = null;
        OutputStream output = null;
        LOGGER.info("执行下载文件功能，文件路径={}， 下载文件名={}", filePathName, downloadName);
        try {

            preTransferOperation(downloadName, request, response);
            ClassPathResource resource = new ClassPathResource(filePathName);
            LOGGER.info("资源是否存在：{}", resource.exists());
            input = resource.getInputStream();
            output = response.getOutputStream();

            //复制流
            IOUtils.copy(input, output);

            LOGGER.info("开始输出>>>>>>>");
        } catch (Exception e) {
            LOGGER.error("下载文件出现异常", e);
            throw new RuntimeException("下载文件出现异常", e);
        } finally {
            IOUtils.closeQuietly(output, input);
        }
    }

    /**
     * 使用字节数组下载文件，未测试
     *
     * @param bytes        字节数组
     * @param downloadName 下载的文件名
     * @param request      request请求
     * @param response     response响应
     */
    public static void downloadFile(byte[] bytes,
                                    String downloadName, HttpServletRequest request, HttpServletResponse response) {
        OutputStream output = null;
        try {
            preTransferOperation(downloadName, request, response);

            output = response.getOutputStream();
            LOGGER.info("开始输出>>>>>>>");
            output.write(bytes);
        } catch (Exception e) {
            LOGGER.error("下载文件出现异常", e);
            throw new RuntimeException("下载文件出现异常", e);
        } finally {
            IOUtils.closeQuietly(output);
        }
    }

    /**
     * 对Response流做预处理，并根据浏览器对输出的文件名进行转码
     *
     * @param targetName 下载的显示的文件名
     * @param request    请求
     * @param response   响应
     * @throws IOException 异常
     */
    private static void preTransferOperation(String targetName, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.reset();
        response.setCharacterEncoding("utf-8");
        //判断浏览器类型防止下载文件名乱码
        String agent = request.getHeader("User-Agent").toUpperCase();
        //火狐浏览器等
        boolean flag = (agent.contains("MSIE")) || ((agent.contains("RV")) && (!agent.contains("FIREFOX")));
        if (flag) {
            targetName = URLEncoder.encode(targetName, "UTF-8");
        } else {
            targetName = new String(targetName.getBytes("UTF-8"), "ISO8859-1");
        }
        //自动识别文件类型
        response.setContentType("application/octet-stream");
        response.setHeader("Content-disposition", "attachment;filename=\"" + targetName + "\"");
    }
}
