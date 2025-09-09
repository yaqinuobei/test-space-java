package code.traveler.test.space.java.mcp.client.controller;

import com.alibaba.cloud.ai.dashscope.api.DashScopeVideoApi;
import com.alibaba.cloud.ai.dashscope.audio.DashScopeAudioSpeechModel;
import com.alibaba.cloud.ai.dashscope.audio.DashScopeAudioSpeechOptions;
import com.alibaba.cloud.ai.dashscope.audio.synthesis.SpeechSynthesisPrompt;
import com.alibaba.cloud.ai.dashscope.audio.synthesis.SpeechSynthesisResponse;
import com.alibaba.cloud.ai.dashscope.video.DashScopeVideoModel;
import com.alibaba.cloud.ai.dashscope.video.DashScopeVideoOptions;
import com.alibaba.cloud.ai.dashscope.video.VideoOptions;
import com.alibaba.cloud.ai.dashscope.video.VideoPrompt;
import org.apache.commons.codec.binary.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Objects;

@RequestMapping("/api/video")
@RestController
@Validated
public class VideoController {

    //文生图模型
    @Autowired
    private DashScopeAudioSpeechModel dashScopeAudioSpeechModel;

    @Autowired
    private DashScopeVideoModel dashScopeVideoModel;

    @Autowired
    private DashScopeVideoApi dashScopeVideoApi;

    @PostMapping("/askQuetion/call")
    public String audioByCall(@RequestBody @Valid VideoModelOptions audioModelOptions) {
        DashScopeAudioSpeechOptions.Builder audioOptionsBuilder = DashScopeAudioSpeechOptions.builder();
        audioOptionsBuilder.model("wan2.2-t2v-plus");

        if (Objects.nonNull(audioModelOptions.getModel())) {
            audioOptionsBuilder.model(audioModelOptions.getModel());
        }

        SpeechSynthesisPrompt audioPrompt = new SpeechSynthesisPrompt(audioModelOptions.getMessage(),
                audioOptionsBuilder.build());
        SpeechSynthesisResponse synthesisResponse = dashScopeAudioSpeechModel.call(audioPrompt);

        File file = new File("E:\\code\\github\\test-space-java");
        try (FileOutputStream fos = new FileOutputStream(file)) {
            ByteBuffer byteBuffer = synthesisResponse
                    .getResult()
                    .getOutput()
                    .getAudio();
            fos.write(byteBuffer.array());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("视频生成成功！");
        return "视频生成成功！";
    }

    @PostMapping("/video/call")
    public String videoByCall(@RequestBody @Valid VideoModelOptions audioModelOptions) {

        VideoOptions videoOptions = DashScopeVideoOptions
                .builder()
                .model("wan2.2-t2v-plus")
                .imageUrl(audioModelOptions.getImageUrl())
                .build();
        String videoUrl = "";
        String taskId = dashScopeVideoModel.submitGenTask(
                new VideoPrompt(audioModelOptions.getMessage(), videoOptions));
        System.out.println("任务ID：" + taskId);

        ResponseEntity<DashScopeVideoApi.VideoGenerationResponse> videoGenerationResponseResponseEntity =
                this.dashScopeVideoApi.queryVideoGenTask(
                taskId);
        if (videoGenerationResponseResponseEntity
                .getStatusCode()
                .is2xxSuccessful()) {
            DashScopeVideoApi.VideoGenerationResponse response =
                    (DashScopeVideoApi.VideoGenerationResponse) videoGenerationResponseResponseEntity.getBody();
            while (StringUtils.equals("RUNNING", response
                    .getOutput()
                    .getTaskStatus())) {
                videoGenerationResponseResponseEntity = this.dashScopeVideoApi.queryVideoGenTask(taskId);
                if (videoGenerationResponseResponseEntity
                        .getStatusCode()
                        .is2xxSuccessful()) {
                    response =
                            (DashScopeVideoApi.VideoGenerationResponse) videoGenerationResponseResponseEntity.getBody();
                } else {
                    System.out.println("视频生成失败接口调用失败");
                }
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            if (StringUtils.equals("SUCCEEDED", response
                    .getOutput()
                    .getTaskStatus())) {
                videoUrl = response
                        .getOutput()
                        .getVideoUrl();
            } else {
                System.out.println("视频生成失败，执行结果：" + response.getOutput());
            }
        } else {
            System.out.println("视频生成失败接口调用失败");
        }

        return videoUrl;
    }
}
