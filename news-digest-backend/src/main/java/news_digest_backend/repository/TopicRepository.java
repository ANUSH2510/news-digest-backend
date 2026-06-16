package news_digest_backend.repository;

import news_digest_backend.model.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TopicRepository extends JpaRepository<Topic,Long> {
   Optional<Topic> findByName(String name);
   boolean existsByName(String name);
}
