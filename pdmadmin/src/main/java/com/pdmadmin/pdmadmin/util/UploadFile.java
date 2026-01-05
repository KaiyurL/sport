package com.pdmadmin.pdmadmin.util;

import lombok.Data;

/**
 * @author 潘多码(微信 : panduoma888)
 * @version 1.0.0
 * @description
 * @website www.panduoma.com
 * @copyright 公众号: 潘多码
 */
@Data
public class UploadFile {
    //文件地址  http://127.0.0.1:8080/uploads/xxx.png
    private String url;
    //文件名 xxx.png
    private String name;
}