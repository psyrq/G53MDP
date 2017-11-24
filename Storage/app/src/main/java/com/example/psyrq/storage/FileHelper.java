package com.example.psyrq.storage;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by qianruofei on 2017/11/24.
 */

public class FileHelper {

    private Context mContext;
    protected String filePath;

    public FileHelper() {}

    public FileHelper(Context mContext) {
        super();
        this.mContext = mContext;
    }

    public void save(String filename, String filecontent) throws Exception {
        //这里我们使用私有模式,创建出来的文件只能被本应用访问,还会覆盖原文件哦
        FileOutputStream output = mContext.openFileOutput(filename, Context.MODE_PRIVATE);
        output.write(filecontent.getBytes());  //将String字符串以字节流的形式写入到输出流中
        output.close();         //关闭输出流
    }

    public String read(String fileName) throws IOException {

        FileInputStream input = mContext.openFileInput(fileName);
        byte[] temp = new byte[1024];
        StringBuilder sb = new StringBuilder("");
        int length = 0;

        while((length = input.read(temp)) > 0) {
            sb.append(new String(temp, 0, length));
        }
        input.close();

        return sb.toString();
    }

    public void savaFileToSD(String filename, String filecontent) throws Exception {
        //如果手机已插入sd卡,且app具有读写sd卡的权限
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            filename = Environment.getExternalStorageDirectory().getCanonicalPath() + "/" + filename;
            //这里就不要用openFileOutput了,那个是往手机内存中写数据的
            FileOutputStream output = new FileOutputStream(filename);
            output.write(filecontent.getBytes());
            //将String字符串以字节流的形式写入到输出流中
            output.close();
            //关闭输出流
        } else Toast.makeText(mContext, "SD卡不存在或者不可读写", Toast.LENGTH_SHORT).show();
    }

    public String readFromSD(String filename) throws IOException {
        StringBuilder sb = new StringBuilder("");
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            filename = Environment.getExternalStorageDirectory().getCanonicalPath() + "/" + filename;
            //打开文件输入流
            FileInputStream input = new FileInputStream(filename);
            byte[] temp = new byte[1024];

            int len = 0;
            //读取文件内容:
            while ((len = input.read(temp)) > 0) {
                sb.append(new String(temp, 0, len));
            }
            //关闭输入流
            input.close();
        }
        return sb.toString();
    }

    public String getFilePath(File file) {
        return file.getAbsolutePath();
        //return file.getPath();
    }
}
