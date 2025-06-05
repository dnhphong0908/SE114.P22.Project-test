package com.se114p12.backend.controllers.payment;

import com.se114p12.backend.services.payment.VnPayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "VNPAY Payment Module", description = "Endpoints for integrating VNPAY payment")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payment")
public class VnPayController {

    private final VnPayService vnPayService;

    @Operation(summary = "Create VnPay payment", description = "Generate a payment URL for VnPay")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment URL created successfully", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input parameters", content = @Content)
    })
    @GetMapping("/create-payment")
    public String createPayment(
            @Parameter(description = "Order ID to pay for") @RequestParam Long orderId,
            @Parameter(description = "Amount of the order") @RequestParam Double amount,
            HttpServletRequest request
    ) throws Exception {
        String ipAddress = request.getRemoteAddr();
        return vnPayService.createPaymentUrl(orderId, amount, "Thanh toan don hang " + orderId, ipAddress);
    }

    @Operation(summary = "Handle VNPAY return", description = "Handle return callback from VnPay after user completes payment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Result of payment process", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @GetMapping("/return")
    public String handleReturn(
            @Parameter(description = "All return parameters from VnPay") @RequestParam Map<String, String> params
    ) {
        String secureHash = params.get("vnp_SecureHash");
        boolean isValid = vnPayService.verifyPayment(params, secureHash);

        if (isValid) {
            String responseCode = params.get("vnp_ResponseCode");
            if ("00".equals(responseCode)) {
                return "Thanh toán thành công đơn hàng #" + params.get("vnp_TxnRef");
            } else {
                return "Thanh toán thất bại: Mã lỗi " + responseCode;
            }
        } else {
            return "Sai chữ ký! Không xác thực được dữ liệu.";
        }
    }
}