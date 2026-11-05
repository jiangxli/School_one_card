package com.project.system.service;
import java.util.*;import java.nio.charset.StandardCharsets;import java.security.*;import javax.crypto.Mac;import javax.crypto.spec.SecretKeySpec;
public final class ApiSecurity{
 private ApiSecurity(){}public static String secret(){byte[] bytes=new byte[32];new SecureRandom().nextBytes(bytes);return Base64.getEncoder().encodeToString(bytes);}
 public static String hex(byte[] b){StringBuilder s=new StringBuilder();for(byte x:b)s.append(String.format("%02x",x&255));return s.toString();}
 public static String hash(String s){try{return hex(MessageDigest.getInstance("SHA-256").digest(s.getBytes(StandardCharsets.UTF_8)));}catch(Exception e){throw new IllegalStateException(e);}}
 public static String sign(String secret,String timestamp,String nonce,String path,String body){try{Mac mac=Mac.getInstance("HmacSHA256");mac.init(new SecretKeySpec(Base64.getDecoder().decode(secret),"HmacSHA256"));return hex(mac.doFinal((timestamp+"\n"+nonce+"\n"+path+"\n"+body).getBytes(StandardCharsets.UTF_8)));}catch(Exception e){throw new IllegalStateException(e);}}
 public static boolean equal(String a,String b){return a!=null&&b!=null&&MessageDigest.isEqual(a.getBytes(StandardCharsets.US_ASCII),b.getBytes(StandardCharsets.US_ASCII));}
}
