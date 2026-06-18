package news_digest_backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import news_digest_backend.model.Article;
import news_digest_backend.model.Topic;
import news_digest_backend.repository.ArticleRepository;
import news_digest_backend.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class NewsService {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Value("${newsapi.key}")
    private String newsApiKey;

    private final ObjectMapper objectMapper = new ObjectMapper();
    public void fetchAndSaveArticle(){
        List<Topic> topics = topicRepository.findAll();

        for(Topic topic : topics){
            try{
                String url = "https://newsapi.org/v2/everything?q="+topic.getName()+"&pageSize=10"+"&apiKey="+newsApiKey;
                System.out.println("Calling URL"+url);

                String response = restTemplate.getForObject(url, String.class);
                JsonNode root =objectMapper.readTree(response);
                JsonNode articles = root.get("articles");

                for(JsonNode articleNode : articles){
                    String articleUrl = articleNode.get("url").asText();

                    if(articleRepository.existsByUrl(articleUrl)){
                        continue;
                    }

                    Article article = new Article();
                    article.setTitle(articleNode.get("title").asText());
                    article.setUrl(articleUrl);
                    article.setSource(articleNode.get("source").get("name").asText());
                    article.setTopic(topic);
                    article.setFetchedAt(LocalDateTime.now());

                    String publishedAt = articleNode.get("publishedAt").asText();
                    if(publishedAt != null && !publishedAt.equals("null")){
                        article.setPublishedAt(LocalDateTime.parse(publishedAt.replace("Z","")));
                    }
                    articleRepository.save(article);
                }
            }catch (Exception e){
                System.err.println("Error fetching news for topic:"+topic.getName()+" - " +  e.getMessage());
            }
        }
    }
}
