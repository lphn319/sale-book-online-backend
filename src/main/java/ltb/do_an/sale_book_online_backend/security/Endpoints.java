package ltb.do_an.sale_book_online_backend.security;

public class Endpoints {
    public static final String front_end_host = "http://localhost:3000";
    public static final String[] PUBLIC_GET_ENDPOINT = {
            "/sach",
            "/sach/**",
            "/hinh-anh",
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
            "/chi-tiet-gio-hang/**",
            "/chi-tiet-gio-hang/lay-tat-ca-gio-hang"
    };
    public static final String[] PUBLIC_POST_ENDPOINTS = {
            "/tai-khoan/dang-ky",
            "/tai-khoan/dang-nhap",
            // Nếu cần thêm sản phẩm vào giỏ hàng mà không yêu cầu xác thực
            "/chi-tiet-gio-hang/them-san-pham"
    };
    public static final String[] PUBLIC_PUT_ENDPOINTS = {
            "/sach/cap-nhat-sach",
            "/chi-tiet-gio-hang/cap-nhat-san-pham"
    };
    public static final String[] ADMIN_GET_ENDPOINTS = {
            "/nguoi-dung",
            "/nguoi-dung/**",
            "/sach",
            "/sach/**",
    };
    public static final String[] ADMIN_POST_ENDPOINTS = {
            "/sach/them-sach",
    };
    public static final String[] ADMIN_PUT_ENDPOINTS = {
            "/sach/cap-nhat-sach",
    };
    public static final String[] ADMIN_DELETE_ENDPOINTS = {
            "/sach/xoa-sach"
    };
}
