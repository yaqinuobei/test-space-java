package code.traveler.test.space.java.mcp.client.controller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatPlatformAndModelOptions {

    private String username;

    private String message;

    private String model;

    private String platform;

    private Double temperature;
}
