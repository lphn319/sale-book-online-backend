package ltb.do_an.sale_book_online_backend.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class NguoiDung {
    private int maNguoiDung;
    private String hoDem;
    private String ten;
    private String tenDangNhap;
    private String matKhau;
    private char gioiTinh;
    private String email;
    private String soDienThoai;
    private String diaChiMuaHang;
    private String diaChiGiaoHang;
    private List<SuDanhGia> danhSachDanhGia;
    private List<SachYeuThich> danhSachYeuThich;
    private List<Quyen> danhSachQuyen;
    private List<DonHang> danhSachDonHang;
}
