package io.coerce.services.modules;

import java.util.Map;

public class ModuleMap {
    private final Map<String, Map<String, String>> modules;

    public ModuleMap(Map<String, Map<String, String>> modules) {
        this.modules = modules;
    }

    public Map<String, Map<String, String>> getModules() {
        return modules;
    }
}
