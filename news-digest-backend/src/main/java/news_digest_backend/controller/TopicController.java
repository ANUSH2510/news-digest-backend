package news_digest_backend.controller;

import news_digest_backend.model.Topic;
import news_digest_backend.model.User;
import news_digest_backend.model.UserTopic;
import news_digest_backend.model.UserTopicId;
import news_digest_backend.repository.TopicRepository;
import news_digest_backend.repository.UserRepository;
import news_digest_backend.repository.UserTopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/topics")
@CrossOrigin(origins = "*")
public class TopicController {

    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserTopicRepository userTopicRepository;

    @GetMapping
    public ResponseEntity<List<Topic>> getAllTopics() {
        return ResponseEntity.ok(topicRepository.findAll());
    }

    @PostMapping("/select")
    public ResponseEntity<String> selectTopics(@RequestBody List<Long> topicIds, Authentication authentication) {
        String email=authentication.getName();
        User user = userRepository.findByEmail(email).orElseThrow(()-> new RuntimeException("User not found"));

        List<UserTopic> existing =userTopicRepository.findByUserId(user.getId());
        userTopicRepository.deleteAll(existing);

        for(Long topicId:topicIds){
            Topic topic = topicRepository.findById(topicId).orElseThrow(()-> new RuntimeException("Topic not found"));

            UserTopicId id =new UserTopicId();
            id.setUserId(user.getId());
            id.setTopicId(topicId);

            UserTopic userTopic = new UserTopic();
            userTopic.setId(id);
            userTopic.setUser(user);
            userTopic.setTopic(topic);

            userTopicRepository.save(userTopic);
        }
        return ResponseEntity.ok("Successfully selected topics");
    }

}
