package ltb.do_an.sale_book_online_backend;

import ltb.do_an.sale_book_online_backend.dao.ChiTietDonHangRepository;
import ltb.do_an.sale_book_online_backend.entity.ChiTietDonHang;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SaleBookOnlineBackendApplicationTests {

    @Autowired
    private ChiTietDonHangRepository chiTietDonHangRepository;

	@Test
	void contextLoads() {
		ChiTietDonHang chiTietDonHang = new ChiTietDonHang();
		chiTietDonHangRepository.saveAndFlush(chiTietDonHang);
	}

}
