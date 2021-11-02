import com.lz.adminweb.utils.MD5Util;

/**
 * Class
 *
 * @author yaoyanhua
 * @version 1.0
 * @date 2021/2/2
 */
public class TestMd5 {
    public static void main(String[] args) {
        String pwd = "Aa123456";
        String md5 = MD5Util.md5WithSalt(pwd);
        System.out.println(md5);
    }
}
