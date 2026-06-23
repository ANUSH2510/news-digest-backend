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

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void summarizeAllArticles() {
        List<Article> articles = articleRepository.findAll();
        int count = 0;

        for(Article article : articles) {
            if(count >= 10) break;
            try{
                List<Digest> existing = digestRepository.findByArticleId(article.getId());
                if(!existing.isEmpty()){
                    continue;
                }
                String summary = callGeminiApi(article.getTitle());

                if(summary != null && !summary.isEmpty()){
                    Digest digest = new Digest();
                    digest.setArticle(article);
                    digest.setSummary(summary);
                    digestRepository.save(digest);
                    count++;
                }
                Thread.sleep(10000);
            }catch (Exception e) {
                System.err.println("Error in summarizing article: "+article.getTitle()+ "-"+e.getMessage());
            }
        }
    }
    private String callGeminiApi(String articleTitle) throws Exception{
        String url ="https://generativelanguage.googleapis.com/v1beta/"+"models/gemini-2.0-flash:generateContent?key="+geminiApiKey;

        String requestBody = """
                {
                  "contents":[{
                    "parts":[{
                       "text":"summarize this news article title in 3-4 clear sentences.just give the summary,no extra text:%s"
                    }]
                  }]
                }
                """.formatted(articleTitle);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        JsonNode root =objectMapper.readTree(response.getBody());
        return root.path("candidates")
                .path(0)
                .path("content")
                .path("parts")
                .path(0)
                .path("text")
                .asText();
    }
}
