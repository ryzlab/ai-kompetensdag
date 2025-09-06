package se.ryz.ai.ailab;

import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;
import se.ryz.ai.ailab.tools.Tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@SpringBootApplication(exclude = {
        org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration.class,
        org.springframework.boot.autoconfigure.web.reactive.WebFluxAutoConfiguration.class
})
@Profile("chat")
public class Chat implements CommandLineRunner {
    private final Assistant assistant;

    // Create file application-apikey.properties in resources directory and run with
    // profile 'apikey'
    public Chat(@Value("${spring.ai.openai.api-key}") String apiKey, Tools timeTools) {

        OpenAiChatModel model = OpenAiChatModel.builder()
                .modelName("gpt-4o")
                .apiKey(apiKey)
                .build();

		OllamaChatModel model2 = OllamaChatModel.builder()
				.baseUrl("http://localhost:11434")
				.modelName("llama3.2")
				.build();

        this.assistant = AiServices.builder(Assistant.class)
                .tools(timeTools)
                .chatModel(model)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(20))
                .build();
    }

    public static void main(String[] args) {
        try {
            SpringApplication.run(Chat.class, args);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run(String... args) {
        System.out.println("Chat with AI (type 'exit' to quit):");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        try {
            while (true) {
                System.out.print("You: ");
                String userInput = reader.readLine();

                if ("exit".equalsIgnoreCase(userInput)) {
                    break;
                }

                String response = assistant.chat(userInput);
                System.out.println("AI: " + response);
            }

            reader.close();
            System.out.println("Chat ended. Goodbye!");
        } catch (IOException e) {
            System.err.println("Error reading input: " + e.getMessage());
        }
    }
}


interface Assistant {
    @SystemMessage("You are a helpful assistant")
    String chat(@UserMessage String message);
}
