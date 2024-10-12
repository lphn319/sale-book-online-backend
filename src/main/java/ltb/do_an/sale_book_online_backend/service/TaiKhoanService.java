package ltb.do_an.sale_book_online_backend.service;

import ltb.do_an.sale_book_online_backend.dao.NguoiDungRepository;
import ltb.do_an.sale_book_online_backend.entity.NguoiDung;
import ltb.do_an.sale_book_online_backend.entity.ThongBao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class TaiKhoanService {

    @Autowired
    private NguoiDungRepository nguoiDungRepository;

    public ResponseEntity<?> DangKyNguoiDung(NguoiDung nguoiDung) {
        //Kiem tra ten dang nhap da ton tai chua
        if (nguoiDungRepository.existsByTenDangNhap(nguoiDung.getTenDangNhap())) {
            return ResponseEntity.badRequest().body(new ThongBao("Tên đăng nhập đã tồn tại!"));
        }
        //Kiem tra email da ton tai chua
        if (nguoiDungRepository.existsByEmail(nguoiDung.getEmail())) {
            return ResponseEntity.badRequest().body(new ThongBao("Email đã tồn tại!"));
        }
        //Luu nguoi dung vao csdl
        NguoiDung nguoiDungMoi = nguoiDungRepository.save(nguoiDung);
        return ResponseEntity.ok("Đăng kí thành công!");
    }
}
