package com.mikea.decompiler;

import org.objectweb.asm.ClassReader;

import java.io.IOException;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class App {
    public static void main(String[] args) throws IOException {
        JarFile jarFile = new JarFile(args[0]);
        Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            JarEntry jarEntry = entries.nextElement();
            decompile(jarFile, jarEntry);
        }
    }

    private static void decompile(JarFile jarFile, JarEntry jarEntry) throws IOException {
        String name = jarEntry.getName();
        if (!name.endsWith(".class")) {
            return;
        }

        String className = name.substring(0, name.length() - ".class".length()).replaceAll("/", ".");

        JavaSourceWriter writer = new JavaSourceWriter(className);

        ClassReader reader = new ClassReader(jarFile.getInputStream(jarEntry));
        DecompileVisitor decompileVisitor = new DecompileVisitor(writer);
        decompileVisitor.decompile(reader);

        writer.close();
    }
}
