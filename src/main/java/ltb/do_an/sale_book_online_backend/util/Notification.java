package ltb.do_an.sale_book_online_backend.util;

public class Notification {
    private String message;

    // Constructor nhận một tham số kiểu String
    public Notification(String message) {
        this.message = message;
    }

    // Getter cho thuộc tính message
    public String getMessage() {
        return message;
    }

    // Setter cho thuộc tính message (tùy chọn, nếu cần thay đổi message sau khi tạo đối tượng)
    public void setMessage(String message) {
        this.message = message;
    }
}
