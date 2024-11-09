package ltb.do_an.sale_book_online_backend.service.donhang;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.ResponseEntity;

public interface DonHangService {
    public ResponseEntity<?> luuDonHang(JsonNode jsonData);
    public ResponseEntity<?> capNhatDonHang(JsonNode jsonData);
    public ResponseEntity<?> huyDonHang(JsonNode jsonData);
}
