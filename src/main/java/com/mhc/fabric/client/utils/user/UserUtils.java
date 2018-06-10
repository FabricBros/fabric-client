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
import org.hyperledger.fabric_ca.sdk.exception.EnrollmentException;
import org.hyperledger.fabric_ca.sdk.exception.RegistrationException;

import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;

public class UserUtils {

    private static Logger logger = Logger.getLogger(UserUtils.class);

    NetworkConfig networkConfig;
    SampleStore store;
    String mspId;
    FabricConfig fabricConfig;
    String orgName;


    public UserUtils(FabricConfig fabricConfig, NetworkConfig networkConfig, SampleStore sampleStore){
        this.networkConfig = networkConfig;
        this.store = sampleStore;
        this.mspId = networkConfig.getClientOrganization().getMspId();
        this.orgName = networkConfig.getClientOrganization().getName();
        this.fabricConfig = fabricConfig;
    }

    public SampleUser getMember(String user){
        return null;
    }

    /** Register's user, should throw exception if user is already registered
     * **/
    public SampleUser registerUser(String user, String pw, String affiliation) throws org.hyperledger.fabric_ca.sdk.exception.InvalidArgumentException, EnrollmentException, MalformedURLException, RegistrationException {
        NetworkConfig.CAInfo caInfo = networkConfig.getClientOrganization().getCertificateAuthorities().get(0);

        SampleUser admin = getAdmin();
        SampleUser newUser = new SampleUser(user, orgName, store);

        if(newUser.isRegistered()){
            throw new IllegalArgumentException("user already registered");
        }

        RegistrationRequest rr = null;
        try {
            rr = RegistrationRequestUtil.getMemberRR(user, pw, affiliation, fabricConfig);
        } catch (Exception e) {
            logger.error(e);
            throw new IllegalArgumentException(e);
        }

        //TODO figure out ways to implement different types of RR here

        newUser.setEnrollmentSecret(getHFCAClient(caInfo).register(rr, admin));
        return newUser;

    }

    public SampleUser getAdmin() throws MalformedURLException, org.hyperledger.fabric_ca.sdk.exception.InvalidArgumentException, EnrollmentException {
        //check store
        NetworkConfig.CAInfo caInfo = networkConfig.getClientOrganization().getCertificateAuthorities().get(0);
        NetworkConfig.UserInfo userInfo = caInfo.getRegistrars().iterator().next();
        String adminName = userInfo.getName();
        String adminSecret = userInfo.getEnrollSecret();

        //check store and make sure admin is enrolled
        if(store.hasMember(adminName, orgName) && store.getMember(adminName, orgName).isEnrolled()){
            return store.getMember(adminName, orgName);
        }

        //If not in store, get enrollment from ca and save to store
        try {
            HFCAClient hfcaClient = getHFCAClient(networkConfig.getClientOrganization().getCertificateAuthorities().get(0));
            userInfo.setEnrollment(hfcaClient.enroll(adminName, adminSecret));
        } catch (MalformedURLException e) {
            logger.error(e);
            throw e;
        } catch (org.hyperledger.fabric_ca.sdk.exception.InvalidArgumentException e) {
            logger.error(e);
            throw e;
        } catch (EnrollmentException e) {
            logger.error(e);
            throw e;
        }
        //TODO Due to stupid sdk, we'll have to do this mapping, figure out a more efficient mapper
        SampleUser adminUser = new SampleUser(adminName, orgName, store);
        adminUser.setEnrollment(userInfo.getEnrollment());
        adminUser.setEnrollmentSecret(userInfo.getEnrollSecret());

        return adminUser;
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

    public HFCAClient getHFCAClient(NetworkConfig.CAInfo caInfo) throws MalformedURLException, org.hyperledger.fabric_ca.sdk.exception.InvalidArgumentException {
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
