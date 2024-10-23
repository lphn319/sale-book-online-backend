package ltb.do_an.sale_book_online_backend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "chi_tiet_gio_hang")
public class ChiTietGioHang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_gio_hang")
    private long maGioHang;

    @Column(name = "so_luong")
    private int soLuong;

    @ManyToOne
    @JoinColumn(name = "ma_sach", nullable = false)
    private Sach sach;

    @ManyToOne
    @JoinColumn(name = "ma_nguoi_dung", nullable = false)
    private NguoiDung nguoiDung;


}
