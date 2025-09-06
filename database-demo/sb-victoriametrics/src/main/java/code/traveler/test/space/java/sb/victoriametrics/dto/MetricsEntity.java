package code.traveler.test.space.java.sb.victoriametrics.dto;

import lombok.Data;

import java.util.Map;

@Data
public class MetricsEntity{
    private String name;
    private double value;
    private Long timestamp;
    private Map<String,Object> labels;
}
