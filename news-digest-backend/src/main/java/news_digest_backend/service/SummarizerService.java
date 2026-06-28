package news_digest_backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import news_digest_backend.model.Article;
import news_digest_backend.model.Digest;
import news_digest_backend.repository.ArticleRepository;
import news_digest_backend.repository.DigestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;

@Service
public class SummarizerService {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private DigestRepository digestRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${groq.api.key}")
    private String groqApiKey;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void summarizeAllArticles() {
        List<Article> articles = articleRepository.findAll();

        for (Article article : articles) {
            try {
                List<Digest> existing = digestRepository
                        .findByArticleId(article.getId());
                if (!existing.isEmpty()) {
                    continue;
                }

                String summary = callGroqApi(article.getTitle());

                if (summary != null && !summary.isEmpty()) {
                    Digest digest = new Digest();
                    digest.setArticle(article);
                    digest.setSummary(summary);
                    digestRepository.save(digest);
                    System.out.println("Summarized: " + article.getTitle());
                }

                Thread.sleep(3000);

            } catch (Exception e) {
                System.err.println("Error summarizing article: "
                        + article.getId() + " - " + e.getMessage());
            }
        }
        System.out.println("Summarization complete.");
    }

    private String callGroqApi(String articleTitle) throws Exception {
        String url = "https://api.groq.com/openai/v1/chat/completions";

        String escapedTitle = articleTitle.replace("\"", "\\\"").replace("\n", " ");

        String requestBody = """
        {
          "model": "llama-3.3-70b-versatile",
          "messages": [
            {
              "role": "user",
              "content": "Summarize this news article title in 3-4 clear sentences. Just give the summary, no extra text: %s"
            }
          ],
          "max_tokens": 150
        }
        """.formatted(escapedTitle);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(groqApiKey);

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.POST, entity, String.class);

        JsonNode root = objectMapper.readTree(response.getBody());
        return root.path("choices")
                .path(0)
                .path("message")
                .path("content")
                .asText();
    }
}