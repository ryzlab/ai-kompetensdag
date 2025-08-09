package se.ryz.ai.ailab;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.milvus.MilvusEmbeddingStore;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessRead;
import org.apache.pdfbox.io.RandomAccessReadBuffer;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication(exclude = {
        org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration.class,
        org.springframework.boot.autoconfigure.web.reactive.WebFluxAutoConfiguration.class
})
@Profile("index")
public class IndexDocument implements CommandLineRunner {

    @Value("${spring.ai.openai.api-key}")
    private String apiKey;

    // Create file application-apikey.properties in resources directory and run with
    // profile 'apikey'
    public IndexDocument() {
    }

    public static void main(String[] args) {
        try {
            SpringApplication.run(IndexDocument.class, args);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run(String... args) throws IOException {
        if (args.length != 1) {
            System.out.println("Please provide a document as the only argument");
            System.exit(1);
        }
        String filePath = args[0];

        // 1. Extrahera text från PDF
        String text = extractText(filePath);

        System.out.println("Indexing '%s'".formatted(text));
        // 2. Dela upp i chunkar
        List<String> chunks = chunkTextWithOverlap(text, 1000, 200); // t.ex. 1000 tecken per chunk

        // 3. Initiera embeddingmodell (t.ex. OpenAI)
        var embeddingModel = new OpenAiEmbeddingModel.OpenAiEmbeddingModelBuilder()
                .apiKey(apiKey)
                .modelName("text-embedding-3-small")
                .dimensions(384)
                .build();

        // 4. Initiera Milvus-anslutning
        EmbeddingStore embeddingStore = MilvusEmbeddingStore.builder()
                .host("localhost")
                .port(19530)
                .dimension(384)
                .collectionName("demo_collection")
                .build();

        // 5. Skapa och spara embeddingar
        for (String chunk : chunks) {
            TextSegment segment = TextSegment.from(chunk);
            var embedding = embeddingModel.embed(chunk);
            embeddingStore.add(embedding.content(), segment);
        }

        System.out.println("Finished indexing");
        System.exit(0);
    }

    /**
     * Splits text into chunks with overlap
     *
     * @param text Texten to splig.
     * @param chunkSize Max size of each chunk (ex. 1000 characters).
     * @param overlap Number of characters that overlaps between chunks (ex. 200 characters).
     * @return List of chunks.
     */
    public static List<String> chunkTextWithOverlap(String text, int chunkSize, int overlap) {
        List<String> chunks = new ArrayList<>();

        if (chunkSize <= overlap) {
            throw new IllegalArgumentException("Chunk size must be greater than overlap.");
        }

        int start = 0;
        int textLength = text.length();

        while (start < textLength) {
            int end = Math.min(start + chunkSize, textLength);
            String chunk = text.substring(start, end);
            chunks.add(chunk);

            // Next starting point: Move forward chunkSize - overlap
            start += (chunkSize - overlap);
        }

        return chunks;
    }


    private String extractText(String pdfPath) throws IOException {
        InputStream inputStream = new FileInputStream(pdfPath);
        RandomAccessRead read = new RandomAccessReadBuffer(inputStream);
        PDFParser parser = new PDFParser(read);

        // Get the document that was parsed.
        COSDocument cosDoc = parser.parse().getDocument();

        // This class will take a pdf document and strip out all of the text and
        // ignore the formatting and such.
        PDFTextStripper pdfStripper = new PDFTextStripper();

        // This is the in-memory representation of the PDF document
        PDDocument pdDoc = new PDDocument(cosDoc);
        pdfStripper.setStartPage(1);
        pdfStripper.setEndPage(pdDoc.getNumberOfPages());

        // This will return the text of a document.
        var statementPDF = pdfStripper.getText(pdDoc);
        return statementPDF;
    }
}


