package solutions.desati.twatter;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

public class Matchers {
    static Matcher<Number> jsonNumber(final BigDecimal d) {
        return new TypeSafeDiagnosingMatcher<Number>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("a numeric value equal to ").appendValue(d);
            }

            @Override
            protected boolean matchesSafely(Number item, Description mismatchDescription) {
                BigDecimal actual;
                if (item instanceof BigDecimal) {
                    actual = (BigDecimal) item;
                } else if (item instanceof BigInteger) {
                    actual = new BigDecimal((BigInteger) item);
                } else {
                    actual = BigDecimal.valueOf(item.doubleValue());
                }

                if (d.compareTo(actual) == 0) {
                    return true;
                }

                mismatchDescription.appendText("numeric value was ").appendValue(item);
                return false;
            }
        };
    }
}
