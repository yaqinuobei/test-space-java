package code.traveler.test.space.java.td.mvn;

import org.apache.maven.model.License;
import org.apache.maven.model.Model;
import org.apache.maven.model.Parent;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarParser {

    public static void main(String[] args) {
        String rootPath="E:\\WorkSpace\\xxx";
        ExcelExport ee = new ExcelExport(rootPath);
        List<MyFileEntity> jarFileList= new java.util.ArrayList<MyFileEntity>();

        File root = new File(rootPath);
        traverseFiles(root,jarFileList);
        for (MyFileEntity fe : jarFileList) {
            System.out.println(fe);
        }
        //导出excel
        ee.exportExcel(jarFileList);
    }

    public static void traverseFiles(File file,List<MyFileEntity> jarFileList) {

        if (file.isDirectory()) { // 如果是目录
            File[] files = file.listFiles(); // 获取目录下的所有子文件和子目录
            if (files != null) {
                for (File subFile : files) {
                    traverseFiles(subFile,jarFileList); // 递归调用遍历子目录
                }
            }
        } else {
            if (file.getName().toLowerCase().endsWith(".jar")) {
                MyFileEntity fe = new MyFileEntity();
                fe.setFile(file.getAbsolutePath());
                boolean result=searchInPom(fe);
                if (result) {
                    jarFileList.add(fe);
                }
            }
        }
    }
    public static boolean searchInPom(MyFileEntity fe) {
        JarFile jf = null;
        try {
            jf = new JarFile(fe.getFile());
            Enumeration<?> entries = jf.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = (JarEntry) entries.nextElement();
                if (entry.getName().toLowerCase().endsWith("pom.xml")) {
                    StringWriter writer = new StringWriter();
                    try {
                        org.springframework.util.FileCopyUtils.copy(new InputStreamReader(jf.getInputStream(entry)),
                                writer);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    StringReader sr = new StringReader(writer.toString());
                    MavenXpp3Reader reader = new MavenXpp3Reader();
                    Model model = null;
                    try {
                        model = reader.read(sr);
                        List<License> licenses = model.getLicenses();
                        // pom中没有license标签,则尝试联网获取
                        String groupId = model.getGroupId();
                        String artifactId = model.getArtifactId();
                        String version = model.getVersion();
                        Parent parent = model.getParent();
                        String groupId2 = null;
                        String artifactId2 = null;
                        String version2 = null;
                        if (parent != null) {
                            groupId2 = parent.getGroupId();
                            artifactId2 = parent.getArtifactId();
                            version2 = parent.getVersion();
                        }
                        // 自定义的fe对象简单封装了这三个变量groupId,artifactId和version
                        fe.setGroupId(groupId == null ? groupId2 : groupId);
                        fe.setArtifactId(artifactId == null ? artifactId2 : artifactId);
                        fe.setVersion(version == null ? version2 : version);
                        // searchOnInet(fe); // searchOnInet方法自己实现
                        if (licenses != null && licenses.size() > 0) {
                            // System.out.println(licenses.get(0).getName());
                            fe.setLicense(licenses.get(0).getName());
                        }
                        return true;
                    } catch (IOException | XmlPullParserException e) {
                        e.printStackTrace();
                    } finally {
                        if (sr != null) {
                            sr.close();
                        }
                    }
                }
            }
        } catch (IOException e2) {
            e2.printStackTrace();
        } finally {
            try {
                if (jf != null) {
                    jf.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean searchOnInet(MyFileEntity fe){
        return false;
    }
}


