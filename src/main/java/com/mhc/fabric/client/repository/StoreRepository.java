package com.mhc.fabric.client.repository;

public class StoreRepository {

    private Repository repository;

    private static StoreRepository instance;

    private StoreRepository(Repository repository){

    }

    public static StoreRepository getRepo(Repository repository){
        if(instance != null){
            return instance;
        }else{
            instance = new StoreRepository(repository);
            return instance;
        }
    }

}
