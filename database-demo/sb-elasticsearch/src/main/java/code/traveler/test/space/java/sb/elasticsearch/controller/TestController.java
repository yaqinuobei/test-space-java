package code.traveler.test.space.java.sb.elasticsearch.controller;

import code.traveler.test.space.java.sb.elasticsearch.service.IndicesService;
import org.elasticsearch.cluster.metadata.AliasMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/test")
public class TestController {

    @Autowired
    private IndicesService indicesService;

    @GetMapping("/indices")
    public String getIndices() {
        Map<String, List<AliasMetadata>> indicesMetadataMap = indicesService.listIndices();
        return "success";
    }
}
