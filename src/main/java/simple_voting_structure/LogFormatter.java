package simple_voting_structure;

import java.util.logging.LogRecord;

public class LogFormatter extends java.util.logging.Formatter {

	@Override
    public String format(LogRecord record) {
        return String.format("%s: %s%n",
            record.getLevel(), record.getMessage());
    }

}
