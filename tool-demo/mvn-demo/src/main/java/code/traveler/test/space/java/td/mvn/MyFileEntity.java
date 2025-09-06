package code.traveler.test.space.java.td.mvn;

import com.oracle.webservices.internal.api.databinding.DatabindingMode;
import jdk.nashorn.internal.objects.annotations.Setter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MyFileEntity {

    String file;

    String groupId;

    String artifactId;

    String version;

    String license;
    
    public String toString() {
            return "{'file':" + this.file + ", 'groupId':" + this.groupId + ", 'artifactId':" + this.artifactId + ", 'version':"
                    + this.version + ", 'license':" + this.license + "}";
    }

}
