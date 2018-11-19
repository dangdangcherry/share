package com.itdlc.android.library.popup_window;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.itdlc.android.library.R;
import com.itdlc.android.library.utils.ImageUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;

/**
 * Created by Administrator on 2017/9/9 0009.
 */

public class TackPicturePopupWindow extends PopupWindow implements View.OnClickListener {

    private String mStrCurrentPic;

    private static final String TAG = "TackPicturePopupWindow";
    private boolean isCrop;
    private int miCropMaxWidth;
    private int miCropMaxHeight;
    private float miCropProption;
    private File currentTimeFile;

    public interface TackPictureListener {
        void newBitmap(Bitmap bitmap);
    }

    public interface TackFileListener {
        void tackFile(File file);
    }

    private final Activity mActivity;
    private final Fragment mCallback;
    private final TextView mTvCamera;
    private final TextView mTvAblum;

    private TackPictureListener tackPictureListener;

    public void setTackPictureListener(TackPictureListener tackPictureListener) {
        this.tackPictureListener = tackPictureListener;
    }

    private TackFileListener mTackFileListener;

    public void setTackFileListener(TackFileListener tackFileListener) {
        this.mTackFileListener = tackFileListener;
    }


    private final int miCameraRequestCode = 101;
    private final int miAlbumRequestCode = 102;

    public TackPicturePopupWindow(Activity activity, TackFileListener tackPictureListener) {
        this(activity, null, null);
        this.mTackFileListener = tackPictureListener;
    }

    public TackPicturePopupWindow(Activity activity, TackPictureListener tackPictureListener) {
        this(activity, null, tackPictureListener);
    }

