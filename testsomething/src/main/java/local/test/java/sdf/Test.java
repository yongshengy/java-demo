package local.test.java.sdf;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Test {
    public static void main(String[] args) throws ParseException {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(sdf.format(date));

        String dateStr = "2022-01-01 00:00:00";
        System.out.println(sdf.parse(dateStr).toString());
    }

}
