package ltb.do_an.sale_book_online_backend.service.giohang;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ltb.do_an.sale_book_online_backend.dao.ChiTietGioHangRepository;
import ltb.do_an.sale_book_online_backend.dao.NguoiDungRepository;
import ltb.do_an.sale_book_online_backend.entity.ChiTietGioHang;
import ltb.do_an.sale_book_online_backend.entity.NguoiDung;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GioHangServiceImpl implements GioHangService {
    private final ObjectMapper objectMapper;

    public GioHangServiceImpl(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
    @Autowired
    public NguoiDungRepository nguoiDungRepository;

    @Autowired
    ChiTietGioHangRepository chiTietGioHangRepository;

    @Override
    public ResponseEntity<?> luu(JsonNode jsonData) {
        try {
            int maNguoiDung = 0;
            //Danh sach item cua data vua truyen
            List<ChiTietGioHang> danhSachChiTietGioHangData = new ArrayList<>();
            for (JsonNode jsonNode : jsonData) {
                ChiTietGioHang chiTietGioHangData = objectMapper.treeToValue(jsonNode, ChiTietGioHang.class);
                maNguoiDung = Integer.parseInt(formatStringByJson(String.valueOf(jsonNode.get("maNguoiDung"))));
                danhSachChiTietGioHangData.add(chiTietGioHangData);
            }
            Optional<NguoiDung> nguoiDung = nguoiDungRepository.findById(maNguoiDung);
            if (!nguoiDung.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Người dùng không tồn tại");
            }

            //Danh sach item cua user
            List<ChiTietGioHang> danhSachChiTietGioHang = nguoiDung.get().getDanhSachChiTietGioHang();

            //Lap qua tung item va xu ly
            for (ChiTietGioHang chiTietGioHangData : danhSachChiTietGioHangData) {
                boolean isHad = false;
                for (ChiTietGioHang chiTietGioHang: danhSachChiTietGioHang){
                    //Neu trong cart cua user co item do roi thi update lai so luong
                    if (chiTietGioHang.getSach().getMaSach() == chiTietGioHangData.getSach().getMaSach()){
                        chiTietGioHang.setSoLuong(chiTietGioHang.getSoLuong()+chiTietGioHangData.getSoLuong());
                        isHad = true;
                        break;
                    }
                }
                //Neu chua co thi them moi item do
                if (!isHad){
                    ChiTietGioHang chiTietGioHang = new ChiTietGioHang();
                    chiTietGioHang.setNguoiDung(nguoiDung.get());
                    chiTietGioHang.setSoLuong(chiTietGioHangData.getSoLuong());
                    chiTietGioHang.setSach(chiTietGioHangData.getSach());
                    danhSachChiTietGioHang.add(chiTietGioHang);
                }
                nguoiDung.get().setDanhSachChiTietGioHang(danhSachChiTietGioHang);
                NguoiDung nguoiDungMoi = nguoiDungRepository.save(nguoiDung.get());

                if (danhSachChiTietGioHangData.size() == 1){
                    List<ChiTietGioHang> danhSachChiTietGioHangTamThoi = nguoiDungMoi.getDanhSachChiTietGioHang();
                    return ResponseEntity.ok(danhSachChiTietGioHangTamThoi.get(danhSachChiTietGioHang.size()-1).getMaGioHang());
                }
            }
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @Override
    public ResponseEntity<?> capNhat(JsonNode jsonData) {
        try {
            int maChiTietGioHang = Integer.parseInt(formatStringByJson(String.valueOf(jsonData.get("maChiTietGioHang"))));
            int soLuong = Integer.parseInt(formatStringByJson(String.valueOf(jsonData.get("soLuong"))));
            Optional<ChiTietGioHang> chiTietGioHang = chiTietGioHangRepository.findById((long) maChiTietGioHang);
            chiTietGioHang.get().setSoLuong(soLuong);
            chiTietGioHangRepository.save(chiTietGioHang.get());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @Override
    public long layTatCaGioHang() {
        return chiTietGioHangRepository.count();
    }

    private String formatStringByJson(String json) {
        return json.replaceAll("\"", "");
    }
}
