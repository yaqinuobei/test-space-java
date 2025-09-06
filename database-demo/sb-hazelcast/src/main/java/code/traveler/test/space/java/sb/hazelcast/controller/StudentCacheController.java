

package code.traveler.test.space.java.sb.hazelcast.controller;

import code.traveler.test.space.java.sb.hazelcast.dao.StudentCacheDAO;
import code.traveler.test.space.java.sb.hazelcast.po.StudentPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/v1/cache/student")
public class StudentCacheController {

    @Autowired
    private StudentCacheDAO studentCacheDAO;

    @GetMapping("/{id}")
    public StudentPO getStudent(@PathVariable("id") String id) {
        return studentCacheDAO.getStudentPOById(id);
    }

    @PutMapping("/{id}") //todo 待补充提供临时修改的入口
    public void putStudent(@RequestBody StudentPO student) {
        studentCacheDAO.updateStudentPOById(student);
    }

    @GetMapping("/pilotFeatureConfig")
    public List<StudentPO> getAllStudent() {
        return studentCacheDAO.getAllStudentPOS();
    }

}
