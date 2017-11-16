package net.sunny.web.italker.push.utils;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {
    public static void main(String[] args) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String pass = "appid=wxd930ea5d5a258f4f&body=test&device_info=1000&mch_id=10000100&nonce_str=ibuaiVcKdpRxkhJA&key=192006250b4c09247ec02edce69f6a2d";
        System.out.println(HashUtil.getMD5String(pass));
    }
}
