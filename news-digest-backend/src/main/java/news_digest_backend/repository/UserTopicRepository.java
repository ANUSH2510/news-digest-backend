package news_digest_backend.repository;

import news_digest_backend.model.UserTopic;
import news_digest_backend.model.UserTopicId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserTopicRepository extends JpaRepository<UserTopic,Long> {
    List<UserTopic> findByUserId(Long id);
}
