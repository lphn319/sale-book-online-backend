package ltb.do_an.sale_book_online_backend.dao;

import ltb.do_an.sale_book_online_backend.entity.Quyen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@RepositoryRestResource(path = "quyen")
public interface QuyenRepository extends JpaRepository<Quyen, Integer> {
    public Quyen findByTenQuyen (String tenQuyen);
}
