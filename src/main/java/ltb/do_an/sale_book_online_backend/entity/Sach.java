package ltb.do_an.sale_book_online_backend.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class Sach {
    private int maSach;
    private String tenSach;
    private String tacGia;
    private String ISBN; //ma sach
    private String moTa;
    private double giaNiemYet;
    private double giaBan;
    private int soLuong;
    private double trungBinhXepHang;

    List<TheLoai> danhSachTheLoai;
    List<HinhAnh> danhSachHinhAnh;
    List<SuDanhGia> danhSachDanhGia;
    List<ChiTietDonHang> danhSachChiTietDonHang;
    List<SachYeuThich> danhSachYeuThich;
}
