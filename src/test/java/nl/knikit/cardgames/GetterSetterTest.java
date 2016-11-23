package nl.knikit.cardgames;

import nl.knikit.cardgames.model.Card;
import nl.knikit.cardgames.model.Casino;
import nl.knikit.cardgames.model.Deck;
import nl.knikit.cardgames.model.Game;
import nl.knikit.cardgames.model.Hand;
import nl.knikit.cardgames.model.Player;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * 
 * Design Patterns:
 */
@Slf4j
public class GetterSetterTest {

	private List<Object> classList = new ArrayList<>();
	private Map<Class, Object> testValues = new HashMap<>();

	public GetterSetterTest() {
		classList.add(new Player());
		classList.add(new Game()); //has enum state
		classList.add(new Deck());
		classList.add(new Card());
		classList.add(new Hand());
		classList.add(new Casino());


		testValues.put(String.class, "testValue");
		testValues.put(Integer.class, 11);
		testValues.put(int.class, 11);
		testValues.put(Boolean.class, true);
		testValues.put(boolean.class, true);
		testValues.put(Object.class, "testObjectValue");
		testValues.put(List.class, new ArrayList());
		testValues.put(BigInteger.class, BigInteger.ONE);
		testValues.put(Date.class, new Date());
	}

	@Test
	public void testGettersSetters() throws InvocationTargetException, IllegalAccessException {
		for (Object testClass : classList) {
			Field[] classFields = testClass.getClass().getDeclaredFields();
			for (Field field : classFields) {
				if (field.isSynthetic()) {
					continue;
				}
				final String fieldName = field.getName();
				Object setterResult;
				try {
					setterResult = mock(field.getType());
				} catch (MockitoException e) {
					setterResult = testValues.get(field.getType());
				}

				final String camelCaseFieldForMethod = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
				final String setMethodName = "set" + camelCaseFieldForMethod;
				final String getMethodName = "get" + camelCaseFieldForMethod;
				final String isMethodName = "is" + camelCaseFieldForMethod;

				reflectSetMethod(testClass, field, fieldName, setterResult, setMethodName);

				reflectIsMethod(testClass, fieldName, setterResult, isMethodName);

				reflectGetMethod(testClass, fieldName, setterResult, getMethodName);

			}
		}
	}

	private void reflectSetMethod(final Object testClass, final Field field, final String fieldName, final Object setterResult,
			String setMethodName) throws IllegalAccessException, InvocationTargetException {
		try {
			Method setterMethod = testClass.getClass().getDeclaredMethod(setMethodName, field.getType());
			setterMethod.setAccessible(true);
			setterMethod.invoke(testClass, setterResult);
		} catch (NoSuchMethodException e) {
			// uncomment for debugging
			// System.out.println(fieldName + " does not have a setter method("
			// + setMethodName + ")");
		}
	}

	private void reflectIsMethod(Object testClass, String fieldName, Object setterResult, String isMethodName)
			throws IllegalAccessException, InvocationTargetException {
		try {
			Method isMethod = testClass.getClass().getDeclaredMethod(isMethodName);
			isMethod.setAccessible(true);
			Object result = isMethod.invoke(testClass);
			if (setterResult != null) {
				assertEquals(setterResult, result);
			}
		} catch (NoSuchMethodException e) {
			// uncomment for debugging
			// System.out.println(fieldName + " does not have a boolean 'is'
			// method(" + isMethodName + ")");
		}
	}

	private void reflectGetMethod(Object testClass, String fieldName, Object setterResult, String getMethodName)
			throws IllegalAccessException, InvocationTargetException {
		try {
			Method getterMethod = testClass.getClass().getDeclaredMethod(getMethodName);
			// continue in the try catch because now we know we have a method
			// set accessible should not be required but we dont want a test
			// failure due to it
			getterMethod.setAccessible(true);
			if (setterResult != null) {
				assertEquals(setterResult, getterMethod.invoke(testClass));
			}
		} catch (NoSuchMethodException e) {
			// uncomment for debugging
			// System.out.println(fieldName + " does not have a getter method("
			// + getMethodName + ")");
		}
	}
}
