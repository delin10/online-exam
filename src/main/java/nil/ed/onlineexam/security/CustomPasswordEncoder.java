package nil.ed.onlineexam.security;

import nil.ed.onlineexam.aop.annotation.MethodInvokeLog;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.Md5Crypt;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.MessageDigest;

public class CustomPasswordEncoder implements PasswordEncoder {
    @Override
    @MethodInvokeLog
    public String encode(CharSequence rawPassword) {
        return DigestUtils.md5Hex(rawPassword.toString());
    }

    @MethodInvokeLog
    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encodedPassword.equals(encode(rawPassword));
    }
}