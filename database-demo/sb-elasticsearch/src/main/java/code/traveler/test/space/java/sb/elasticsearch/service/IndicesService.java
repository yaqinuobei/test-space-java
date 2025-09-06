package code.traveler.test.space.java.sb.elasticsearch.service;

import org.elasticsearch.cluster.metadata.AliasMetadata;

import java.util.List;
import java.util.Map;

public interface IndicesService {

    Map<String, List<AliasMetadata>> listIndices();
}
