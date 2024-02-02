package jadx.api.archive;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.attribute.FileTime;

import com.reandroid.archive.InputSource;

public class ThirdPartyZipEntry implements IZipArchiveEntry {

	InputSource source;

	public ThirdPartyZipEntry(InputSource source) {
		this.source = source;
	}

	public InputStream openStream() throws IOException {
		return source.openStream();
	}

	@Override
	public String getName() {
		return source.getName();
	}

	@Override
	public long getSize() {
		try {
			return source.getLength();
		} catch (Exception e) {
			return -1;
		}
	}

	@Override
	public boolean isDirectory() {
		return false;
	}

	@Override
	public long getCompressedSize() {
		try {
			return source.getLength();
		} catch (Exception e) {
			return -1;
		}
	}

	@Override
	public FileTime getLastModifiedTime() {
		return FileTime.fromMillis(0);
	}
}
