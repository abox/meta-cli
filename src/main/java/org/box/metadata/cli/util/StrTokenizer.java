package org.box.metadata.cli.util;


public class StrTokenizer {

	public static final byte UNESCAPING_OFF = 0;
	public static final byte UNESCAPING_ALL = -1;
	public static final byte UNESCAPING_SPEC = 1;
	
	private final String string;
	private final char separator;
	private int idx;
	private int token_start = 0;
	private boolean skipping = true;
	private boolean quoting1, quoting2;
	private byte unescaping = UNESCAPING_ALL;

	public StrTokenizer(String string, char separator) {
		this.string = string;
		this.separator = separator;
	}

	public String nextToken() {
		if (idx >= string.length())
			return null;
		
		while (idx < string.length()) {
			char c = string.charAt(idx);
			if (c == separator && !quoting1 && !quoting2 && !escaped(c, idx)) {
				if (!skipping) {
					String token = removeEscapes(
									string.substring(token_start, idx));
					token_start = ++idx;
					skipping = true;
					return token;
				} else {
					skipping = true;
					token_start = ++idx;
				}
			} else {
				skipping = false;
				if (c == '\'' && !quoting2 && !escaped(c, idx)) 
					quoting1 = !quoting1;
				else if (c == '"' && !quoting1 && !escaped(c, idx))
					quoting2 = !quoting2;
				idx++;
			}
		}
		return skipping ? null : removeEscapes(
						string.substring(token_start, idx++));
	}

	public String nextTokenUnquoted() {
		if (unescaping == UNESCAPING_OFF)
			throw new IllegalStateException(
					"Only one of the following is required for unqouting: " 
					+ "[UNESCAPING_ALL, UNESCAPING_SPEC]");
					
		String token = nextToken();
		if (token == null || token.length() < 2)
			return token;
		
		char c1 = token.charAt(0);
		char c2 = token.charAt(token.length()-1);
		return (c1 == '\'' && c2 == '\'')
				|| (c1 == '"' && c2 == '"') 
				? token.substring(1, token.length() -1) : token;
	}


	public int tokenStart() {
		return token_start;
	}

	public boolean isStringIncomplete() {
		return quoting1 || quoting2;
	}
	
	public void setUnescaping(byte type) {
		if (type != UNESCAPING_OFF 
				&& type != UNESCAPING_ALL
				&& type != UNESCAPING_SPEC)
			throw new IllegalArgumentException(
					"Only one of the following is allowed: " 
					+ "[UNESCAPING_OFF, UNESCAPING_ALL, UNESCAPING_SPEC]");
		this.unescaping = type;
	}


	private String removeEscapes(String token) {
		if (unescaping != UNESCAPING_OFF && token.indexOf('\\') != -1) {
			int len = token.length();
			char[] buffer = new char[len];
			token.getChars(0, token.length(), buffer, 0);
			if (unescaping == UNESCAPING_SPEC) {
				for (int i = buffer.length-1; i >= 0; i--) {
					char c = buffer[i];
					if ((c == '\'' || c == '"' || c == separator || c == '\\') 
							&& escaped(buffer, c, i)) {
						remove(buffer, --i, len--);
					}
				} 
			} else {
				// unescaping == UNESCAPING_ALL
				for (int i = buffer.length-1; i >= 0; i--) {
					char c = buffer[i];
					if (escaped(buffer, c, i)) {
						remove(buffer, --i, len--);
					}
				}
			}
		    return new String(buffer, 0, len);
		} else
			return token;
	}

	private boolean escaped(char c, int idx) {
		if (idx-- < 1)
			return false;
		
		return string.charAt(idx) == '\\' && !escaped(c, idx);
	}
	
	private static boolean escaped(char[] buffer, char c, int idx) {
		if (idx-- < 1)
			return false;
		
		return buffer[idx] == '\\' && !escaped(buffer, c, idx);
	}

	private static void remove(char[] buffer, int idx, int len) {
		for (int i = idx; i < len-1; i++) {
			buffer[i] = buffer[i+1];
		}
		buffer[len-1] = 0;
	}
}
