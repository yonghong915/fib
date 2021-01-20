package com.fib.gateway.message.util;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.fib.commons.util.ClassUtil;
import com.fib.gateway.message.xml.message.MultiLanguageResourceBundle;

public class MapUtil {
	private static final String DEFAULT_ENCODING = System.getProperty("file.encoding");
	private static final byte[] MAP_LAYER_TAB = new byte[] { 32, 32, 32, 32 };
	private static final byte[] MAP_LAYER_LINE = "|".getBytes();
	private static final byte[] MAP_LAYER_NODE = "+----".getBytes();
	private static final byte[] MAP_HEAD = "[Map]".getBytes();
	private static final byte[] LIST_HEAD = "[List]".getBytes();
	private static final byte[] NEW_LINE = System.getProperty("line.separator").getBytes();

	public static Object getValue(Map var0, String var1) {
		if (null == var0) {
			throw new IllegalArgumentException(
					MultiLanguageResourceBundle.getInstance().getString("parameter.null", new String[] { "map" }));
		} else if (null == var1) {
			throw new IllegalArgumentException(
					MultiLanguageResourceBundle.getInstance().getString("parameter.null", new String[] { "pathKey" }));
		} else {
			int var2 = var1.indexOf(46);
			String var5;
			if (-1 == var2) {
				int var11 = var1.indexOf(91);
				if (-1 == var11) {
					return var0.get(var1);
				} else {
					int var12 = var1.indexOf(93, var11 + 1);
					if (-1 == var12) {
						throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
								.getString("MapUtil.getValue.wrongPath.notLegalArray.1", new String[] { var1 }));
					} else {
						var5 = var1.substring(0, var11);
						Object var6 = var0.get(var5);
						if (null == var6) {
							return var6;
						} else if (!(var6 instanceof List)) {
							throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
									"MapUtil.getValue.wrongPath.notList.1", new String[] { var1, "" + var6 }));
						} else {
							List var7 = (List) var6;
							if (var12 == var11 + 1) {
								return var7;
							} else {
								boolean var8 = true;

								int var13;
								try {
									var13 = Integer.parseInt(var1.substring(var11 + 1, var12));
								} catch (Exception var10) {
									throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
											"MapUtil.getValue.wrongPath.notLegalArray.2",
											new String[] { var1, var10.getMessage() }), var10);
								}

								if (var13 < 0) {
									throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
											"MapUtil.getValue.wrongPath.notLegalArray.3",
											new String[] { var1, "" + var13 }));
								} else {
									return var13 > var7.size() - 1 ? null : var7.get(var13);
								}
							}
						}
					}
				}
			} else if (var2 == var1.length() - 1) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
						.getString("MapUtil.getValue.wrongPath.noLowerKey", new String[] { var1 }));
			} else {
				String var3 = var1.substring(0, var2);
				Object var4 = getValue(var0, var3);
				if (null == var4) {
					return null;
				} else if (var4 instanceof Map) {
					var5 = var1.substring(var2 + 1);
					return getValue((Map) var4, var5);
				} else {
					throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
							.getString("MapUtil.getValue.wrongPath.notMap", new String[] { var1, "" + var4 }));
				}
			}
		}
	}

	public static void setValue(Map var0, String var1, Object var2) {
		if (null == var0) {
			throw new IllegalArgumentException(
					MultiLanguageResourceBundle.getInstance().getString("parameter.null", new String[] { "map" }));
		} else if (null == var1) {
			throw new IllegalArgumentException(
					MultiLanguageResourceBundle.getInstance().getString("parameter.null", new String[] { "pathKey" }));
		} else {
			int var3 = var1.indexOf(46);
			String var7;
			if (-1 == var3) {
				int var4 = var1.indexOf(91);
				if (-1 == var4) {
					var0.put(var1, var2);
					return;
				}

				int var5 = var1.indexOf(93, var4 + 1);
				if (-1 == var5) {
					throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
							.getString("MapUtil.getValue.wrongPath.notLegalArray.1", new String[] { "pathKey" }));
				}

				boolean var6 = false;
				if (var5 == var4 + 1) {
					var6 = true;
					if (var2 != null && !(var2 instanceof List)) {
						throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
								.getString("MapUtil.getValue.wrongPath.notList.2", new String[] { var1, "" + var2 }));
					}
				}

				var7 = var1.substring(0, var4);
				Object var8 = var0.get(var7);
				if (null == var8) {
					if (var6) {
						var0.put(var7, var2);
						return;
					}

					var8 = new ArrayList();
					var0.put(var7, var8);
				} else if (!(var8 instanceof List)) {
					throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
							.getString("MapUtil.getValue.wrongPath.notList.3", new String[] { var1, "" + var2 }));
				}

				List var9 = (List) var8;
				if (var6) {
					var0.put(var7, var2);
					return;
				}

				boolean var10 = true;

				int var16;
				try {
					var16 = Integer.parseInt(var1.substring(var4 + 1, var5));
				} catch (Exception var12) {
					throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
							"MapUtil.getValue.wrongPath.notLegalArray.2", new String[] { var1, var12.getMessage() }),
							var12);
				}

				if (var16 < 0) {
					throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
							"MapUtil.getValue.wrongPath.notLegalArray.3", new String[] { var1, "" + var16 }));
				}

				if (var16 >= var9.size()) {
					for (int var11 = var9.size(); var11 < var16 + 1; ++var11) {
						var9.add((Object) null);
					}
				}

				var9.set(var16, var2);
			} else {
				if (var3 == var1.length() - 1) {
					throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
							.getString("MapUtil.getValue.wrongPath.noLowerKey", new String[] { var1 }));
				}

				String var13 = var1.substring(0, var3);
				if (var13.indexOf("[]") != -1) {
					throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
							.getString("MapUtil.getValue.wrongPath.canNotHaveLowerKey", new String[] { var1 }));
				}

				Object var14 = getValue(var0, var13);
				Object var15 = null;
				if (null == var14) {
					var15 = new HashMap();
					setValue(var0, var13, var15);
				} else {
					if (!(var14 instanceof Map)) {
						throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
								.getString("MapUtil.getValue.wrongPath.notMap", new String[] { var1, "" + var14 }));
					}

					var15 = (Map) var14;
				}

				var7 = var1.substring(var3 + 1);
				setValue((Map) var15, var7, var2);
			}

		}
	}

	public static Object[] toArray(Map var0) {
		if (null == var0) {
			throw new IllegalArgumentException(
					MultiLanguageResourceBundle.getInstance().getString("parameter.null", new String[] { "map" }));
		} else {
			Object[] var1 = var0.keySet().toArray();
			Arrays.sort(var1);
			Object[] var2 = new Object[var1.length];

			for (int var3 = 0; var3 < var1.length; ++var3) {
				var2[var3] = var0.get(var1[var3]);
			}

			return var2;
		}
	}

	public static List toList(Map var0) {
		return Arrays.asList(toArray(var0));
	}

	public static void printMap(Map var0, String var1) {
		if (null == var0) {
			throw new IllegalArgumentException(
					MultiLanguageResourceBundle.getInstance().getString("parameter.null", new String[] { "map" }));
		} else {
			try {
				printMap(var0, System.out, var1);
			} catch (IOException var3) {
				throw new RuntimeException(var3);
			}
		}
	}

	/** @deprecated */
	public static void printMap(Map var0) {
		printMap(var0, DEFAULT_ENCODING);
	}

	/** @deprecated */
	public static void printMap(Map var0, OutputStream var1) throws IOException {
		printMap(var0, var1, DEFAULT_ENCODING);
	}

	public static void printMap(Map var0, OutputStream var1, String var2) throws IOException {
		if (null == var0) {
			throw new IllegalArgumentException(
					MultiLanguageResourceBundle.getInstance().getString("parameter.null", new String[] { "map" }));
		} else if (null == var1) {
			throw new IllegalArgumentException(
					MultiLanguageResourceBundle.getInstance().getString("parameter.null", new String[] { "out" }));
		} else {
			var1.write(
					MultiLanguageResourceBundle.getInstance().getString("MapUtil.printMap.mapStruct").getBytes(var2));
			var1.write(NEW_LINE);
			printMap(var0, var1, 1, var2);
		}
	}

	public static Map deepCloneMap(Map var0) {
		if (null == var0) {
			return null;
		} else {
			Map var1 = (Map) ClassUtil.createClassInstance(var0.getClass());
			Iterator var2 = var0.entrySet().iterator();
			Entry var3 = null;

			while (var2.hasNext()) {
				var3 = (Entry) var2.next();
				var1.put(cloneObject(var3.getKey()), cloneObject(var3.getValue()));
			}

			return var1;
		}
	}

	public static Object cloneObject(Object var0) {
		if (null == var0) {
			return null;
		} else {
			Class var1 = var0.getClass();
			if (var0 instanceof String) {
				return var0;
			} else if (var0 instanceof Integer) {
				return var1 == Integer.TYPE ? var0 : new Integer((Integer) var0);
			} else if (var0 instanceof Long) {
				return var1 == Long.TYPE ? var0 : new Long((Long) var0);
			} else if (var0 instanceof Double) {
				return var1 == Double.TYPE ? var0 : new Double((Double) var0);
			} else if (var0 instanceof Short) {
				return var1 == Short.TYPE ? var0 : new Short((Short) var0);
			} else if (var0 instanceof Float) {
				return var1 == Float.TYPE ? var0 : new Float((Float) var0);
			} else if (var0 instanceof BigDecimal) {
				return new BigDecimal(var0.toString());
			} else if (var0 instanceof Byte) {
				return var1 == Byte.TYPE ? var0 : new Byte((Byte) var0);
			} else {
				int var4;
				if (var0 instanceof byte[]) {
					byte[] var7 = (byte[]) ((byte[]) var0);
					byte[] var10 = new byte[var7.length];

					for (var4 = 0; var4 < var7.length; ++var4) {
						var10[var4] = var7[var4];
					}

					return var10;
				} else if (var0 instanceof Boolean) {
					return var1 == Boolean.TYPE ? var0 : new Boolean((Boolean) var0);
				} else if (var0 instanceof String[]) {
					String[] var6 = (String[]) ((String[]) var0);
					String[] var9 = new String[var6.length];

					for (var4 = 0; var4 < var6.length; ++var4) {
						var9[var4] = var6[var4];
					}

					return var9;
				} else if (var0 instanceof Set) {
					Set var5 = (Set) ClassUtil.createClassInstance(var1);
					AbstractSet var8 = (AbstractSet) var0;
					Iterator var11 = var8.iterator();

					while (var11.hasNext()) {
						var5.add(cloneObject(var11.next()));
					}

					return var5;
				} else if (var0 instanceof Map) {
					return deepCloneMap((Map) var0);
				} else if (!(var0 instanceof List)) {
					return ClassUtil.invoke(var0, "clone", (Class[]) null, (Object[]) null);
				} else {
					List var2 = (List) ClassUtil.createClassInstance(var1);
					List var3 = (List) var0;

					for (var4 = 0; var4 < var3.size(); ++var4) {
						var2.add(cloneObject(var3.get(var4)));
					}

					return var2;
				}
			}
		}
	}

	private static void printMap(Map var0, OutputStream var1, int var2, String var3) throws IOException {
		Iterator var4 = var0.entrySet().iterator();
		Entry var5 = null;
		Object var6 = null;
		Object var7 = null;

		while (var4.hasNext()) {
			var5 = (Entry) var4.next();
			var6 = var5.getKey();
			var7 = var5.getValue();
			printLayer(var2, var1);
			var1.write(("\"" + var6 + "\" = ").getBytes(var3));
			if (var7 == null) {
				var1.write(NEW_LINE);
			} else if (var7 instanceof Map) {
				var1.write(MAP_HEAD);
				var1.write(NEW_LINE);
				printMap((Map) var7, var1, var2 + 1, var3);
			} else if (var7 instanceof List) {
				var1.write(LIST_HEAD);
				var1.write(NEW_LINE);
				printList((List) var7, var1, var2 + 1, var3);
			} else if (var7 instanceof byte[]) {
				var1.write(CodeUtil.BytetoHex((byte[]) ((byte[]) var7)));
				var1.write(NEW_LINE);
			} else {
				var1.write(var7.toString().getBytes(var3));
				var1.write(NEW_LINE);
			}
		}

	}

	private static void printList(List var0, OutputStream var1, int var2, String var3) throws IOException {
		Iterator var4 = var0.iterator();
		int var5 = 0;

		while (var4.hasNext()) {
			Object var6 = var4.next();
			printLayer(var2, var1);
			var1.write(("[" + var5++ + "] = ").getBytes());
			if (var6 == null) {
				var1.write(NEW_LINE);
			} else if (var6 instanceof Map) {
				var1.write(MAP_HEAD);
				var1.write(NEW_LINE);
				printMap((Map) var6, var1, var2 + 1, var3);
			} else if (var6 instanceof List) {
				var1.write(LIST_HEAD);
				var1.write(NEW_LINE);
				printList((List) var6, var1, var2 + 1, var3);
			} else if (var6 instanceof byte[]) {
				var1.write(CodeUtil.BytetoHex((byte[]) ((byte[]) var6)));
				var1.write(NEW_LINE);
			} else {
				var1.write(var6.toString().getBytes(var3));
				var1.write(NEW_LINE);
			}
		}

	}

	private static void printLayer(int var0, OutputStream var1) throws IOException {
		if (0 != var0) {
			int var2;
			for (var2 = 0; var2 < var0; ++var2) {
				var1.write(MAP_LAYER_TAB);
			}

			var1.write(MAP_LAYER_LINE);
			var1.write(NEW_LINE);

			for (var2 = 0; var2 < var0; ++var2) {
				var1.write(MAP_LAYER_TAB);
			}

			var1.write(MAP_LAYER_NODE);
		}
	}
}
