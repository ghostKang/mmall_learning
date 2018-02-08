package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.mmall.service.IFileService;
import com.mmall.util.FTPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * 文件上传
 * Created by Yuk on 2018/2/8.
 */
@Service("iFileService")
public class FileServiceImpl implements IFileService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public String uploadFile(MultipartFile file,String path){
        String fileName = file.getOriginalFilename();// 文件名
        String fileExtensionName = fileName.substring(fileName.lastIndexOf(".")+1);// 文集拓展名
        String uploadFileName = UUID.randomUUID().toString()+"."+fileExtensionName;
        logger.info("开始上传文件，文件名：{}，上传的路径：{}，新文件名：{}",fileName,path,uploadFileName);

        // 创建文件夹
        File fileDir = new File(path);
        if(!fileDir.exists()){
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }
        // 文件
        File targetFile = new File(path,uploadFileName);
        try {
            // 上传文件
            file.transferTo(targetFile);
            // 将targetFile上传到FTP服务器
            //FTPUtil.uploadFile(Lists.<File>newArrayList(targetFile));
            //上传完后，删除upload下的文件
            //targetFile.delete();
        } catch (IOException e) {
            logger.error("文件上传异常{}",e);
            return  null;
        }
        return targetFile.getName();
    }
}
