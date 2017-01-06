package nl.knikit.cardgames.commons.businessrules.rules;

//import nl.ing.riaf.core.util.JNDIUtil;

import nl.knikit.cardgames.commons.businessrules.threshold.ThresholdUtil;

import javax.annotation.Resource;

import lombok.Getter;

public abstract class Rule<INPUT_TYPE> {

	/**
	 * threshold util available to the implementing classes to obtain dynamic threshold values
	 * contains NOSONAR because the protected visibility is as designed
	 */
	@Resource
	protected ThresholdUtil threshold; //NOSONAR

    //@Resource
    //private JNDIUtil util;

	/**
	 * ErrorCode which is consulted when a rule returns false
	 * contains NOSONAR because the protected visibility is as designed
	 */
	@Getter
	protected Integer errorCode = 0; //NOSONAR

    @Getter
    protected boolean enabled = true; //NOSONAR

    /**
     * Loads the configuration file to assert whether the business rule is enabled
     */
    protected final void load() {
        //final Boolean jndiValue = util.getJndiBooleanValue("param/" + this.getClass().getSimpleName() + "/enabled", false);
        final Boolean jndiValue = false;
        
        this.enabled = jndiValue == null || jndiValue;
    }

	public abstract void init();

	public abstract boolean evaluate(INPUT_TYPE input);

}