package ltb.do_an.sale_book_online_backend.service.donhang;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import ltb.do_an.sale_book_online_backend.dao.*;
import ltb.do_an.sale_book_online_backend.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class DonHangServiceImpl implements DonHangService {
    private final ObjectMapper objectMapper;
    private static final Logger logger = Logger.getLogger(DonHangServiceImpl.class.getName());

    @Autowired
    private DonHangRepository donHangRepository;
    @Autowired
    private ChiTietDonHangRepository chiTietDonHangRepository;
    @Autowired
    private NguoiDungRepository nguoiDungRepository;
    @Autowired
    private ChiTietGioHangRepository chiTietGioHangRepository;
    @Autowired
    private SachRepository sachRepository;
    @Autowired
    private HinhThucThanhToanRepository hinhThucThanhToanRepository;

    public DonHangServiceImpl(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    @Transactional
    public ResponseEntity<?> luuDonHang(JsonNode jsonData) {
        try {
            // In thông tin dữ liệu đầu vào
            logger.info("Dữ liệu JSON đầu vào: " + jsonData.toString());

            // Ánh xạ dữ liệu JSON thành đối tượng DonHang
            DonHang donHang = objectMapper.treeToValue(jsonData, DonHang.class);

            // Kiểm tra và gán giá trị "tongTien"
            JsonNode tongTienNode = jsonData.get("tongTien");
            if (tongTienNode != null && !tongTienNode.isNull()) {
                donHang.setTongTien(tongTienNode.asDouble());
            } else {
                logger.warning("Lỗi: Giá trị 'tongTien' không có trong JSON.");
                return ResponseEntity.badRequest().body("Giá trị 'tongTien' bị thiếu");
            }

            donHang.setNgayTao(Date.valueOf(LocalDate.now()));
            donHang.setTrangThai("Đang xử lý");

            // Lấy thông tin người dùng
            JsonNode maNguoiDungNode = jsonData.get("maNguoiDung");
            if (maNguoiDungNode != null && !maNguoiDungNode.isNull()) {
                int maNguoiDung = Integer.parseInt(formatStringByJson(maNguoiDungNode.asText()));
                Optional<NguoiDung> nguoiDungOpt = nguoiDungRepository.findById(maNguoiDung);
                if (nguoiDungOpt.isEmpty()) {
                    logger.warning("Lỗi: Người dùng không tồn tại với mã: " + maNguoiDung);
                    return ResponseEntity.badRequest().body("Người dùng không tồn tại");
                }
                donHang.setNguoiDung(nguoiDungOpt.get());
            } else {
                logger.warning("Lỗi: Giá trị 'maNguoiDung' không có trong JSON.");
                return ResponseEntity.badRequest().body("Giá trị 'maNguoiDung' bị thiếu");
            }

            // Lấy thông tin hình thức thanh toán
            JsonNode maHinhThucThanhToanNode = jsonData.get("maHinhThucThanhToan");
            if (maHinhThucThanhToanNode != null && !maHinhThucThanhToanNode.isNull()) {
                int maHinhThucThanhToan = maHinhThucThanhToanNode.asInt();
                Optional<HinhThucThanhToan> hinhThucThanhToanOpt = hinhThucThanhToanRepository.findById(maHinhThucThanhToan);
                if (hinhThucThanhToanOpt.isEmpty()) {
                    logger.warning("Lỗi: Hình thức thanh toán không tồn tại với mã: " + maHinhThucThanhToan);
                    return ResponseEntity.badRequest().body("Hình thức thanh toán không tồn tại");
                }
                donHang.setHinhThucThanhToan(hinhThucThanhToanOpt.get());
            } else {
                logger.warning("Lỗi: Giá trị 'maHinhThucThanhToan' không có trong JSON.");
                return ResponseEntity.badRequest().body("Giá trị 'maHinhThucThanhToan' bị thiếu");
            }

            // Lưu đơn hàng vào cơ sở dữ liệu
            DonHang donHangMoi = donHangRepository.save(donHang);
            logger.info("Đơn hàng mới đã được lưu: " + donHangMoi.getMaDonHang());

            // Lấy thông tin chi tiết đơn hàng từ JSON
            JsonNode chiTietSachNodes = jsonData.get("sach");
            if (chiTietSachNodes != null && chiTietSachNodes.isArray()) {
                for (JsonNode chiTietNode : chiTietSachNodes) {
                    int soLuong = Integer.parseInt(formatStringByJson(chiTietNode.get("soLuong").asText()));
                    Sach sach = objectMapper.treeToValue(chiTietNode.get("sach"), Sach.class);
                    Optional<Sach> sachOpt = sachRepository.findById(sach.getMaSach());
                    if (sachOpt.isEmpty()) {
                        logger.warning("Lỗi: Sách không tồn tại với mã: " + sach.getMaSach());
                        return ResponseEntity.badRequest().body("Sách không tồn tại");
                    }

                    Sach sachDB = sachOpt.get();
                    if (sachDB.getSoLuong() < soLuong) {
                        logger.warning("Lỗi: Số lượng sách không đủ (yêu cầu: " + soLuong + ", có: " + sachDB.getSoLuong() + ")");
                        return ResponseEntity.badRequest().body("Số lượng sách không đủ");
                    }

                    sachDB.setSoLuong(sachDB.getSoLuong() - soLuong);

                    ChiTietDonHang chiTietDonHang = new ChiTietDonHang();
                    chiTietDonHang.setSach(sachDB);
                    chiTietDonHang.setSoLuong(soLuong);
                    chiTietDonHang.setDonHang(donHangMoi);
                    chiTietDonHang.setGiaBan(sachDB.getGiaBan() * soLuong);

                    // Lưu chi tiết đơn hàng và cập nhật sách
                    chiTietDonHangRepository.save(chiTietDonHang);
                    sachRepository.save(sachDB);
                    logger.info("Chi tiết đơn hàng đã được lưu với mã: " + chiTietDonHang.getMaChiTietDonHang());
                }
            } else {
                logger.warning("Lỗi: Chi tiết sách không tồn tại hoặc không phải mảng.");
                return ResponseEntity.badRequest().body("Chi tiết sách không tồn tại hoặc không phải mảng");
            }

            // Xóa các mục giỏ hàng của người dùng
            chiTietGioHangRepository.deleteByMaNguoiDung(Integer.parseInt(formatStringByJson(maNguoiDungNode.asText())));
            logger.info("Đã xóa các mục giỏ hàng của người dùng với mã: " + maNguoiDungNode.asText());

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Lỗi trong quá trình lưu đơn hàng: " + e.getMessage(), e);
            return ResponseEntity.badRequest().body("Lỗi trong quá trình lưu đơn hàng: " + e.getMessage());
        }
        return ResponseEntity.ok("Đơn hàng được lưu thành công");
    }


    @Override
    @Transactional
    public ResponseEntity<?> capNhatDonHang(JsonNode jsonData) {
        try {
            // Lấy thông tin mã đơn hàng và trạng thái từ dữ liệu JSON
            int maDonHang = Integer.parseInt(formatStringByJson(String.valueOf(jsonData.get("maDonHang"))));
            String trangThai = jsonData.get("trangThai").asText();
            logger.info("Cập nhật đơn hàng mã: " + maDonHang + " với trạng thái: " + trangThai);

            // Kiểm tra sự tồn tại của đơn hàng
            Optional<DonHang> donHangOpt = donHangRepository.findById(maDonHang);
            if (donHangOpt.isEmpty()) {
                logger.warning("Lỗi: Đơn hàng không tồn tại với mã: " + maDonHang);
                return ResponseEntity.badRequest().body("Đơn hàng không tồn tại");
            }

            // Lấy đối tượng đơn hàng từ Optional
            DonHang donHang = donHangOpt.get();
            donHang.setTrangThai(trangThai);

            // Kiểm tra trạng thái và thực hiện hành động phù hợp
            if ("Bị hủy".equalsIgnoreCase(trangThai)) {
                List<ChiTietDonHang> chiTietDonHangs = chiTietDonHangRepository.findChiTietDonHangByDonHang(donHang);
                for (ChiTietDonHang chiTiet : chiTietDonHangs) {
                    Sach sach = chiTiet.getSach();
                    sach.setSoLuong(sach.getSoLuong() + chiTiet.getSoLuong());
                    sachRepository.save(sach);
                    logger.info("Đã hoàn lại số lượng sách với mã: " + sach.getMaSach());
                }
            }

            // Lưu đơn hàng đã cập nhật
            donHangRepository.save(donHang);
            logger.info("Cập nhật đơn hàng thành công mã: " + maDonHang);

        } catch (NumberFormatException e) {
            logger.log(Level.SEVERE, "Lỗi định dạng mã đơn hàng: " + e.getMessage(), e);
            return ResponseEntity.badRequest().body("Lỗi định dạng mã đơn hàng");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Lỗi trong quá trình cập nhật đơn hàng: " + e.getMessage(), e);
            return ResponseEntity.badRequest().body("Lỗi trong quá trình cập nhật đơn hàng: " + e.getMessage());
        }
        return ResponseEntity.ok("Cập nhật đơn hàng thành công");
    }


    @Override
    @Transactional
    public ResponseEntity<?> huyDonHang(JsonNode jsonData) {
        try {
            // Lấy mã người dùng từ JSON
            int maNguoiDung;
            try {
                maNguoiDung = Integer.parseInt(formatStringByJson(String.valueOf(jsonData.get("maNguoiDung"))));
            } catch (NumberFormatException e) {
                logger.log(Level.WARNING, "Lỗi định dạng mã người dùng: " + e.getMessage(), e);
                return ResponseEntity.badRequest().body("Mã người dùng không hợp lệ");
            }

            logger.info("Hủy đơn hàng cho người dùng mã: " + maNguoiDung);

            // Kiểm tra người dùng tồn tại
            Optional<NguoiDung> nguoiDungOpt = nguoiDungRepository.findById(maNguoiDung);
            if (nguoiDungOpt.isEmpty()) {
                logger.warning("Lỗi: Người dùng không tồn tại với mã: " + maNguoiDung);
                return ResponseEntity.badRequest().body("Người dùng không tồn tại");
            }

            // Lấy đơn hàng gần đây nhất
            Optional<DonHang> donHangOpt = donHangRepository.findTopByNguoiDungOrderByMaDonHangDesc(nguoiDungOpt.get());
            if (donHangOpt.isEmpty()) {
                logger.warning("Lỗi: Không tìm thấy đơn hàng gần đây của người dùng mã: " + maNguoiDung);
                return ResponseEntity.badRequest().body("Không tìm thấy đơn hàng gần đây của người dùng");
            }

            DonHang donHang = donHangOpt.get();

            // Cập nhật trạng thái đơn hàng
            donHang.setTrangThai("Bị hủy");
            logger.info("Đã cập nhật trạng thái đơn hàng mã: " + donHang.getMaDonHang() + " thành 'Bị hủy'");

            // Hoàn lại số lượng sách trong chi tiết đơn hàng
            List<ChiTietDonHang> chiTietDonHangs = chiTietDonHangRepository.findChiTietDonHangByDonHang(donHang);
            for (ChiTietDonHang chiTiet : chiTietDonHangs) {
                Sach sach = chiTiet.getSach();
                sach.setSoLuong(sach.getSoLuong() + chiTiet.getSoLuong());
                sachRepository.save(sach);
                logger.info("Đã hoàn lại số lượng sách với mã: " + sach.getMaSach());
            }

            // Lưu lại đơn hàng sau khi cập nhật
            donHangRepository.save(donHang);
            logger.info("Hủy đơn hàng thành công mã: " + donHang.getMaDonHang());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Lỗi trong quá trình hủy đơn hàng: " + e.getMessage(), e);
            return ResponseEntity.badRequest().body("Lỗi trong quá trình hủy đơn hàng: " + e.getMessage());
        }
        return ResponseEntity.ok("Đơn hàng đã bị hủy thành công");
    }



    private String formatStringByJson(String json) {
        return json.replaceAll("\"", "");
    }
}
