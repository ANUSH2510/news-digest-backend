package news_digest_backend.repository;

import news_digest_backend.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<Article,Long> {
    Optional<Article> findByUrl(String url);
    List<Article> findByTopicId(Long topicId);
    boolean existsByUrl(String url);
}
