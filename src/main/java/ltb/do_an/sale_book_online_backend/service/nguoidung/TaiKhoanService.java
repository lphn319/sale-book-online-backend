package ltb.do_an.sale_book_online_backend.service.nguoidung;

import com.fasterxml.jackson.databind.JsonNode;
import ltb.do_an.sale_book_online_backend.entity.NguoiDung;
import org.springframework.http.ResponseEntity;

public interface TaiKhoanService {
    public ResponseEntity<?> dangKyNguoiDung(NguoiDung nguoiDung);
    public ResponseEntity<?> luuNguoiDung(JsonNode nguoiDungJson, String option);
    public ResponseEntity<?> xoaNguoiDung(int maNguoiDung);
    public ResponseEntity<?> thayDoiMatKhau(JsonNode nguoiDungJson);
    public ResponseEntity<?> thayDoiAvatar(JsonNode nguoiDungJson);
    public ResponseEntity<?> capNhatThongTin(JsonNode nguoiDungJson);
    public ResponseEntity<?> quenMatKhau(JsonNode jsonNode);
}
