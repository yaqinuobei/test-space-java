

package code.traveler.test.space.java.sb.hazelcast.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentPO implements Serializable {

    private String id;

    private String name;
}
