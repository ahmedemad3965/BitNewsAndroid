package com.bitnews.bitnews.data.repositories;


import android.content.Context;

import androidx.lifecycle.LiveData;

import com.bitnews.bitnews.data.db.AppDatabase;
import com.bitnews.bitnews.data.db.dao.AuthTokenDao;
import com.bitnews.bitnews.data.db.dao.UserDao;
import com.bitnews.bitnews.data.models.AuthToken;
import com.bitnews.bitnews.data.models.User;
import com.bitnews.bitnews.data.network.APIEndpoints;
import com.bitnews.bitnews.data.network.APIResponse;
import com.bitnews.bitnews.data.network.APIService;
import com.bitnews.bitnews.data.network.NetworkBoundResource;
import com.bitnews.bitnews.utils.AppExecutors;

import retrofit2.Call;

public class UserRepository {
    private static UserRepository instance;
    private AppExecutors appExecutors = AppExecutors.getInstance();
    private APIEndpoints apiEndpoints = APIService.getService();
    private UserDao userDao;
    private AuthTokenDao authTokenDao;

    public static UserRepository getInstance(Context context) {
        if (instance == null) {
            instance = new UserRepository();
            AppDatabase appDatabase = AppDatabase.getInstance(context);
            instance.userDao = appDatabase.getUserDao();
            instance.authTokenDao = appDatabase.getAuthTokenDao();
        }
        return instance;
    }

    public LiveData<APIResponse<User>> getCurrentUser() {
        return new NetworkBoundResource<User>() {
            @Override
            protected void saveToDB(User user) {
                // TODO: 2020-07-10: should change it to update
                userDao.addUser(user);
            }

            @Override
            protected boolean shouldFetchFromAPI(User data) {
                return true;
            }

            @Override
            protected User fetchFromDB() {
                return userDao.getCurrentUser();
            }

            @Override
            protected Call<User> getCall() {
                return apiEndpoints.getCurrentUser();
            }

            @Override
            protected boolean shouldFetchFromDB() {
                return true;
            }
        }.asLiveData();
    }

    public LiveData<APIResponse<User>> signupUser(String firstName, String lastName,
                                                  String userName, String password) {
        return new NetworkBoundResource<User>() {
            @Override
            protected void saveToDB(User user) {
                user.setCurrentUser(true);
                userDao.addUser(user);
            }

            @Override
            protected boolean shouldFetchFromAPI(User data) {
                return true;
            }

            @Override
            protected User fetchFromDB() {
                return null;
            }

            @Override
            protected boolean shouldFetchFromDB() {
                return false;
            }

            @Override
            protected Call<User> getCall() {
                return apiEndpoints.signUp(firstName, lastName, userName, password);
            }
        }.asLiveData();
    }

    public LiveData<APIResponse<AuthToken>> loginUser(String userName, String password) {
        return new NetworkBoundResource<AuthToken>() {
            @Override
            protected void saveToDB(AuthToken token) {
                authTokenDao.addAuthToken(token);
            }

            @Override
            protected boolean shouldFetchFromAPI(AuthToken data) {
                return true;
            }

            @Override
            protected AuthToken fetchFromDB() {
                return null;
            }

            @Override
            protected boolean shouldFetchFromDB() {
                return false;
            }

            @Override
            protected Call<AuthToken> getCall() {
                return apiEndpoints.logIn(userName, password);
            }
        }.asLiveData();
    }

    public void logoutUser() {
        appExecutors.getDiskIO().execute(() -> authTokenDao.deleteAuthToken());
    }
}