package ltb.do_an.sale_book_online_backend.service.giohang;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.ResponseEntity;

public interface GioHangService {
    public ResponseEntity<?> luu(JsonNode jsonNode);
    public ResponseEntity<?> capNhat(JsonNode jsonNode);
    public long layTatCaGioHang();
}
