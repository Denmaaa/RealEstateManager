package com.openclassrooms.realestatemanager.utils.di;

import android.app.Application;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.ContentValues;
import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.openclassrooms.realestatemanager.models.local.immovables.Picture;
import com.openclassrooms.realestatemanager.models.local.immovables.Vicinity;
import com.openclassrooms.realestatemanager.models.database.Converters;
import com.openclassrooms.realestatemanager.models.database.RealEstateDB;
import com.openclassrooms.realestatemanager.models.database.dao.AgentDao;
import com.openclassrooms.realestatemanager.models.database.dao.CerDao;
import com.openclassrooms.realestatemanager.models.database.dao.ImmoDao;
import com.openclassrooms.realestatemanager.repositories.AgentDataRepository;
import com.openclassrooms.realestatemanager.repositories.CurrencyExchangeRateDataRepository;
import com.openclassrooms.realestatemanager.repositories.ImmoDataRepository;
import com.openclassrooms.realestatemanager.utils.api.AlphaVantageService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module(includes = ViewModelModule.class)
public class AppModule {

    // --- CONTEXT INJECTION ---
    @Provides
    @Singleton
    Context provideAppContext(Application application){
        return application;
    }

    // --- DB INJECTION ---
    @Provides
    @Singleton
    RealEstateDB provideDatabase(Application application){
        return Room.databaseBuilder(application,
                RealEstateDB.class, "RealEsateDB.db")
                .addCallback(prepopulateDatabase())
                .build();
    }

    private static RoomDatabase.Callback prepopulateDatabase(){
        return new RoomDatabase.Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);

                ContentValues contentValues = new ContentValues();
                contentValues.put("id", 1);
                contentValues.put("userName", "Anthony");
                contentValues.put("mailAddress", "anthony.mineo.pro@gmail.com");
                contentValues.put("urlPicture", "none");

                db.insert("Agent", OnConflictStrategy.IGNORE, contentValues);
            }
        };
    }

    @Provides
    @Singleton
    ImmoDao provideImmoDao(RealEstateDB db) { return db.immoDao(); }

    @Provides
    @Singleton
    AgentDao provideAgentDao(RealEstateDB db) { return db.agentDao(); }

    @Provides
    @Singleton
    CerDao provideCerDao(RealEstateDB db) { return db.cerDao(); }

    // --- REPOSITORY INJECTION ---
    @Provides
    Executor provideExecutor(){
        return Executors.newSingleThreadExecutor();
    }

    @Provides
    @Singleton
    ImmoDataRepository provideImmoRepository(ImmoDao immoDao, Executor executor){
        return new ImmoDataRepository(immoDao, executor);
    }

    @Provides
    @Singleton
    AgentDataRepository provideAgentRepository(AgentDao agentDao){
        return new AgentDataRepository(agentDao);
    }

    @Provides
    @Singleton
    CurrencyExchangeRateDataRepository provideCerRepository(CerDao cerDao, Executor executor, AlphaVantageService alphaVantageService){
        return new CurrencyExchangeRateDataRepository(cerDao, executor, alphaVantageService);
    }

    // --- NETWORK INJECTION ---
    private static String ALPHA_VANTAGE_URL = "https://www.alphavantage.co/";

    @Provides
    Gson provideGson() { return new GsonBuilder().create(); }

    @Provides
    Retrofit provideRetrofit(Gson gson){
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(ALPHA_VANTAGE_URL)
                .build();
        return retrofit;
    }

    @Provides
    @Singleton
    AlphaVantageService provideAlphaVantageService(Retrofit restAdapter){
        return restAdapter.create(AlphaVantageService.class);
    }
}
