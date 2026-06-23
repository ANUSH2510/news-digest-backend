package news_digest_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.SQLOutput;

@Service
public class SchedularService {
    @Autowired
    private NewsService newsService;

    @Autowired
    private SummarizerService summarizerService;

    @Scheduled(fixedRate = 86400000)
    public void fetchNewsDaily(){
        System.out.println("Schedular triggered - fetching news...");
        newsService.fetchAndSaveArticle();
        System.out.println("news fetch complete...");

       // System.out.println("Starting summarization...");
       //summarizerService.summarizeAllArticles();
        //System.out.println("Summarization complete.");
    }
}
