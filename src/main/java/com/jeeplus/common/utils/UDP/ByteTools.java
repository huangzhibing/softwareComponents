package com.jeeplus.common.utils.UDP;


/**
 * @author Administrator
 *
 */
public class ByteTools {
    
	
	/**
	 * @author Administrator @date 2017-7-26
	 * @note 复制指定下标且指定长度的字节
	 * @return void
	 * @param src		数据源
	 * @param dest		目标数据
	 * @param index		起始下标
	 * @param n
	 */
	public static void copyBytes(byte[] src, byte[] dest, int index, int n)
	{
		for (int i = 0; i < n && i < dest.length; ++i)
		{
			dest[i] = src[i + index];
		}
	}

	/**
	 * @author Administrator @date 2017-7-26
	 * @note 复制指定长度的字节
	 * @return void
	 * @param src
	 * @param dest
	 * @param n
	 */
	public static void copyBytes(byte[] src, byte[] dest, int n)
	{
		for (int i = 0; i < n && i < dest.length; ++i)
		{
			dest[i] = src[i];
		}
	}
	
	/**
	 * @author Administrator @date 2017-7-26
	 * @note 计算从指定下标开始、结束的n个字节的累加和
	 * @return byte		累加和
	 * @param bytes		数据源
	 * @param index		起始下标
	 * @param index2	结束下标
	 */
	public static byte getLRC(byte[] bytes,int index,int index2)
	{
		byte result = 0x00;
		for(int i=index; i<index2 && i<bytes.length; i++)
		{
			result += bytes[i];
		}
//		System.out.println("getLRC:"+byteToHexString(result));
		return result;
	}
	
	public static byte[] getLRC2(byte[] bytes,int index,int index2)
	{
		int sum = 0x00;
		byte[] result = new byte[2];
		for(int i=index; i<index2 && i<bytes.length; i++)
		{
			sum += Integer.parseInt(byteToHexString(bytes[i]),16);
//			System.out.println("bytes["+i+"]:"+byteToHexString(bytes[i]));
//			System.out.println("sum:"+Integer.toHexString(sum));
		}
		
//		System.out.println("getLRC:"+Integer.toHexString(sum));
		result[0] = (byte)(sum & 0xFF);
		result[1] = (byte)(sum>>8 & 0xFF);
		return result;
	}
	
	/**
	 * @author Administrator @date 2017-7-26
	 * @note 将int数值转换为占四个字节的byte数组，本方法适用于(低位在前，高位在后)的顺序
	 * @return byte[]
	 * @param value
	 * @return
	 */
	public static byte[] intToBytesL( int value )   
	{   
	    byte[] src = new byte[4];  
	    src[3] =  (byte) ((value>>24) & 0xFF);  
	    src[2] =  (byte) ((value>>16) & 0xFF);  
	    src[1] =  (byte) ((value>>8) & 0xFF);    
	    src[0] =  (byte) (value & 0xFF);                  
	    return src;   
	}  
	
	/**
	 * @author Administrator @date 2017-7-26
	 * @note 将int数值转换为占四个字节的byte数组，本方法适用于(高位在前，低位在后)的顺序
	 * @return byte[]
	 * @param value
	 * @return
	 */
	public static byte[] intToBytesH(int value)   
	{   
	    byte[] src = new byte[4];  
	    src[0] = (byte) ((value>>24) & 0xFF);  
	    src[1] = (byte) ((value>>16)& 0xFF);  
	    src[2] = (byte) ((value>>8)&0xFF);    
	    src[3] = (byte) (value & 0xFF);       
	    return src;  
	}
	
	/**
	 * @author Administrator @date 2017-7-26
	 * @note byte数组中取int数值，本方法适用于(低位在前，高位在后)的顺序
	 * @return int
	 * @param src		byte数组
	 * @param offset	从数组的第offset位开始
	 * @param n			n位
	 */
	public static int bytesToIntL(byte[] src, int offset, int n) 
	{  
	    int value = 0;    
	    for(int i=0;i<n&&i<4;i++){
	    	value += (int) (src[offset+i] & 0xFF)<<(i*8);
	    }
	    return value;  
	}  
	  
