package com.fib.gateway.message.xml.message.bean.generator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Stack;

import com.fib.gateway.message.xml.message.CodeUtil;
import com.fib.gateway.message.xml.message.MultiLanguageResourceBundle;

public class FileUtil {
	public static String loadData(String var0, String var1) {
		if (null != var0 && 0 != var0.length()) {
			File var2 = new File(var0);
			if (!var2.exists()) {
				throw new IllegalArgumentException(
						MultiLanguageResourceBundle.getInstance().getString("file.notExist", new String[] { var0 }));
			} else if (var2.isDirectory()) {
				throw new IllegalArgumentException(
						MultiLanguageResourceBundle.getInstance().getString("file.isDirectory", new String[] { var0 }));
			} else {
				char[] var3 = new char[(int) var2.length()];
				FileInputStream var4 = null;
				InputStreamReader var5 = null;

				try {
					var4 = new FileInputStream(var2);
					var5 = new InputStreamReader(var4, var1);
					var5.read(var3);
				} catch (Exception var20) {
					//ExceptionUtil.throwActualException(var20);
				} finally {
					if (null != var5) {
						try {
							var5.close();
						} catch (Exception var19) {
							var19.printStackTrace(System.err);
						}
					}

					if (null != var4) {
						try {
							var4.close();
						} catch (Exception var18) {
							var18.printStackTrace(System.err);
						}
					}

				}

				return new String(var3);
			}
		} else {
			throw new IllegalArgumentException(
					MultiLanguageResourceBundle.getInstance().getString("parameter.null", new String[] { "fileName" }));
		}
	}

	public static byte[] loadData(String var0) {
		if (null != var0 && 0 != var0.length()) {
			File var1 = new File(var0);
			if (!var1.exists()) {
				throw new IllegalArgumentException(
						MultiLanguageResourceBundle.getInstance().getString("file.notExist", new String[] { var0 }));
			} else if (var1.isDirectory()) {
				throw new IllegalArgumentException(
						MultiLanguageResourceBundle.getInstance().getString("file.isDirectory", new String[] { var0 }));
			} else {
				byte[] var2 = new byte[(int) var1.length()];
				FileInputStream var3 = null;

				try {
					var3 = new FileInputStream(var1);
					var3.read(var2);
				} catch (Exception var14) {
					ExceptionUtil.throwActualException(var14);
				} finally {
					if (null != var3) {
						try {
							var3.close();
						} catch (Exception var13) {
							var13.printStackTrace(System.err);
						}
					}

				}

				return var2;
			}
		} else {
			throw new IllegalArgumentException(
					MultiLanguageResourceBundle.getInstance().getString("parameter.null", new String[] { "fileName" }));
		}
	}

	public static void writeData(String var0, byte[] var1, boolean var2) {
		if (null != var1 && 0 != var1.length) {
			if (null != var0 && 0 != var0.length()) {
				File var3 = new File(var0);
				if (!var3.exists()) {
					throw new IllegalArgumentException(MultiLanguageResourceBundle.getInstance()
							.getString("file.notExist", new String[] { var0 }));
				} else if (var3.isDirectory()) {
					throw new IllegalArgumentException(MultiLanguageResourceBundle.getInstance()
							.getString("file.isDirectory", new String[] { var0 }));
				} else {
					FileOutputStream var4 = null;

					try {
						var4 = new FileOutputStream(var3, var2);
						var4.write(var1);
						var4.flush();
					} catch (Exception var15) {
						ExceptionUtil.throwActualException(var15);
					} finally {
						if (null != var4) {
							try {
								var4.close();
							} catch (Exception var14) {
								var14.printStackTrace(System.err);
							}
						}

					}

				}
			} else {
				throw new IllegalArgumentException(MultiLanguageResourceBundle.getInstance().getString("parameter.null",
						new String[] { "fileName" }));
			}
		} else {
			throw new IllegalArgumentException(
					MultiLanguageResourceBundle.getInstance().getString("parameter.null", new String[] { "data" }));
		}
	}

