package com.fib.ecny.test;

import com.fib.ecny.EcnyApplication;
import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;


public class ModuleArchitectureTest {
    @Test
    void verifyModuleArchitecture() {
        // 自动扫描模块并校验所有架构规则，违规直接抛异常
       var modules = ApplicationModules.of(EcnyApplication.class).verify();

//        // 自动生成模块结构图（target/modulith-docs）
        new Documenter(modules)
                .writeModulesAsPlantUml()
                .writeIndividualModulesAsPlantUml();
    }
}
