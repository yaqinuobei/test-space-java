package code.traveler.test.space.java.mcp.client.controller;

import com.alibaba.cloud.ai.dashscope.api.DashScopeImageApi;
import com.alibaba.cloud.ai.dashscope.image.DashScopeImageModel;
import com.alibaba.cloud.ai.dashscope.image.DashScopeImageOptions;
import org.apache.commons.codec.binary.StringUtils;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Objects;

@RequestMapping("/api/image")
@RestController
@Validated
public class ImageController {

    //文生图模型
    @Autowired
    private DashScopeImageModel dashScopeImageModel;

    /**
     * 文生图
     * DashScopeImageAsyncResponseOutput[taskId=b4f67d5d-2a6b-4da3-a4ae-5faeb5b94546, taskStatus=FAILED,
     * results=null, taskMetrics=null, code=InvalidParameter, message=The size does not match the allowed size
     * 1664*928,1472*1140,1328*1328,1140*1472,928*1664.]
     *
     * @param imageModelOptions
     * @return
     */
    @PostMapping("/askQuetion/call")
    public String imageByCall(@RequestBody @Valid ImageModelOptions imageModelOptions) {
        String imageUrl = "";
        DashScopeImageOptions.Builder imageOptionsBuilder = DashScopeImageOptions.builder();
        imageOptionsBuilder.withHeight(1140)
                           .withWidth(1472)
                           .withModel("wan2.2-t2i-flash");

        if (Objects.nonNull(imageModelOptions.getModel())) {
            imageOptionsBuilder
                    .withModel(imageModelOptions.getModel());
        }

        ImagePrompt imagePrompt = new ImagePrompt(imageModelOptions.getMessage(), imageOptionsBuilder.build());
        String taskId = dashScopeImageModel.submitImageGenTask(imagePrompt);
        System.out.println("任务ID：" + taskId);
        DashScopeImageApi.DashScopeImageAsyncResponse imageGenTask = dashScopeImageModel.getImageGenTask(taskId);
        while (StringUtils.equals("RUNNING", imageGenTask.output()
                                                         .taskStatus())) {
            imageGenTask = dashScopeImageModel.getImageGenTask(taskId);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        if (StringUtils.equals("SUCCEEDED", imageGenTask.output()
                                                        .taskStatus())) {
            imageUrl = imageGenTask.output()
                                   .results()
                                   .get(0)
                                   .url();
        } else {
            System.out.println("图片生成失败，执行结果：" + imageGenTask.output());
        }

        System.out.println("图片地址：" + imageUrl);
        return "图片地址：" + imageUrl;
    }
}
