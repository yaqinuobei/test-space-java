package code.traveler.test.space.java.td.git;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class PythonCodeCount {

    private static long 空行数 = 0;
    private static long 注释行数 = 0;
    private static long 代码行数 = 0;
    private static long 总行数 = 0;

    public static void main(String[] args) {
        // 这里只统计 \src\main 文件夹里的文件，其他文件像 .idea、test 这些文件夹里的就不算进去了
        // 循环文件夹("D:\\Java\\workspace\\AssetsJava","\\src\\main\\");
//        循环文件夹("E:\\gorepository\\src\\configcenter\\src","\\vendor\\");
//        循环文件夹("E:\\WorkSpace\\xxx","\\vendor\\");
//        循环文件夹("E:\\WorkSpace\\xxx","\\ui\\");

        循环文件夹("E:\\WorkSpace\\xxx","\\ui\\");

        System.out.println("空行:" + 空行数);
        System.out.println("注释行:" + 注释行数);
        System.out.println("代码行：" + 代码行数);
        System.out.println("总行：" + 总行数);
    }

    private static void 循环文件夹(String 项目路径,String 排除指定文件夹) {
        File f = new File(项目路径);
        if (!f.exists()){
            System.out.println("项目路径不存在！");
            return;
        }
        File[] childs = f.listFiles(); // 获取这个项目下的文件、文件夹
        for (int i = 0; i < childs.length; i++) {
            File child = childs[i];
            if (!child.isDirectory()) { // 当前文件不是文件夹，就读取
                if (!child.getParent().contains(排除指定文件夹)){ // java文件、xml文件、配置文件 只统计main文件夹下的
                    if (child.getName().matches(".*\\.py") || child.getName().endsWith(".yml") ||
                            child.getName().endsWith(".properties") || child.getName().endsWith(".xml")) {
                        long 单个文件代码行数 = 统计代码行数(child);
                        System.out.println(单个文件代码行数+"\t\t"+child.getName());
                    }
                }else if ("pom.xml".equals(child.getName())){
                    long 单个文件代码行数 = 统计代码行数(child);
                    System.out.println(单个文件代码行数+"\t\t"+child.getName());
                }
            }else { // 当前文件是文件夹，继续递归
                循环文件夹(child.getPath(),排除指定文件夹);
            }
        }
    }

    private static long 统计代码行数(File file){
        long 单个文件代码行数 = 0;
        BufferedReader br = null;
        try{
            br = new BufferedReader(new FileReader(file));
            String line = "";
            boolean flag = false; // 用于标记xml多行注释，为true表示当前行在多行注释中，一直到最后一行注释
            while ((line = br.readLine()) != null){
                总行数++;
                String 内容 = line.trim(); // 每一行的内容，去掉空格
                if (flag){
                    注释行数++;
                    // 当多行注释结尾是 --> 说明多行注释结束，重新标记为false，表示当前不在统计多行注释
                    if (内容.endsWith("-->")) flag = false;
                }else {
                    if (内容.length() == 0){
                        空行数++;
                    }else if (内容.startsWith("//") || 内容.startsWith("/**") || 内容.startsWith("*") ||
                            内容.startsWith("*/") || 内容.startsWith("/*") || 内容.startsWith("#")){
                        注释行数++;
                    }else if(内容.startsWith("<!--")){
                        注释行数++;
                        // 当前行属于xml的注释，且结尾不是 --> 时，表示是多行注释，设置标记为true
                        if (!内容.endsWith("-->")) flag = true;
                    }else {
                        代码行数++;
                        单个文件代码行数++;
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return 单个文件代码行数;
    }
}