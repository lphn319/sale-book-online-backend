package ltb.do_an.sale_book_online_backend.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.util.List;

@Data
@Getter
@Setter
public class DonHang {
    private int maDonHang;
    private Date ngayTao;
    private String diaChiMuaHang;
    private String diaChiNhanHang;
    private double tongTien;
    private double chiPhiGiaoHang;
    private double chiPhiThanhToan;
    private List<ChiTietDonHang> danhSachChiTietDonHang;
    private NguoiDung nguoiDung;
    private HinhThucThanhToan hinhThucThanhToan;
    private HinhThucGiaoHang hinhThucGiaoHang;
}
