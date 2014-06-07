package com.wcs.base.util.des;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

public class FileUtils {

    private static final String ENCODING = "UTF-8";
    /**
     * 在目标文件夹上创建文件
     * 
     * @throws IOException
     */
    public static File creatFileInDir(String dir, String fileName) throws IOException {
        File file = new File(dir + File.separator + fileName);
        file.createNewFile();
        return file;
    }

    /**
     * 删除文件
     * 
     * @param filename
     *            被删除文件的文件名
     * @return boolean 是否删除成功
     * @throws Exception
     *             删除文件过程中的任何异常
     */
    public static boolean deleteFile(String dir, String filename) throws Exception {
        File file = new File(dir + File.separator + filename);

        return deleteFile(file);
    }

    /**
     * 删除文件
     * 
     * @param file
     *            被删除文件
     * @return boolean 是否删除成功
     * @throws Exception
     *             删除文件过程中的任何异常
     */
    public static boolean deleteFile(File file) throws Exception {
        if (file.isDirectory()) { return deleteDir(file); }

        if (!file.exists()) { return false; }

        return file.delete();
    }

    /**
     * 创建一个目录
     * 
     * @param dir
     *            目录路径
     * @param ignoreIfExitst
     *            如果已经存在该目录是否忽略
     * @return boolean 是否创建成功
     * @throws Exception
     *             创建目录过程中的任何异常
     */
    public static boolean createDir(String path, boolean ignoreIfExitst) throws Exception {
        File file = new File(path);

        if (ignoreIfExitst && file.exists()) { return false; }

        return file.mkdir();
    }

    /**
     * 
     * <p>Description:判断路径是否为文件夹 </p>
     * @param path 文件路径
     * @return
     * @throws Exception
     */
    public static boolean isDirectory(String path) throws Exception {
        File file = new File(path);
        return file.isDirectory();
    }

    /**
     * 删除目录，包括其下的所有子目录和文件
     * 
     * @param dir
     *            被删除的目录名
     * @return boolean 是否删除成功
     * @throws Exception
     *             删除目录过程中的任何异常
     */
    public static boolean deleteDir(File dir) throws Exception {
        if (dir.isFile()) {
            deleteFile(dir);
        }

        File[] files = dir.listFiles();

        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                File file = files[i];

                if (file.isFile()) {
                    file.delete();
                } else {
                    deleteDir(file);
                }
            }
        }

        return dir.delete();
    }

    /**
     * 判断文件夹下文件是否存在
     */
    public static boolean isFileExist(String path, String fileName) {
        File file = new File(path + File.separator + fileName);
        return file.exists();
    }

    /**
     * 将一个String写入到文件中
     */
    public static File write2FileByString(String path, String fileName, String content,String charsetName) {
        System.out.println("写入文件的编码："+charsetName);
        File file = null;
        OutputStream output = null;
        OutputStreamWriter osw = null;
        try {
            createDir(path, false);
            file = creatFileInDir(path, fileName);
            output = new FileOutputStream(file);
            
            osw = new OutputStreamWriter(output, ENCODING);
            osw.write(content);
            osw.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                output.close();
                osw.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    /**
     * 将一个InputStream里面的数据写入到文件中
     */
    public static File write2FolderFromInput(String path, String fileName, InputStream input) {
        File file = null;
        OutputStream output = null;
        try {
            createDir(path, false);
            file = creatFileInDir(path, fileName);
            output = new FileOutputStream(file);

            byte buffer[] = new byte[4 * 1024];
            int temp;
            while ((temp = input.read(buffer)) != -1) {
                output.write(buffer, 0, temp);
            }
            output.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                output.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    /**
     * 
     * <p>Description:读取文件中的内容</p>
     * @param file
     * @return
     * @throws Exception
     */
    public static String getFileString(File file) throws Exception {
        //System.out.println("源文件编码是：" + getCharsetNameByFile(file));
       
        StringBuffer sb = new StringBuffer();
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file),ENCODING));
        String line = null;
        while ((line = br.readLine()) != null) {
            sb.append(line + "\n");
        }
        return sb.toString();
    }

    /**
     * 
     * <p>Description:获取txt文件的编码格式 </p>
     * @param file 
     * @return
     * @throws Exception
     */
//    public static String getCharsetNameByFile(File file) throws Exception {
//        /*
//         * detector是探测器，它把探测任务交给具体的探测实现类的实例完成。 cpDetector内置了一些常用的探测实现类，这些探测实现类的实例可以通过add方法 加进来，如ParsingDetector、
//         * JChardetFacade、ASCIIDetector、UnicodeDetector。 detector按照“谁最先返回非空的探测结果，就以该结果为准”的原则返回探测到的
//         * 字符集编码。使用需要用到三个第三方JAR包：antlr.jar、chardet.jar和cpdetector.jar cpDetector是基于统计学原理的，不保证完全正确。
//         */
//        CodepageDetectorProxy detector = CodepageDetectorProxy.getInstance();
//        /*
//         * ParsingDetector可用于检查HTML、XML等文件或字符流的编码,构造方法中的参数用于 指示是否显示探测过程的详细信息，为false不显示。
//         */
//        detector.add(new ParsingDetector(false));
//        /*
//         * JChardetFacade封装了由Mozilla组织提供的JChardet，它可以完成大多数文件的编码 测定。所以，一般有了这个探测器就可满足大多数项目的要求，如果你还不放心，可以
//         * 再多加几个探测器，比如下面的ASCIIDetector、UnicodeDetector等。
//         */
//        detector.add(JChardetFacade.getInstance());// 用到antlr.jar、chardet.jar
//        // ASCIIDetector用于ASCII编码测定
//        detector.add(ASCIIDetector.getInstance());
//        // UnicodeDetector用于Unicode家族编码的测定
//        detector.add(UnicodeDetector.getInstance());
//        java.nio.charset.Charset charset = null;
//
//        try {
//            charset = detector.detectCodepage(file.toURI().toURL());
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        if (charset != null) return charset.name();
//        else return null;
//    }

    /**
     * 
     * <p>Description:将文本转成流 </p>
     * @param str
     * @return
     * @throws UnsupportedEncodingException
     */
    public static InputStream stringToInputStream(String str) throws UnsupportedEncodingException {
        ByteArrayInputStream stream = new ByteArrayInputStream(str.getBytes(ENCODING));
        return stream;
    }

    /**
     * 
     * <p>Description:文件夹路径过滤 </p>
     * @param path
     * @return
     * @throws Exception
     */
    public static String filterFolderPath(String path) throws Exception {
        String retPath = path;
        if (".".equals(path)) {
            retPath = System.getProperty("user.dir");
        }
        return retPath;
    }

    /**
     * 读取某个路径中的文件列表
     * @throws Exception 
     */
    public static File[] getFilesByPath(String path) throws Exception {

        File file = new File(path + File.separator);
        if (!file.exists()) { throw new Exception("传入的路径不存在！"); }
        File[] files = file.listFiles();

        return files;
    }

    public static void main(String[] args) throws Exception {
        //System.out.println(getCharsetNameByFile(new File("d:/source/1111.txt")));

        System.out.println(getFileString(new File("d:/source/1111.txt")));
    }
}