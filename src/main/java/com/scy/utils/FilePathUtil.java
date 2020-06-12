package com.scy.utils;

public class FilePathUtil {
	public static final String FILE_SEPARATOR = System.getProperty("file.separator");
	//public static final String FILE_SEPARATOR = File.separator;

	/**
	 * 转换图片路径分隔符
	 *	转换为和当前系统一致
	 * @param path
	 * @return
	 */
	public static String getRealFilePath(String path) {
		return path.replace("/", FILE_SEPARATOR).replace("\\", FILE_SEPARATOR);
	}

	/**
	 * 转换图片路径分隔符
	 *	转换为HttpURL一致
	 * @param path
	 * @return
	 */
	public static String getHttpURLPath(String path) {
		return path.replace("\\", "/");
	}
}
