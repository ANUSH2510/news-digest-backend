package news_digest_backend.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "digests")
@Data

public class Digest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "article_id")
    private Article article;

    @ManyToOne
    @JoinColumn(name = "user_id" , nullable = true)
    private User user;

    @Column(nullable = false)
    private String summary;

    @Column(name = "createdAt")
    private LocalDateTime createdAt =  LocalDateTime.now();

}
