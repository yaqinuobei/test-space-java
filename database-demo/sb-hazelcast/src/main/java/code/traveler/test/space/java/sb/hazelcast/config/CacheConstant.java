

package code.traveler.test.space.java.sb.hazelcast.config;

public interface CacheConstant {

    interface Config{
        //配置属性名：namespace
        String KUBERNETES_CONFIG_PROPERTY_NAMESPACE = "namespace";

        //配置属性名：service-name
        String KUBERNETES_CONFIG_PROPERTY_SERVICE_NAME = "service-name";
    }

    interface DistributedDataName{

        //student存储名称：类似表名
        String STUDENT_DATA_NAME = "student";
    }

}
