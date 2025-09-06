

package code.traveler.test.space.java.pure.kafka.support;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.regex.Pattern;

@Component
@Slf4j
public class ApplicationStartupRunner implements ApplicationRunner {

    @Autowired
    private Consumer consumer;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        try {
            Pattern apmTopicPattern = Pattern.compile(".*abc.*");
            consumer.subscribe(apmTopicPattern);
            autoCommitOffset();//�Զ��ύ
            manualCommitOffset();//�ֶ��ύ
        }catch (Exception e){
            log.error("kafka��ʼ��ע�����topic�쳣���쳣��Ϣ��{},�쳣���飺{}",e.getMessage(),e);
        }
    }

    public void autoCommitOffset(){
        //�Զ��ύ
        while (true) {
            // ��Topic����ȡ���ݣ�ÿ1000������ȡһ��
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
            // ÿ����ȡ���ܶ���һ�����ݣ���Ҫ��������
            for (ConsumerRecord<String, String> record : records) {
                System.out.printf("partition = %d, offset = %d, key = %s, value = %s%n",
                        record.partition(), record.offset(), record.key(), record.value());
            }
        }
    }

    public void manualCommitOffset(){
        while (true) {
            // ��Topic����ȡ���ݣ�ÿ1000������ȡһ��
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
            // ÿ����ȡ���ܶ���һ�����ݣ���Ҫ��������
            for (ConsumerRecord<String, String> record : records) {
                try {
                    // ģ�⽫����д�����ݿ�
                    Thread.sleep(1000);
                    System.out.println("save to db...");
                    System.out.printf("partition = %d, offset = %d, key = %s, value = %s%n",
                            record.partition(), record.offset(), record.key(), record.value());
                } catch (Exception e) {
                    // д��ʧ����Ҫ����commit���������൱���𵽻ع������ã�
                    // �´����ѻ��Ǵ�֮ǰ��offset��ʼ����
                    e.printStackTrace();
                    return;
                }
            }
            // д��ɹ������commit��ط���ȥ�ֶ��ύoffset
            consumer.commitAsync();

        }
    }
}
