package ltb.do_an.sale_book_online_backend.service.sach;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import ltb.do_an.sale_book_online_backend.dao.HinhAnhRepository;
import ltb.do_an.sale_book_online_backend.dao.SachRepository;
import ltb.do_an.sale_book_online_backend.dao.TheLoaiRepository;
import ltb.do_an.sale_book_online_backend.entity.Sach;
import ltb.do_an.sale_book_online_backend.entity.TheLoai;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.type.TypeReference;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SachServiceImpl implements SachService {
    private final ObjectMapper objectMapper;
    @Autowired
    private SachRepository sachRepository;
    @Autowired
    private TheLoaiRepository theLoaiRepository;
    @Autowired
    private HinhAnhRepository hinhAnhRepository;


    public SachServiceImpl(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }


    @Override
    public ResponseEntity<?> luu(JsonNode sachJson) {
        try {
            Sach sach = objectMapper.treeToValue(sachJson, Sach.class);

            //Luu the loai cua sach
            List<Integer> danhSachMaTheLoai = objectMapper.readValue(sachJson.get("maTheLoai").traverse(), new TypeReference<List<Integer>>() {
            });
            List<TheLoai> danhSachTheLoai = new ArrayList<>();
            for (int maTheLoai : danhSachMaTheLoai) {
                Optional<TheLoai> theLoai = theLoaiRepository.findById(maTheLoai);
                danhSachTheLoai.add(theLoai.get());
            }
            sach.setDanhSachTheLoai(danhSachTheLoai);

            //Luu sach
            sachRepository.save(sach);
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Override
    @Transactional
    public ResponseEntity<?> capNhat(JsonNode sachJson) {
        try {
            Sach sach = objectMapper.treeToValue(sachJson, Sach.class);
            //Luu the loai cua sach
            List<Integer> danhSachMaTheLoai = objectMapper.readValue(sachJson.get("maTheLoai").traverse(), new
                    TypeReference<List<Integer>>() {});
            List<TheLoai> danhSachTheLoai = new ArrayList<>();
            for (int maTheLoai : danhSachMaTheLoai) {
                Optional<TheLoai> theLoai = theLoaiRepository.findById(maTheLoai);
                danhSachTheLoai.add(theLoai.get());
            }
            sachRepository.save(sach);

            return ResponseEntity.ok("Success!");

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();

        }
    }

    @Override
    public long layTatCaSach() {
        return sachRepository.count();
    }

    private String formatStringByJson(String json) {
        return json.replaceAll("\"", "");
    }
}
