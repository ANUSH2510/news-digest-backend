package news_digest_backend.model;

import jakarta.persistence.Embeddable;
import lombok.Data;
import java.io.Serializable;

@Embeddable
@Data

public class UserTopicId implements Serializable {
    private Long userId;
    private Long topicId;
}
