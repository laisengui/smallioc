package cn.lsg.smallioc.util;

/**
 * 字符串工具
 * @author Norton Lai
 * @created 2017-12-23 下午10:30:27
 */
public class SUtil {
    
    /**
     * 如果字符串为null 或者长度为0返回 true
     * @author Norton Lai
     * @created 2017-12-23 下午10:31:18
     * @param str
     * @return
     */
    public static boolean isEmp(String str){
        return str==null||str.length()==0;
    }
    
    /**
     * 如果字符串不为null 且长度不为0返回 true
     * @author Norton Lai
     * @created 2017-12-23 下午10:31:18
     * @param str
     * @return
     */
    public static boolean isNotEmp(String str){
        return !isEmp(str);
    }
    /**
     * 将首字母小写
     * @author Norton Lai
     * @created 2017-12-24 上午1:02:13
     * @param str
     * @return
     */
    public static String firstLow(String str){
        if (isEmp(str)) {
            return str;
        }
        char c = str.charAt(0);
        if (c>='A'&&c<='Z') {
            c+=32;
        }
        char[] array = str.toCharArray();
        array[0]=c;
        return new String(array);
    }
}
