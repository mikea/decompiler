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
public class DecompileVisitor implements ClassVisitor {
    private final JavaSourceWriter writer;
    private Map<String, MethodInfo> methodInfos = new HashMap<String, MethodInfo>();

    public DecompileVisitor(JavaSourceWriter writer) {
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

        return new FieldVisitor() {
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

    private String toJavaType(Type type) {
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

        final Evaluator evaluator = new Evaluator(methodInfo, writer);

        return new MethodVisitor() {
            @Override
            public AnnotationVisitor visitAnnotationDefault() {
                throw new UnsupportedOperationException("Method visitAnnotationDefault not implemented in  .");
            }

            @Override
            public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
                evaluator.flush();
                writer.writeComment("visitAnnotation: " + desc + " - " + visible);
                return null;
            }

            @Override
            public AnnotationVisitor visitParameterAnnotation(int i, String s, boolean b) {
                evaluator.flush();
                throw new UnsupportedOperationException("Method visitParameterAnnotation not implemented in  .");

            }

            @Override
            public void visitAttribute(Attribute attribute) {
                evaluator.flush();
                throw new UnsupportedOperationException("Method visitAttribute not implemented in  .");

            }

            @Override
            public void visitCode() {
                evaluator.flush();
                writer.writeComment("visitCode");
            }

            @Override
            public void visitFrame(int i, int i1, Object[] objects, int i2, Object[] objects1) {
                evaluator.flush();
                throw new UnsupportedOperationException("Method visitFrame not implemented in  .");
            }

            @Override
            public void visitInsn(int opcode) {
                switch (opcode) {
                    case Opcodes.ICONST_1: {
                        evaluator.push("1", Type.INT_TYPE);
                        break;
                    }
                    case Opcodes.ICONST_0: {
                        evaluator.push("0", Type.INT_TYPE);
                        break;
                    }
                    case Opcodes.DUP: {
                        evaluator.dup();
                        break;
                    }
                    case Opcodes.POP2: {
                        evaluator.pop2();
                        break;
                    }
                    case Opcodes.AASTORE: {
                        evaluator.stmt3("({0})[{1}]={2}");
                        break;
                    }
                    default: {
                        evaluator.flush();
                        writer.writeComment("visitInsn: " + opcode);
                        break;
                    }
                }
            }

            @Override
            public void visitIntInsn(int opcode, int operand) {
                switch (opcode) {
                    case Opcodes.SIPUSH: {
                        evaluator.push(String.valueOf(operand), Type.INT_TYPE);
                        break;
                    }
                    default: {
                        evaluator.flush();
                        writer.writeComment("visitIntInsn: " + opcode + " - " + operand);
                        break;
                    }
                }
            }

            @Override
            public void visitVarInsn(int opcode, int var) {
                switch (opcode) {
                    case Opcodes.ILOAD: {
                        evaluator.push(methodInfo.getVarName(var), Type.INT_TYPE);
                        break;
                    }
                    case Opcodes.ALOAD: {
                        evaluator.push(methodInfo.getVarName(var), methodInfo.getVarType(var));
                        break;
                    }
                    default: {
                        evaluator.flush();
                        writer.writeComment("visitVarInsn: " + opcode + " - " + var);
                        break;
                    }
                }
            }

            @Override
            public void visitTypeInsn(int opcode, String type) {
                switch (opcode) {
                    case Opcodes.ANEWARRAY: {
                        evaluator.expr1("new " + type + "[{0}]", Type.getType("[" + type));
                        return;
                    }
                    default: {
                        evaluator.flush();
                        writer.writeComment("visitTypeInsn: " + opcode + " - " + type);
                        return;
                    }
                }
            }

            @Override
            public void visitFieldInsn(int opcode, String owner, String name, String desc) {
                switch (opcode) {
                    case Opcodes.GETFIELD: {
                        evaluator.getField(owner, name, Type.getType(desc));
                        return;
                    }
                    default: {
                        evaluator.flush();
                        writer.writeComment("visitFieldInsn: " + opcode + " - " + owner + " - " + name + " - " + desc);
                        break;
                    }
                }
            }

            @Override
            public void visitMethodInsn(int opcode, String owner, String name, String desc) {
                switch (opcode) {
                    case Opcodes.INVOKESTATIC: {
                        Type returnType = Type.getReturnType(desc);
                        Type[] types = Type.getArgumentTypes(desc);
                        evaluator.staticCall(owner + "." + name, types.length, returnType);
                        return;
                    }
                    case Opcodes.INVOKEVIRTUAL: {
                        Type returnType = Type.getReturnType(desc);
                        Type[] types = Type.getArgumentTypes(desc);
                        evaluator.virtualCall(name, types.length, returnType);
                        return;
                    }
                    default: {
                        evaluator.flush();
                        writer.writeComment("visitMethodInsn: " + opcode + " - " + owner + " - " + name + " - " + desc);
                        return;
                    }
                }
            }

            @Override
            public void visitJumpInsn(int opcode, Label label) {
                switch (opcode) {
                    case Opcodes.IF_ACMPNE: {
                        evaluator.condJump("!=", label);
                        break;
                    }
                    case Opcodes.GOTO: {
                        writer.writeComment("GOTO " + label);
                        break;
                    }
                    default: {
                        evaluator.flush();
                        writer.writeComment("visitJumpInsn: " + opcode + " - " + label);
                    }
                }
            }

            @Override
            public void visitLabel(Label label) {
                evaluator.flush();
                writer.writeComment("visitLabel: " + label);
            }

            @Override
            public void visitLdcInsn(Object cst) {
                if (cst instanceof String) {
                    evaluator.push("\"" + cst.toString() + "\"", Type.getType(cst.getClass()));
                    return;
                } else {
                    evaluator.flush();
                    writer.writeComment("visitLdcInsn: " + cst);
                }
            }

            @Override
            public void visitIincInsn(int var, int increment) {
                evaluator.flush();
                writer.writeComment("visitIincInsn: " + var + " - " + increment);
            }

            @Override
            public void visitTableSwitchInsn(int min, int max, Label dflt, Label[] labels) {
                evaluator.flush();
                writer.writeComment("visitTableSwitchInsn");
            }

            @Override
            public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
                evaluator.flush();
                writer.writeComment("visitLookupSwitchInsn: " + dflt + " - " + keys + " - " + labels);
            }

            @Override
            public void visitMultiANewArrayInsn(String desc, int dims) {
                evaluator.flush();
                writer.writeComment("visitMultiANewArrayInsn: " + desc + " - " + dims);
            }

            @Override
            public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
                evaluator.flush();
                writer.writeComment("visitTryCatchBlock: " + start + " - " + end + " - " + handler + " - " + type);
            }

            @Override
            public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
                evaluator.flush();
                writer.writeComment("visitLocalVariable: " + name + "- " + desc + " - " + signature + " - " + start + " - " + end + " - " + index);
            }

            @Override
            public void visitLineNumber(int line, Label start) {
                evaluator.flush();
                writer.writeComment("visitLineNumber: " + line + " " + start);
            }

            @Override
            public void visitMaxs(int maxStack, int maxLocals) {
                evaluator.flush();
                writer.writeComment("visitMaxs: " + maxStack + " - " + maxLocals);
            }

            @Override
            public void visitEnd() {
                evaluator.flush();
                writer.endMethod();
            }
        };
    }

    @Override
    public void visitEnd() {
        writer.writeComment("visitEnd");
        writer.endClass();
    }

    public void decompile(ClassReader reader) {
        reader.accept(new AbstractClassVisitor() {
            @Override
            public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
                final MethodInfo methodInfo = new MethodInfo(access, name, desc, signature, exceptions);
                methodInfos.put(methodInfo.getTag(), methodInfo);
                return new AbstractMethodVisitor() {
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
