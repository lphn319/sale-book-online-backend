package ltb.do_an.sale_book_online_backend.service;

import ltb.do_an.sale_book_online_backend.dao.NguoiDungRepository;
import ltb.do_an.sale_book_online_backend.dao.QuyenRepository;
import ltb.do_an.sale_book_online_backend.entity.NguoiDung;
import ltb.do_an.sale_book_online_backend.entity.Quyen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private NguoiDungRepository nguoiDungRepository;
    private QuyenRepository quyenRepository;

    @Autowired
    public UserServiceImpl(NguoiDungRepository nguoiDungRepository, QuyenRepository quyenRepository) {
        this.nguoiDungRepository = nguoiDungRepository;
        this.quyenRepository = quyenRepository;
    }

    @Override
    public NguoiDung findByUsername(String tenDangNhap) {
        return nguoiDungRepository.findByTenDangNhap(tenDangNhap);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        NguoiDung nguoiDung = findByUsername(username);
        if (nguoiDung == null) {
            throw new UsernameNotFoundException("Tài khoản không tồn tại!");
        }
        return new User(nguoiDung.getTenDangNhap(), nguoiDung.getMatKhau(), rolesToAuthorities(nguoiDung.getDanhSachQuyen()));

    }

    private Collection<? extends GrantedAuthority> rolesToAuthorities(Collection<Quyen> quyens) {
        return quyens.stream().map(quyen -> new SimpleGrantedAuthority(quyen.getTenQuyen())).collect(Collectors.toList());
    }
}
