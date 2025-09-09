package code.traveler.test.space.java.mcp.client.controller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoModelOptions {

    private String message;

    private String model;

    // 首帧图片：
    // 方式一、需要调用PublicUrlHandler上传到阿里临时存储，获取此url。注意：api-key与模型都需要保持一致（测试oss地址不能识别）
    // 方式3二、网络上搜一个图片，获取地址
    private String imageUrl;

}
