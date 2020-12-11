package com.offcn.sellergoods.controller;


import com.offcn.entity.Result;
import com.offcn.util.FastDFSClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UploadController {
    @Value("${file_url}")
    private String file_url;


    @RequestMapping("/upload")
    public Result upload(MultipartFile file){
        try {
            String oldFileName = file.getOriginalFilename();


            String exName = oldFileName.substring(oldFileName.lastIndexOf(".") + 1);

            FastDFSClient client = new FastDFSClient("classpath:fastdfs.conf");
            String url = client.uploadFile(file.getBytes(), exName);
            url = file_url + url;

            System.out.println(url);
            return new Result(true,url);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,"上传失败");
        }
    }

}
