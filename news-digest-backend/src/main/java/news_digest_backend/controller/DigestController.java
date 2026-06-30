package news_digest_backend.controller;

import news_digest_backend.model.Digest;
import news_digest_backend.model.User;
import news_digest_backend.model.UserTopic;
import news_digest_backend.repository.DigestRepository;
import news_digest_backend.repository.UserRepository;
import news_digest_backend.repository.UserTopicRepository;
import news_digest_backend.service.SchedularService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/digest")
@CrossOrigin(origins = "*")

public class DigestController {

    @Autowired
    private DigestRepository digestRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserTopicRepository userTopicRepository;

    @GetMapping
    public ResponseEntity<List<Digest>> getUserDigests(Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElseThrow(()-> new RuntimeException("User not found"));

        List<UserTopic> userTopics = userTopicRepository.findByUserId(user.getId());

        List<Digest> feed = new ArrayList<>();
        for (UserTopic userTopic : userTopics) {
            Long topicId = userTopic.getTopic().getId();
            List<Digest> topicDigests = digestRepository.findByArticleTopicId(topicId);
            feed.addAll(topicDigests);
        }
        return ResponseEntity.ok(feed);
    }
    @Autowired
    private SchedularService schedularService;

    @GetMapping("/test/trigger-digest")
    public String triggerDigest() {
        schedularService.fetchNewsDaily();
        return "Digest generation triggered manually";
    }
}
