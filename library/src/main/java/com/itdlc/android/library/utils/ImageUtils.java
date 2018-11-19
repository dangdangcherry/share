package com.itdlc.android.library.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.DrawableRes;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.itdlc.android.library.ApplicationContext;
import com.itdlc.android.library.Const;
import com.itdlc.android.library.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * Created by Administrator on 2017/8/18.
 */

public class ImageUtils {

    private static String TAG = "ImageUtils";
    private static String IMG_BASE = Const.API_CLIENT_HOST_SHORT;

    //将imageurl转换成完整的地址
    public static String transformUrl(String url) {

        if (url == null || url.length() == 0) {
            return url;
        }
        String newUrl = url;
        if (url.startsWith("/upload")) {
            newUrl = IMG_BASE + url;
        } else if (url.startsWith("upload")) {
            newUrl = IMG_BASE + "/" + url;
        }

        Log.d(TAG, newUrl);

        return newUrl;

    }

    //将imageurl转换成完整的地址
    public static String transformAllUrl(String url) {

        if (url == null || url.length() == 0) {
            return url;
        }
        if (url.contains("/upload")) {
            return url.replaceAll("/upload", IMG_BASE + "/upload");
        }
        return url.replaceAll("upload", IMG_BASE + "/upload");
    }

    // 普通带默认图标
    public static void loadImageUrl(Context mContextm, ImageView imageView, String url) {

        RequestOptions requestOptions = RequestOptions.placeholderOf(R.mipmap.defalut_bg);
        Glide.with(mContextm).
                load(transformUrl(url))
                .apply(requestOptions)
                .into(imageView);
    }

    // 普通带默认图标
    public static void loadImageUrlNo(Context mContextm, ImageView imageView, String url) {

        Glide.with(mContextm).
                load(transformUrl(url))
                .into(imageView);
    }

