package ltb.do_an.sale_book_online_backend.controller;

import com.fasterxml.jackson.databind.JsonNode;
import ltb.do_an.sale_book_online_backend.service.donhang.DonHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/don-hang")
public class DonHangController {

    @Autowired
    private DonHangService donHangService;

    @PostMapping("/them-don-hang")
    public ResponseEntity<?> luuDonHang(@RequestBody JsonNode jsonData) {
        try {
            return donHangService.luuDonHang(jsonData);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Lỗi khi thêm đơn hàng: " + e.getMessage());
        }
    }

    @PutMapping("/cap-nhat-don-hang")
    public ResponseEntity<?> capNhatDonHang(@RequestBody JsonNode jsonData) {
        try {
            return donHangService.capNhatDonHang(jsonData);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Lỗi khi cập nhật đơn hàng: " + e.getMessage());
        }
    }

    @PutMapping("/huy-don-hang")
    public ResponseEntity<?> huyDonHang(@RequestBody JsonNode jsonNode) {
        try {
            return donHangService.huyDonHang(jsonNode);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Lỗi khi hủy đơn hàng: " + e.getMessage());
        }
    }
}
