package ltb.do_an.sale_book_online_backend.service.sach;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.ResponseEntity;

public interface SachService {
    public ResponseEntity<?> luu(JsonNode sachJson);
    public ResponseEntity<?> capNhat(JsonNode sachJson);
    public long layTatCaSach();
}
