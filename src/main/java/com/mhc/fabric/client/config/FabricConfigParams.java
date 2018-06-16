package com.mhc.fabric.client.config;

public class FabricConfigParams {

    private FabricConfigParams(){}

    public static final String PRE = "mhc.fabric.";
    public static final String MHC_FABRIC_DYNAMODBID = PRE+"dynamodbId";
    public static final String MHC_FABRIC_DYNAMODBSECRET = PRE+"dynamodbSecret";
    public static final String MHC_FABRIC_MAXENROLLMENT = PRE+"maxEnrollment";
    public static final String MHC_FABRIC_NETWORKCONFIG = PRE+"networkConfig";
    public static final String MHC_FABRIC_STORETABLENAME = PRE+"storeTableName";
    public static final String MHC_FABRIC_ORGAFFILIATION = PRE+"orgAffiliation";
    public static final String MHC_FABRIC_PROPOSALWAITTIME = PRE+"proposalWaitTime";
    public static final String MHC_FABRIC_TRANSACTIONWAITTIME = PRE+"transactionWaitTime";
}

