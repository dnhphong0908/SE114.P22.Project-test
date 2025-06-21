package com.se114p12.backend.configs;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Configuration
@Data
public class VnPayConfig {
    @Value("${vnpay.tmnCode:DUMMY_CODE}")
    private String vnp_TmnCode;

    @Value("${vnpay.hashSecret:DUMMY_SECRET}")
    private String vnp_HashSecret;

    @Value("${vnpay.payUrl:https://sandbox.vnpay.vn/paymentv2/vpcpay.html}")
    private String vnp_PayUrl;

    @Value("${vnpay.returnUrl:http://localhost:8080/api/v1/payment/return}")
    private String vnp_ReturnUrl;

    public String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty()) {
            ip = request.getRemoteAddr();
        } else {
            ip = ip.split(",")[0]; // Nếu có nhiều IP trong chuỗi
        }
        return ip;
    }

    public String hmacSHA512(String key, String data) {
        try {
            Mac hmac = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
            hmac.init(secretKeySpec);
            byte[] bytes = hmac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder hash = new StringBuilder();
            for (byte b : bytes)
                hash.append(String.format("%02x", b));
            return hash.toString();
        } catch (Exception e) {
            throw new RuntimeException("Lỗi tạo chữ ký HMAC", e);
        }
    }
}
