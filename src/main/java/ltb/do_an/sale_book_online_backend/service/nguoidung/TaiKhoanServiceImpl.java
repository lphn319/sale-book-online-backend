package ltb.do_an.sale_book_online_backend.service.nguoidung;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import ltb.do_an.sale_book_online_backend.dao.NguoiDungRepository;
import ltb.do_an.sale_book_online_backend.dao.QuyenRepository;
import ltb.do_an.sale_book_online_backend.entity.NguoiDung;
import ltb.do_an.sale_book_online_backend.entity.Quyen;
import ltb.do_an.sale_book_online_backend.entity.ThongBao;
import ltb.do_an.sale_book_online_backend.service.JWT.JwtService;
import ltb.do_an.sale_book_online_backend.service.email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.sql.Date;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TaiKhoanServiceImpl implements TaiKhoanService{

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private NguoiDungRepository nguoiDungRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private JwtService jwtService;
    private final ObjectMapper objectMapper;
    @Autowired
    private QuyenRepository quyenRepository;


    public TaiKhoanServiceImpl(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public ResponseEntity<?> dangKyNguoiDung(NguoiDung nguoiDung) {

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

        // Xử lý lưu avatar nếu người dùng có chọn ảnh, nếu không thì để null hoặc dùng avatar mặc định
        String avatarBase64 = nguoiDung.getAvatar();  // Lấy avatar base64 từ đối tượng người dùng

        if (avatarBase64 != null && avatarBase64.length() > 500) {
            nguoiDung.setAvatar(avatarBase64);  // Lưu ảnh base64 trực tiếp vào cơ sở dữ liệu
        } else {
            nguoiDung.setAvatar(""); // Nếu không có avatar thì để trống (hoặc sử dụng avatar mặc định)
        }

        // Gán và gửi thông tin kích hoạt
        nguoiDung.setMaKichHoat(taoMaKichHoat());
        nguoiDung.setDaKichHoat(false);

        //Luu nguoi dung vao csdl
        NguoiDung nguoiDungMoi = nguoiDungRepository.save(nguoiDung);

        // Gửi email cho người dùng để họ kích hoạt
        guiEmailKichHoat(nguoiDung.getEmail(), nguoiDung.getMaKichHoat());

        return ResponseEntity.ok("Đăng kí thành công!");
    }

    @Override
    public ResponseEntity<?> luuNguoiDung(JsonNode nguoiDungJson, String option) {
        try {
            NguoiDung nguoiDung = objectMapper.treeToValue(nguoiDungJson, NguoiDung.class);

            // Kiểm tra người dùng đã tồn tại chưa
            if (!option.equals("cap-nhat")){
                if (nguoiDungRepository.existsByTenDangNhap(nguoiDung.getTenDangNhap())){
                    return ResponseEntity.badRequest().body(new ThongBao("Ten dang nhap da ton tai."));
                }

                // Kiểm tra email
                if (nguoiDungRepository.existsByEmail(nguoiDung.getEmail())){
                    return ResponseEntity.badRequest().body(new ThongBao("Email da ton tai."));
                }
            }

            // Chuyển đổi Instant thành java.sql.Date bằng cách lấy milliseconds
            DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT;
            Instant instant = Instant.from(formatter.parse(formatStringByJson(String.valueOf(nguoiDungJson.get("ngaySinh")))));
            java.sql.Date ngaySinh = new java.sql.Date(instant.toEpochMilli());
            nguoiDung.setNgaySinh(ngaySinh);

            // Set quyền cho người dùng
            int maQuyenRequest = Integer.parseInt(String.valueOf(nguoiDungJson.get("quyen")));
            Optional<Quyen> quyen = quyenRepository.findById(maQuyenRequest);
            List<Quyen> danhSachQuyen = new ArrayList<>();
            danhSachQuyen.add(quyen.get());
            nguoiDung.setDanhSachQuyen(danhSachQuyen);

            // Mã hóa mật khẩu
            if (nguoiDung.getMatKhau() != null) {
                String maKhauMaHoa = bCryptPasswordEncoder.encode(nguoiDung.getMatKhau());
                nguoiDung.setMatKhau(maKhauMaHoa);
            } else {
                // Trường hợp không thay đổi mật khẩu
                Optional<NguoiDung> nguoiDungTamThoi = nguoiDungRepository.findById(nguoiDung.getMaNguoiDung());
                nguoiDung.setMatKhau(nguoiDungTamThoi.get().getMatKhau());
            }

            // Xử lý avatar (nếu có)
            String avatarBase64 = formatStringByJson(String.valueOf(nguoiDungJson.get("avatar")));
            if (avatarBase64.length() > 500) {
                nguoiDung.setAvatar(avatarBase64);  // Lưu trực tiếp base64 vào cơ sở dữ liệu
            }

            nguoiDung.setDaKichHoat(true);
            nguoiDungRepository.save(nguoiDung);

            return ResponseEntity.ok(new ThongBao("Lưu thông tin người dùng thành công."));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new ThongBao("Lỗi trong quá trình lưu."));
        }
    }



    @Override
    @Transactional
    public ResponseEntity<?> xoaNguoiDung(int maNguoiDung) {
        try{
            Optional<NguoiDung> nguoiDung = nguoiDungRepository.findById(maNguoiDung);

            if (nguoiDung.isPresent()) {
                // Xóa quyền liên kết với người dùng
                nguoiDungRepository.deleteQuyenByNguoiDungId(maNguoiDung);

                // Sau khi xóa liên kết, xóa người dùng
                nguoiDungRepository.deleteById(maNguoiDung);
            } else {
                // Xử lý trường hợp người dùng không tồn tại
                return ResponseEntity.badRequest().body(new ThongBao("Người dùng không tồn tại!"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok("thành công");
    }

    @Override
    public ResponseEntity<?> thayDoiMatKhau(JsonNode nguoiDungJson) {
        try{
            int maNguoiDung = Integer.parseInt(formatStringByJson(String.valueOf(nguoiDungJson.get("maNguoiDung"))));
            String matKhauMoi = formatStringByJson(String.valueOf(nguoiDungJson.get("matKhauMoi")));
            System.out.println(maNguoiDung);
            System.out.println(matKhauMoi);
            Optional<NguoiDung> user = nguoiDungRepository.findById(maNguoiDung);
            user.get().setMatKhau(bCryptPasswordEncoder.encode(matKhauMoi));
            nguoiDungRepository.save(user.get());
        } catch (Exception e) {
            e.printStackTrace();
            ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    @Override
    @Transactional
    public ResponseEntity<?> thayDoiAvatar(JsonNode nguoiDungJson) {
        try {
            // Lấy thông tin mã người dùng từ JSON
            int maNguoiDung = Integer.parseInt(formatStringByJson(String.valueOf(nguoiDungJson.get("maNguoiDung"))));
            String avatarBase64 = formatStringByJson(String.valueOf(nguoiDungJson.get("avatar")));  // Lấy chuỗi Base64 của ảnh đại diện

            Optional<NguoiDung> user = nguoiDungRepository.findById(maNguoiDung);

            if (user.isPresent()) {
                NguoiDung nguoiDung = user.get();

                // Lưu trực tiếp avatar dưới dạng chuỗi Base64 vào cơ sở dữ liệu
                if (avatarBase64.length() > 500) {  // Đảm bảo avatar không phải là chuỗi rỗng hoặc quá ngắn
                    nguoiDung.setAvatar(avatarBase64);
                } else {
                    return ResponseEntity.badRequest().body(new ThongBao("Ảnh đại diện không hợp lệ."));
                }

                nguoiDungRepository.save(nguoiDung);
                return ResponseEntity.ok(new ThongBao("Thay đổi avatar thành công."));
            } else {
                return ResponseEntity.badRequest().body(new ThongBao("Người dùng không tồn tại."));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new ThongBao("Lỗi trong quá trình thay đổi avatar."));
        }
    }

    @Override
    public ResponseEntity<?> capNhatThongTin(JsonNode nguoiDungJson) {
        try {
            NguoiDung nguoiDungRequest = objectMapper.treeToValue(nguoiDungJson, NguoiDung.class);
            Optional<NguoiDung> user = nguoiDungRepository.findById(nguoiDungRequest.getMaNguoiDung());

            if (user.isPresent()) {
                NguoiDung nguoiDung = user.get();

                // Cập nhật thông tin cá nhân (ngoại trừ mật khẩu)
                nguoiDung.setTen(nguoiDungRequest.getTen());
                nguoiDung.setHoDem(nguoiDungRequest.getHoDem());
                nguoiDung.setEmail(nguoiDungRequest.getEmail());
                nguoiDung.setSoDienThoai(nguoiDungRequest.getSoDienThoai());
                nguoiDung.setDiaChiGiaoHang(nguoiDungRequest.getDiaChiGiaoHang());

                // Cập nhật ngày sinh
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSX");
                Instant instant = Instant.from(formatter.parse(formatStringByJson(String.valueOf(nguoiDungJson.get("ngaySinh")))));
                Date ngaySinh = new Date(Date.from(instant).getTime());
                nguoiDung.setNgaySinh(ngaySinh);

                nguoiDungRepository.save(nguoiDung);
                return ResponseEntity.ok(new ThongBao("Cập nhật thông tin thành công."));
            } else {
                return ResponseEntity.badRequest().body(new ThongBao("Người dùng không tồn tại."));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new ThongBao("Lỗi trong quá trình cập nhật thông tin."));
        }
    }

    @Override
    public ResponseEntity<?> quenMatKhau(JsonNode jsonNode) {
        try {
            String email = formatStringByJson(String.valueOf(jsonNode.get("email")));
            NguoiDung nguoiDung = nguoiDungRepository.findByEmail(email);

            if (nguoiDung == null) {
                return ResponseEntity.badRequest().body(new ThongBao("Email không tồn tại trong hệ thống."));
            }

            // Tạo mật khẩu tạm thời
            String matKhauTamThoi = taoMatKhauTamThoi();
            nguoiDung.setMatKhau(bCryptPasswordEncoder.encode(matKhauTamThoi));
            nguoiDungRepository.save(nguoiDung);

            // Gửi email thông báo mật khẩu mới
            guiEmailMatKhauMoi(nguoiDung.getEmail(), matKhauTamThoi);

            return ResponseEntity.ok(new ThongBao("Mật khẩu tạm thời đã được gửi tới email của bạn."));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new ThongBao("Lỗi trong quá trình xử lý quên mật khẩu."));
        }
    }
    // Tạo mật khẩu tạm thời ngẫu nhiên
    private String taoMatKhauTamThoi() {
        return UUID.randomUUID().toString().substring(0, 10); // Lấy 10 ký tự đầu tiên
    }
    // Gửi email chứa mật khẩu tạm thời
    private void guiEmailMatKhauMoi(String email, String matKhauTamThoi) {
        String subject = "Mật khẩu tạm thời của bạn";
        String text = "Mật khẩu tạm thời của bạn là: <strong>" + matKhauTamThoi + "</strong>";
        text += "<br/>Vui lòng đăng nhập và đổi lại mật khẩu của bạn.";

        try {
            emailService.sendMessage("linhxomgiua2909@gmail.com", email, subject, text);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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

    private String formatStringByJson(String json) {
        return json.replaceAll("\"", "");
    }
}
