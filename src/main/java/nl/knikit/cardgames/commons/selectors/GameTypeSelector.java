package nl.knikit.cardgames.commons.selectors;

import nl.knikit.cardgames.commons.selectors.ing.AccountType;
import nl.knikit.cardgames.commons.selectors.ing.PortfolioCode;
import nl.knikit.cardgames.commons.selectors.ing.ProductType;

import java.util.ArrayList;
import java.util.List;

import javax.management.InvalidAttributeValueException;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static nl.knikit.cardgames.commons.selectors.GameTypeSelector.PricingProgram.LimitType.EQUAL_OR_MORE;
import static nl.knikit.cardgames.commons.selectors.GameTypeSelector.PricingProgram.LimitType.LESS;
import static nl.knikit.cardgames.commons.selectors.ing.AccountType.MEMBER;
import static nl.knikit.cardgames.commons.selectors.ing.AccountType.POSITION;
import static nl.knikit.cardgames.commons.selectors.ing.AccountType.REGULAR;
import static nl.knikit.cardgames.commons.selectors.ing.PortfolioCode.CREDITCARD_CHARGE;
import static nl.knikit.cardgames.commons.selectors.ing.PortfolioCode.CREDITCARD_REVOLVING;
import static nl.knikit.cardgames.commons.selectors.ing.PortfolioCode.PLATINUMCARD_CHARGE;
import static nl.knikit.cardgames.commons.selectors.ing.PortfolioCode.PLATINUMCARD_REVOLVING;
import static nl.knikit.cardgames.commons.selectors.ing.ProductType.CREDITCARD;
import static nl.knikit.cardgames.commons.selectors.ing.ProductType.PLATINUMCARD;


/**
 * PricingProgram selector selects the correct list of PricingPrograms that is required to change
 * the 'price' of a creditcard via the POSITION, MEMBER or REGULAR creditcard account. <p> The
 * 'price' that can be changed by sending a PricingProgram for a creditcard account to SIA are: <p>-
 * membershipFee (eg. € 17,40), <p>- interestRate (eg. 12,4%), <p>- minimumPaymentCalculation (eg.
 * 100% or 5%min45e), <p>- atmFee (eg. € 4,50) etc <p> NB Repricing means sending one or more a
 * pricing programs to SIA to replace the current pricing programs.
 */
public class GameTypeSelector {
	
	public List<String> findAllForChangeRepayment(ProductType checkProductType, AccountType checkAccountType, Integer checkNewCreditLimit, PortfolioCode checkNewPortfolioCode) throws InvalidAttributeValueException {
		
		checkAttributeValueConsistency(checkProductType, checkNewPortfolioCode);
		return findAll(checkProductType, checkAccountType, checkNewCreditLimit, checkNewPortfolioCode);
	}
	
	public List<String> findAllForChangeLimit(ProductType checkProductType, AccountType checkAccountType, int checkNewCreditLimit) {
		
		return findAll(checkProductType, checkAccountType, checkNewCreditLimit, null);
	}
	
	private List<String> findAll(ProductType checkProductType, AccountType checkAccountType, Integer checkNewCreditLimit, PortfolioCode checkNewPortfolioCode) {
		
		List<String> selected = new ArrayList<>();
		String found;
		
		for (PricingProgram findPricingProgram : PricingProgram.values()) {
			found = PricingProgram.find(findPricingProgram, checkProductType, checkAccountType, checkNewCreditLimit, checkNewPortfolioCode);
			if (found != null) {
				selected.add(found);
			}
		}
		return selected;
	}
	
	private void checkAttributeValueConsistency(ProductType checkProductType, PortfolioCode checkNewPortfolioCode) throws InvalidAttributeValueException {
		if (checkProductType == PLATINUMCARD) {
			if (checkNewPortfolioCode == CREDITCARD_CHARGE || checkNewPortfolioCode == CREDITCARD_REVOLVING) {
				throw new InvalidAttributeValueException("Supplied ProductType PLATINUMCARD not consistent with supplied PortfolioCode CREDITCARD");
			}
		} else {
			if (checkNewPortfolioCode == PLATINUMCARD_CHARGE || checkNewPortfolioCode == PLATINUMCARD_REVOLVING) {
				throw new InvalidAttributeValueException("Supplied ProductType CREDITCARD not consistent with supplied PortfolioCode PLATINUMCARD");
			}
		}
	}
	
	@AllArgsConstructor
	@Getter
	public enum PricingProgram {
		
		// TO_100 = 100 % repayment every month
		REPRICING_01_REPAYMENT_TO_100(CREDITCARD, REGULAR, null, CREDITCARD_CHARGE, "RFUL01"),
		REPRICING_02_REPAYMENT_TO_100(PLATINUMCARD, REGULAR, null, PLATINUMCARD_CHARGE, "RFUL01"),
		REPRICING_03_REPRICING_TO_100(CREDITCARD, POSITION, null, CREDITCARD_CHARGE, "RNWEBF"),
		REPRICING_04_REPAYMENT_TO_100(PLATINUMCARD, POSITION, null, PLATINUMCARD_CHARGE, "RNWEBF"),
		REPRICING_05_REPRICING_TO_100(CREDITCARD, MEMBER, null, CREDITCARD_CHARGE, "RNWEBF"),
		REPRICING_06_REPAYMENT_TO_100(PLATINUMCARD, MEMBER, null, PLATINUMCARD_CHARGE, "RNWEBF"),
		
