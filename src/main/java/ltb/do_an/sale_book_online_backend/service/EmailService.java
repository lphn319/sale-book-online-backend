package ltb.do_an.sale_book_online_backend.service;

public interface EmailService {
    public void sendMessage(String from, String to, String subject, String text);
}
