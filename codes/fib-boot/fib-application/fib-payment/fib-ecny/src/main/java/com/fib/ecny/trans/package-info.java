@org.springframework.modulith.ApplicationModule(
        displayName = "交易模块",
        allowedDependencies = {"wallet::api", "common", "base", "base :: trans-api"})
package com.fib.ecny.trans;