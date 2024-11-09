package ltb.do_an.sale_book_online_backend.service.JWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import ltb.do_an.sale_book_online_backend.entity.NguoiDung;
import ltb.do_an.sale_book_online_backend.entity.Quyen;
import ltb.do_an.sale_book_online_backend.service.NguoiDungSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtService {
    public static final String JWT_SECRET = "390920083762374358977823487898923462877578972789239892hhawuisf8978sfjhsfhui2456782354";

    @Autowired
    private NguoiDungSecurityService userService;

    // Tạo JWT dựa trên username và id người dùng
    public String generateToken(String tenDangNhap) {
        Map<String, Object> claims = new HashMap<>();
        NguoiDung nguoiDung = userService.findByUsername(tenDangNhap);

        boolean isAdmin = false;
        boolean isStaff = false;
        boolean isUser = false;

        if (nguoiDung != null && nguoiDung.getDanhSachQuyen().size() > 0) {
            List<Quyen> list = nguoiDung.getDanhSachQuyen();
            for (Quyen q : list) {
                if (q.getTenQuyen().equals("ADMIN")) {
                    isAdmin = true;
                }
                if (q.getTenQuyen().equals("STAFF")) {
                    isStaff = true;
                }
                if (q.getTenQuyen().equals("USER")) {
                    isUser = true;
                }
            }
        }

        if (nguoiDung != null) {
            claims.put("id", nguoiDung.getMaNguoiDung());
        }
        claims.put("isAdmin", isAdmin);
        claims.put("isStaff", isStaff);
        claims.put("isUser", isUser);

        String token = createToken(claims, tenDangNhap);

        // Log thông tin token đã tạo ra
        System.out.println("Generated Token for user " + tenDangNhap + ": " + token);
        System.out.println("Claims: isAdmin=" + isAdmin + ", isStaff=" + isStaff + ", isUser=" + isUser);

        return token;
    }

    // Tạo JWT với các claim đã chọn
    private String createToken(Map<String, Object> claims, String tenDangNhap) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(tenDangNhap)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 30 * 60 * 1000)) // JWT hết hạn sau 30 phút
                .signWith(SignatureAlgorithm.HS256, getSigningKey())
                .compact();
    }

    // Lấy secret key từ mã hóa
    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(JWT_SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Trích xuất tất cả các claims từ JWT
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Trích xuất một claim cụ thể
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Trích xuất ID người dùng từ JWT
    public Integer extractUserId(String token) {
        return extractClaim(token, claims -> claims.get("id", Integer.class));
    }

    // Trích xuất Quyen người dùng từ JWT
    public List<String> extractRoles(String token) {
        return extractClaim(token, claims -> claims.get("roles", List.class));
    }

    public boolean isAdmin(String token) {
        return extractClaim(token, claims -> claims.get("isAdmin", Boolean.class)) != null
                && extractClaim(token, claims -> claims.get("isAdmin", Boolean.class));
    }

    public boolean isStaff(String token) {
        return extractClaim(token, claims -> claims.get("isStaff", Boolean.class)) != null
                && extractClaim(token, claims -> claims.get("isStaff", Boolean.class));
    }

    public boolean isUser(String token) {
        return extractClaim(token, claims -> claims.get("isUser", Boolean.class)) != null
                && extractClaim(token, claims -> claims.get("isUser", Boolean.class));
    }



    // Trích xuất username (subject) từ JWT
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Kiểm tra thời gian hết hạn từ JWT
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Kiểm tra xem JWT có hết hạn không
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Kiểm tra tính hợp lệ của token dựa trên username và trạng thái hết hạn
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
