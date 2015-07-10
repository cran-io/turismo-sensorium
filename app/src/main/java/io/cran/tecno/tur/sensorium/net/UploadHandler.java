package io.cran.tecno.tur.sensorium.net;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.client.Response;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.mime.TypedFile;

/**
 * Created by lbalmaceda on 7/10/15.
 */
public class UploadHandler {

    private static RestAdapter mApiAdapter;
    private static ApiService mApiService;

    private static final String ENDPOINT = "http://www.cran.io";

    public static ApiService getInstance() {
        if (mApiService == null) {
            mApiAdapter = new RestAdapter.Builder()
                    .setEndpoint(ENDPOINT)
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .build();
            mApiService = mApiAdapter.create(ApiService.class);
        }
        return mApiService;
    }

    public interface ApiService {
        @Multipart
        @POST("/upload")
        void upload(@Part("picture") TypedFile picture, Callback<Response> cb);
    }
}
