package ltb.do_an.sale_book_online_backend.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class HinhAnh {
    private int maHinhAnh;
    private String tenHinhAnh;
    private boolean laIcon;
    private String duongDan;
    private String duLieuAnh;
    private Sach sach;
}
