package ltb.do_an.sale_book_online_backend.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import ltb.do_an.sale_book_online_backend.service.theloai.TheLoaiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/the-loai")
public class TheLoaiController {

    @Autowired
    private TheLoaiService theLoaiService;

    // Thêm thể loại mới
    @PostMapping("/them-the-loai")
    public ResponseEntity<?> luuTheLoai(@RequestBody JsonNode theLoaiJson) {
        return theLoaiService.luu(theLoaiJson);
    }

    // Cập nhật thể loại theo mã thể loại
    @PutMapping("/cap-nhat-the-loai/{maTheLoai}")
    public ResponseEntity<?> capNhatTheLoai(@PathVariable int maTheLoai, @RequestBody JsonNode theLoaiJson) {
        // Gắn mã thể loại vào JSON để service xử lý
        ((ObjectNode) theLoaiJson).put("maTheLoai", maTheLoai);
        return theLoaiService.capNhat(theLoaiJson);
    }
}
