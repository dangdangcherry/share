package com.itdlc.android.library.http;

import android.util.Log;

import com.itdlc.android.library.ApplicationContext;
import com.itdlc.android.library.BuildConfig;
import com.itdlc.android.library.Const;
import com.itdlc.android.library.http.adapter.DlcRxJavaFactory;
import com.itdlc.android.library.http.api.LibApiService;
import com.itdlc.android.library.http.converter.DlcGsonConverterFactory;
import com.itdlc.android.library.http.cookie.NovateCookieManger;
import com.itdlc.android.library.sp.UserSp;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * 对外的网络请求核心服务
 */

public class LibApiClient {

    private volatile static LibApiClient sInstance;

    private static LibApiClient getInstance() {
        if (sInstance == null) {
            synchronized (LibApiClient.class) {
                if (sInstance == null) {
                    sInstance = new LibApiClient();
                }
            }
        }
        return sInstance;
    }

    public void refreshApi() {
        mLibApiService = null;
    }

    private OkHttpClient buildOkHttp() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request()
                                .newBuilder()
                                .addHeader("Authorization", UserSp.getInstance().getToken(""))
                                .build();

                        Log.e("retrofitRequest", String.format("Sending request %s on %s%n%s",
                                request.url(), chain.connection(), request.headers()));
                        Response response = chain.proceed(request);
                        ResponseBody body = response.peekBody(1024 * 1024);
                        String ss = body.string();
                        Log.e("retrofitResponse", ss);
                        return response;
                    }
                }).cookieJar(new NovateCookieManger(ApplicationContext.get()));

        return builder.build();
    }

    public static LibApiService getApi() {
        return getInstance().getApiService();
    }

    private LibApiService getApiService() {
        if (mLibApiService == null) {
            mLibApiService = new Retrofit.Builder()
                    .baseUrl(Const.API_CLIENT_HOST)
                    .client(buildOkHttp())
                    .addCallAdapterFactory(DlcRxJavaFactory.getInstance())
                    .addConverterFactory(DlcGsonConverterFactory.create())
                    .build().create(LibApiService.class);
        }
        return mLibApiService;
    }

    private LibApiService mLibApiService;

}