	private static File checkFileName(String var0) {
		if (null != var0 && 0 != var0.length()) {
			File var1 = new File(var0);
			if (!var1.exists()) {
				throw new IllegalArgumentException(
						MultiLanguageResourceBundle.getInstance().getString("file.notExist", new String[] { var0 }));
			} else if (var1.isDirectory()) {
				throw new IllegalArgumentException(
						MultiLanguageResourceBundle.getInstance().getString("file.isDirectory", new String[] { var0 }));
			} else {
				return var1;
			}
		} else {
			throw new IllegalArgumentException(
					MultiLanguageResourceBundle.getInstance().getString("parameter.null", new String[] { "fileName" }));
		}
	}

	public static void saveAsData(String var0, byte[] var1, boolean var2) {
		if (null != var1 && 0 != var1.length) {
			if (null != var0 && 0 != var0.length()) {
				File var3 = new File(var0);
				if (var3.isDirectory()) {
					throw new IllegalArgumentException(MultiLanguageResourceBundle.getInstance()
							.getString("file.isDirectory", new String[] { var0 }));
				} else {
					if (var3.getParentFile() != null) {
						var3.getParentFile().mkdirs();
					}

					FileOutputStream var4 = null;

					try {
						var4 = new FileOutputStream(var3, var2);
						var4.write(var1);
						var4.flush();
					} catch (Exception var15) {
						ExceptionUtil.throwActualException(var15);
					} finally {
						if (null != var4) {
							try {
								var4.close();
							} catch (Exception var14) {
								var14.printStackTrace(System.err);
							}
						}

					}

				}
			} else {
				throw new IllegalArgumentException(MultiLanguageResourceBundle.getInstance().getString("parameter.null",
						new String[] { "fileName" }));
			}
		} else {
			throw new IllegalArgumentException(
					MultiLanguageResourceBundle.getInstance().getString("parameter.null", new String[] { "data" }));
		}
	}

	public static void saveAsData(String var0, String var1, String var2, boolean var3) {
		if (null != var1 && 0 != var1.length()) {
			if (null != var0 && 0 != var0.length()) {
				File var4 = new File(var0);
				if (var4.isDirectory()) {
					throw new IllegalArgumentException(MultiLanguageResourceBundle.getInstance()
							.getString("file.isDirectory", new String[] { var0 }));
				} else {
					if (var4.getParentFile() != null) {
						var4.getParentFile().mkdirs();
					}

					FileOutputStream var5 = null;
					OutputStreamWriter var6 = null;

					try {
						var5 = new FileOutputStream(var4, var3);
						var6 = new OutputStreamWriter(var5, var2);
						var6.write(var1);
						var6.flush();
					} catch (Exception var21) {
						ExceptionUtil.throwActualException(var21);
					} finally {
						if (null != var6) {
							try {
								var6.close();
							} catch (Exception var20) {
								var20.printStackTrace(System.err);
							}
						}

						if (null != var5) {
							try {
								var5.close();
							} catch (Exception var19) {
								var19.printStackTrace(System.err);
							}
						}

					}

				}
			} else {
				throw new IllegalArgumentException(MultiLanguageResourceBundle.getInstance().getString("parameter.null",
						new String[] { "fileName" }));
			}
		} else {
			throw new IllegalArgumentException(
					MultiLanguageResourceBundle.getInstance().getString("parameter.null", new String[] { "data" }));
		}
	}

	public static boolean isExist(String var0) {
		if (null != var0 && 0 != var0.length()) {
			File var1 = new File(var0);
			return var1.exists();
		} else {
			throw new IllegalArgumentException(
					MultiLanguageResourceBundle.getInstance().getString("parameter.null", new String[] { "fileName" }));
		}
	}

	/** @deprecated */
	public static boolean isExits(String var0) {
		return isExist(var0);
	}

