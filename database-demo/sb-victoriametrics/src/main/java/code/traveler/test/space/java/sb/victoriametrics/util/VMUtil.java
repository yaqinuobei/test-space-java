package code.traveler.test.space.java.sb.victoriametrics.util;

import code.traveler.test.space.java.sb.victoriametrics.dto.MetricsEntity;
import org.xerial.snappy.Snappy;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class VMUtil {

    public static final String PROMETHEUS_PROPERTY_NAME = "__name__";

    /**
     * <p>Pb序列化性能数据</p>
     *
     * @param list 源数据
     * @return byte[] 字节数组
     * @throws IOException Snappy.compress可能出现IO异常
     */
    public static byte[] deSerialise(List<MetricsEntity> list) throws IOException {
        protobuf.Remote.WriteRequest.Builder rwb = protobuf.Remote.WriteRequest.newBuilder();
        for (MetricsEntity data : list) {
            if (data.getName() == null) {
                continue;
            }
            protobuf.Types.TimeSeries.Builder ttb = protobuf.Types.TimeSeries.newBuilder();
            protobuf.Types.Sample sample = protobuf.Types.Sample.newBuilder()
                                                                .setValue(ConvertUtil.convertObjectToDouble(data.getValue()))
                                                                .setTimestamp(data.getTimestamp())
                                                                .build();
            for (Map.Entry<String, Object> entry : data.getLabels().entrySet()) {
                if (entry.getKey() == null || entry.getValue() == null) {
                    continue;
                }
                ttb.addLabels(protobuf.Types.Label.newBuilder()
                                                  .setName(entry.getKey())
                                                  .setValue(entry.getValue().toString())
                                                  .build());
            }
            ttb.addLabels(protobuf.Types.Label.newBuilder().setName(PROMETHEUS_PROPERTY_NAME).setValue(data.getName()).build());
            ttb.addSamples(sample);
            rwb.addTimeseries(ttb.build());
        }
        return Snappy.compress(rwb.build().toByteArray());
    }
}
