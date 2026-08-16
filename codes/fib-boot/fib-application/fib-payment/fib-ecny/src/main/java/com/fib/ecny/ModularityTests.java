package com.fib.ecny;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;

/**
 * Tests to verify the modular structure and generate documentation for the modules.
 */
public class ModularityTests {
    ApplicationModules modules = ApplicationModules.of(EcnyApplication.class);

    @Test
    void verifyModularity() {
        modules.verify();  // 验证模块边界，任何违规都会导致测试失败
    }

    @Test
    void createModuleDocumentation() {
        new Documenter(modules)
                .writeModulesAsPlantUml()           // 生成PlantUML架构图
                .writeModuleCanvases();              // 生成每个模块的详细说明文档
    }
}
