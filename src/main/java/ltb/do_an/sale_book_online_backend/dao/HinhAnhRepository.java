package ltb.do_an.sale_book_online_backend.dao;

import jakarta.transaction.Transactional;
import ltb.do_an.sale_book_online_backend.entity.HinhAnh;
import ltb.do_an.sale_book_online_backend.entity.Sach;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.List;

@RepositoryRestResource(path = "hinh-anh")
public interface HinhAnhRepository extends JpaRepository<HinhAnh, Integer> {
    public List<HinhAnh> findBySach(Sach sach);
    @Modifying
    @Transactional
    @Query("DELETE FROM HinhAnh ha WHERE ha.laIcon = false AND ha.sach.maSach = :maSach")
    public void deleteHinhAnhWithFalseIconByMaSach(@Param("maSach") int maSach);
}
