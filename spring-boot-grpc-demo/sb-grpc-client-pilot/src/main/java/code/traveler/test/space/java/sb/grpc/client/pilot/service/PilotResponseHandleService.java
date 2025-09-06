package code.traveler.test.space.java.sb.grpc.client.pilot.service;

import org.springframework.stereotype.Service;

import code.traveler.test.space.java.sb.grpc.lib.pilot.PPilotResponse;
import java.util.Locale;
import java.util.Objects;

/**
 * @author panda
 * @description pilot 业务处理
 * @time 2023/7/27 23:49
 */
@Service
public class PilotResponseHandleService {

    /**
     * @param response
     * @return void
     * @description 处理返回信息
     * @author panda
     * @time 2023/7/27 23:48
     */
    public void handler(PPilotResponse response) {

        if (isHeartBeat(response)) {

        } else if (isPilotConfig(response)) {

        }
    }

    /**
     * @param response
     * @return boolean
     * @description 是否是心跳ping
     * @author panda
     * @time 2023/7/27 14:07
     */
    private boolean isHeartBeat(PPilotResponse response) {
        return Objects.equals(response
                .getMessageCase()
                .name()
                .toLowerCase(Locale.ROOT), MessageCase.PING
                .name()
                .toLowerCase(Locale.ROOT));
    }

    /**
     * @param response
     * @return boolean
     * @description 是否是下发的配置
     * @author panda
     * @time 2023/7/27 14:07
     */
    private boolean isPilotConfig(PPilotResponse response) {
        return Objects.equals(response
                .getMessageCase()
                .name()
                .toLowerCase(Locale.ROOT), MessageCase.PILOT_CONFIG
                .name()
                .toLowerCase(Locale.ROOT));
    }

    /**
     * @author tiger
     * @description pilot的message类型
     * @time 2023/6/1 3:51 PM
     */
    enum MessageCase {PING, PILOT_CONFIG;}
}
