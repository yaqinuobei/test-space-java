package code.traveler.test.space.java.td.ed;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Base64;

public class Base64ATest {



    public static void main(String[] args) throws IOException {

        try {
            System.out.println("当前运行目录："+System.getProperty("user.dir"));
            BufferedReader reader=new BufferedReader(new FileReader("E:\\WorkSpace\\code\\github\\test-space-java"
                    + "\\tool-demo\\encript-demo\\target\\classes\\redis-base-without-huanhang.txt"));
            StringBuffer sb=new StringBuffer();
            String line=null;
            while((line= reader.readLine())!=null){
                sb.append(line);
            }
            System.out.println(sb.toString());
            String adjustStr = sb.toString().replace(" ","\n");
            System.out.println(Base64.getDecoder().decode(adjustStr));
            reader.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
//        String src_str="";

    }
}
