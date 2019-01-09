package org.apache.nifi.processor;

import org.apache.nifi.controller.ControllerServiceLookup;
import org.apache.nifi.controller.NodeTypeProvider;
import org.apache.nifi.logging.ComponentLog;

import java.io.File;

public class JniInitializationContext implements ProcessorInitializationContext {

    @Override
    public String getIdentifier() {
        return null;
    }

    @Override
    public ComponentLog getLogger() {
System.out.println("get log!");
        return new JniComponentLogger();
    }

    @Override
    public ControllerServiceLookup getControllerServiceLookup() {
        return null;
    }

    @Override
    public NodeTypeProvider getNodeTypeProvider() {
        return null;
    }

    @Override
    public String getKerberosServicePrincipal() {
        return null;
    }

    @Override
    public File getKerberosServiceKeytab() {
        return null;
    }

    @Override
    public File getKerberosConfigurationFile() {
        return null;
    }
}
