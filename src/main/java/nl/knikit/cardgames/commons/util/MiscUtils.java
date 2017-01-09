package nl.knikit.cardgames.commons.util;

import org.springframework.jdbc.datasource.lookup.DataSourceLookupFailureException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class MiscUtils {

	private MiscUtils() {
	}

	public static Properties loadProperties(final String filename, final Class<?> clazz) throws IOException {
		Properties result = new Properties();
		final ClassLoader classLoader = clazz.getClassLoader();

		if (classLoader != null) {
			final InputStream resourceAsStream = classLoader.getResourceAsStream(filename + ".properties");

			if (resourceAsStream == null) {
				throw new FileNotFoundException("could not read from database.properties");
			}
			try {
				result.load(resourceAsStream);
			} finally {
				resourceAsStream.close();
			}

		} else {
			String errorMessage = "could not get class loader from data source";
			throw new DataSourceLookupFailureException(errorMessage);
		}
		return result;
	}
	}