		// TO_5MIN45 = 5% with a minimum of 45 euro every month
		REPRICING_07_REPAYMENT_TO_5MIN45(CREDITCARD, REGULAR, null, CREDITCARD_REVOLVING, "RREV01"),
		REPRICING_08_REPAYMENT_TO_5MIN45(PLATINUMCARD, REGULAR, null, PLATINUMCARD_REVOLVING, "RREV02"),
		REPRICING_09_REPAYMENT_TO_5MIN45(CREDITCARD, POSITION, null, CREDITCARD_REVOLVING, "RNWEBR"),
		REPRICING_10_REPAYMENT_TO_5MIN45(PLATINUMCARD, POSITION, null, PLATINUMCARD_REVOLVING, "RNWEGR"),
		REPRICING_11_REPAYMENT_TO_5MIN45(CREDITCARD, MEMBER, null, CREDITCARD_REVOLVING, "RNWEBR"),
		REPRICING_12_REPAYMENT_TO_5MIN45(PLATINUMCARD, MEMBER, null, PLATINUMCARD_REVOLVING, "RNWEGR"),
		
		// TO_139INTEREST = lower limits have higher interest rates
		REPRICING_13_INTEREST_TO_139(CREDITCARD, REGULAR, LESS, null, "RINT01"),
		REPRICING_14_INTEREST_TO_139(PLATINUMCARD, REGULAR, LESS, null, "RINT01"),
		REPRICING_15_INTEREST_TO_139(CREDITCARD, POSITION, LESS, null, "RINTBL"),
		REPRICING_16_INTEREST_TO_139(PLATINUMCARD, POSITION, LESS, null, "RINTBL"),
		REPRICING_17_INTEREST_TO_139(CREDITCARD, MEMBER, LESS, null, "RINTBL"),
		REPRICING_18_INTEREST_TO_139(PLATINUMCARD, MEMBER, LESS, null, "RINTBL"),
		
		// TO_124INTEREST = higher limits have higher interest rates
		REPRICING_19_INTEREST_TO_124(CREDITCARD, REGULAR, EQUAL_OR_MORE, null, "RINT07"),
		REPRICING_20_INTEREST_TO_124(PLATINUMCARD, REGULAR, EQUAL_OR_MORE, null, "RINT07"),
		REPRICING_21_INTEREST_TO_124(CREDITCARD, POSITION, EQUAL_OR_MORE, null, "RINTGD"),
		REPRICING_22_INTEREST_TO_124(PLATINUMCARD, POSITION, EQUAL_OR_MORE, null, "RINTGD"),
		REPRICING_23_INTEREST_TO_124(CREDITCARD, MEMBER, EQUAL_OR_MORE, null, "RINTGD"),
		REPRICING_24_INTEREST_TO_124(PLATINUMCARD, MEMBER, EQUAL_OR_MORE, null, "RINTGD");
		
		public enum LimitType {LESS, EQUAL_OR_MORE}
		
		private static int LIMIT_BOUNDARY = 5000;
		
		private ProductType productType;
		private AccountType accountType;
		private LimitType creditLimit;
		private PortfolioCode portfolioCode;
		
		public String resultingPricingProgram;
		
		private static String find(PricingProgram findPricingProgram, ProductType checkProductType, AccountType checkAccountType, Integer checkCreditLimit, PortfolioCode checkPortfolioCode) {
			
			if (matchingProductType(findPricingProgram, checkProductType) &&
					    matchingAccountType(findPricingProgram, checkAccountType) &&
					    matchingCreditLimit(findPricingProgram, checkCreditLimit) &&
					    matchingPortfolioCode(findPricingProgram, checkPortfolioCode)) {
				return findPricingProgram.resultingPricingProgram;
			}
			return null;
		}
		
		static boolean matchingProductType(PricingProgram matchPricingProgram, ProductType checkProductType) {
			return matchPricingProgram.getProductType().equals(checkProductType);
		}
		
		static boolean matchingAccountType(PricingProgram matchPricingProgram, AccountType checkAccountType) {
			return matchPricingProgram.getAccountType().equals(checkAccountType);
		}
		
		static boolean matchingCreditLimit(PricingProgram matchPricingProgram, Integer newCreditLimit) {
			if (matchPricingProgram.getCreditLimit() == null) {
				// null means 'any' -> always a positive match
				return true;
			}
			if (newCreditLimit == null) {
				return matchPricingProgram.getCreditLimit() == null;
			}
			if (matchPricingProgram.getCreditLimit() == EQUAL_OR_MORE) {
				return newCreditLimit >= LIMIT_BOUNDARY;
			} else {
				return newCreditLimit < LIMIT_BOUNDARY;
			}
		}
		
		static boolean matchingPortfolioCode(PricingProgram matchPricingProgram, PortfolioCode newPortfolioCode) {
			if (matchPricingProgram.getPortfolioCode() == null) {
				// null means 'any' -> always a positive match
				return true;
			}
			if (newPortfolioCode == null) {
				return matchPricingProgram.getPortfolioCode() == null;
			}
			return matchPricingProgram.getPortfolioCode().equals(newPortfolioCode);
		}
	}
}


