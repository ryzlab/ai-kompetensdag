package se.ryz.ai.ailab.tools;

import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.data.image.Image;
import dev.langchain4j.model.image.ImageModel;
import dev.langchain4j.model.openai.OpenAiImageModel;
import dev.langchain4j.model.openai.OpenAiImageModelName;
import dev.langchain4j.model.output.Response;
import groovy.util.logging.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

@Slf4j
@Component
public class Tools {
    @Value("${spring.ai.openai.api-key}")
    private String apiKey;


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
        System.out.println("Turning switch '" + switchName + "' '" + onOff + "'");
        return "Switch " + switchName + " turned " + onOff;
    }

    @Tool("Generate an image given a prompt and save it to a file with a given name")
    public String generateImage(String prompt, String fileName) {
        System.out.println("Generating image for prompt: " + prompt + ", downloading to " + fileName);
        ImageModel model = OpenAiImageModel.builder()
                .apiKey(apiKey)
                .modelName(OpenAiImageModelName.DALL_E_3)
                .build();

        Response<Image> response = model.generate(prompt);
        System.out.println(response.content().url());
        try {
            downloadFile(response.content().url(), fileName);
        } catch (IOException e) {
            System.out.println("Unable to download file %s".format(fileName));
            e.printStackTrace();
        }
        return "Image generated for prompt: " + prompt;
    }

    @Tool("When asked to describe an image, use this tool to create a base-64 representation of the given file")
    public String describeImage(String fileName) {
        System.out.println("Describing...");
        return "The image to describe: " + encodeImageToBase64(fileName);
    }

    public static void downloadFile(URI url, String destinationFilePath) throws IOException {
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.toURL().openConnection();
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.setConnectTimeout(5000);
        httpURLConnection.setReadTimeout(5000);

        // Check for successful response code
        if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            // Create an InputStream to read the file data
            try (InputStream inputStream = httpURLConnection.getInputStream()) {
                // Create a Path object for the destination file
                Path destinationPath = Path.of(destinationFilePath);
                // Copy the input stream to the destination file
                Files.copy(inputStream, destinationPath, StandardCopyOption.REPLACE_EXISTING);
            }
        } else {
            System.out.println("Failed to download file: " + httpURLConnection.getResponseCode());
        }

        // Disconnect the connection
        httpURLConnection.disconnect();
    }

    private static String encodeImageToBase64(String filePath) {
        try {
            File file = new File(filePath);
            BufferedImage image = ImageIO.read(file);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "jpg", baos);
            byte[] imageBytes = baos.toByteArray();
            return Base64.getEncoder().encodeToString(imageBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

} 
