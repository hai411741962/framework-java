package net.getbang.core.exception;




import java.io.Writer;


public class FastStringWriter extends Writer {
	private StringBuilder builder;

	public FastStringWriter() {
		builder = new StringBuilder(64);
	}


	@Override
	public void write(int c) {
		builder.append((char) c);
	}

	@Override
	public void write(char[] cbuilder, int off, int len) {
		if ((off < 0) || (off > cbuilder.length) || (len < 0) ||
			((off + len) > cbuilder.length) || ((off + len) < 0)) {
			throw new IndexOutOfBoundsException();
		} else if (len == 0) {
			return;
		}
		builder.append(cbuilder, off, len);
	}

	@Override
	public void write(String str) {
		builder.append(str);
	}

	@Override
	public void write(String str, int off, int len) {
		builder.append(str.substring(off, off + len));
	}

	@Override
	public FastStringWriter append(CharSequence csq) {
		if (csq == null) {
			write("");
		} else {
			write(csq.toString());
		}
		return this;
	}

	@Override
	public FastStringWriter append(CharSequence csq, int start, int end) {
		CharSequence cs = (csq == null ? "" : csq);
		write(cs.subSequence(start, end).toString());
		return this;
	}

	@Override
	public FastStringWriter append(char c) {
		write(c);
		return this;
	}

	@Override
	public String toString() {
		return builder.toString();
	}

	@Override
	public void flush() {
	}

	@Override
	public void close() {
		builder.setLength(0);
		builder.trimToSize();
	}
}
