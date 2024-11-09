package ltb.do_an.sale_book_online_backend.service.email;

public interface EmailService {
    public void sendMessage(String from, String to, String subject, String text);
}
