package ltb.do_an.sale_book_online_backend.controller;

import ltb.do_an.sale_book_online_backend.entity.NguoiDung;
import ltb.do_an.sale_book_online_backend.security.JwtReponse;
import ltb.do_an.sale_book_online_backend.security.LoginRequest;
import ltb.do_an.sale_book_online_backend.service.JwtService;
import ltb.do_an.sale_book_online_backend.service.TaiKhoanService;
import ltb.do_an.sale_book_online_backend.service.UserService;
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
    private TaiKhoanService taiKhoanService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    //@CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/dang-ky")
    public ResponseEntity<?> DangKyNguoiDung(@Validated @RequestBody NguoiDung nguoiDung) {
        ResponseEntity<?> response = taiKhoanService.DangKyNguoiDung(nguoiDung);
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
}
