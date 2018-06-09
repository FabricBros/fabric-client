package com.mhc.fabric.client.utils.chaincode;

import com.mhc.fabric.client.models.ChaincodeInfo;
import org.hyperledger.fabric.sdk.HFClient;

import java.util.concurrent.CompletableFuture;

public class CCStub {

    /**
     * Return the string payload
     * **/
    public String query(HFClient caller, String fcn, String[] args, ChaincodeInfo chaincodeInfo){
        return null;
    }

    /**
     * Return the txId
     * **/
    public CompletableFuture<String> invoke(HFClient caller, String fcn, String[] args, ChaincodeInfo chaincodeInfo){
        return null;
    }

}
