package com.mhc.fabric.client;

import com.mhc.fabric.client.config.FabricConfig;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.Properties;

import static com.mhc.fabric.client.config.FabricConfigParams.MHC_FABRIC_NOSQL_DYNAMODB_ID;
import static com.mhc.fabric.client.config.FabricConfigParams.MHC_FABRIC_NOSQL_DYNAMODB_SECRET;
import static org.junit.Assert.assertEquals;


public class FabricConfigExample {
    /** Demonstration how host App will populate FabricConfig and retrieve properties from application.properties
     *
     *
     * **/
    FabricConfig fabricConfig;

    @Before
    public void setup() throws IOException {

        this.fabricConfig = getConfig();
    }

    @Test
    public void testApplicationProperties(){
        assertEquals("testid", fabricConfig.getProperty(MHC_FABRIC_NOSQL_DYNAMODB_ID));
        assertEquals("testsecret", fabricConfig.getProperty(MHC_FABRIC_NOSQL_DYNAMODB_SECRET));
    }

    private FabricConfig getConfig() throws IOException {

        FabricConfig fabricConfig = new FabricConfig();

        Resource resource = new ClassPathResource("application.properties");
        Properties properties = new Properties();
        properties.load(resource.getInputStream());

        fabricConfig.putAll(properties);

        return fabricConfig;
    }
}
