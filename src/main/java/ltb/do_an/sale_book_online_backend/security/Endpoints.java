package ltb.do_an.sale_book_online_backend.security;

public class Endpoints {
    public static final String front_end_host = "http://localhost:3000";
    public static final String[] PUBLIC_GET_ENDPOINT = {
            "/sach",
            "/sach/**",
            "/hinh-anh",
            "/su-danh-gia/**",
            "/nguoi-dung/**",
            "/nguoi-dung/search/existsByTenDangNhap",
            "/nguoi-dung/search/existsByEmail",
            "/tai-khoan/kich-hoat",
            // Add endpoint giỏ hàng nếu không yêu cầu xác thực
            // Nếu giỏ hàng công khai cho xem không cần xác thực
            "/quyen",
            "/quyen/**",
            "/the-loai/**",
            "/the-loai/",
            "/chi-tiet-gio-hang/",
            "/chi-tiet-don-hang/",
            "/chi-tiet-don-hang/**",
            "/chi-tiet-gio-hang/**",
            "/chi-tiet-gio-hang/lay-tat-ca-gio-hang",
            "/don-hang/**"
    };
    public static final String[] PUBLIC_POST_ENDPOINTS = {
            "/tai-khoan/dang-ky",
            "/tai-khoan/dang-nhap",
            // Nếu cần thêm sản phẩm vào giỏ hàng mà không yêu cầu xác thực
            "/chi-tiet-gio-hang/them-san-pham",
            "/don-hang/**",
            "/the-loai/**",
            "/the-loai/",
    };
    public static final String[] PUBLIC_PUT_ENDPOINTS = {
            "/don-hang/cap-nhat-don-hang",
            "/don-hang//huy-don-hang",
            "/chi-tiet-gio-hang/cap-nhat-san-pham",
            "/the-loai/**",
            "/the-loai/",
    };
    public static final String[] PUBLIC_DElETE_ENDPOINTS = {
            "/tai-khoan/xoa-tai-khoan/**",
    };
    public static final String[] ADMIN_GET_ENDPOINTS = {
            "/nguoi-dung",
            "/nguoi-dung/**",
            "/sach",
            "/sach/**",
//            "/quyen",
//            "/quyen/**",
            "/don-hang/**"

    };
    public static final String[] ADMIN_POST_ENDPOINTS = {
            "/nguoi-dung",
            "/nguoi-dung/**",
            "/sach/them-sach",
            "/tai-khoan/them-nguoi-dung",
            "/sach/**",
            "/sach",
    };
    public static final String[] ADMIN_PUT_ENDPOINTS = {
            "/nguoi-dung",
            "/nguoi-dung/**",
            "/sach/cap-nhat-sach",
            "/sach/**",
            "/sach",
            "/tai-khoan/cap-nhat-nguoi-dung",
    };
    public static final String[] ADMIN_DELETE_ENDPOINTS = {
            "/nguoi-dung",
            "/nguoi-dung/**",
            "/sach/xoa-sach",
            "/tai-khoan/xoa-tai-khoan/**",
            "/tai-khoan/xoa-tai-khoan/{maNguoiDung}",
            "/tai-khoan/**",
            "/sach/**",
            "/the-loai/**",
            "/the-loai/",
    };
}
