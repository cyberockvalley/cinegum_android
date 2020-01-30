package com.jinminetics.cinegum.utils;

import android.os.Build;

import com.jinminetics.cinegum.BuildConfig;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.TlsVersion;
import okio.Buffer;

public class OkhttpBrowser {
    private Response lastResponse;
    private OkHttpClient client;
    private String fullname;
    private static final String FULL_NAME_FORMAT = "%s %";
    public static final String APP_NAME = "Screentagram";
    public static final String APP_VERSION = BuildConfig.VERSION_NAME;
    public static final String USER_AGENT = "Mozilla/5.0 (Linux; Android " + Build.VERSION.RELEASE + "; " + Build.MODEL + " Build/" + Build.ID + ") AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.133 Mobile Safari/535.19";
    /*
            String.format(APP_NAME+"/"+APP_VERSION+" (%s) Mobile",
                    String.format("Linux; Android %s; %s Build/%s",
                            Build.VERSION.RELEASE,
                            Build.MODEL,
                            Build.ID));*/
    private final HashMap<String, Cookie> cookieStore = new HashMap();

    public OkhttpBrowser(){

    }

    public static String getCodePhrase(int responseCode) {
        if(responseCode == 200){
            return "ok";
        } else {
            return "unknown";
        }
    }

    public void setup() {
        Interceptor logging = new LoggingInterceptor();

        ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.COMPATIBLE_TLS)
                .tlsVersions(TlsVersion.TLS_1_2, TlsVersion.TLS_1_1, TlsVersion.TLS_1_0, TlsVersion.TLS_1_3,
                        TlsVersion.SSL_3_0)
                .cipherSuites(
                        CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_ECDHE_ECDSA_WITH_CHACHA20_POLY1305_SHA256,
                        CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA,
                        CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA)
                .build();
        TLSSocketFactory tlsSocketFactory = null;
        try {
            tlsSocketFactory = new TLSSocketFactory();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        this.client = (new OkHttpClient.Builder()).cookieJar(new CookieJar() {
            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                if (cookies != null) {
                    Iterator var3 = cookies.iterator();

                    while(var3.hasNext()) {
                        Cookie cookie = (Cookie)var3.next();
                        OkhttpBrowser.this.cookieStore.put(cookie.name(), cookie);
                    }
                }

            }

            public List<Cookie> loadForRequest(HttpUrl url) {
                List<Cookie> validCookies = new ArrayList();
                Iterator var3 = OkhttpBrowser.this.cookieStore.entrySet().iterator();

                while(var3.hasNext()) {
                    Map.Entry<String, Cookie> entry = (Map.Entry)var3.next();
                    Cookie cookie = (Cookie)entry.getValue();
                    if (cookie.expiresAt() >= System.currentTimeMillis()) {
                        validCookies.add(cookie);
                    }
                }

                return validCookies;
            }
        }).sslSocketFactory(tlsSocketFactory)
                .addInterceptor(logging).build();
    }

    public class LoggingInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Response response = chain.proceed(request);

            if (request.method().equals("POST")) {
                StaticMethods.log("Interceptor", bodyToString(request));
            }

            return response;
        }
    }

    private static String bodyToString(final Request request){

        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            copy.body().writeTo(buffer);
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }

    public Response getLastResponse() {
        return lastResponse;
    }

    public void setLastResponse(Response lastResponse) {
        this.lastResponse = lastResponse;
    }

    public OkHttpClient getClient() {
        return client;
    }

    public void setClient(OkHttpClient client) {
        this.client = client;
    }

    public HashMap<String, Cookie> getCookieStore() {
        return cookieStore;
    }
}
