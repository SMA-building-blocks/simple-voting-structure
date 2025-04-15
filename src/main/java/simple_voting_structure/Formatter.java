package simple_voting_structure;

import java.util.logging.LogRecord;

public class Formatter extends java.util.logging.Formatter {

	public Formatter() {
		// TODO Auto-generated constructor stub
	}

	@Override
    public String format(LogRecord record) {
        return String.format("%s: %s%n",
            record.getLevel(), record.getMessage());
    }

}
