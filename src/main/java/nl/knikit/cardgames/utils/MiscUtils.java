package nl.knikit.cardgames.utils;

import org.springframework.jdbc.datasource.lookup.DataSourceLookupFailureException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.util.Properties;
import java.util.UUID;

// copied filereader for: .properties files
public final class MiscUtils {

	private MiscUtils() {
	}

	public static Properties loadProperties(final String filename, final Class<?> clazz) throws IOException {
		Properties result = new Properties();
		final ClassLoader classLoader = clazz.getClassLoader();

		if (classLoader != null) {
			final InputStream resourceAsStream = classLoader.getResourceAsStream(filename);

			if (resourceAsStream == null) {
				throw new FileNotFoundException("could not read from : "+ filename);
			}
			try {
				result.load(resourceAsStream);
			} finally {
				resourceAsStream.close();
			}

		} else {
			String errorMessage = "could not get class loader : " + filename;
			throw new DataSourceLookupFailureException(errorMessage);
		}
		return result;
	}

}