    public TackPicturePopupWindow(final Activity activity, Fragment callback, TackPictureListener tackPictureListener) {
        super(LayoutInflater.from(activity).inflate(R.layout.pw_confirm_dialog, null, false)
                , ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        setTouchable(true);
        // 设置背景颜色
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setAnimationStyle(R.style.pw_slide);

        this.tackPictureListener = tackPictureListener;
        mActivity = activity;
        mCallback = callback;

        mTvCamera = (TextView) getContentView().findViewById(R.id.tv_pw_title);
        mTvAblum = (TextView) getContentView().findViewById(R.id.tv_pw_confirm);
        TextView tvCancel = (TextView) getContentView().findViewById(R.id.tv_pw_cancel);

        mTvCamera.setText("拍照");
        mTvAblum.setText("从相册选择");
        tvCancel.setText("取消");

        mTvCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 打开相机
                openCamera();
            }
        });
        mTvAblum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAlbum();

            }
        });
        tvCancel.setOnClickListener(this);

        //  弹出窗监听
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams params = mActivity.getWindow().getAttributes();
                params.alpha = 1;
                mActivity.getWindow().setAttributes(params);
            }
        });
    }

    public void openAlbum() {
        new RxPermissions(mActivity)
                .request(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new io.reactivex.functions.Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            dismiss();
                            // 打开相册
                            Intent intent = new Intent(Intent.ACTION_PICK, null);
                            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                            if (mCallback == null) {
                                mActivity.startActivityForResult(intent, miAlbumRequestCode);
                            } else {
                                mCallback.startActivityForResult(intent, miAlbumRequestCode);
                            }
                        }
                    }
                });
    }

    public void openCamera() {
        new RxPermissions(mActivity)
                .request(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new io.reactivex.functions.Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            //这个可以作为参数，自定义
                            //                                    //这个可以作为参数，自定义
                            File file = null;
                            if (currentTimeFile != null) {
                                file = currentTimeFile;
                            } else {
                                file = new File(getPhotoPath());
                            }
                            if (file.exists()) {
                                file.delete();
                            }
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            Uri uri = null;
                            if (Build.VERSION.SDK_INT >= 24) {
                                try {
                                    uri = FileProvider.getUriForFile(mActivity.getApplicationContext(), mActivity.getPackageName() + ".fileprovider", file);
                                } catch (Exception e) {
                                    Log.e(TAG, "accept: " + e);
                                }
                            }

                            if (uri == null) {
                                uri = Uri.fromFile(file);
                            }
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//这里加入flag
                            if (mCallback == null) {
                                mActivity.startActivityForResult(intent, miCameraRequestCode);
                            } else {
                                mCallback.startActivityForResult(intent, miCameraRequestCode);
                            }
                            dismiss();
                        }
                    }
                });
    }

    public void setResult(int requestCode, int resultCode, Intent data) {

        Bitmap bitmap = null;
        String path = null;
        Log.e(TAG, "setResult: " + resultCode);
        File outFile = null;
        if (currentTimeFile != null) {
            outFile = currentTimeFile;
        } else {
            outFile = new File(getPhotoPathByTime());
        }

        switch (requestCode) {
            //相机
            case miCameraRequestCode:
                String photoPath = getPhotoPath();
                File filePhoto = null;
                if (currentTimeFile != null) {
                    filePhoto = currentTimeFile;
                } else {
                    filePhoto = new File(photoPath);
                }
                if (filePhoto.exists()) {
                    if (isCrop) {
                        UCrop.of(Uri.fromFile(filePhoto), Uri.fromFile(outFile))
                                .withMaxResultSize(miCropMaxWidth, miCropMaxHeight)
                                .withAspectRatio(miCropProption, 1)
                                .start(mActivity);
                    } else {
                        path = currentTimeFile != null ? currentTimeFile.getPath() : getPhotoPath();
                        bitmap = getBitmap(path, 1500, 1500);
                        ImageUtils.saveBitmap(bitmap, path);
                        bitmap = adjustPhotoRotation(bitmap, readPictureDegree(path));
                    }
                }
                break;
            case miAlbumRequestCode:
                // 相册
                if (resultCode != Activity.RESULT_OK) {
                    return;
                }

                Uri selectedImage = data.getData();
                if (isCrop) {
                    Uri uri = Uri.fromFile(outFile);
                    UCrop.of(selectedImage, uri)
                            .withMaxResultSize(miCropMaxWidth, miCropMaxHeight)
                            .withAspectRatio(miCropProption, 1)
                            .start(mActivity);
                } else {
                    String[] filePathColumns = {MediaStore.Images.Media.DATA};
                    Cursor c = mActivity.getContentResolver().query(selectedImage, filePathColumns, null, null, null);
                    c.moveToFirst();
                    int columnIndex = c.getColumnIndex(filePathColumns[0]);
                    String imagePath = c.getString(columnIndex);
                    bitmap = getBitmap(imagePath, 1500, 1500);
                    String newPath = getPhotoPathByTime();
                    path = ImageUtils.saveBitmap(bitmap, newPath);
                    c.close();
                }

                break;
            //裁剪照片回调
            case UCrop.REQUEST_CROP:
                // 相册
                if (resultCode != Activity.RESULT_OK) {
                    return;
                }
                path = UCrop.getOutput(data).getPath();
                if (path != null)
                    bitmap = BitmapFactory.decodeFile(path);

                break;
            default:
                return;
        }

        if (bitmap != null && tackPictureListener != null) {
            tackPictureListener.newBitmap(bitmap);
        }
        if (path != null && mTackFileListener != null) {
            mTackFileListener.tackFile(new File(path));
        }
    }

    public void setUcrop(int maxWidth, int maxHeight, float proption) {
        isCrop = true;
        miCropMaxWidth = maxWidth;
        miCropMaxHeight = maxHeight;
        miCropProption = proption;

    }

    /**
     * 根据路径压缩图片
     *
     * @param filePath   要加载的图片路径
     * @param destWidth  显示图片的控件宽度
     * @param destHeight 显示图片的控件的高度
     * @return
     */
    public static Bitmap getBitmap(String filePath, int destWidth, int destHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        int outWidth = options.outWidth;
        int outHeight = options.outHeight;
        int sampleSize = 1;
        while (outHeight / sampleSize > destHeight || outWidth / sampleSize > destWidth) {
            sampleSize *= 2;
        }
        options.inJustDecodeBounds = false;
        options.inSampleSize = sampleSize;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        return BitmapFactory.decodeFile(filePath, options);
    }

    /**
     * 获取原图片存储路径
     *
     * @return
     */
    private String getPhotoPath() {
        // 照片全路径
        String fileName = "";
        // 文件夹路径
        String pathUrl = mActivity.getExternalCacheDir() + "/TackPictureTemp/.nomedia";
        String imageName = "imageTest.jpg";
        File file = new File(pathUrl);
        if (file.exists()) {
            file.delete();
        }
        file.mkdirs();// 创建文件夹
        fileName = pathUrl + imageName;

        return fileName;
    }

    /**
     * 获取原图片存储路径
     *
     * @return
     */
    private String getPhotoPathByTime() {
        // 照片全路径
        String fileName = "";
        // 文件夹路径
        String pathUrl = Environment.getExternalStorageDirectory() + "/TackPictureTemp/";
        String imageName = System.currentTimeMillis() + ".jpg";
        File file = new File(pathUrl);
        if (file.exists()) {
            file.delete();
        }
        file.mkdirs();// 创建文件夹
        fileName = pathUrl + imageName;

        return fileName;
    }

    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    public static Bitmap adjustPhotoRotation(Bitmap bm, final int orientationDegree) {

        Matrix m = new Matrix();
        m.setRotate(orientationDegree, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
        Matrix ms = new Matrix();

        try {
            int w = bm.getWidth();

            if (w > 1024) {
                ms.setScale(1024f / w, 1024f / w);
                Log.e(TAG, "adjustPhotoRotation: " + w + " : " + bm.getHeight());
                bm = Bitmap.createBitmap(bm, 0, 0, w, bm.getHeight(), ms, true);
            }

            bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), m, true);

            return bm;

        } catch (OutOfMemoryError e) {

            Log.e(TAG, "adjustPhotoRotation: " + e);

        }
        return null;

    }


    @Override
    public void onClick(View v) {
        dismiss();
    }

    public void show() {
        show(false);
        WindowManager.LayoutParams params = mActivity.getWindow().getAttributes();
        params.alpha = 0.6f;
        mActivity.getWindow().setAttributes(params);
        showAtLocation(mActivity.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }

    public void show(boolean isMultiple) {
        if (isMultiple) {
            currentTimeFile = new File(getPhotoPathByTime());
        }
        WindowManager.LayoutParams params = mActivity.getWindow().getAttributes();
        params.alpha = 0.6f;
        mActivity.getWindow().setAttributes(params);
        showAtLocation(mActivity.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }
}
