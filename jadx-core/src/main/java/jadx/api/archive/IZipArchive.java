package jadx.api.archive;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;

import com.reandroid.archive.ArchiveFile;
import com.reandroid.archive.InputSource;

public interface IZipArchive extends AutoCloseable {

	Enumeration<? extends IZipArchiveEntry> entries() throws IOException;

	IZipArchiveEntry getEntry(String name);

	InputStream getInputStream(IZipArchiveEntry entry) throws IOException;

	static IZipArchive open(File file) throws IOException {
		try (ArchiveFile archiveFile = new ArchiveFile(file)) {
			return new IZipArchive() {
				@Override
				public void close() throws Exception {
					archiveFile.close();
				}

				@Override
				public Enumeration<? extends IZipArchiveEntry> entries() throws IOException {
					Iterator<Map.Entry<String, InputSource>> entryIterator = archiveFile.mapEntrySource().entrySet().iterator();
					return new Enumeration<>() {
						@Override
						public boolean hasMoreElements() {
							return entryIterator.hasNext();
						}

						@Override
						public IZipArchiveEntry nextElement() {
							InputSource source = entryIterator.next().getValue();
							return new ThirdPartyZipEntry(source);
						}
					};
				}

				@Override
				public IZipArchiveEntry getEntry(String name) {
					return new ThirdPartyZipEntry(archiveFile.mapEntrySource().get(name));
				}

				@Override
				public InputStream getInputStream(IZipArchiveEntry entry) throws IOException {

					if (entry instanceof ThirdPartyZipEntry) {
						return ((ThirdPartyZipEntry) entry).openStream();
					}

					throw new IOException(entry.getName());
				}
			};
		}
	}
}
