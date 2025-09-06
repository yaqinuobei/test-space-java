

package code.traveler.test.space.java.sb.grpc.client.pilot.message;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description
 * @author tiger
 * @time 2023/7/11 7:50 PM
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PilotIdCacheItem {
    String pilotId;

    String pilotInstanceId;

    Long transportId;
}