	public static byte[] getDigest(String var0) {
		File var1 = checkFileName(var0);
		MessageDigest var2 = null;

		try {
			var2 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException var21) {
			ExceptionUtil.throwActualException(var21);
		}

		long var3 = var1.length();
		long var5 = 0L;
		boolean var7 = false;
		FileInputStream var8 = null;

		try {
			var8 = new FileInputStream(var1);

			int var24;
			for (byte[] var9 = new byte[10240]; var5 < var3; var5 += (long) var24) {
				var24 = var8.read(var9);
				if (var24 <= 0) {
					throw new RuntimeException("onceRead=" + var24);
				}

				var2.update(var9, 0, var24);
			}

			byte[] var10 = var2.digest();
			return var10;
		} catch (Exception var22) {
			ExceptionUtil.throwActualException(var22);
		} finally {
			if (var8 != null) {
				try {
					var8.close();
				} catch (IOException var20) {
					var20.printStackTrace();
				}
			}

		}

		return null;
	}

	public static String getDigestAsString(String var0) {
		return new String(CodeUtil.BytetoHex(getDigest(var0)));
	}

	public static boolean verifyDigest(String var0, byte[] var1) {
		if (null == var1) {
			throw new IllegalArgumentException(
					MultiLanguageResourceBundle.getInstance().getString("parameter.null", new String[] { "digest" }));
		} else {
			byte[] var2 = getDigest(var0);
			if (var2.length != var1.length) {
				return false;
			} else {
				for (int var3 = 0; var3 < var2.length; ++var3) {
					if (var2[var3] != var1[var3]) {
						return false;
					}
				}

				return true;
			}
		}
	}

	public static boolean verifyDigest(String var0, String var1) {
		if (null != var1 && 0 != var1.length()) {
			byte[] var2 = CodeUtil.HextoByte(var1);
			return verifyDigest(var0, var2);
		} else {
			throw new IllegalArgumentException(MultiLanguageResourceBundle.getInstance().getString("parameter.null",
					new String[] { "digestString" }));
		}
	}

	public static void move(String var0, String var1) {
		if (var0 == null || !var0.equals(var1)) {
			File var2 = new File(var0);
			if (!var2.exists()) {
				throw new IllegalArgumentException(
						MultiLanguageResourceBundle.getInstance().getString("FileUtil.sourceFile.notExist"));
			} else {
				File var3 = new File(var1);
				if (var3.exists()) {
					var3.delete();
				}

				File var4 = var3.getParentFile();
				var4.mkdirs();
				var2.renameTo(var3);
			}
		}
	}

	public static void copy(String var0, String var1) {
		if (null != var0 && 0 != var0.length()) {
			if (var0.equals(var1)) {
				throw new IllegalArgumentException(
						MultiLanguageResourceBundle.getInstance().getString("FileUtil.sourceFile.destFile.same"));
			} else {
				File var2 = new File(var1);
				File var3 = var2.getParentFile();
				if (!var3.exists()) {
					var3.mkdirs();
				}

				byte[] var4 = new byte[8192];
				boolean var5 = false;
				FileInputStream var6 = null;
				FileOutputStream var7 = null;

				try {
					var6 = new FileInputStream(var0);
					var7 = new FileOutputStream(var1);

					int var15;
					while ((var15 = var6.read(var4)) > 0) {
						var7.write(var4, 0, var15);
					}
				} catch (Exception var13) {
					ExceptionUtil.throwActualException(var13);
				} finally {
					close((InputStream) var6);
					close((OutputStream) var7);
				}

			}
		} else {
			throw new IllegalArgumentException(
					MultiLanguageResourceBundle.getInstance().getString("FileUtil.sourceFile.null"));
		}
	}

	public static void waitForAccess(File var0) {
		if (var0 != null && var0.exists()) {
			Stack var1 = new Stack();
			int var2 = 0;

			while (true) {
				var1.push(var0.length());

				try {
					Thread.sleep(5000L);
				} catch (InterruptedException var6) {
					var6.printStackTrace();
				}

				++var2;
				if (var2 == 3) {
					Long var3 = (Long) var1.pop();
					Long var4 = (Long) var1.pop();
					Long var5 = (Long) var1.pop();
					if (var3.equals(var4) && var3.equals(var5)) {
						return;
					}

					var2 = 0;
					var1.clear();
				}
			}
		}
	}

