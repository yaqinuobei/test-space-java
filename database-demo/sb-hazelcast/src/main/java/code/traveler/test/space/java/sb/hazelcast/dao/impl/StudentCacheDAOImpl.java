

package code.traveler.test.space.java.sb.hazelcast.dao.impl;

import code.traveler.test.space.java.sb.hazelcast.dao.StudentCacheDAO;
import code.traveler.test.space.java.sb.hazelcast.po.StudentPO;
import com.hazelcast.core.HazelcastInstance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@DependsOn("hazelcastInstance")
@Slf4j
public class StudentCacheDAOImpl implements StudentCacheDAO {

    private HazelcastInstance hazelcastInstance;

    private Map<String, StudentPO> studentPOMap;

    @Autowired
    public StudentCacheDAOImpl(@Qualifier("hazelcastInstance") final HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
        studentPOMap = hazelcastInstance.getMap("student");
    }

    @Override
    public StudentPO getStudentPOById(String id) {
        return studentPOMap.get(id);
    }

    @Override
    public void updateStudentPOById(StudentPO studentPO) {
        studentPOMap.put(studentPO.getId(), studentPO);
    }

    @Override
    public List<StudentPO> getAllStudentPOS() {
        return studentPOMap
                .values()
                .stream()
                .collect(Collectors.toList());
    }
}
