

package code.traveler.test.space.java.sb.grpc.client.pilot.message;

import io.grpc.Metadata;
import io.grpc.Status;
import io.micrometer.core.instrument.util.StringUtils;

/**
 * @description
 * @author tiger
 * @time 2023/5/30 5:31 PM
 */
public class PilotHeaderReader implements HeaderReader<PilotHeader> {
    @Override
    public PilotHeader extract(Metadata headers) {
        return new PilotHeader(
                getStringHeader(headers, PilotHeader.PILOT_ID_KEY),
                getStringHeader(headers, PilotHeader.PILOT_INSTANCE_ID_KEY),
                getTime(headers, PilotHeader.START_TIME_KEY));
    }

    private String getStringHeader(Metadata headers, Metadata.Key<String> stringKey) {
        String pilotId = headers.get(stringKey);
        if (StringUtils.isBlank(pilotId)) {
            throw Status.INVALID_ARGUMENT.withDescription(stringKey.name() + " header is missing").asRuntimeException();
        }
        return pilotId;
    }

    protected long getTime(Metadata headers, Metadata.Key<String> timeKey) {
        final String timeStr = headers.get(timeKey);
        if (timeStr == null) {
            throw Status.INVALID_ARGUMENT.withDescription(timeKey.name() + " header is missing").asRuntimeException();
        }
        try {
            // check number format
            return Long.parseLong(timeStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("unsupported format");
        }
    }
}
