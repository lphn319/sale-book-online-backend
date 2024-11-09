package ltb.do_an.sale_book_online_backend.service.sach;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import ltb.do_an.sale_book_online_backend.dao.HinhAnhRepository;
import ltb.do_an.sale_book_online_backend.dao.SachRepository;
import ltb.do_an.sale_book_online_backend.dao.TheLoaiRepository;
import ltb.do_an.sale_book_online_backend.entity.Sach;
import ltb.do_an.sale_book_online_backend.entity.TheLoai;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.type.TypeReference;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SachServiceImpl implements SachService {
    private final ObjectMapper objectMapper;
    @Autowired
    private SachRepository sachRepository;
    @Autowired
    private TheLoaiRepository theLoaiRepository;
    @Autowired
    private HinhAnhRepository hinhAnhRepository;


    public SachServiceImpl(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }


    @Override
    public ResponseEntity<?> luu(JsonNode sachJson) {
        try {
            System.out.println("JSON trước khi ánh xạ: " + sachJson.toString());
            Sach sach = objectMapper.treeToValue(sachJson, Sach.class);
            System.out.println("Đối tượng Sach sau khi ánh xạ: " + sach.toString());

            // Lưu danh sách thể loại
            List<Integer> danhSachMaTheLoai = objectMapper.readValue(sachJson.get("danhSachMaTheLoai").traverse(), new TypeReference<List<Integer>>() {});
            List<TheLoai> danhSachTheLoai = new ArrayList<>();
            for (int maTheLoai : danhSachMaTheLoai) {
                Optional<TheLoai> theLoai = theLoaiRepository.findById(maTheLoai);
                if (theLoai.isPresent()) {
                    danhSachTheLoai.add(theLoai.get());
                } else {
                    System.out.println("Không tìm thấy thể loại với mã: " + maTheLoai);
                    return ResponseEntity.badRequest().body("Thể loại với mã " + maTheLoai + " không tồn tại");
                }
            }
            sach.setDanhSachTheLoai(danhSachTheLoai);

            // Lưu sách
            sachRepository.save(sach);
            return ResponseEntity.ok("Lưu sách thành công!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Lỗi trong quá trình lưu sách: " + e.getMessage());
        }
    }


    @Override
    @Transactional
    public ResponseEntity<?> capNhat(JsonNode sachJson) {
        try {
            System.out.println("JSON trước khi ánh xạ: " + sachJson.toString());
            Sach sach = objectMapper.treeToValue(sachJson, Sach.class);
            System.out.println("Đối tượng Sach sau khi ánh xạ: " + sach.toString());

            // Lưu danh sách thể loại
            List<Integer> danhSachMaTheLoai = objectMapper.readValue(sachJson.get("danhSachMaTheLoai").traverse(), new TypeReference<List<Integer>>() {});
            List<TheLoai> danhSachTheLoai = new ArrayList<>();
            for (int maTheLoai : danhSachMaTheLoai) {
                Optional<TheLoai> theLoai = theLoaiRepository.findById(maTheLoai);
                if (theLoai.isPresent()) {
                    danhSachTheLoai.add(theLoai.get());
                } else {
                    System.out.println("Không tìm thấy thể loại với mã: " + maTheLoai);
                    return ResponseEntity.badRequest().body("Thể loại với mã " + maTheLoai + " không tồn tại");
                }
            }
            sach.setDanhSachTheLoai(danhSachTheLoai);

            // Lưu sách
            sachRepository.save(sach);
            return ResponseEntity.ok("Cập nhật sách thành công!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Lỗi trong quá trình cập nhật sách: " + e.getMessage());
        }
    }


    @Override
    public long layTatCaSach() {
        return sachRepository.count();
    }

    private String formatStringByJson(String json) {
        return json.replaceAll("\"", "");
    }
}
