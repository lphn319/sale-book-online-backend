package ltb.do_an.sale_book_online_backend.controller;

import com.fasterxml.jackson.databind.JsonNode;
import ltb.do_an.sale_book_online_backend.service.giohang.GioHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chi-tiet-gio-hang")
public class ChiTietGioHangController {
    @Autowired
    private GioHangService gioHangService;

    @PostMapping(path ="/them-san-pham")
    public ResponseEntity<?> them(@RequestBody JsonNode jsonData) {
        try{
            return gioHangService.luu(jsonData);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping(path ="/cap-nhat-san-pham")
    public ResponseEntity<?> capNhat(@RequestBody JsonNode jsonData) {
        try{
            gioHangService.capNhat(jsonData);
            return ResponseEntity.ok("success");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }

    }

    @GetMapping(path ="/lay-tat-ca-gio-hang")
    public long layTatCaGioHang() {
        return gioHangService.layTatCaGioHang();
    }
}
