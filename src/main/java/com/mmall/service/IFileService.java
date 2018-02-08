package com.mmall.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Created by Yuk on 2018/2/8.
 */
public interface IFileService {

    String uploadFile(MultipartFile file, String path);
}
