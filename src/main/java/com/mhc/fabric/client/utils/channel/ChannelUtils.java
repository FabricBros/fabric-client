package com.mhc.fabric.client.utils.channel;

import org.apache.log4j.Logger;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.NetworkConfig;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.NetworkConfigurationException;
import org.hyperledger.fabric.sdk.exception.TransactionException;

public class ChannelUtils {
    private static Logger logger = Logger.getLogger(ChannelUtils.class);
    //use a cache service to cache channels, initialization of channel has a long latency TODO

    public static Channel constructChannel(HFClient hfClient, NetworkConfig networkConfig, String channelName) throws InvalidArgumentException, NetworkConfigurationException, TransactionException {
        Channel channel;
        logger.debug("constructing channel");

        try {

            channel = hfClient.loadChannelFromConfig(channelName, networkConfig);
            if(channel == null){
                throw new NetworkConfigurationException("Channel "+channelName+" cannot be found.");
            }
        } catch (InvalidArgumentException e) {
            e.printStackTrace();
            logger.error(e);
            throw e;
        } catch (NetworkConfigurationException e) {
            e.printStackTrace();
            logger.error(e);
            throw e;
        }

        if(channel.isInitialized()){
            return channel;
        }
        return channel.initialize();
    }
}
