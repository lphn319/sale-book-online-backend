package ltb.do_an.sale_book_online_backend.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ChiTietDonHang {
    private int maChiTietDonHang;
    private int soLuong;
    private double giaBan;

    private Sach sach;
}
