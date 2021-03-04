package com.onlinejudge.JudgeHost.core.enumerations;
import java.util.stream.Stream;

//编译脚本枚举类
public enum LanguageScriptEnum {
    // for python
    PYTHON("#!/bin/sh\n" +
            "\n" +
            "echo '#!/bin/sh' > run" +
            "\n" +
            "echo 'python3 CODE_PATH' >> run", "py"),

    // for java
    JAVA("#!/bin/sh\n" +
            "\n" +
            "javac CODE_PATH"+
            "\n" +
            "echo '#!/bin/sh' > run" +
            "\n" +
            "echo 'cd SUBMISSION_PATH' >> run" +
            "\n" +
            "echo 'java Main' >> run", "java"),

            // for c
    C("#!/bin/sh\n" +
            "\n" +
            "gcc -Wall -O2 -std=gnu11 CODE_PATH -o run -lm", "c"),

    // for cpp
    C_PLUS_PLUS("#!/bin/sh\n" +
            "\n" +
            "g++ -Wall -O2  CODE_PATH -o run", "cpp");

    public String getBuildScript() {
        return buildScript;
    }

    public String getBuildScriptByRunningPath(String submissionPath,String codePath){
        String script = getBuildScript();
        script = script.replace("SUBMISSION_PATH", submissionPath).replace("CODE_PATH", codePath);
        return script;
    }

    public String getExtensionName() {
        return extensionName;
    }
    private final String buildScript;
    private final String extensionName;

    LanguageScriptEnum(String buildScript, String extensionName) {
        this.buildScript = buildScript;
        this.extensionName = extensionName;
    }

    public static LanguageScriptEnum toLanguageType(String language) {
        return Stream.of(LanguageScriptEnum.values())
                .filter(c -> c.toString().equals(language))
                .findAny()
                .orElse(null);
    }

    public static Boolean isLanguageAccepted(String languageName){
        return toLanguageType(languageName) != null;
    }

}
