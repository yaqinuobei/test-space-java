package code.traveler.test.space.java.sb.app.bo;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PreachingPerson {

    private String name;

    private PreachingActivity pa;

    private boolean isPreaching;
}
