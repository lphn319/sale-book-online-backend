package ltb.do_an.sale_book_online_backend.dao;

import ltb.do_an.sale_book_online_backend.entity.TheLoai;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TheLoaiRepository extends JpaRepository<TheLoai, Integer> {
}
