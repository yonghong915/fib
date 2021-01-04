package com.fib.gateway.message.util;


import com.fib.gateway.message.xml.message.MultiLanguageResourceBundle;

public class ConfigManager {
	private static boolean isWebApp = true;
	private static String rootPath = System.getProperty("user.dir");
//	private static ServletContext servletContext = null;
//
//	public static void setServletContext(ServletContext var0) {
//		if (null == var0) {
//			throw new IllegalArgumentException(
//					MultiLanguageResourceBundle.getInstance().getString("parameter.null", new String[] { "context" }));
//		} else {
//			servletContext = var0;
//			isWebApp = true;
//		}
//	}

	public static void setRootPath(String var0) {
		if (null == var0) {
			throw new IllegalArgumentException(MultiLanguageResourceBundle.getInstance().getString("parameter.null",
					new String[] { "newRootPath" }));
		} else {
			rootPath = var0;
			if (!rootPath.endsWith(System.getProperty("file.separator"))) {
				rootPath = rootPath + System.getProperty("file.separator");
			}

			isWebApp = false;
		}
	}

	public static String getFullPathFileName(String var0) {
		if (null == var0) {
			throw new IllegalArgumentException(MultiLanguageResourceBundle.getInstance().getString("parameter.null",
					new String[] { "fileNameWithoutPath" }));
		} else if (isWebApp) {
			throw new IllegalArgumentException(
					MultiLanguageResourceBundle.getInstance().getString("ConfigManager.getFullPathFileName.webApp"));
		} else {
			return rootPath + var0;
		}
	}

//	public static InputStream getInputStream(String var0) {
//		if (null != var0 && 0 != var0.length()) {
//			if (isWebApp) {
//				if (null == servletContext) {
//					throw new RuntimeException(
//							MultiLanguageResourceBundle.getInstance().getString("ConfigManager.getInputStream.webApp"));
//				} else {
//					InputStream var5 = servletContext.getResourceAsStream("/WEB-INF/" + var0);
//					if (null == var5) {
//						throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("file.notExist",
//								new String[] { "/WEB-INF/" + var0 }));
//					} else {
//						return var5;
//					}
//				}
//			} else {
//				var0 = rootPath + var0;
//				File var1 = new File(var0);
//				if (!var1.exists()) {
//					throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("file.notExist",
//							new String[] { var0 }));
//				} else if (var1.isDirectory()) {
//					throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("file.isDirectory",
//							new String[] { var0 }));
//				} else {
//					FileInputStream var2 = null;
//
//					try {
//						var2 = new FileInputStream(var1);
//					} catch (Exception var4) {
//						ExceptionUtil.throwActualException(var4);
//					}
//
//					return var2;
//				}
//			}
//		} else {
//			throw new IllegalArgumentException(
//					MultiLanguageResourceBundle.getInstance().getString("parameter.null", new String[] { "fileName" }));
//		}
//	}

//	public static Properties loadProperties(String var0) {
//		if (null != var0 && 0 != var0.length()) {
//			InputStream var1 = getInputStream(var0);
//			Properties var2 = new Properties();
//
//			try {
//				var2.load(var1);
//			} catch (Exception var11) {
//				System.out.println("load file " + var0 + " failed!" + var11.getMessage());
//				var11.printStackTrace(System.err);
//				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("loadFile.failed",
//						new String[] { var0, var11.getMessage() }), var11);
//			} finally {
//				if (null != var1) {
//					try {
//						var1.close();
//					} catch (Exception var10) {
//						var10.printStackTrace(System.err);
//					}
//				}
//
//			}
//
//			return var2;
//		} else {
//			throw new IllegalArgumentException(
//					MultiLanguageResourceBundle.getInstance().getString("parameter.null", new String[] { "fileName" }));
//		}
//	}

	private ConfigManager() {
	}
}
