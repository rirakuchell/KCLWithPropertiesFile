package com.amazonaws.services.kinesis.kclapp.processor;

import com.amazonaws.services.kinesis.clientlibrary.interfaces.v2.IRecordProcessor;
import com.amazonaws.services.kinesis.clientlibrary.interfaces.v2.IRecordProcessorFactory;

/**
 * Used to create new stock trade record processors.
 *
 */
public class RecordProcessorFactoryWithProperties implements IRecordProcessorFactory {

    /**
     * Constructor.
     */
    public RecordProcessorFactoryWithProperties() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IRecordProcessor createProcessor() {
        return new RecordProcessorWithProperties();
    }

}
