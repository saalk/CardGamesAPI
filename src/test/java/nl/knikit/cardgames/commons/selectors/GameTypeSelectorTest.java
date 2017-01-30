package nl.knikit.cardgames.commons.selectors;

import nl.knikit.cardgames.commons.selectors.ing.AccountType;
import nl.knikit.cardgames.commons.selectors.ing.PortfolioCode;
import nl.knikit.cardgames.commons.selectors.ing.ProductType;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import javax.management.InvalidAttributeValueException;

import static nl.knikit.cardgames.commons.selectors.ing.PortfolioCode.CREDITCARD_CHARGE;
import static nl.knikit.cardgames.commons.selectors.ing.PortfolioCode.CREDITCARD_REVOLVING;
import static nl.knikit.cardgames.commons.selectors.ing.PortfolioCode.PLATINUMCARD_CHARGE;
import static nl.knikit.cardgames.commons.selectors.ing.PortfolioCode.PLATINUMCARD_REVOLVING;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@RunWith(MockitoJUnitRunner.class)
public class GameTypeSelectorTest {
	
	
	@Test
	public void pricingProgramSelector_findEveryChangeLimit_ShouldGive12ValidPricingPrograms() throws Exception {
		
		GameTypeSelector test = new GameTypeSelector();
		
		int[] limits = {4999, 5000};
		List<String> actual = new ArrayList<>();
		List<String> found = new ArrayList<>();
		
		for (ProductType findProductType : ProductType.values()) {
			for (AccountType findAccountType : AccountType.values()) {
				for (int findLimit : limits) {
					found.clear();
					found = test.findAllForChangeLimit(findProductType, findAccountType, findLimit);
					if (found != null) {
						actual.addAll(found);
					}
				}
			}
		}
		assertThat(actual.size(), is(12));
	}
	
	@Test
	public void pricingProgramSelector_findEveryChangeRepayment_ShouldGive24ValidPricingPrograms() throws Exception {
		
		GameTypeSelector test = new GameTypeSelector();
		
		int[] limits = {4999, 5000};
		List<String> actual = new ArrayList<>();
		List<String> found = new ArrayList<>();
		
		for (ProductType findProductType : ProductType.values()) {
			for (AccountType findAccountType : AccountType.values()) {
				for (PortfolioCode findPortfolioCode : PortfolioCode.values()) {
					
					if (findProductType == ProductType.PLATINUMCARD) {
						if ((findPortfolioCode == CREDITCARD_CHARGE) || (findPortfolioCode == CREDITCARD_REVOLVING)) {
							break; // prevents InvalidAttributeValueException
						}
					} else {
						if ((findPortfolioCode == PLATINUMCARD_CHARGE) || (findPortfolioCode == PLATINUMCARD_REVOLVING)) {
							break; // prevents InvalidAttributeValueException
						}
					}
						
					for (int findLimit : limits) {
						
						found.clear();
						found = test.findAllForChangeRepayment(findProductType, findAccountType, findLimit, findPortfolioCode);
						if (found != null) {
							actual.addAll(found);
						}
					}
				}
			}
		}
		assertThat(actual.size(), is(24));
	}
	
	@Test
	public void pricingProgramSelector_findAllForSpecificChangeLimit_ShouldGiveOneCorrectPricingProgram() throws Exception {
		
		GameTypeSelector test = new GameTypeSelector();
		
		ProductType productType = ProductType.CREDITCARD;
		AccountType accountType = AccountType.REGULAR;
		int limit = 4999;
		
		List<String> expected = new ArrayList<>();
		expected.add("RINT01");
		
		List<String> actual = new ArrayList<>();
		actual.addAll(test.findAllForChangeLimit(productType, accountType, limit));
		
		assertThat(actual.size(), is(1));
		assertThat(actual, is(expected));
		
		
	}
	
	@Test
	public void pricingProgramSelector_findAllForSpecificChangeRepayment_ShouldGive2CorrectPricingPrograms() throws Exception {
		
		GameTypeSelector test = new GameTypeSelector();
		
		ProductType productType = ProductType.PLATINUMCARD;
		AccountType accountType = AccountType.POSITION;
		PortfolioCode portfolioCode = PLATINUMCARD_REVOLVING;
		int limit = 4999;
		
		
		List<String> expected = new ArrayList<>();
		expected.add("RNWEGR");
		expected.add("RINTBL");
		
		List<String> actual = new ArrayList<>();
		actual.addAll(test.findAllForChangeRepayment(productType, accountType, limit, portfolioCode));
		
		assertThat(actual.size(), is(2));
		assertThat(actual, is(expected));
		
	}
	
	@Test
	public void pricingProgramSelector_supplyInconsistentChangeRepayment_ShouldGive12InvalidAttributeValueException() throws Exception {
		
		GameTypeSelector test = new GameTypeSelector();
		
		int[] limits = {4999, 5000};
		List<String> actual = new ArrayList<>();
		List<String> found = new ArrayList<>();
		int countInvalidAttributeValueException = 0;
		
		for (ProductType findProductType : ProductType.values()) {
			for (AccountType findAccountType : AccountType.values()) {
				for (PortfolioCode findPortfolioCode : PortfolioCode.values()) {
					
					if (findProductType == ProductType.CREDITCARD) {
						if ((findPortfolioCode == CREDITCARD_CHARGE) || (findPortfolioCode == CREDITCARD_REVOLVING)) {
							break; // prevents InvalidAttributeValueException
						}
					} else {
						if ((findPortfolioCode == PLATINUMCARD_CHARGE) || (findPortfolioCode == PLATINUMCARD_REVOLVING)) {
							break; // prevents InvalidAttributeValueException
						}
					}
					
					for (int findLimit : limits) {
						
						found.clear();
						try {
							found = test.findAllForChangeRepayment(findProductType, findAccountType, findLimit, findPortfolioCode);
							
						} catch (InvalidAttributeValueException _ignore) {
							countInvalidAttributeValueException++;
						}
						
						if (found != null) {
							actual.addAll(found);
						}
					}
				}
			}
		}
		// all repayments will fail once
		assertThat(countInvalidAttributeValueException, is(12));
	}
	
	
}
