package com.itdlc.android.library.utils;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.DOWNLOAD_SERVICE;

/**
 * Created by dusj on 2018/6/29.
 */

public class FileDownloadUtils {

    private DownloadManager downloadManager;
    private DownloadManager.Request request;
    private Timer timer;
    private long id;
    private TimerTask task;
    private Handler handler =new Handler(Looper.getMainLooper());
    private Activity mActivity;
    public FileDownloadUtils(Activity mActivity){
        this.mActivity = mActivity;
    }

    public void  download( String downloadUrl, String filePath, String fileName, final IDownloadProgress downloadProgress){
        downloadManager = (DownloadManager) mActivity.getSystemService(DOWNLOAD_SERVICE);
        request = new DownloadManager.Request(Uri.parse(downloadUrl));

        request.setTitle(fileName);
        //request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        request.setAllowedOverRoaming(false);
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        String mimeString = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(downloadUrl));
        request.setMimeType(mimeString);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        //创建目录
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).mkdir() ;

        if (filePath == null){
            //设置文件存放路径
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS  , fileName ) ;
        }else {
            request.setDestinationInExternalPublicDir(filePath  , fileName ) ;
        }
        Log.d("TAG","downloadUrl :  " + downloadUrl  );
        final  DownloadManager.Query query = new DownloadManager.Query();
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                Cursor cursor = downloadManager.query(query.setFilterById(id));
                if (cursor != null && cursor.moveToFirst()) {
                    if (cursor.getInt(
                            cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                        task.cancel();
                    }
                    String title = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_TITLE));
                    String address = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                    int bytes_downloaded = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                    int bytes_total = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                    Log.d("TAG","bytes_downloaded :  " + bytes_downloaded + "   bytes_total : " +bytes_total );
                   // final int pro =  (bytes_downloaded * 100) / bytes_total;
                }
                cursor.close();
            }
        };
        timer.schedule(task, 0,1000);
        id = downloadManager.enqueue(request);
        task.run();
        listener(id,mActivity,downloadProgress);
    }
    private BroadcastReceiver broadcastReceiver;
    private void listener(final long Id, final Activity mActivity, final IDownloadProgress downloadProgress) {

        // 注册广播监听系统的下载完成事件。
        IntentFilter intentFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                long ID = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (ID == Id) {
                    if (timer != null){
                        timer.cancel();
                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (downloadProgress != null){
                                downloadProgress.progress(1);
                            }
                        }
                    });
                }
            }
        };

        mActivity.registerReceiver(broadcastReceiver, intentFilter);
    }
    public void canal(){
        if (downloadManager != null){
            downloadManager.remove(id);
        }
    }

    public void unregisterReceiver(){
        if (broadcastReceiver != null){
            mActivity.unregisterReceiver(broadcastReceiver);
        }
    }
    public interface IDownloadProgress {
        void progress(int status);
    }
}
