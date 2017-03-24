package com.onlyhiedu.mobile.Utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encrypt
{  
  
  /**
   * 传入文本内容，返回 SHA-512 串 
   *  
   * @param strText 
   * @return 
   */  
  public static String SHA512(final String strText)
  {  
    return SHA(strText, "SHA-512");  
  }  
  
  /** 
   * 字符串 SHA 加密 
   *  
   * @param strType
   * @return 
   */
  public static String SHA(final String strText, final String strType)
  {  
    String strResult = null;
    if (strText != null && strText.length() > 0)
    {  
      try  
      {  
        MessageDigest messageDigest = MessageDigest.getInstance(strType);
        messageDigest.update(strText.getBytes());
        byte byteBuffer[] = messageDigest.digest();
  
        StringBuffer strHexString = new StringBuffer();
        for (int i = 0; i < byteBuffer.length; i++)
        {  
          String hex = Integer.toHexString(0xff & byteBuffer[i]);  
          if (hex.length() == 1)  
          {  
            strHexString.append('0');  
          }  
          strHexString.append(hex);  
        }  
        strResult = strHexString.toString();
      }  
      catch (NoSuchAlgorithmException e)
      {  
        e.printStackTrace();  
      }  
    }  
  
    return strResult;  
  }


  public static String md5hex(byte[] s) {
    MessageDigest messageDigest = null;
    try {
      messageDigest = MessageDigest.getInstance("MD5");
      messageDigest.update(s);
      return hexlify(messageDigest.digest());
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
      return "";
    }
  }

  public static String hexlify(byte[] data) {
    char[] DIGITS_LOWER = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * 用于建立十六进制字符的输出的大写字符数组
     */
    char[] DIGITS_UPPER = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    char[] toDigits = DIGITS_LOWER;
    int l = data.length;
    char[] out = new char[l << 1];
    // two characters form the hex value.
    for (int i = 0, j = 0; i < l; i++) {
      out[j++] = toDigits[(0xF0 & data[i]) >>> 4];
      out[j++] = toDigits[0x0F & data[i]];
    }
    return String.valueOf(out);

  }

}  