package com.wxxr.mobile.stock.app.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.microkernel.api.AbstractModule;
import com.wxxr.mobile.core.util.StringUtils;
import com.wxxr.mobile.stock.app.IStockAppContext;
import com.wxxr.mobile.stock.app.service.IContentManager;

public class ContentManager extends AbstractModule<IStockAppContext> implements
		IContentManager {

	private static final String CONTENT_FILE = "content.bin";

	private static Trace log = Trace.register("com.wxxr.mobile.stock.app.service.impl.ContentManager");

	private File storeDir;

	protected File getResourceFile(String root, String url, int depth) {
		return new File(getContentRoot(root), getRelativePath(url, depth));
	}

	protected String getRelativePath(String url, int depth) {
		if (depth == 0) {
			return getFileName(url, depth);
		} else {
			return "resources/" + getFileName(url, depth);
		}
	}

	protected File getContentRoot(String root) {
		return new File(getWebCacheRoot(), root);
	}

	protected File getWebCacheRoot() {
		return new File(storeDir, "webCache");
	}

	protected File getStoreRoot(String name) {
		return new File(storeDir, name);
	}

	@Override
	protected void initServiceDependency() {

	}

	@Override
	protected void startService() {
		storeDir = this.context.getApplication().getDataDir("com.wxxr.mobile.stock",Context.MODE_PRIVATE);
		log.info("store dir" + storeDir);
		this.context.registerService(IContentManager.class, this);
		if (log.isDebugEnabled()) {
			log.debug("content manager  is started");
		}
	}

	@Override
	protected void stopService() {
		this.context.unregisterService(IContentManager.class, this);
		if (log.isDebugEnabled()) {
			log.debug("content manager  is stop");
		}
	}

	protected String getFileName(String path, int depth) {

		int idx = path.lastIndexOf('/');
		if (idx > 0) {
			path = path.substring(idx + 1);
		}
		idx = path.lastIndexOf('.');
		if ((depth == 0) && (idx < 0)) {
			return path + ".html";
		}
		try {
			return URLDecoder.decode(path, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("System shoud support UTF-8 charset!!!");
		}
	}

	public void saveContent(String type, String id, byte[] content)
			throws IOException {
		validateArgumentsNotEmpty(type, id, content);

		if (log.isDebugEnabled()) {
			log.debug("save content type:" + type + ",id:" + id);
		}
		File contentFile = getContentFile(type, id, true);
		File savingFile = new File(contentFile.getParent(),
				contentFile.getName() + ".saving");
		if ((!savingFile.exists()) && (!savingFile.createNewFile())) {
			log.error("create file exception. file:" + savingFile);
			throw new IOException("create file exception. file:" + savingFile);
		}
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(savingFile);
			fos.write(content);
			fos.flush();
		} finally {
			if (fos != null) {
				fos.close();
			}
		}
		if (!savingFile.renameTo(contentFile)) {
			throw new IOException("rename file failed,saving file:"
					+ savingFile + " to file" + contentFile);
		}
		savingFile.setLastModified(System.currentTimeMillis());
	}

	@Override
	public byte[] getContent(String type, String id) throws IOException {
		validateArgumentsNotEmpty(type, id);
		File storeFile;
		try {
			storeFile = getContentFile(type, id, false);
			if (storeFile.exists() == false) {
				return null;
			}
			FileInputStream fis = null;
			ByteArrayOutputStream os = null;
			try {
				fis = new FileInputStream(storeFile);
				os = new ByteArrayOutputStream();
				byte[] tmp = new byte[4096];
				int l;
				while ((l = fis.read(tmp)) != -1) {
					os.write(tmp, 0, l);
				}
				return os.toByteArray();
			} finally {
				if (os != null) {
					os.close();
				}
				if (fis != null) {
					fis.close();
				}
			}

		} catch (IOException e) {
			return null;
		}

	}

	@Override
	public void delete(String type, String id) throws IOException {
		validateArgumentsNotEmpty(type, id);
		try {
			File dir = getContentFile(type, id, false);
			deleteFile(dir);
		} catch (Exception e) {

		}

	}

	private File getContentFile(String type, String id,
			boolean createDirIfNotExisting) throws IOException {
		File dir = new File(getStoreRoot(type), id);
		File storeFile = new File(dir, CONTENT_FILE);
		if ((storeFile.exists() == false) && createDirIfNotExisting) {
			if ((dir.exists() == false) && (dir.mkdirs() == false)) {
				throw new IOException("Failed to create directory :"
						+ dir.getCanonicalPath());
			}
		}
		return storeFile;
	}

	protected File getStatusFile(String type, String id, String statusName,
			String status, boolean createDirIfNotExisting) throws IOException {
		File dir = new File(getStoreRoot(type), id);
		File statusFile = new File(dir, statusName + "." + status + ".s");
		if ((statusFile.exists() == false) && createDirIfNotExisting) {
			File parent = statusFile.getParentFile();
			if ((dir.exists() == false) && (parent.mkdirs() == false)) {
				throw new IOException("Failed to create directory :"
						+ parent.getCanonicalPath());
			}
		}
		return statusFile;
	}

	protected File findStatusFile(String type, String id,
			final String statusName) {
		File dir = new File(getStoreRoot(type), id);
		File[] files = dir.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String filename) {
				return filename.startsWith(statusName)
						&& filename.endsWith(".s");
			}
		});
		if ((files != null) && (files.length > 0)) {
			if (files.length > 1) {
				for (int i = 1; i < files.length; i++) { // delete all redundant
															// files
					if (!files[i].delete()) {
						log.warn("Failed to delete redundant status file :"
								+ files[i]);
					}
				}
			}
			return files[0];
		}
		return null;
	}

	@Override
	public void updateStatus(String type, String id, String statusName,
			String status) throws IOException {
		validateArgumentsNotEmpty(type, id, statusName, status);
		File oldStatusFile = findStatusFile(type, id, statusName);
		File statusFile = getStatusFile(type, id, statusName, status, true);
		if (oldStatusFile != null) {
			if (!oldStatusFile.renameTo(statusFile)) {
				log.error("create not rename status file from :"
						+ oldStatusFile + " to file:" + statusFile);
				throw new IOException("create not rename status file from :"
						+ oldStatusFile + " to file:" + statusFile);
			}
			oldStatusFile.setLastModified(System.currentTimeMillis());
		} else {
			if (!statusFile.createNewFile()) {
				log.error("create file exception. file:" + statusFile);
				throw new IOException("create file exception. file:"
						+ statusFile);
			}
			statusFile.setLastModified(System.currentTimeMillis());
		}
		// String[] fileNames=getFileListWithPrefix(storeDir, statusName);
		// if(fileNames==null || fileNames.length==0){
		// if(!statusFile.createNewFile()){
		// log.error("create file exception. file:"+statusFile);
		// throw new IOException("create file exception. file:"+statusFile);
		// }
		// return ;
		// }
		//
		// if(fileNames.length>1){
		// for(int i=1;i<fileNames.length;i++){
		// deleteFile(new File(storeDir,fileNames[i]));
		// }
		// }
		// File oldStatusFile=new File(storeDir,fileNames[0]);
		// if(!oldStatusFile.renameTo(statusFile)){
		// log.error("create file exception. file:"+statusFile);
		// throw new IOException("rename file exception. file:"+oldStatusFile
		// +" to file:"+statusFile);
		// }

	}

	private void validateArgumentsNotEmpty(Object... args) {
		if (args == null) {
			throw new IllegalArgumentException("Validate arguments error");
		}
		for (Object arg : args) {
			if (arg instanceof String) {
				if (StringUtils.isBlank((String) arg)) {
					throw new IllegalArgumentException("Argument " + arg
							+ " can't be empty");
				}
			} else {
				if (arg == null) {
					throw new IllegalArgumentException("Argument " + arg
							+ " can't be null");
				}
			}
		}

	}

	@Override
	public String getStatus(String type, String id, String statusName)
			throws IOException {
		validateArgumentsNotEmpty(type, id, statusName);
		File statusFile = findStatusFile(type, id, statusName);
		if (statusFile == null) {
			return null;
		}
		return StringUtils.split(statusFile.getName(), '.')[1];
		// File storeDir = getStoreDir(type, id);
		// String[] fileNames=getFileListWithPrefix(storeDir, statusName);
		// if(fileNames==null || fileNames.length==0){
		// log.error("not found "+statusName +" file");
		// return "";
		// }
		// if(fileNames.length>1){
		// log.error(" found many "+statusName +" file");
		// throw new IOException(" found many "+statusName +" file");
		//
		// }
		// String suffix= getFileSuffix(fileNames[0]);
		// return suffix;
	}

	@Override
	public void deleteStatus(String type, String id, String statusName)
			throws IOException {
		validateArgumentsNotEmpty(type, id, statusName);
		File statusFile = findStatusFile(type, id, statusName);
		if (statusFile != null) {
			if (!statusFile.delete()) {
				throw new IOException(" Failed to delete status file :"
						+ statusFile.getCanonicalPath());
			}
		}
	}

	@Override
	public String[] queryContentIds(String type, String statusName,
			String statusValue) throws IOException {
		validateArgumentsNotEmpty(type);
		File dir = getStoreRoot(type);
		if (!dir.exists()) {
			if (log.isDebugEnabled()) {
				log.debug("dir:" + dir.getCanonicalPath() + " not exists");
			}
			return null;
		}

		ArrayList<String> matchFiles = new ArrayList<String>();
		String fileName = "";
		if (statusName == null) {
			fileName = CONTENT_FILE;
		} else {
			validateArgumentsNotEmpty(statusValue);
			fileName = statusName + "." + statusValue + ".s";
		}
		searchMatchStatusContentIds(dir.getCanonicalPath(), dir, fileName,
				matchFiles);
		if (matchFiles.isEmpty()) {
			return null;
		}
		return matchFiles.toArray(new String[matchFiles.size()]);
	}

	protected String getContentIdFromFile(String rootPath, File file)
			throws IOException {
		String path = file.getParentFile().getCanonicalPath();
		if (path.startsWith(rootPath)) {
			path = path.substring(rootPath.length());
			if (path.startsWith("/")) {
				path = path.substring(1);
			}
			return path;
		}
		return null;
	}

	protected void searchMatchStatusContentIds(String rootPath, File dir,
			String matchname, List<String> result) throws IOException {
		if (!dir.isDirectory()) {
			return;
		}
		File[] files = dir.listFiles();
		if (files != null) {
			for (File file : files) {
				if (file.isDirectory()) {
					searchMatchStatusContentIds(rootPath, file, matchname,
							result);
				} else {
					String fname = file.getName();
					if (fname.equals(matchname)) {
						String id = getContentIdFromFile(rootPath, file);
						if (id != null) {
							result.add(id);
						}
					}
				}
			}
		}
	}

	protected void deleteFile(File file) {

		if (!file.exists()) {
			return;
		}
		if (file.isFile()) {
			file.delete();
		} else if (file.isDirectory()) {
			File[] files = file.listFiles();
			if (files != null) {
				for (File f : files) {
					deleteFile(f);
				}
			}
			file.delete();
		}
	}

	

	protected String[] getFileListWithSuffix(File dir, final String suffix) {
		return dir.list(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String filename) {
				return filename.endsWith(suffix);
			}
		});
	}

	@Override
	public boolean isExistContent(String type, String id) {
		validateArgumentsNotEmpty(type, id);
		try {
			File f = getContentFile(type, id, false);
			return f.exists() && f.canRead();
		} catch (IOException e) {
			// should never happen
			return false;
		}
	}

	@Override
	public Long getStatusLastModified(String type, String id, String statusName) {
		File file = findStatusFile(type, id, statusName);
		if (file == null || !file.exists()) {
			return null;
		}
		return file.lastModified();
	}

	@Override
	public Long getContentLastModified(String type, String id) {
		File file;
		try {
			file = getContentFile(type, id, false);
			if (file == null || !file.exists()) {
				return null;
			}
			return file.lastModified();
		} catch (IOException e) {
			return null;
		}

	}

}
