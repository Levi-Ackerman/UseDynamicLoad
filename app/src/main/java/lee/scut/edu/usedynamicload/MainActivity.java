package lee.scut.edu.usedynamicload;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view) {
        FileOutputStream fileOutputStream = null;
        InputStream inputStream = null;
        try {
            inputStream = getAssets().open("app-debug.apk");
            byte[] bytes = new byte[1024];
            fileOutputStream = new FileOutputStream(getFilesDir() + "/app-debug.apk");
            int length;
            while ((length = inputStream.read(bytes)) != -1) {
                fileOutputStream.write(bytes, 0, length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null)
                    inputStream.close();
                if (fileOutputStream != null)
                    fileOutputStream.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        File path = new File(getFilesDir() + "/app-debug.apk");

        ClassLoader dexClassLoader = new DexClassLoader(path.getAbsolutePath(), getCacheDir().getAbsolutePath(), null, getClass().getClassLoader());
        Class libProviderClazz = null;
        try {
            libProviderClazz = dexClassLoader.loadClass("lee.scut.edu.notificationmonitor.Util");
            // 遍历类里所有方法
            Method[] methods = libProviderClazz.getDeclaredMethods();
            for (int i = 0; i < methods.length; i++) {
                Log.e("lee..", methods[i].toString());
            }
            Method start = libProviderClazz.getDeclaredMethod("getString");// 获取方法
            start.setAccessible(true);// 把方法设为public，让外部可以调用
            String string = (String) start.invoke(null);// 调用方法并获取返回值
            Toast.makeText(this, string, Toast.LENGTH_LONG).show();
        } catch (Exception exception) {
            // Handle exception gracefully here.
            Log.i("lee..", exception.toString());
            exception.printStackTrace();
        }
    }
}
