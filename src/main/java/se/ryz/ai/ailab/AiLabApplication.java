package se.ryz.ai.ailab;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.beans.factory.annotation.Value;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.UserMessage;
import se.ryz.ai.ailab.tools.Tools;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.service.SystemMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@SpringBootApplication(exclude = {
	org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration.class,
	org.springframework.boot.autoconfigure.web.reactive.WebFluxAutoConfiguration.class
})
public class AiLabApplication implements CommandLineRunner {

	//private final TimeTools timeTools;
	private final Assistant assistant;

	public AiLabApplication(@Value("${spring.ai.openai.api-key}") String apiKey, Tools timeTools) {
		//this.timeTools = timeTools;
		
		OpenAiChatModel model = OpenAiChatModel.builder()
		.apiKey(apiKey)
		.build();

		this.assistant = AiServices.builder(Assistant.class)
				.tools(timeTools)
				.chatLanguageModel(model)
				.chatMemory(MessageWindowChatMemory.withMaxMessages(20))
				.build();
	}

	public static void main(String[] args) {
		SpringApplication.run(AiLabApplication.class, args);
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
	@SystemMessage("You are a helpful assistant with a studder")
	String chat(@UserMessage String message);
}
