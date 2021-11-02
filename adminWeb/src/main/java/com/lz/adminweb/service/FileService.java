package com.lz.adminweb.service;

import com.lz.adminweb.vo.JsonResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * 文件/图片上传
 *
 * @author pangshihe
 * @date 2020/7/24
 */
public interface FileService {
    /**
     * 上传文件
     *
     * @param file    上传的文件
     * @return com.sihc.supervision.entity.JsonResult
     * @author zhujinming
     * @date 2020/6/22 11:45
     */
    JsonResult uploadPic(MultipartFile file) throws IOException;

    /**
     * 上传excel文件
     *
     * @param multipartFile 上传的文件
     * @return java.io.File 磁盘保存的文件
     * @author qinmingtong
     * @date 2021/1/29 16:14
     */
    File uploadExcel(MultipartFile multipartFile);

}
