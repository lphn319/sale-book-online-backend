package ltb.do_an.sale_book_online_backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Data
@Getter
@Setter
@Table(name = "hinh_thuc_thanh_toan")
public class HinhThucThanhToan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_hinh_thuc_thanh_toan")
    private int maHinhThucThanhToan;

    @Column(name = "ten_hinh_thuc_thanh_toan")
    private String tenHinhThucThanhToan;

    @Column(name = "mo_ta", columnDefinition = "text")
    private String moTa;

    @Column(name = "chi_phi_thanh_toan")
    private double chiPhiThanhToan;

    @OneToMany(mappedBy = "hinhThucThanhToan", fetch = FetchType.LAZY, cascade = {
            CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH
    })
    private List<DonHang> danhSachDonHang;
}
