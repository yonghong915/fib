
package com.fib.msgconverter.commgateway.util;

import com.giantstone.common.util.CodeUtil;
import java.util.HashSet;

public class A {
    public A() {
    }

    public static String A(String var0, String var1, char var2) {
        boolean var3 = false;
        boolean var4 = false;
        boolean var5 = false;
        int var6 = 0;
        boolean var7 = false;
        int var8 = 0;
        HashSet var9 = new HashSet();
        String[] var10 = var1.split(",");

        for(int var11 = 0; var11 < var10.length; ++var11) {
            var9.add(var10[var11].trim());
        }

        String var25 = new String(CodeUtil.BytetoHex((byte)var2));
        StringBuffer var12 = new StringBuffer();

        while(true) {
            while(true) {
                while(true) {
                    String var14;
                    int var15;
                    do {
                        while(true) {
                            int var21 = var0.indexOf("k=\"", var6 + "\"".length());
                            if(-1 == var21) {
                                var12.append(var0, var8, var0.length());
                                return var12.toString();
                            }

                            int var13 = var21 + "k=\"".length();
                            int var22 = var0.indexOf("\"", var13);
                            var14 = var0.substring(var13, var22);
                            int var23 = var0.indexOf("t=\"", var22);
                            if(var0.indexOf(">", var22) >= var23 && -1 != var23) {
                                var15 = var23 + "t=\"".length();
                                var6 = var0.indexOf("\"", var15);
                                break;
                            }

                            var6 = var22 + "\"".length();
                        }
                    } while(!var9.contains(var14));

                    String var16 = var0.substring(var15, var6);
                    String var17;
                    int var24;
                    int var26;
                    if("S".equals(var16)) {
                        var24 = var0.indexOf(">", var6);
                        var12.append(var0, var8, var24 + 1);
                        if(47 == var0.charAt(var24 - 1)) {
                            var8 = var24 + 1;
                        } else {
                            var8 = var0.indexOf("<", var24);
                            var17 = var0.substring(var24 + ">".length(), var8);

                            for(var26 = 0; var26 < var17.length(); ++var26) {
                                var12.append(var2);
                            }
                        }
                    } else if("b".equals(var16)) {
                        var24 = var0.indexOf(">", var6);
                        var12.append(var0, var8, var24 + 1);
                        if(47 == var0.charAt(var24 - 1)) {
                            var8 = var24 + 1;
                        } else {
                            var12.append(var25);
                        }
                    } else if("B".equals(var16)) {
                        var24 = var0.indexOf(">", var6);
                        var12.append(var0, var8, var24 + 1);
                        if(47 == var0.charAt(var24 - 1)) {
                            var8 = var24 + 1;
                        } else {
                            var8 = var0.indexOf("<", var24);
                            var17 = var0.substring(var24 + ">".length(), var8);

                            for(var26 = 0; var26 < var17.length(); var26 += 2) {
                                var12.append(var25);
                            }
                        }
                    } else if("SS".equals(var16)) {
                        var24 = var0.indexOf(">", var6);
                        var12.append(var0, var8, var24 + 1);
                        if(47 == var0.charAt(var24 - 1)) {
                            var8 = var24 + 1;
                        } else {
                            var8 = var0.indexOf("<", var24);
                            var17 = var0.substring(var24 + ">".length(), var8);
                            String[] var18 = var17.split(",");

                            for(int var19 = 0; var19 < var18.length; ++var19) {
                                for(int var20 = 0; var20 < var18[var19].length(); ++var20) {
                                    var12.append(var2);
                                }

                                if(var19 != var18.length - 1) {
                                    var12.append(",");
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
