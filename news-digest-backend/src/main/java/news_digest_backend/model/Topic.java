package news_digest_backend.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Topics")
@Data

public class Topic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false , unique = true)
    private String name;

}
