package code.traveler.test.space.java.pure.grpc.lib.constant;

import io.grpc.CallOptions;
import io.grpc.Context;
import io.grpc.Metadata;

import static io.grpc.Metadata.ASCII_STRING_MARSHALLER;

public interface GrpcConstant {

     Metadata.Key<String> METADATE_KEY_SNUM  = Metadata.Key.of("sNum", ASCII_STRING_MARSHALLER);

     CallOptions.Key<String> CALLOPTIONS_KEY_SNUM = CallOptions.Key.create("sNum");

     Context.Key<String> CONTEXT_KEY_SNUM = Context.key("sNum");
}
