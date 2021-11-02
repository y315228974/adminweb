package com.lz.adminweb.service.impl;

import com.lz.adminweb.service.FileService;
import com.lz.adminweb.vo.JsonResult;
import com.lz.adminweb.constant.Configs;
import com.lz.adminweb.exception.ConsciousException;
import com.lz.adminweb.domain.FileConfigEntity;
import com.lz.adminweb.utils.FileTypeUtil;
import com.lz.adminweb.utils.HttpUtil;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 文件上传实现
 *
 * @author pangshihe
 * @date 2020/7/24
 */
@Service
public class FileServiceImpl implements FileService {
    private static Logger log = LoggerFactory.getLogger(FileServiceImpl.class);

    @Resource
    FileConfigEntity fileConfigEntity;

    @Resource
    private HttpServletRequest request;

    /**
     * 允许文件上传最大BYTE数
     */
    private static final int MAX_FILE_SIZE = Configs.MAX_UPLOAD_SIZE * 1024 * 1024;

    /**
     * 上传文件
     *
     * @param file 上传的文件
     * @return com.sihc.supervision.entity.JsonResult
     * @author zhujinming
     * @date 2020/6/22 11:45
     */
    @Override
    public JsonResult uploadPic(MultipartFile file) throws IOException {
        log.error("上传文件file/upload开始：" + System.currentTimeMillis());
        if (file == null || file.getSize() == 0) {
            return JsonResult.fail("未找到上传的文件");
        }
        if (!FileTypeUtil.isAllowPicType(file)) {
            return JsonResult.fail("上传的文件类型不合法");
        }
        long fileSize = file.getSize();
        if (fileSize > Configs.MAX_UPLOAD_SIZE * 1024 * 1024) {
            return JsonResult.fail(String.format("上传图片不得超过%sM", Configs.MAX_UPLOAD_SIZE));
        }
        Map map = saveFile(file);
        return map == null ? JsonResult.fail("上传失败") : JsonResult.ok(map);
    }

    /**
     * 上传excel文件
     *
     * @param multipartFile 上传的文件
     * @return java.io.File 磁盘保存的文件
     * @author qinmingtong
     * @date 2021/1/29 16:14
     */
    @Override
    public File uploadExcel(MultipartFile multipartFile) {
        //校验
        if (multipartFile == null || multipartFile.getSize() == 0) {
            throw new ConsciousException("上传文件不能为空");
        }
        if (!FileTypeUtil.checkFile(multipartFile, FileTypeUtil.ALLOW_EXCEL_TYPEV2)) {
            throw new ConsciousException("文件类型错误！");
        }
        long fileSize = multipartFile.getSize();
        if (fileSize > MAX_FILE_SIZE) {
            throw new ConsciousException(String.format("上传图片不得超过%sM", Configs.MAX_UPLOAD_SIZE));
        }
        //保存
        String suffix = StringUtils.substringAfterLast(multipartFile.getOriginalFilename(), ".");
        String fileName = System.currentTimeMillis() + RandomUtils.nextInt(0, 50) + "." + suffix;
        String path = String.format("/%s/%s", new SimpleDateFormat("yyyy/MM/dd").format(new Date()), fileName);
        File destFile = new File(fileConfigEntity.getUrl(), path);
        File directory = destFile.getParentFile();
        if (!directory.exists() && !directory.mkdirs()) {
            throw new ConsciousException(String.format("系统找不到指定的路径:%s", directory.getAbsolutePath()));
        }
        try {
            multipartFile.transferTo(destFile);
            return destFile;
        }catch (IOException e){
            log.error(e.getMessage(), e);
            throw new ConsciousException("文件保存失败");
        }
    }

    /**
     * 保存文件
     *
     * @param file 文件
     * @return java.util.Map
     * @author pangshihe
     * @date 2020/6/28 10:32
     */
    private Map saveFile(MultipartFile file) {
        Map map = null;

        final String originName = file.getOriginalFilename();
        final String typeFile = StringUtils.substringAfterLast(originName, ".");
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String fileName = System.currentTimeMillis() + RandomUtils.nextInt(0, 50) + "." + typeFile;
        String path = String.format("/%s/%s", sdf.format(new Date()), fileName);
        String iosPath = fileConfigEntity.getUrl() + sdf.format(new Date()) + "/";
        String fullPath = fileConfigEntity.getUrl() + path.substring(1);
        log.error("上传文件file/upload保存文件：" + System.currentTimeMillis());
        try {
            InputStream inputStream = file.getInputStream();
            File dest = new File(fileConfigEntity.getUrl(), path);
            File parentFile = dest.getParentFile();
            if (!parentFile.exists()) {
                if (!parentFile.mkdirs()) {
                    return null;
                }
            }
            file.transferTo(dest);
//            ImageUtil imageUtil=new ImageUtil();
//            imageUtil.getPictureByName(iosPath,fileName);
            String[] thumbSize = Configs.REPORT_PICTURE_THUMBNAIL.split("x");
            String thumbPath = saveThumbnail(inputStream, fullPath, Integer.valueOf(thumbSize[0]), Integer.valueOf(thumbSize[1]));
            if (thumbPath == null) {
                return null;
            }
            String returnThumbPath = "/files/" + thumbPath.replace(fileConfigEntity.getUrl(), "");
            map = new HashMap(6);
            map.put("name", originName);
            map.put("path", "/files" + path);
            map.put("netResourcePath", HttpUtil.getNetResourcePath(request,"/files" + path));
            map.put("thumbPath", returnThumbPath);
            map.put("size", file.getSize());
            map.put("addDate", new Date());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return null;
        }
        log.error("上传文件file/upload结束：" + System.currentTimeMillis());
        return map;
    }

    /**
     * 生成缩略图
     *
     * @param inputStream 输入流
     * @param fullPath    原图路径
     * @param width       生成的宽度
     * @param height      高度
     * @return void
     * @author liuzhaowei
     * @date 2020/7/27
     */
    private String saveThumbnail(InputStream inputStream, String fullPath, int width, int height) {
        String[] arr = fullPath.split("\\.");
        String thumbPath = String.format("%s_%sx%s.%s", arr[0], width, height, arr[1]);
        try {
            ByteArrayOutputStream outputBuffer = new ByteArrayOutputStream();
            Thumbnails.of(inputStream)
                    .size(width, height)
                    .toOutputStream(outputBuffer);
            InputStream thumbInputStream = new ByteArrayInputStream(outputBuffer.toByteArray());
            FileUtils.copyInputStreamToFile(thumbInputStream, new File(thumbPath));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
        return thumbPath;

    }
}
