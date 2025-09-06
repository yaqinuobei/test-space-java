

package code.traveler.test.space.java.sb.hazelcast.dao;

import code.traveler.test.space.java.sb.hazelcast.po.StudentPO;

import java.util.List;


public interface StudentCacheDAO {

    StudentPO getStudentPOById(String id);

    void updateStudentPOById(StudentPO studentPO);

    List<StudentPO> getAllStudentPOS();

}
