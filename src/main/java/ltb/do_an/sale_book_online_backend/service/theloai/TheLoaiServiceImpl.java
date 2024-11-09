package ltb.do_an.sale_book_online_backend.service.theloai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ltb.do_an.sale_book_online_backend.dao.TheLoaiRepository;
import ltb.do_an.sale_book_online_backend.entity.TheLoai;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class TheLoaiServiceImpl implements TheLoaiService {

    private final ObjectMapper objectMapper;
    private static final Logger logger = Logger.getLogger(TheLoaiServiceImpl.class.getName());

    @Autowired
    private TheLoaiRepository theLoaiRepository;

    public TheLoaiServiceImpl(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public ResponseEntity<?> luu(JsonNode theLoaiJson) {
        try {
            // Chuyển dữ liệu JSON thành đối tượng TheLoai
            TheLoai theLoai = objectMapper.treeToValue(theLoaiJson, TheLoai.class);

            // Lưu thể loại vào cơ sở dữ liệu
            TheLoai theLoaiMoi = theLoaiRepository.save(theLoai);
            logger.info("Thể loại mới đã được lưu: " + theLoaiMoi.getMaTheLoai());

            return ResponseEntity.ok(theLoaiMoi);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Lỗi trong quá trình lưu thể loại: " + e.getMessage(), e);
            return ResponseEntity.badRequest().body("Lỗi trong quá trình lưu thể loại: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> capNhat(JsonNode theLoaiJson) {
        try {
            // Lấy mã thể loại từ JSON và kiểm tra sự tồn tại của nó trong cơ sở dữ liệu
            int maTheLoai = theLoaiJson.get("maTheLoai").asInt();
            Optional<TheLoai> theLoaiOpt = theLoaiRepository.findById(maTheLoai);
            if (theLoaiOpt.isEmpty()) {
                logger.warning("Không tìm thấy thể loại với mã: " + maTheLoai);
                return ResponseEntity.badRequest().body("Không tìm thấy thể loại với mã: " + maTheLoai);
            }

            // Cập nhật thông tin thể loại
            TheLoai theLoaiHienTai = theLoaiOpt.get();
            theLoaiHienTai.setTenTheLoai(theLoaiJson.get("tenTheLoai").asText());

            // Lưu cập nhật vào cơ sở dữ liệu
            TheLoai theLoaiDaCapNhat = theLoaiRepository.save(theLoaiHienTai);
            logger.info("Thể loại đã được cập nhật: " + theLoaiDaCapNhat.getMaTheLoai());

            return ResponseEntity.ok(theLoaiDaCapNhat);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Lỗi trong quá trình cập nhật thể loại: " + e.getMessage(), e);
            return ResponseEntity.badRequest().body("Lỗi trong quá trình cập nhật thể loại: " + e.getMessage());
        }
    }
}
