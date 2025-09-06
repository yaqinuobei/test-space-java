package code.traveler.test.space.java.sb.app.bo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PreachingActivity {

    private String order;

    private String date;

    private String title;

    private boolean isAllocated;

    private boolean isFake;
}
