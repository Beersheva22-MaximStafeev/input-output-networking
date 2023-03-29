package telran.view;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

public interface InputOutput {
	String readString(String prompt);

	void writeString(Object obj);

	default void writeLine(Object obj) {
		writeString(obj.toString() + "\n");
	}

	default <R> R readObject(String prompt, String errorPrompt, Function<String, R> mapper) {
		boolean running = true;
		R result = null;
		while (running) {
			try {
				String str = readString(prompt);
				result = mapper.apply(str);
				running = false;
			} catch (Exception e) {
				writeLine(errorPrompt + " - " + e.getMessage());
			}
		}
		return result;
	}

	default String readStringPredicate(String prompt, String errorPrompt, Predicate<String> predicate) {
		Function<String, String> f = el -> {
			if (!predicate.test(el))
				throw new RuntimeException("String doesn't corresponds to predicate");
			return el;
		};
		return readObject(prompt, errorPrompt, f);
	}

	default String readStringOptions(String prompt, String errorPrompt, Set<String> options) {
		return readStringPredicate(prompt, errorPrompt, el -> options.contains(el));
	}

	default int readInt(String prompt, String errorPrompt) {
		return readObject(prompt, errorPrompt, Integer::parseInt);
	}

	default int readInt(String prompt, String errorPrompt, int min, int max) {
		Function<String, Integer> f = el -> {
			int res = Integer.parseInt(el); 
			if (res < min || res > max) 
				throw new RuntimeException(String.format("Number must be in range [%s..%s]", min, max)); 
			return res;
		};
		return readObject(prompt, errorPrompt, f);
	}

	default long readLong(String prompt, String errorPrompt) {
		return readObject(prompt, errorPrompt, Long::parseLong);
	}

	default long readLong(String prompt, String errorPrompt, long min, long max) {
		return readObject(prompt, errorPrompt, el -> {
			long res = Integer.parseInt(el);
			if (res < min || res > max)
				throw new RuntimeException(String.format("Number must be in range [%s..%s]", min, max));
			return res;
		});
	}

	default LocalDate readDateISO(String prompt, String errorPrompt) {
		return readObject(prompt, errorPrompt, LocalDate::parse);
	}

	default LocalDate readDate(String prompt, String errorPrompt, String format, LocalDate min, LocalDate max) {
		return readObject(prompt, errorPrompt, el -> {
			LocalDate res = LocalDate.parse(el, DateTimeFormatter.ofPattern(format));
			if (res.isBefore(min) || res.isAfter(max))
				throw new RuntimeException(String.format("Date must be in range [%s..%s]", min, max));
			return res;
		});
	}
	
	default <T extends Comparable<T>> T getAndCheck(String input, Function<String, T> fn, T min, T max) {
		T res = fn.apply(input);
		checkRange(res, min, max);
		return res;
	}
	
	default <T extends Comparable<T>> void checkRange(T res, T min, T max) {
		if (res.compareTo(min) < 0 || res.compareTo(max) > 0) {
			throw new RuntimeException("value must be in range [" + min + ".." + max + "]");
		}
	}
	
//	default <T> void check(Comparable<T> res, T min, T max) {
//		  if (res.compareTo(min) < 0 || res.compareTo(max) > 0) {
//		    throw new IllegalArgumentException();
//		  }
//		}

	default double readNumber(String prompt, String errorPrompt) {
		return readObject(prompt, errorPrompt, Double::parseDouble);
	}

	default double readNumber(String prompt, String errorPrompt, double min, double max) {
		return readObject(prompt, errorPrompt, el -> {
			double res = Double.parseDouble(el);
			if (res < min || res > max)
				throw new RuntimeException(String.format("Number must be in range [%s..%s]", min, max));
			return res;
		});
	}
	
	default double readNumberDouble(String prompt, String errorPrompt, double min, double max) {
		return readObject(prompt, errorPrompt, el -> getAndCheck(el, el1 -> Double.parseDouble(el1), min, max));
	}

	default LocalDate readDateNew(String prompt, String errorPrompt, String format, LocalDate min, LocalDate max) {
		return (LocalDate) readObject(prompt, errorPrompt, el -> getAndCheck(el, el1 -> LocalDate.parse(el1, DateTimeFormatter.ofPattern(format)), min, max));
	}
}
