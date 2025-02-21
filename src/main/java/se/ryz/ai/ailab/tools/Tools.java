package se.ryz.ai.ailab.tools;

import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class Tools {
    @Tool("Get the current date and time")
    public String getCurrentTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return now.format(formatter);
    }

    @Tool("Create a timer given a specific date and time")
    public String setTimer(String dateTime) {
        System.out.println("Setting timer for " + dateTime);
        return "Timer set for " + dateTime;
    }

    @Tool("Turn on or off the given switch")
    public String turnSwitch(String switchName, String onOff) {
        System.out.println("Turning switch " + switchName + " " + onOff);
        return "Switch " + switchName + " turned " + onOff;
    }
} 