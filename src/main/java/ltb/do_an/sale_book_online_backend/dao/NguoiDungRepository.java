package ltb.do_an.sale_book_online_backend.dao;

import ltb.do_an.sale_book_online_backend.entity.NguoiDung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(excerptProjection = NguoiDung.class, path = "nguoi-dung")
public interface NguoiDungRepository extends JpaRepository<NguoiDung, Integer> {
    public boolean existsByTenDangNhap(String tenDangNhap);
    public boolean existsByEmail(String email);
    public NguoiDung findByTenDangNhap(String tenDangNhap);
    public NguoiDung findByEmail(String email);
    @Modifying
    @Query(value = "DELETE FROM nguoidung_quyen WHERE ma_nguoi_dung = :maNguoiDung", nativeQuery = true)
    void deleteQuyenByNguoiDungId(@Param("maNguoiDung") int maNguoiDung);

    @Modifying
    @Query(value = "DELETE FROM nguoi_dung WHERE ma_nguoi_dung = :maNguoiDung", nativeQuery = true)
    void deleteById(@Param("maNguoiDung") int maNguoiDung);
}
