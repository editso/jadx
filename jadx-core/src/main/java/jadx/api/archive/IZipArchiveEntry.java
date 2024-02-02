package jadx.api.archive;

import java.nio.file.attribute.FileTime;

public interface IZipArchiveEntry {
	String getName();

	long getSize();

	boolean isDirectory();

	long getCompressedSize();

	FileTime getLastModifiedTime();
}
