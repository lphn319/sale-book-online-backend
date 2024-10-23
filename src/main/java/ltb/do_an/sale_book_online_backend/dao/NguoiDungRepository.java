package ltb.do_an.sale_book_online_backend.dao;

import ltb.do_an.sale_book_online_backend.entity.NguoiDung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "nguoi-dung")
public interface NguoiDungRepository extends JpaRepository<NguoiDung, Integer> {
    public boolean existsByTenDangNhap(String tenDangNhap);
    public boolean existsByEmail(String email);
    public NguoiDung findByTenDangNhap(String tenDangNhap);
    public NguoiDung findByEmail(String email);
}
