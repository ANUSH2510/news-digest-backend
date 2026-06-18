package news_digest_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class SchedularService {
    @Autowired
    private NewsService newsService;

    @Scheduled(fixedRate = 86400000)
    public void fetchNewsDaily(){
        System.out.println("Schedular triggered - fetching news...");
        newsService.fetchAndSaveArticle();
        System.out.println("news fetch complete...");
    }
}
