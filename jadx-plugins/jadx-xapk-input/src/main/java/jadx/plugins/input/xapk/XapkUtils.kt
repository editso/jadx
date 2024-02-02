package jadx.plugins.input.xapk

import com.google.gson.Gson
import jadx.api.archive.IZipArchive
import jadx.api.plugins.utils.ZipSecurity
import jadx.core.utils.files.FileUtils
import java.io.File
import java.io.InputStreamReader
import java.util.zip.ZipFile

object XapkUtils {
	fun getManifest(file: File): XapkManifest? {
		if (!FileUtils.isZipFile(file)) return null
		try {
			IZipArchive.open(file).use { zip ->
				val manifestEntry = zip.getEntry("manifest.json") ?: return null
				return InputStreamReader(ZipSecurity.getInputStreamForEntry(zip, manifestEntry)).use {
					Gson().fromJson(it, XapkManifest::class.java)
				}
			}
		} catch (e: Exception) {
			return null
		}
	}

	fun isSupported(manifest: XapkManifest): Boolean {
		return manifest.xapkVersion == 2 && manifest.splitApks.isNotEmpty()
	}
}