	public static boolean delete(String var0) {
		File var1 = new File(var0);
		if (!var1.exists()) {
			throw new IllegalArgumentException(
					MultiLanguageResourceBundle.getInstance().getString("file.notExist", new String[] { var0 }));
		} else {
			return var1.delete();
		}
	}

	private static void close(InputStream var0) {
		if (var0 != null) {
			try {
				var0.close();
			} catch (IOException var2) {
				var2.printStackTrace();
			}
		}

	}

	private static void close(OutputStream var0) {
		if (var0 != null) {
			try {
				var0.close();
			} catch (IOException var2) {
				var2.printStackTrace();
			}
		}

	}

	public static String getFilePath(String var0, String var1) {
		if (var1 == null) {
			throw new IllegalArgumentException(
					MultiLanguageResourceBundle.getInstance().getString("parameter.null", new String[] { "file" }));
		} else if (var0 != null && var0.length() >= 1) {
			File var2 = new File(var0);
			var2.mkdirs();
			return (new File(var0, var1)).getAbsolutePath();
		} else {
			return (new File(var1)).getAbsolutePath();
		}
	}

	public static void copyAllFromDirectory(File var0, File var1) {
		if (null == var0) {
			throw new IllegalArgumentException(
					MultiLanguageResourceBundle.getInstance().getString("parameter.null", new String[] { "srcFile" }));
		} else if (null == var1) {
			throw new IllegalArgumentException(
					MultiLanguageResourceBundle.getInstance().getString("parameter.null", new String[] { "srcFile" }));
		} else {
			String var2 = null;

			try {
				var2 = var0.getCanonicalPath();
			} catch (IOException var6) {
				ExceptionUtil.throwActualException(var6);
			}

			String var3 = null;

			try {
				var3 = var1.getCanonicalPath();
			} catch (IOException var5) {
				ExceptionUtil.throwActualException(var5);
			}

			if (var2.equals(var3)) {
				throw new IllegalArgumentException(
						MultiLanguageResourceBundle.getInstance().getString("FileUtil.sourcePath.destPath.same"));
			} else {
				//copyAllFromDirectory(var2, var3);
			}
		}
	}

//	public static void copyAllFromDirectory(String var0, String var1) {
//		if (null != var0 && 0 != var0.length()) {
//			if (null != var1 && 0 != var1.length()) {
//				if (var0.equals(var1)) {
//					throw new IllegalArgumentException(
//							MultiLanguageResourceBundle.getInstance().getString("FileUtil.sourcePath.destPath.same"));
//				} else {
//					File var2 = new File(var0);
//					if (var2.exists()) {
//						File var3 = new File(var1);
//						if (var3.isFile()) {
//							throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
//									.getString("FileUtil.destPath.notDirectory", new String[] { var1 }));
//						} else {
//							if (!var3.exists()) {
//								var3.mkdirs();
//							}
//
//							Project var4 = new Project();
//							Copy var5 = new Copy();
//							var5.setProject(var4);
//							if (var2.isFile()) {
//								var5.setFile(var2);
//							} else {
//								FileSet var6 = new FileSet();
//								var6.setDir(var2);
//								var5.addFileset(var6);
//							}
//
//							var5.setTodir(var3);
//							var5.execute();
//						}
//					}
//				}
//			} else {
//				throw new IllegalArgumentException(MultiLanguageResourceBundle.getInstance().getString("parameter.null",
//						new String[] { "destPath" }));
//			}
//		} else {
//			throw new IllegalArgumentException(
//					MultiLanguageResourceBundle.getInstance().getString("parameter.null", new String[] { "srcPath" }));
//		}
//	}
}
