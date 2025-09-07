package code.traveler.test.space.java.mcp.client.controller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AudioModelOptions {

    private String message;

    private String model;
}
