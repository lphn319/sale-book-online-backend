package ltb.do_an.sale_book_online_backend.service;

import ltb.do_an.sale_book_online_backend.entity.NguoiDung;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    public NguoiDung findByUsername(String tenDangNhap);
}