	/**
	 * @author Administrator @date 2017-7-26
	 * @note byte数组中取int数值，本方法适用于(低位在后，高位在前)的顺序
	 * @return int
	 * @param src		byte数组
	 * @param offset	从数组的第offset位开始
	 * @param n			n位
	 */
	public static int bytesToIntH(byte[] src, int offset, int n) 
	{  
	    int value = 0;
	    for(int i=0;i<n&&i<4;i++){
	    	value += (int) (src[offset+i] & 0xFF)<<((n-1-i)*8);
	    }
	    return value;  
	}
	
	/**
	 * @author Administrator @date 2017-8-1
	 * @note 	将字节数据转换成16进制字符串。
	 * @param b		字节
	 * @return		十六进制字符串
	 */
	public static String byteToHexString(byte b)
	{
		StringBuilder stringBuilder = new StringBuilder(""); 
		int v = b & 0xFF;   
        String hv = Integer.toHexString(v); 
        if (hv.length() < 2) {   
            stringBuilder.append(0);   
        }   
        stringBuilder.append(hv);
        return stringBuilder.toString(); 
	}
	
	/**
	 * @author Administrator @date 2017-7-26
	 * @note 	将字节数组转换成16进制字符串。 
	 * @return String		十六进制字符串
	 * @param src			字节数组
	 * @return
	 */
	public static String bytesToHexString(byte[] src)
	{   
	    StringBuilder stringBuilder = new StringBuilder("");   
	    if (src == null || src.length <= 0) {   
	        return null;   
	    }   
	    for (int i = 0; i < src.length; i++) {   
	        int v = src[i] & 0xFF;   
	        String hv = Integer.toHexString(v);   
	        if (hv.length() < 2) {   
	            stringBuilder.append(0);   
	        }   
	        stringBuilder.append(hv);   
	    }   
	    return stringBuilder.toString();   
	} 
	
	public static String bytesToHexString(byte src)
	{
		StringBuilder stringBuilder = new StringBuilder("");
		int v = src & 0xFF;   
        String hv = Integer.toHexString(v);  
        if (hv.length() < 2) {   
            stringBuilder.append(0);   
        }   
        stringBuilder.append(hv);
        return stringBuilder.toString();
	}
	
	
	/**
	 * @author Administrator @date 2017-7-31
	 * @note 	将字节数组转换成16进制字符串。
	 * @param src	字节数组
	 * @param len	截止长度
	 * @return 十六进制字符串
	 */
	public static String bytesToHexString(byte[] src,int len)
	{   
	    StringBuilder stringBuilder = new StringBuilder("");   
	    if (src == null || src.length <= 0) {   
	        return null;   
	    }   
	    for (int i = 0; i < src.length&&i<len; i++) {   
	        int v = src[i] & 0xFF;   
	        String hv = Integer.toHexString(v);   
	        if (hv.length() < 2) {   
	            stringBuilder.append(0);   
	        }   
	        stringBuilder.append(hv);   
	    }   
	    return stringBuilder.toString();   
	} 
	
	/**
	 * @author Administrator @date 2017-7-26
	 * @note 	转换十六进制字符串为字节数组
	 * @return byte[]		返回字节数组
	 * @param hexString		十六进制字符串
	 * @return
	 */
	public static byte[] hexStringToBytes(String hexString) 
	{   
	    if (hexString == null || hexString.equals("")) {   
	        return null;   
	    }   
	    hexString = hexString.toUpperCase();   
	    int length = hexString.length() / 2;   
	    char[] hexChars = hexString.toCharArray();   
	    byte[] d = new byte[length];   
	    for (int i = 0; i < length; i++) {   
	        int pos = i * 2;   
	        d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));   
	    }   
	    return d;   
	}  
	
	/**  
	 * Convert char to byte  
	 * @param c char  
	 * @return byte  
	 */  
	 private static byte charToByte(char c)
	 {   
	    return (byte) "0123456789ABCDEF".indexOf(c);   
	 } 


}