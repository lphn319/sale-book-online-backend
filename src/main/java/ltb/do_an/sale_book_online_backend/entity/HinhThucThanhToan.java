package ltb.do_an.sale_book_online_backend.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class HinhThucThanhToan {
    private int maHinhThucGiaoHang;
    private String tenHinhThucGiaoHang;
    private String moTa;
    private double chiPhiGiaoHang;
    private List<DonHang> danhSachDonHang;
}
