package nl.knikit.cardgames.commons.businessrules.threshold;

//import nl.ing.riaf.core.util.JNDIUtil;

import org.springframework.stereotype.Service;

/**
 * Purpose of this wrapper class is to allow ease of changeability when a different source is selected
 */
@Service
public class ThresholdUtil {

	//@Resource
	//private JNDIUtil jndiUtil;

	/**
	 * @param threshold will be concatenated with param, so in the end it will look like "param/" + threshold
	 */
	public String obtainThreshold(final String threshold) {
		
		//return jndiUtil.getJndiValue(threshold);
		return "true";
	}
}