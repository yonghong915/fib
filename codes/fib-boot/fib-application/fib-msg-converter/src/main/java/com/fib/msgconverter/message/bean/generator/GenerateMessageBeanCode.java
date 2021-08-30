// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.fib.msgconverter.message.bean.generator;

import java.io.File;
import java.io.FilenameFilter;

import com.giantstone.common.config.ConfigManager;
import com.giantstone.message.metadata.MessageMetadataManager;

// Referenced classes of package com.giantstone.message.bean.generator:
//			MessageBeanCodeGenerator

public class GenerateMessageBeanCode
{

	public GenerateMessageBeanCode()
	{
	}

	public static void main(String args[])
	{
		if (args.length != 4 && args.length != 5)
		{
			printUsage();
			System.exit(0);
		}
		if (args.length == 4 && !"-a".equals(args[2]))
		{
			printUsage();
			System.exit(0);
		}
		if (args.length == 5 && !"-i".equals(args[2]))
		{
			printUsage();
			System.exit(0);
		}
		ConfigManager.setRootPath(args[0]);
		String s = args[0];
		MessageMetadataManager.loadMetadataGroup(s, "");
		Object obj = null;
		if (args.length == 5)
		{
			MessageBeanCodeGenerator messagebeancodegenerator = new MessageBeanCodeGenerator();
			messagebeancodegenerator.setOutputDir(args[1]);
			messagebeancodegenerator.generate(MessageMetadataManager.getMessage(s, args[3]), args[4]);
		} else
		{
			File file = new File(args[0]);
			String args1[] = file.list(new FilenameFilter() {

				public boolean accept(File file1, String s1)
				{
					return s1.endsWith(".xml");
				}

			});
			for (int i = 0; i < args1.length; i++)
			{
				MessageBeanCodeGenerator messagebeancodegenerator1 = new MessageBeanCodeGenerator();
				messagebeancodegenerator1.setOutputDir(args[1]);
				messagebeancodegenerator1.generate(MessageMetadataManager.getMessage(s, args1[i].substring(0, args1[i].lastIndexOf(".xml"))), args[3]);
			}

		}
	}

	private static void printUsage()
	{
		System.out.println("Usage:");
		System.out.println((new StringBuilder()).append("\tjava ").append(MessageBeanCodeGenerator.class.getName()).append(" inputDir outputDir -i messageBeanId encoding").toString());
		System.out.println((new StringBuilder()).append("\tjava ").append(MessageBeanCodeGenerator.class.getName()).append(" inputDir outputDir -a encoding").toString());
	}
}
