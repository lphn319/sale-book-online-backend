package ltb.do_an.sale_book_online_backend.service.theloai;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.ResponseEntity;

public interface TheLoaiService {
    public ResponseEntity<?> luu(JsonNode theLoaiJson);
    public ResponseEntity<?> capNhat(JsonNode theLoaiJson);
}
