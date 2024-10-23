package ltb.do_an.sale_book_online_backend.service;

import ltb.do_an.sale_book_online_backend.dao.NguoiDungRepository;
import ltb.do_an.sale_book_online_backend.entity.NguoiDung;
import ltb.do_an.sale_book_online_backend.entity.ThongBao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TaiKhoanService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private NguoiDungRepository nguoiDungRepository;
    @Autowired
    private EmailService emailService;

    public ResponseEntity<?> DangKyNguoiDung(NguoiDung nguoiDung) {

        //Kiem tra ten dang nhap da ton tai chua
        if (nguoiDungRepository.existsByTenDangNhap(nguoiDung.getTenDangNhap())) {
            return ResponseEntity.badRequest().body(new ThongBao("Tên đăng nhập đã tồn tại!"));
        }

        //Kiem tra email da ton tai chua
        if (nguoiDungRepository.existsByEmail(nguoiDung.getEmail())) {
            return ResponseEntity.badRequest().body(new ThongBao("Email đã tồn tại!"));
        }

        //Ma hoa mat khau
        String encryptPassword = bCryptPasswordEncoder.encode(nguoiDung.getMatKhau());
        nguoiDung.setMatKhau(encryptPassword);
        // Gán và gửi thông tin kích hoạt

        nguoiDung.setMaKichHoat(taoMaKichHoat());
        nguoiDung.setDaKichHoat(false);

        //Luu nguoi dung vao csdl
        NguoiDung nguoiDungMoi = nguoiDungRepository.save(nguoiDung);

        // Gửi email cho người dùng để họ kích hoạt
        guiEmailKichHoat(nguoiDung.getEmail(), nguoiDung.getMaKichHoat());

        return ResponseEntity.ok("Đăng kí thành công!");
    }
    private String taoMaKichHoat(){
        // Tạo mã ngẫu nhiên
        return UUID.randomUUID().toString();
    }
    private void guiEmailKichHoat(String email, String maKichHoat){
        String subject = "Kích hoạt tài khoản của bạn tại WebBanSach";
        String text = "Vui lòng sử dụng mã sau để kich hoạt cho tài khoản <"+email+">:<html><body><br/><h1>"+maKichHoat+"</h1></body></html>";
        text+="<br/> Click vào đường link để kích hoạt tài khoản: ";
        String url = "http://localhost:3000/kich-hoat/"+email+"/"+maKichHoat;
        text+=("<br/> <a href="+url+">"+url+"</a> ");

        emailService.sendMessage("linhxomgiua2909@gmail.com", email, subject, text);
    }

    public ResponseEntity<?> kichHoatTaiKHoan(String email, String maKichHoat) {
        NguoiDung nguoiDung = nguoiDungRepository.findByEmail(email);

        if (nguoiDung == null) {
            return ResponseEntity.badRequest().body(new ThongBao("Người dùng không tồn tại!"));
        }

        if (nguoiDung.isDaKichHoat()) {
            return ResponseEntity.badRequest().body(new ThongBao("Tài khoản đã được kích hoạt!"));
        }

        if (maKichHoat.equals(nguoiDung.getMaKichHoat())) {
            nguoiDung.setDaKichHoat(true);
            nguoiDungRepository.save(nguoiDung);
            return ResponseEntity.ok("Kích hoạt tài khoản thành công!");
        } else {
            return ResponseEntity.badRequest().body(new ThongBao("Mã kích hoạt không chính xác!"));
        }
    }
}
