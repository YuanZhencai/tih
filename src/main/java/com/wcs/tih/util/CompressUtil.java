package com.wcs.tih.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class CompressUtil {
	private static String encoding = "GB2312";

	private static Logger logger = LoggerFactory.getLogger(CompressUtil.class.getName());
	private static List<String> list = new ArrayList<String>();

	private static List<String> listFile(String path) {
		File file = new File(path);
		String[] array = null;
		String sTemp = "";
		if (!(file.isDirectory())) {
			return null;
		}
		array = file.list();
		if (array.length > 0)
			for (int i = 0; i < array.length; ++i) {
				sTemp = path + array[i];
				file = new File(sTemp);
				if (file.isDirectory())
					listFile(sTemp + "/");
				else
					list.add(sTemp);
			}
		else {
			return null;
		}
		return list;
	}

	public static void zip(String needtozipfilepath, String zipfilepath) {
		try {
			byte[] b = new byte[512];
			File needtozipfile = new File(needtozipfilepath);
			if (!(needtozipfile.exists())) {
				throw new RuntimeException("指定的要压缩的文件或目录不存在.");
			}
			String zipFile = zipfilepath;

			String filepath = needtozipfilepath;
			List<String> fileList = listFile(filepath);
			for (String string : fileList) {
				System.out.println("[file]" + string);
			}
			FileOutputStream fileOutputStream = new FileOutputStream(zipFile);
			CheckedOutputStream cs = new CheckedOutputStream(fileOutputStream, new CRC32());
			java.util.zip.ZipOutputStream out = new java.util.zip.ZipOutputStream(new BufferedOutputStream(cs));
			for (int i = 0; i < fileList.size(); ++i) {
				InputStream in = new FileInputStream(fileList.get(i));
				String fileName = fileList.get(i).replace(File.separatorChar, '/');
				fileName = fileName.substring(fileName.indexOf("/") + 1);
				java.util.zip.ZipEntry e = new java.util.zip.ZipEntry(fileName);
				out.putNextEntry(e);
				int len = 0;
				while ((len = in.read(b)) != -1) {
					out.write(b, 0, len);
				}
				out.closeEntry();
				in.close();
			}
			out.close();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	public static boolean zip(String baseDirName, String[] fileNames, String targetFileName, String encoding) {
		boolean flag = false;
		try {
			File baseDir = new File(baseDirName);
			if ((!(baseDir.exists())) || (!(baseDir.isDirectory()))) {
				throw new RuntimeException("压缩失败! 根目录不存在: " + baseDirName);
			}

			String baseDirPath = baseDir.getAbsolutePath();

			File targetFile = new File(targetFileName);
			if (!(targetFile.exists())) {
				FileOutputStream fos = new FileOutputStream(targetFile);
				OutputStreamWriter osw = new OutputStreamWriter(fos);
				osw.close();
			}
			java.util.zip.ZipOutputStream out = new java.util.zip.ZipOutputStream(new FileOutputStream(targetFile));

			if (fileNames.equals("*")) {
				dirToZip(baseDirPath, baseDir, out);
			} else {
				File[] files = new File[fileNames.length];
				for (int i = 0; i < files.length; ++i) {
					files[i] = new File(baseDir, fileNames[i]);
				}
				if (files[0].isFile()) {
					filesToZip(baseDirPath, files, out);
				}
			}
			out.close();

			flag = true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return flag;
	}

	private static void fileToZip(String baseDirPath, File file, java.util.zip.ZipOutputStream out) throws IOException {
		FileInputStream in = null;
		org.apache.tools.zip.ZipEntry entry = null;

		byte[] buffer = new byte[4096];
		int bytes_read = 0;
		if (file.isFile()) {
			in = new FileInputStream(file);

			String zipFileName = getEntryName(baseDirPath, file);
			entry = new org.apache.tools.zip.ZipEntry(zipFileName);

			out.putNextEntry(entry);

			while ((bytes_read = in.read(buffer)) != -1) {
				out.write(buffer, 0, bytes_read);
			}
			out.closeEntry();
			in.close();
		}
	}

	private static void filesToZip(String baseDirPath, File[] files, java.util.zip.ZipOutputStream out) throws IOException {
		for (int i = 0; i < files.length; ++i) {
			File file = files[i];
			if (file.isFile()) {
				fileToZip(baseDirPath, file, out);
			} else {
				dirToZip(baseDirPath, file, out);
			}
		}
	}

	private static void dirToZip(String baseDirPath, File dir, java.util.zip.ZipOutputStream out) throws IOException {
		File[] files = dir.listFiles();

		if (files.length == 0) {
			String zipFileName = getEntryName(baseDirPath, dir);
			org.apache.tools.zip.ZipEntry entry = new org.apache.tools.zip.ZipEntry(zipFileName);
			out.putNextEntry(entry);
			out.closeEntry();
		} else {
			for (int i = 0; i < files.length; ++i) {
				File file = files[i];
				if (file.isFile()) {
					fileToZip(baseDirPath, file, out);
				} else {
					dirToZip(baseDirPath, file, out);
				}
			}
		}
	}

	private static String getEntryName(String baseDirPath, File file) {
		if (!(baseDirPath.endsWith(File.separator))) {
			baseDirPath = baseDirPath + File.separator;
		}
		String filePath = file.getAbsolutePath();

		if (file.isDirectory()) {
			filePath = filePath + "/";
		}
		int index = filePath.indexOf(baseDirPath);
		return filePath.substring(index + baseDirPath.length());
	}

	public static boolean unZip(String zipFileName, String outputDirectory) {
		boolean flag = false;
		try {
			ZipFile zipFile = new ZipFile(zipFileName);
			Enumeration<ZipEntry> e = zipFile.getEntries();
			org.apache.tools.zip.ZipEntry zipEntry = null;
			createDirectory(outputDirectory, "");
			while (e.hasMoreElements()) {
				zipEntry = e.nextElement();
				File f;
				if (zipEntry.isDirectory()) {
					String name = zipEntry.getName();
					name = name.substring(0, name.length() - 1);
					f = new File(outputDirectory + File.separator + name);
					f.mkdir();
					System.out.println("创建目录：" + outputDirectory + File.separator + name);
				} else {
					String fileName = zipEntry.getName();
					fileName = fileName.replace('\\', '/');

					if (fileName.indexOf("/") != -1) {
						createDirectory(outputDirectory, fileName.substring(0, fileName.lastIndexOf("/")));
						fileName = fileName.substring(fileName.lastIndexOf("/") + 1, fileName.length());
					}
					f = new File(outputDirectory + File.separator + zipEntry.getName());
					f.createNewFile();
					InputStream in = zipFile.getInputStream(zipEntry);
					FileOutputStream out = new FileOutputStream(f);
					byte[] by = new byte[1024];
					int c;
					while ((c = in.read(by)) != -1) {
						out.write(by, 0, c);
					}
					out.close();
					in.close();
				}
				flag = true;
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		return flag;
	}

	private static void createDirectory(String directory, String subDirectory) {
		File fl = new File(directory);
		try {
			if ((subDirectory == "") && (fl.exists() != true)) {
				fl.mkdir();
			} else if (subDirectory != "") {
				String[] dir = subDirectory.replace('\\', '/').split("/");
				for (int i = 0; i < dir.length; ++i) {
					File subFile = new File(directory + File.separator + dir[i]);
					if (!(subFile.exists()))
						subFile.mkdir();
					directory = directory + File.separator + dir[i];
				}
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
	}

	public static void zip(List<Map<String, InputStream>> entries, OutputStream os) {
		try {
			byte[] b = new byte[512];
			CheckedOutputStream cs = new CheckedOutputStream(os, new CRC32());
			org.apache.tools.zip.ZipOutputStream out = new org.apache.tools.zip.ZipOutputStream(new BufferedOutputStream(cs));
			for (Map<String, InputStream> entry : entries) {
				for (String fileName : entry.keySet()) {
					InputStream in = (InputStream) entry.get(fileName);
					org.apache.tools.zip.ZipEntry e = new org.apache.tools.zip.ZipEntry(fileName);
					out.setEncoding(encoding);
					out.putNextEntry(e);
					int len = 0;
					while ((in.available() > 0) && ((len = in.read(b)) != -1)) {
						out.write(b, 0, len);
					}

					out.closeEntry();
					in.close();
				}
			}
			out.flush();
			out.close();
			os.flush();
			os.close();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	public static void main(String[] temp) {
		String sourcePath = "C:\\Users\\Yuan\\Desktop\\test\\";
		String zipFilePath = "C:\\Users\\Yuan\\Desktop\\abc.zip";
		zip(sourcePath, zipFilePath);
	}
}