package ltb.do_an.sale_book_online_backend.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class Quyen {
    private int maQuyen;
    private String tenQuyen;
    List<NguoiDung> danhSachNguoiDung;
}
