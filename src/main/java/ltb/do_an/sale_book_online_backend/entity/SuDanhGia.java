package ltb.do_an.sale_book_online_backend.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class SuDanhGia {
    private long maDanhGia;
    private float diemXepHang;
    private String nhanXet;

    private NguoiDung nguoiDung;
    private Sach sach;
}
