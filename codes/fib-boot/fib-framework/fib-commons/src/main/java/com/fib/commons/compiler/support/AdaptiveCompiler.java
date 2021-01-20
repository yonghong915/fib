package com.fib.commons.compiler.support;

import com.fib.commons.extension.Adaptive;
import com.fib.commons.extension.ExtensionLoader;
import com.fib.commons.compiler.Compiler;

/**
 * 
 * @author fangyh
 * @version 1.0
 * @since 1.0
 * @date 2021-01-20
 */
@Adaptive
public class AdaptiveCompiler implements Compiler {

	private static volatile String defaultCompiler;

	public static void setDefaultCompiler(String compiler) {
		defaultCompiler = compiler;
	}

	@Override
	public Class<?> compile(String code, ClassLoader classLoader) {
		Compiler compiler;
		ExtensionLoader<Compiler> loader = ExtensionLoader.getExtensionLoader(Compiler.class);
		String name = defaultCompiler; // copy reference
		if (name != null && name.length() > 0) {
			compiler = loader.getExtension(name);
		} else {
			compiler = loader.getDefaultExtension();
		}
		return compiler.compile(code, classLoader);
	}

}
