package com.mikea.decompiler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 */
public class JavaSourceWriter {
    private final String className;
    private final PrintWriter printWriter;
    private int block = 0;

    public JavaSourceWriter(String className) throws FileNotFoundException {
        this.className = className;

        String filename = "out/" + className.replaceAll("\\.", "/") + ".java";
        new File(filename).getParentFile().mkdirs();
        printWriter = new PrintWriter(filename);
    }

    public void close() {
        printWriter.close();
    }

    public void writeComment(String format) {
        printWriter.write("// " + format + "\n");
    }

    public void writePackage(String packageName) {
        writeStatement("package " + packageName);
    }

    void writeStatement(String statement) {
        printWriter.write(statement);
        printWriter.write(";\n");
    }

    public void startClass(String className) {
        startBlock("class " + className);
    }

    private void startBlock(String s) {
        printWriter.write(s + " {\n");
        block++;
    }

    public void endClass() {
        endBlock();
    }

    private void endBlock() {
        printWriter.write("}\n");
        block--;
    }

    public void writeField(String name, String desc) {
        writeStatement(desc + " " + name);
    }

    public void startMethod(String name) {
        startBlock("void " + name + "()");
    }

    public void endMethod() {
        endBlock();
    }
}
