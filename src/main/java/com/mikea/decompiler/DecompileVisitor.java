package com.mikea.decompiler;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 */
public class DecompileVisitor extends ClassVisitor {
    private final JavaSourceWriter writer;
    private Map<String, MethodInfo> methodInfos = new HashMap<String, MethodInfo>();

    public DecompileVisitor(JavaSourceWriter writer) {
        super(Opcodes.ASM4);
        this.writer = writer;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        String className = name;
        if (name.indexOf("/") >= 0) {
            String packageName = name.substring(0, name.lastIndexOf("/")).replaceAll("/", ".");
            className = name.substring(name.lastIndexOf("/") + 1);
            writer.writePackage(packageName);
        }

        writer.writeComment(String.format("visit: version=%d, access=%d, name=%s, signature=%s, superName=%s, interfaces=%s",
                version, access, name, signature, superName, Arrays.asList(interfaces).toString()));
        writer.writeComment("todo: access, signature, supername, interfaces");
        writer.startClass(className);
    }

    @Override
    public void visitSource(String source, String debug) {
        writer.writeComment(String.format("visitSource: %s, %s", source, debug));
    }

    @Override
    public void visitOuterClass(String owner, String name, String desc) {
        writer.writeComment(String.format("visitOuterClass: owner=%s, name=%s, desc=%s", owner, name, desc));

    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        writer.writeComment(String.format("visitAnnotation: desc=%s, visible=%b", desc, visible));
        return null;
    }

    @Override
    public void visitAttribute(Attribute attribute) {
        throw new UnsupportedOperationException("Method visitAttribute not implemented in com.mikea.decompiler.DecompileVisitor .");

    }

    @Override
    public void visitInnerClass(String name, String outerName, String innerName, int access) {
        writer.writeComment(String.format("visitInnerClass: name=%s, outerName=%s, innerName=%s, access=%d", name, outerName, innerName, access));
    }

    @Override
    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
        writer.writeComment(String.format("visitField: %d, name=%s, desc=%s, signature=%s, value=%s",
                access, name, desc, signature, value));
        writer.writeComment("todo: access, signature, value");
        Type type = Type.getType(desc);
        writer.writeField(name, toJavaType(type));

        return new FieldVisitor(Opcodes.ASM4) {
            @Override
            public AnnotationVisitor visitAnnotation(String s, boolean b) {
                throw new UnsupportedOperationException("Method visitAnnotation not implemented in  .");

            }

            @Override
            public void visitAttribute(Attribute attribute) {
                throw new UnsupportedOperationException("Method visitAttribute not implemented in  .");

            }

            @Override
            public void visitEnd() {
            }
        };
    }

    public static String toJavaType(Type type) {
        switch (type.getSort()) {
            case Type.DOUBLE:
                return "double";
            case Type.INT:
                return "int";
            case Type.ARRAY:
                return toJavaType(type.getElementType()) + "[]";
            case Type.OBJECT: {
                return type.getClassName();
            }
        }
        return type.toString();
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        writer.writeComment(String.format("visitMethod: %d, name=%s, desc=%s, signature=%s, exceptions=%s",
                access, name, desc, signature, exceptions != null ? Arrays.asList(exceptions).toString() : ""));
        writer.writeComment("todo: access, desc, signature, exceptions");
        writer.startMethod(name);

        final MethodInfo methodInfo = methodInfos.get(MethodInfo.getMethodTag(access, name, desc, signature, exceptions));

        final Evaluator evaluator = new Evaluator(writer);

        return new EvaluateMethodVisitor(evaluator, methodInfo, writer);
    }

    @Override
    public void visitEnd() {
        writer.writeComment("visitEnd");
        writer.endClass();
    }

    public void decompile(ClassReader reader) {
        reader.accept(new ClassVisitor(Opcodes.ASM4) {
            public String className;

            @Override
            public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
                this.className = name;
            }

            @Override
            public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
                final MethodInfo methodInfo = new MethodInfo(className, access, name, desc, signature, exceptions);
                methodInfos.put(methodInfo.getTag(), methodInfo);
                return new MethodVisitor(Opcodes.ASM4) {
                    @Override
                    public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
                        methodInfo.addVar(index, name, Type.getType(desc));
                    }
                };
            }
        }, 0);
        reader.accept(this, 0);
    }

}
