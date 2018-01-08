package com.amazonaws.services.kinesis.kclapp.processor;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.amazonaws.services.kinesis.clientlibrary.config.KinesisClientLibConfigurator;
import com.amazonaws.services.kinesis.clientlibrary.interfaces.v2.IRecordProcessorFactory;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.KinesisClientLibConfiguration;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.Worker;

/**
 * Uses the Kinesis Client Library (KCL) to continuously consume and process stock trade
 * records from the stock trades stream. KCL monitors the number of shards and creates
 * record processor instances to read and process records from each shard. KCL also
 * load balances shards across all the instances of this processor.
 *
 */
public class ProcessorWithProperties {

    private static final Log LOG = LogFactory.getLog(ProcessorWithProperties.class);
        
    private static final Logger ROOT_LOGGER = Logger.getLogger("");
    private static final Logger PROCESSOR_LOGGER =
            Logger.getLogger("com.amazonaws.services.kinesis.kclapp.processor");

    /**
     * Sets the global log level to WARNING and the log level for this package to INFO,
     * so that we only see INFO messages for this processor. This is just for the purpose
     * of this tutorial, and should not be considered as best practice.
     *
     */
    private static void setLogLevels() {
        
        ROOT_LOGGER.setLevel(Level.ALL);
        PROCESSOR_LOGGER.setLevel(Level.INFO);
    }

    private static Properties loadPropertiesFile(String path) {
        final Properties prop = new Properties();
        InputStream inStream = null;
        
        try {
            //inStream = new BufferedInputStream(
        	//    new FileInputStream(path));
            inStream = ProcessorWithProperties.class.getClassLoader()
                    .getResourceAsStream(path);
            
            prop.load(inStream);

        } catch (IOException e) {
        	LOG.error(e.getStackTrace().toString());
        } finally {
            try {
                if (inStream != null) {
                    inStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        return prop;
    }
    
    public static void main(String[] args) throws Exception {
    	
    	LOG.info("Starting processor.");
    	
        setLogLevels();

        //String workerId = InetAddress.getLocalHost().getCanonicalHostName() + ":" + UUID.randomUUID();
        
        // https://github.com/awslabs/amazon-kinesis-client/blob/master/src/main/java/com/amazonaws/services/kinesis/clientlibrary/lib/worker/KinesisClientLibConfiguration.java
        KinesisClientLibConfigurator configurator = new KinesisClientLibConfigurator();
        KinesisClientLibConfiguration kclConfig = configurator.getConfiguration(loadPropertiesFile("kcl.properties"));

        IRecordProcessorFactory recordProcessorFactory = new RecordProcessorFactoryWithProperties();

        // Create the KCL worker with the stock trade record processor factory
        final Worker worker = new Worker.Builder()
        	    .recordProcessorFactory(recordProcessorFactory)
        	    .config(kclConfig)
        	    .build();

        int exitCode = 0;
        try {
            worker.run();
        } catch (Throwable t) {
            LOG.error("Caught throwable while processing data.", t);
            exitCode = 1;
        }
        System.exit(exitCode);

    }

}
