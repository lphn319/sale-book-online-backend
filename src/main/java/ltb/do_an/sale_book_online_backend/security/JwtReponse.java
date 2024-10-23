package ltb.do_an.sale_book_online_backend.security;

public class JwtReponse {
    private final String jwt;

    public JwtReponse(String jwt) {
        this.jwt = jwt;
    }

    public String getJwt() {
        return jwt;
    }
}
