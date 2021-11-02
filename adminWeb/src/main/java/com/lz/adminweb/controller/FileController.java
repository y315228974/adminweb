package com.lz.adminweb.controller;

import com.lz.adminweb.service.FileService;
import com.lz.adminweb.vo.JsonResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * 文件上传
 *
 * @author pangshihe
 * @date 2020/7/24
 */
@RestController
@RequestMapping("/file")
public class FileController {
    @Resource
    FileService fileService;

    /**
     * 上传文件
     *
     * @param file 文件
     * @return JsonResult
     * @author pangshihe
     * @date 2020/9/28 9:59
     */
    @PostMapping(value = "/upload", headers = "content-type=multipart/form-data")
    public JsonResult uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        return fileService.uploadPic(file);
    }
}
