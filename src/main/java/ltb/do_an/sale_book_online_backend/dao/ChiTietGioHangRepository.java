package ltb.do_an.sale_book_online_backend.dao;

import jakarta.transaction.Transactional;
import ltb.do_an.sale_book_online_backend.entity.ChiTietGioHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.List;

@RepositoryRestResource(path = "chi-tiet-gio-hang")
public interface ChiTietGioHangRepository extends JpaRepository<ChiTietGioHang, Long> {
    @Modifying
    @Transactional
    @Query("DELETE FROM ChiTietGioHang gh WHERE gh.nguoiDung.maNguoiDung = :maNguoiDung")
    public void deleteByMaNguoiDung(@Param("maNguoiDung") int maNguoiDung);

    long count();

}
