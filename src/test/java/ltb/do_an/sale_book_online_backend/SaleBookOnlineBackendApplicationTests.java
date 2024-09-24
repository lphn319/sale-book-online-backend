package ltb.do_an.sale_book_online_backend;

import ltb.do_an.sale_book_online_backend.entity.TheLoai;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SaleBookOnlineBackendApplicationTests {

	@Test
	void contextLoads() {
		TheLoai theLoai = new TheLoai();
		theLoai.setMaTheLoai(1);
		theLoai.setTenTheLoai("Kinh di");

	}

}