    // 普通带默认图标不带缓存
    public static void loadImageUrlNone(ImageView imageView, String fileName) {
        FileInputStream fis = null;
        File file = new File(fileName);
        if (!file.exists()) {
            imageView.setImageBitmap(null);
            return;
        }
        try {
            fis = new FileInputStream(fileName);
            Bitmap bitmap = BitmapFactory.decodeStream(fis);
            imageView.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    // 加载圆形且带默认图标
    public static void loadCircleImageUrl(Context context, ImageView imageView, String url) {

        RequestOptions requestOptions = RequestOptions.circleCropTransform()
                .placeholder(R.mipmap.ic_head_default)
                .dontAnimate();

        Glide.with(context)
                .load(transformUrl(url))
                .apply(requestOptions)
                .into(imageView);
    }

    public static void loadCircleImageUrl(Context context, ImageView imageView, String url, int placeId) {

        RequestOptions requestOptions = RequestOptions.circleCropTransform()
                .placeholder(placeId)
                .dontAnimate();

        Glide.with(context)
                .load(transformUrl(url))
                .apply(requestOptions)
                .into(imageView);
    }

    public static void loadBlurBackgroundUrl(final View view, String url) {
        RequestOptions requestOptions = RequestOptions
                .bitmapTransform(new BlurTransformation(25, 4));
        Glide.with(ApplicationContext.get())
                .load(transformUrl(url))
                .apply(requestOptions)  // “23”：设置模糊度(在0.0到25.0之间)，默认”25";"4":图片缩放比例,默认“1”。
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                        view.setBackground(resource);
                    }
                });
    }

    public static void loadBlurBackgroundUrl(Context mContextm, final View view, @DrawableRes int res) {

        RequestOptions requestOptions = RequestOptions
                .bitmapTransform(new BlurTransformation(25, 4));

        Glide.with(mContextm)
                .load(res)
                .apply(requestOptions) // “23”：设置模糊度(在0.0到25.0之间)，默认”25";"4":图片缩放比例,默认“1”。
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                        view.setBackground(resource);
                    }
                });
    }


    public static void loadBlurBackgroundUrl(Context mContextm, ImageView view, String url, int placeId) {
        RequestOptions requestOptions = RequestOptions
                .bitmapTransform(new BlurTransformation(14, 3));
        requestOptions.placeholder(placeId);
        Glide.with(mContextm)
                .load(transformUrl(url))
                .apply(requestOptions) // “23”：设置模糊度(在0.0到25.0之间)，默认”25";"4":图片缩放比例,默认“1”。
                .into(view);
    }

    // 普通带默认图标
    public static void loadImageUrl(Context mContextm, ImageView imageView, String url, int placeId) {
        Glide.with(mContextm)
                .load(transformUrl(url))
                .apply(RequestOptions.placeholderOf(placeId))
                .into(imageView);
    }

    // 普通带默认图标并且图片centerCrop
    public static void loadCenterCropImageUrl(Context mContextm, ImageView imageView, String url, int placeId) {

        RequestOptions placeholder = RequestOptions.circleCropTransform().placeholder(placeId);

        Glide.with(mContextm)
                .load(transformUrl(url))
                .apply(placeholder)
                .into(imageView);
    }

    public static String getPhotopath(String name) {
        String pathUrl = Environment.getExternalStorageDirectory() + "/wswsvipnew/";
        String imageName = name + ".jpg";
        File file = new File(pathUrl);
        if (!file.exists()) {
            file.mkdirs();
        }
        String fileName = pathUrl + imageName;
        return fileName;
    }

    //base64位转码
    public static String bitmaptoString(Bitmap bitmap) {
        String string = null;
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 95, bStream);
        byte[] bytes = bStream.toByteArray();
        string = Base64.encodeToString(bytes, Base64.DEFAULT);
        return string;
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
        //第一次采样
        BitmapFactory.Options options = new BitmapFactory.Options();
        //该属性设置为true只会加载图片的边框进来，并不会加载图片具体的像素点
        options.inJustDecodeBounds = true;
        //第一次加载图片，这时只会加载图片的边框进来，并不会加载图片中的像素点
        BitmapFactory.decodeFile(filePath, options);
        //获得原图的宽和高
        int outWidth = options.outWidth;
        int outHeight = options.outHeight;
        //定义缩放比例
        int sampleSize = 1;
        while (outHeight / sampleSize > destHeight || outWidth / sampleSize > destWidth) {
            //如果宽高的任意一方的缩放比例没有达到要求，都继续增大缩放比例
            //sampleSize应该为2的n次幂，如果给sampleSize设置的数字不是2的n次幂，那么系统会就近取值
            sampleSize *= 2;
        }
        /********************************************************************************************/
        //至此，第一次采样已经结束，我们已经成功的计算出了sampleSize的大小
        /********************************************************************************************/
        //二次采样开始
        //二次采样时我需要将图片加载出来显示，不能只加载图片的框架，因此inJustDecodeBounds属性要设置为false
        options.inJustDecodeBounds = false;
        //设置缩放比例
        options.inSampleSize = sampleSize;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        //加载图片并返回
        return BitmapFactory.decodeFile(filePath, options);
    }

    /**
     * 根据Uri压缩图片
     *
     * @param uri        要加载的图片路径
     * @param destWidth  显示图片的控件宽度
     * @param destHeight 显示图片的控件的高度
     * @return
     */
    public static Bitmap getBitmap(Uri uri, Activity activity, int destWidth, int destHeight) {
        //第一次采样
        BitmapFactory.Options options = new BitmapFactory.Options();
        //该属性设置为true只会加载图片的边框进来，并不会加载图片具体的像素点
        options.inJustDecodeBounds = true;
        //第一次加载图片，这时只会加载图片的边框进来，并不会加载图片中的像素点
        InputStream input = null;
        try {
            input = activity.getContentResolver().openInputStream(uri);
            BitmapFactory.decodeStream(input, null, options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {

            try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //获得原图的宽和高
        int outWidth = options.outWidth;
        int outHeight = options.outHeight;
        //定义缩放比例
        int sampleSize = 1;
        while (outHeight / sampleSize > destHeight || outWidth / sampleSize > destWidth) {
            //如果宽高的任意一方的缩放比例没有达到要求，都继续增大缩放比例
            //sampleSize应该为2的n次幂，如果给sampleSize设置的数字不是2的n次幂，那么系统会就近取值
            sampleSize *= 2;
        }
        /********************************************************************************************/
        //至此，第一次采样已经结束，我们已经成功的计算出了sampleSize的大小
        /********************************************************************************************/
        //二次采样开始
        //二次采样时我需要将图片加载出来显示，不能只加载图片的框架，因此inJustDecodeBounds属性要设置为false
        options.inJustDecodeBounds = false;
        //设置缩放比例
        options.inSampleSize = sampleSize;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        //加载图片并返回
        Bitmap bitmap = null;
        try {
            input = activity.getContentResolver().openInputStream(uri);
            bitmap = BitmapFactory.decodeStream(input, null, options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {

            try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    /**
     * 保存bitmap到本地
     *
     * @param mBitmap
     * @return
     */
    public static String saveBitmap(Bitmap mBitmap, String savePath) {
        File filePic = null;

        try {
            filePic = new File(savePath);
            if (!filePic.exists()) {
                filePic.getParentFile().mkdirs();
                filePic.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(filePic);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

        return filePic.getAbsolutePath();
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
}
