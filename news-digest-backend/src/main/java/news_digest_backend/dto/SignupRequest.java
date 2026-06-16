package news_digest_backend.dto;
import lombok.Data;

@Data
public class SignupRequest {
    private String Name;
    private String email;
    private String password;
}
