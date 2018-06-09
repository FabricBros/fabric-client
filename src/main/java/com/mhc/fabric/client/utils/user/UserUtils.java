package com.mhc.fabric.client.utils.user;


import com.mhc.fabric.client.config.FabricConfig;
import com.mhc.fabric.client.models.SampleStore;
import com.mhc.fabric.client.models.SampleUser;
import org.apache.log4j.Logger;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.NetworkConfig;
import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.hyperledger.fabric_ca.sdk.RegistrationRequest;

import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;

public class UserUtils {

    private static Logger logger = Logger.getLogger(UserUtils.class);

    NetworkConfig networkConfig;
    SampleStore store;
    String mspId;
    FabricConfig fabricConfig;


    public UserUtils(FabricConfig fabricConfig, NetworkConfig networkConfig, SampleStore sampleStore){
        this.networkConfig = networkConfig;
        this.store = sampleStore;
        this.mspId = networkConfig.getClientOrganization().getMspId();
        this.fabricConfig = fabricConfig;
    }

    public SampleUser getMember(String user){
        return null;
    }

    /** Register's user, should throw exception if user is already registered
     * **/
    public SampleUser registerUser(String user, String pw, String affiliation) throws Exception {
        NetworkConfig.CAInfo caInfo = networkConfig.getClientOrganization().getCertificateAuthorities().get(0);

        SampleUser admin = store.getMember("admin", mspId);
        SampleUser newUser = store.getMember(user, networkConfig.getClientOrganization().getName());

        if(newUser.isRegistered()){
            throw new IllegalArgumentException("user already registered");
        }

//        RegistrationRequest rr = new RegistrationRequest(user, affiliation);
        RegistrationRequest rr = RegistrationRequestUtil.getMemberRR(user, pw, affiliation, fabricConfig);
        rr.setSecret(pw);

        //TODO figure out ways to implement different types of RR here

        newUser.setEnrollmentSecret(getHFCAClient(caInfo).register(rr, admin));
        return newUser;

    }

    public Enrollment enrollUser(SampleUser userToEnroll) throws Exception {
        if(!userToEnroll.isRegistered()){
            throw new IllegalArgumentException("user is not registered");
        }
        if(userToEnroll.isEnrolled()){
            throw new IllegalArgumentException("user is already enrolled");
        }
        HFCAClient ca;
        try {
            ca = getHFCAClient(networkConfig.getClientOrganization().getCertificateAuthorities().get(0));
        } catch (MalformedURLException|org.hyperledger.fabric_ca.sdk.exception.InvalidArgumentException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            throw new Exception(e.getMessage());
        }


        Enrollment enrollment = ca.enroll(userToEnroll.getName(), userToEnroll.getEnrollmentSecret());
        userToEnroll.setEnrollment(enrollment);
        return enrollment;
    }

    private HFCAClient getHFCAClient(NetworkConfig.CAInfo caInfo) throws MalformedURLException, org.hyperledger.fabric_ca.sdk.exception.InvalidArgumentException {
        HFCAClient hfcaClient = HFCAClient.createNewInstance(caInfo);

        return hfcaClient;
    }

    public static HFClient getHFClient(){
        HFClient client = HFClient.createNewInstance();
        try {
            client.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());
        } catch (CryptoException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } catch (InvalidArgumentException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } catch (InstantiationException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return client;
    }
}
