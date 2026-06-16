package news_digest_backend.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "articles")
@Data

public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false ,  unique = true)
    private String url;

    @Column
    private String source;

    @ManyToOne
    @JoinColumn(name = "topic_id")
    private Topic topic;

    @Column(name = "PublishedAt")
    private LocalDateTime publishedAt;

    @Column(name = "FetchedAt")
    private LocalDateTime fetchedAt = LocalDateTime.now();
}
