package net.anotheria.anosite.guard;

import javax.servlet.http.HttpServletRequest;

import net.anotheria.anosite.guard.ConditionalGuard;
import net.anotheria.asg.data.DataObject;
import net.anotheria.asg.exception.ASGRuntimeException;

import org.configureme.ConfigurationManager;

/**
 * Abstract class that provides common methods and String constants for 
 * all derived guards.
 * 
 * @author Alex Osadchy
 *
 */
public abstract class SystemEnvironmentAbstractGuard implements ConditionalGuard {
    
    /**
     * Override this method to provide name of target environment.
     * 
     * @return name of the environment
     */
    protected abstract String getTargetEnvironmentName();
    
    /**
     * Override this method in a way that it would return true if 
     * environments should match, false otherwise.
     * 
     * @return boolean value.
     */
    protected abstract boolean shouldMatch();
    
    @Override
    public final boolean isConditionFullfilled(DataObject object, HttpServletRequest req) throws ASGRuntimeException {
       return getEnvironmentName().startsWith(getTargetEnvironmentName()) == shouldMatch();
    }
    
    /**
     * Extracts environment name configured by ConfigureMe.
     * 
     * @return {@link String} with environment name
     */
    private String getEnvironmentName() {
        return ConfigurationManager.INSTANCE.getDefaultEnvironment().expandedStringForm();
    }  
   
}
