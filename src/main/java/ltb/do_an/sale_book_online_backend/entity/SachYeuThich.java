package ltb.do_an.sale_book_online_backend.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class SachYeuThich {
    private int maSachYeuThich;
    private NguoiDung nguoiDung;
    private Sach sach;
}
