package news_digest_backend.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "user_topics")
@Data

public class UserTopic {
    @EmbeddedId
    private UserTopicId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("topicId")
    @JoinColumn(name = "topic_id")
    private Topic topic;

}
