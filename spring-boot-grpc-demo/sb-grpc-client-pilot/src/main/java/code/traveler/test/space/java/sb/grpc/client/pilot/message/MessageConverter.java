

package code.traveler.test.space.java.sb.grpc.client.pilot.message;

/**
 * @author original
 */
public interface MessageConverter<IN, OUT> {

    OUT toMessage(IN message);

}
