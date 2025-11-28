package com.datta.amazon.util;

import java.nio.charset.StandardCharsets;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class CryptoUtil {
	public static String hmacSha256Hex(String data, String secret) {
		try {
			Mac hmac = Mac.getInstance("HmacSHA256");
			SecretKeySpec keySpec = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
			hmac.init(keySpec);
			byte[] raw = hmac.doFinal(data.getBytes(StandardCharsets.UTF_8));
			StringBuilder sb = new StringBuilder(2 * raw.length);
			for (byte b : raw) {
				sb.append(String.format("%02x", b & 0xff));
			}
			return sb.toString();
		} catch (Exception ex) {
			throw new RuntimeException("Failed to calculate HMAC SHA256", ex);
		}
	}
}

//public boolean verifyPayment(String orderId, String paymentId, String signature) {
//    String secret = config.getSecret();
//
//    try {
//        String payload = orderId + "|" + paymentId;
//        String expected = Utils.calculateRFC2104HMAC(payload, secret);
//
//        return expected.equals(signature);
//    } catch (Exception e) {
//        return false;
//    }
//}
