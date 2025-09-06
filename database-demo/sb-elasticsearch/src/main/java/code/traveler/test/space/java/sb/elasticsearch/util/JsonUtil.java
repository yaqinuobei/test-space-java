
package code.traveler.test.space.java.sb.elasticsearch.util;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/**
 * JSON处理工具类
 *
 * @author: yuaying
 * @Date: 2018年5月22日 下午2:11:29
 */
public class JsonUtil {

    private static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);

    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);

        mapper.configure(JsonParser.Feature.ALLOW_MISSING_VALUES, true);
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        // 解析器支持解析特殊字符
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);

        final JavaTimeModule javaTimeModule = new JavaTimeModule();
        // LocalDateTime ---> String
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.SIMPLIFIED_CHINESE)));

        // String ---> LocalDateTime
        javaTimeModule.addDeserializer(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {

            @Override
            public LocalDateTime deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException {

                return DateUtils.mapString2LocalDateTime(p.getValueAsString());
            }
        });

        final JacksonXmlModule jacksonXmlModule = new JacksonXmlModule();
        jacksonXmlModule.setDefaultUseWrapper(false);

        mapper.registerModule(jacksonXmlModule);
        mapper.registerModule(javaTimeModule);
    }

    /**
     * @param pojo
     * @return String
     */
    public static String map2String(final Object pojo) {

        String jsonStr = null;

        if (Objects.isNull(pojo)) {
            return null;
        }

        try {
            jsonStr = mapper.writeValueAsString(pojo);
        } catch (final JsonProcessingException e) {
            logger.error("[JSON]序列化错误", e);
        }

        return jsonStr;
    }

    /**
     * @param pojo
     * @return byte[]
     */
    public static byte[] map2Bytes(final Object pojo) {

        byte[] jsonBytes = null;

        if (Objects.isNull(pojo)) {
            return null;
        }

        try {
            jsonBytes = mapper.writeValueAsBytes(pojo);
        } catch (final JsonProcessingException e) {
            logger.error("[JSON]序列化错误", e);
        }

        return jsonBytes;
    }

    /**
     * java.util.LinkedHashMap
     *
     * @param pojo
     * @return Map<String, Object>
     */
    public static Map<String, Object> map2Map(final Object pojo) {

        Map<String, Object> jsonMap = null;

        if (Objects.isNull(pojo)) {
            return null;
        }
        // 你也可以使用commons-beanutils的两个方法实现: 1) BeanUtils.describe(pojo); 2)
        // PropertyUtils.describe(pojo)
        try {
            jsonMap = mapper.readValue(pojo.toString(), Map.class);
        } catch (final JsonProcessingException e) {
            logger.error("[JSON]反序列化错误", e);
        }

        return jsonMap;
    }

    /**
     * JSON 数据一般三种类型 : String/byte[]/Map<String,Object>
     *
     * @param source
     * @param _clazz
     * @return POJO
     */
    public static <T> T map2Pojo(final Object source, final Class<T> _clazz) {

        T t = null;

        if (Objects.isNull(source)) {
            return null;
        }

        try {
            // String 类型 JSON -> POJO
            if (String.class.equals(source.getClass())) {
                t = mapper.readValue(source.toString(), _clazz);
                return t;

                // byte[] 类型 JSON -> POJO
            } else if (byte[].class.equals(source.getClass())) {
                t = mapper.convertValue(source, new TypeReference<T>() {
                });
                return t;

                // Map 类型 JSON -> POJO
            } else if (source instanceof Map) {
                t = mapper.convertValue(source, _clazz);
                return t;
            } else {
                t = mapper.convertValue(source, _clazz);
                return t;
            }
        } catch (final IOException e) {
            logger.error("[JSON]反序列化错误", e);
        }

        return null;
    }

    /**
     * JSON 数据一般三种类型 : String/byte[]/Map<String,Object>
     *
     * @param source
     * @param type
     * @return POJO
     */
    public static <T> T map2Pojo(final Object source, final TypeReference<T> type) {

        T t = null;

        if (Objects.isNull(source)) {
            return null;
        }

        try {
            t = mapper.convertValue(source, type);
            return t;
        } catch (final IllegalArgumentException e) {
            logger.error("[JSON]反序列化错误", e);
        }

        return null;
    }

    public static <T> List<T> map2PojoList(final Object source, final Class<T> _clazz) {
        final List<T> result;
        if (null == source) {
            return new ArrayList<>();
        }
        try {
            result = mapper.readValue(source.toString(), CollectionsTypeFactory.listOf(_clazz));
            return result;
        } catch (final Exception e) {
            logger.error("[JSON]反序列化错误", e);
        }

        return new ArrayList<>();
    }

    static class CollectionsTypeFactory {
        public static <T> CollectionType listOf(final Class<T> clazz) {
            return TypeFactory.defaultInstance().constructCollectionType(List.class, clazz);
        }
    }

    /**
     * 将JSON数组转换为List<Map<String, String>>
     *
     * @param jsonArray
     * @return mapList
     */
    public static List<Map<String, String>> map2MapList(final Object jsonArray) {

        List<Map<String, String>> mapList = null;

        if (Objects.isNull(jsonArray)) {
            return null;
        }

        mapList = mapper.convertValue(jsonArray, new TypeReference<List<Map<String, String>>>() {
        });

        return mapList;
    }

    /**
     * 将JSON数组转换为List<Map<String, Object>>
     *
     * @param jsonArray
     * @return mapList
     */
    public static List<Map<String, Object>> map2MapObjList(final Object jsonArray) {

        List<Map<String, Object>> mapList = null;

        if (Objects.isNull(jsonArray)) {
            return null;
        }

        mapList = mapper.convertValue(jsonArray, new TypeReference<List<Map<String, Object>>>() {
        });

        return mapList;
    }


    /**
     * 将JSON数组转换为List<LinkedhashMap<String, String>>
     *
     * @param jsonArray
     * @return mapList
     */
    public static List<LinkedHashMap<String, String>> map2LinkedHashMapList(final Object jsonArray) {

        List<LinkedHashMap<String, String>> mapList = null;

        if (Objects.isNull(jsonArray)) {
            return null;
        }

        mapList = mapper.convertValue(jsonArray, new TypeReference<List<LinkedHashMap<String, String>>>() {
        });

        return mapList;
    }


    public static Object map2Parametric(final Object source, final Class<?> clazz, final Class<?> refClazz) {
        if (null == source) {
            return null;
        }
        try {
            if (null == refClazz) {
                return mapper.readValue(source.toString(), clazz);
            }

            return mapper.readValue(source.toString(), ParametricTypeFactory.parametricOf(clazz, refClazz));
        } catch (final Exception e) {
            logger.error("[JSON]反序列化错误", e);
            return null;
        }
    }

    static class ParametricTypeFactory {
        public static JavaType parametricOf(final Class<?> clazz, final Class<?> refClazz) {
            return TypeFactory.defaultInstance().constructParametricType(clazz, refClazz);
        }
    }


    /**
     * Base64编码
     *
     * @param target
     * @return String
     */
    public static String encodeBase64(final String target) {

        if (!StringUtils.isEmpty(target)) {
            return mapper.convertValue(target, String.class);
        }

        return "";
    }

    /**
     * Base64解码
     *
     * @param target
     * @return String
     */
    public static String decodeBase64(final String target) {

        if (!StringUtils.isEmpty(target)) {
            return new String(mapper.convertValue(target, byte[].class));
        }

        return "";
    }

    /**
     * JSON->JsonNode
     *
     * @param json
     * @return JsonNode
     */
    public static JsonNode toJsonNode(final String json) {

        if (null == json) {
            return null;
        }
        try {
            return mapper.readTree(json);
        } catch (final Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * JSON bytes->BEAN
     *
     * @param json  bytes
     * @param clazz
     * @return bean
     */
    public static <T> T toBean(final byte[] json, final Class<T> clazz) {

        if (null == json) {
            return null;
        }
        try {
            return mapper.readValue(json, clazz);
        } catch (final Exception e) {
            throw new IllegalArgumentException(e);
        }

    }

    /**
     * JSON->BEAN
     *
     * @param json
     * @param clazz
     * @return bean
     */
    public static <T> T toBean(final String json, final Class<T> clazz) {

        if (null == json) {
            return null;
        }
        try {
            return mapper.readValue(json, clazz);
        } catch (final Exception e) {
            throw new IllegalArgumentException(e);
        }

    }

    /**
     * JSON->Bean
     *
     * @param json
     * @param clazz
     * @return bean
     */
    public static <T> T toBean(final String json, final Class<?> clazz, final Class<?>... parameterClasses) {

        if (null == json) {
            return null;
        }
        try {
            return mapper
                    .readValue(json, mapper.getTypeFactory().constructParametrizedType(clazz, clazz, parameterClasses));
        } catch (final Exception e) {
            throw new IllegalArgumentException(e);
        }

    }

    /**
     * JSON->BEAN
     *
     * @param json
     * @param type
     * @return bean
     */
    public static <T> T toBean(final String json, final TypeReference<T> type) {

        if (null == json) {
            return null;
        }
        try {
            return mapper.readValue(json, type);
        } catch (final Exception e) {
            throw new IllegalArgumentException(e);
        }

    }

    /**
     * Bean->Bean
     *
     * @param source
     * @param target
     * @return target bean
     */
    public static <T> T convertBean(final Object source, final Class<T> target) {

        return toBean(toJson(source), target);
    }


    /**
     * JSON->List
     *
     * @param json
     * @param clazz
     * @return list
     */
    public static <T> List<T> toList(final String json, final Class<T> clazz) {

        if (StringUtils.isEmpty(json)) {
            return null;
        }
        try {
            return mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (final Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
    
    /**
     * Bean->JSON
     *
     * @param bean
     * @return JSON
     */
    public static String toJson(final Object bean) {

        if (bean == null) {
            return null;
        }

        try {
            return mapper.writeValueAsString(bean);
        } catch (final JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }

    }


}