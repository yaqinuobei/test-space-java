package code.traveler.test.space.java.sb.victoriametrics.util;

import cn.hutool.core.util.StrUtil;

import java.util.Objects;

public class ConvertUtil {

    public static double convertObjectToDouble(Object valueString) {
        double doubleValue;
        if(Objects.isNull(valueString)){
            return 0;
        }
        String value = valueString.toString();

        if(StrUtil.isEmpty(value)){
            return 0;
        }
        if (value.equalsIgnoreCase("NaN")) {
            doubleValue = Double.NaN;
        } else if (value.equalsIgnoreCase("+Inf")) {
            doubleValue = Double.POSITIVE_INFINITY;
        } else if (value.equalsIgnoreCase("-Inf")) {
            doubleValue = Double.NEGATIVE_INFINITY;
        } else {
            doubleValue = Double.parseDouble(value);
        }
        return doubleValue;
    }

}
