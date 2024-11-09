package ltb.do_an.sale_book_online_backend.controller;

import com.fasterxml.jackson.databind.JsonNode;
import ltb.do_an.sale_book_online_backend.entity.NguoiDung;
import ltb.do_an.sale_book_online_backend.security.JwtReponse;
import ltb.do_an.sale_book_online_backend.security.LoginRequest;
import ltb.do_an.sale_book_online_backend.service.JWT.JwtService;
import ltb.do_an.sale_book_online_backend.service.nguoidung.TaiKhoanServiceImpl;
import ltb.do_an.sale_book_online_backend.service.NguoiDungSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/tai-khoan")
public class TaiKhoanController {

    @Autowired
    private TaiKhoanServiceImpl taiKhoanService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private NguoiDungSecurityService userService;

    @Autowired
    private JwtService jwtService;
    @Autowired
    private TaiKhoanServiceImpl taiKhoanServiceImpl;

    @PostMapping("/dang-ky")
    public ResponseEntity<?> DangKyNguoiDung(@Validated @RequestBody NguoiDung nguoiDung) {
        ResponseEntity<?> response = taiKhoanService.dangKyNguoiDung(nguoiDung);
        return response;
    }

    @GetMapping("/kich-hoat")
    public ResponseEntity<?> dangKyNguoiDung(@RequestParam String email, @RequestParam String maKichHoat) {
        ResponseEntity<?> response = taiKhoanService.kichHoatTaiKHoan(email, maKichHoat);
        return response;
    }

    @PostMapping("/dang-nhap")
    public ResponseEntity<?> dangNhap(@RequestBody LoginRequest loginRequest) {
        //Xac thuc nguoi dung bang ten dang nhap va mat khau
        try {
            // authentication sẽ giúp ta lấy dữ liệu từ db để kiểm tra
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                            loginRequest.getPassword()));
            //Neu xac thuc thanh cong, tao token JWT
            if (authentication.isAuthenticated()) {
                final String jwt = jwtService.generateToken(loginRequest.getUsername());
                return ResponseEntity.ok(new JwtReponse(jwt));
            }
        } catch (AuthenticationException e){
            //Xac thuc khong thanh cong tra ve thong bao loi
            return ResponseEntity.badRequest().body("Ten dang nhap hoac mat khau khong chinh xac");
        }
        return ResponseEntity.badRequest().body("Xac thuc khong thanh cong");
    }
    @PutMapping(path = "/quen-mat-khau")
    public ResponseEntity<?> quenMatKhau(@RequestBody JsonNode jsonNode) {
        try{
            return taiKhoanServiceImpl.quenMatKhau(jsonNode);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
    @PutMapping(path = "/thay-doi-mat-khau")
    public ResponseEntity<?> thayDoiMatKhau(@RequestBody JsonNode jsonNode) {
        System.out.println(jsonNode);
        try{
            return taiKhoanServiceImpl.thayDoiMatKhau(jsonNode);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
    @PutMapping(path = "/cap-nhat-thong-tin-tai-khoan")
    public ResponseEntity<?> capNhatTaiKhoan(@RequestBody JsonNode jsonNode) {
        try{
            return taiKhoanServiceImpl.capNhatThongTin(jsonNode);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
    @PostMapping(path = "/them-nguoi-dung")
    public ResponseEntity<?> themNguoiDung(@RequestBody JsonNode jsonNode) {
        try{
            return taiKhoanServiceImpl.luuNguoiDung(jsonNode, "them");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
    @PutMapping(path = "/cap-nhat-nguoi-dung")
    public ResponseEntity<?> capNhatNguoiDung(@RequestBody JsonNode jsonNode) {
        try{
            return taiKhoanServiceImpl.luuNguoiDung(jsonNode, "cap-nhat");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping(path = "/xoa-tai-khoan/{maNguoiDung}")
    public ResponseEntity<?> xoaTaiKhoan(@PathVariable int maNguoiDung) {
        try {
            return taiKhoanServiceImpl.xoaNguoiDung(maNguoiDung);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
}
