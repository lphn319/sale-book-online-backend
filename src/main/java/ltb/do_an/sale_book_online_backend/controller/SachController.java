package ltb.do_an.sale_book_online_backend.controller;

import com.fasterxml.jackson.databind.JsonNode;
import ltb.do_an.sale_book_online_backend.service.sach.SachService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sach")
public class SachController {

    @Autowired
    private SachService sachService;

    @PostMapping(path = "/them-sach")
    public ResponseEntity<?> luu(@RequestBody JsonNode jsonData) {
        try {
            return sachService.luu(jsonData);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage() + "LOI ROI");
            return ResponseEntity.badRequest().build();
        }
    }
    @PutMapping(path = "/cap-nhat-sach")
    public ResponseEntity<?> capNhat(@RequestBody JsonNode jsonData) {
        try {
            return sachService.capNhat(jsonData);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage() + "LOI ROI");
            return ResponseEntity.badRequest().build();
        }
    }
    @GetMapping(path = "/lay-tat-ca-sach")
    public long layTatCaSach() {
        return sachService.layTatCaSach();
    }
}
