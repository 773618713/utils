package com.scy.utils;

import java.io.*;

public class FileUtil {


    /**
     * 以行为单位读取文件，常用于读面向行的格式化文件
     */
    public static String readFileByLines(String fileName) {
        StringBuilder sb = new StringBuilder();
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                sb.append(tempString + "\r\n");
                // 显示行号
                System.out.println("line " + line + ": " + tempString);
                line++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
            return sb.toString();
        }
    }

    /**
     * 文件写出
     * @param file			文件
     * @param filePath		路径
     * @param fileName		文件名
     * @throws Exception
     */
    public static void uploadFile(byte[] file, String filePath, String fileName) throws Exception {
        File targetFile = new File(filePath);
        if(!targetFile.exists()){
            targetFile.mkdirs();
        }
        FileOutputStream out = new FileOutputStream(filePath+fileName);
        out.write(file);
        out.flush();
        out.close();
    }

    public static File createFile(String filePath, String fileName) throws Exception {
        File targetFile = new File(filePath);
        if(!targetFile.exists()){
            targetFile.mkdirs();
        }
        File file = new File(filePath+fileName);
        return file;
    }

    /**
     * 文本文件写出
     * @param filePath	路径
     * @param fileName	名称
     * @param info		文本
     * @throws IOException
     */
    public static void writeFile(String filePath, String fileName, String info) throws IOException {
        FileOutputStream fos = new FileOutputStream(new File(filePath+File.separator+fileName));
        byte b[] = info.getBytes();
        fos.write(b);
        fos.close();
    }



    public static void main(String[] args) {
        try {

            //writeFile("C:\\Users\\sun\\Desktop\\1907338581111", "data.json", "abc");
            //String fileName = getFileName("C:\\Users\\sun\\Desktop\\1907338581111\\abc.jpg");
            //System.out.println(fileName);
            String oldPath = "D:\\SCApp\\Data\\tempscanfile\\5cd4589d-eeae-4dfb-90b5-55ee1c9a5a2b\\159_1.jpeg";
            String newPath = "D:\\SCApp\\Data\\scanfile\\5cd4589d-eeae-4dfb-90b5-55ee1c9a5a2b\\159_1.jpeg";
            boolean flag = transferredFile(oldPath, newPath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 从路径中获取文件名
     * 例如输入 /123/abc.jpg, 返回 abc.jpg
     * @param pathName
     * @return
     */
    public static String getFileName(String pathName) {
        return pathName.substring(pathName.lastIndexOf(File.separator)+1);
    }

    /**
     * 从路径名称中获取文件路径
     * 例如输入 /123/abc.jpg, 返回 /123/
     * @param pathName
     * @return
     */
    public static String getFilePath(String pathName) {
        return pathName.substring(0, pathName.lastIndexOf(File.separator)+1);
    }

    /**
     * 转存文件
     * <br/>
     * 将一个文件剪切到另一个目录，如果目标目录不存在，则进行创建，如果目标文件已经存在则进行删除
     * @param oldPath   //源文件路径
     * @param newPath   //目标文件路径
     * @return
     */
    public static boolean transferredFile(String oldPath, String newPath) {
        File file = new File(oldPath); //源文件
        File toFile = new File(newPath);//目标文件
        File toPathFile = new File(getFilePath(newPath)); //源文件
        //文件夹不存在，进行创建
        if (!toPathFile.exists()) {
            toPathFile.mkdirs();
        }
        //文件已存在，进行删除
        if (toFile.exists()){
            toFile.delete();
        }
        if (file.renameTo(toFile))//源文件移动至目标文件目录
        {
            System.out.println("File is moved successful!");//输出移动成功
            return true;
        }
        else
        {
            System.out.println("File is failed to move !");//输出移动失败
            return false;
        }
    }

    /**
     * 删除文件夹（强制删除）
     *
     * @param path
     */
    public static void deleteAllFilesOfDir(File path) {
        if (null != path) {
            if (!path.exists())
                return;
            if (path.isFile()) {
                boolean result = path.delete();
                int tryCount = 0;
                while (!result && tryCount++ < 10) {
                    System.gc(); // 回收资源
                    result = path.delete();
                }
            }
            File[] files = path.listFiles();
            if (null != files) {
                for (int i = 0; i < files.length; i++) {
                    deleteAllFilesOfDir(files[i]);
                }
            }
            path.delete();
        }
    }


}
