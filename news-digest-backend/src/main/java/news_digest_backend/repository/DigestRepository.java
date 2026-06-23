package news_digest_backend.repository;

import news_digest_backend.model.Digest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DigestRepository extends JpaRepository<Digest,Long> {
    List<Digest> findByUserId(Long id);
    List<Digest> findByArticleId(Long articleId);
    List<Digest> findByArticleTopicId(Long TopicId);
}
