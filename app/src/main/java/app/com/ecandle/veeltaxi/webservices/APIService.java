package app.com.ecandle.veeltaxi.webservices;

import app.com.ecandle.veeltaxi.model.DataManager;

import retrofit.RestAdapter;

/**
 * Created by SmartCoder on 17/04/2015.
 */
public class APIService {
    public APIService() {

    }



    public static <S> S createService(Class<S> serviceClass) {
        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setEndpoint(DataManager.MAIN);
        RestAdapter adapter = builder.build();

        return adapter.create(serviceClass);
    }
}
