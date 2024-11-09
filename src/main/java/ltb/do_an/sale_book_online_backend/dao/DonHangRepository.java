package ltb.do_an.sale_book_online_backend.dao;

import ltb.do_an.sale_book_online_backend.entity.DonHang;
import ltb.do_an.sale_book_online_backend.entity.NguoiDung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RepositoryRestResource(path = "don-hang")
public interface DonHangRepository extends JpaRepository<DonHang, Integer> {
    // Phương thức truy vấn đơn giản
    Optional<DonHang> findTopByNguoiDungOrderByMaDonHangDesc(NguoiDung nguoiDung);

    // HOẶC sử dụng truy vấn tùy chỉnh
    @Query("SELECT d FROM DonHang d WHERE d.nguoiDung = :nguoiDung ORDER BY d.maDonHang DESC")
    Optional<DonHang> findLatestOrderByNguoiDung(@Param("nguoiDung") NguoiDung nguoiDung);
}
