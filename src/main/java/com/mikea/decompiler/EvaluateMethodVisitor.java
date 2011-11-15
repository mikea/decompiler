package com.mikea.decompiler;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

/**
 */
class EvaluateMethodVisitor extends MethodVisitor {
    private final Evaluator evaluator;
    private final MethodInfo methodInfo;
    private final JavaSourceWriter writer;

    public EvaluateMethodVisitor(Evaluator evaluator, MethodInfo methodInfo, JavaSourceWriter writer) {
        super(Opcodes.ASM4);
        this.evaluator = evaluator;
        this.methodInfo = methodInfo;
        this.writer = writer;

        if (methodInfo.className.equals("com/numericalmethod/suanshu/stats/timeseries/linear/multivariate/stationaryprocess/InnovationAlgorithmImpl")) {
            System.out.println("");
        }
    }

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
    }

    @Override
    public void visitFrame(int i, int i1, Object[] objects, int i2, Object[] objects1) {
        evaluator.flush();
        throw new UnsupportedOperationException("Method visitFrame not implemented in  .");
    }

    @Override
    public void visitInsn(int opcode) {
        switch (opcode) {
            case Opcodes.ACONST_NULL: {
                evaluator.load("null", Type.getType(Object.class));
                break;
            }
            case Opcodes.ICONST_M1: {
                evaluator.load("-1", Type.INT_TYPE);
                break;
            }
            case Opcodes.ICONST_0: {
                evaluator.load("0", Type.INT_TYPE);
                break;
            }
            case Opcodes.ICONST_1: {
                evaluator.load("1", Type.INT_TYPE);
                break;
            }
            case Opcodes.ICONST_2: {
                evaluator.load("2", Type.INT_TYPE);
                break;
            }
            case Opcodes.ICONST_3: {
                evaluator.load("3", Type.INT_TYPE);
                break;
            }
            case Opcodes.ICONST_4: {
                evaluator.load("4", Type.INT_TYPE);
                break;
            }
            case Opcodes.ICONST_5: {
                evaluator.load("5", Type.INT_TYPE);
                break;
            }
            case Opcodes.DCONST_0: {
                evaluator.load("0d", Type.DOUBLE_TYPE);
                break;
            }
            case Opcodes.DCONST_1: {
                evaluator.load("1d", Type.DOUBLE_TYPE);
                break;
            }
            case Opcodes.LCONST_0: {
                evaluator.load("0l", Type.LONG_TYPE);
                break;
            }
            case Opcodes.LCONST_1: {
                evaluator.load("1l", Type.LONG_TYPE);
                break;
            }
            case Opcodes.DUP: {
                evaluator.dup();
                break;
            }
            case Opcodes.SWAP: {
                evaluator.swap();
                break;
            }
            case Opcodes.DUP_X1: {
                evaluator.dupx1();
                break;
            }
            case Opcodes.POP: {
                evaluator.pop();
                break;
            }
            case Opcodes.POP2: {
                evaluator.pop2();
                break;
            }
            case Opcodes.DUP2: {
                evaluator.dup2();
                break;
            }
            case Opcodes.DUP_X2: {
                evaluator.dupx2();
                break;
            }
            case Opcodes.DUP2_X1: {
                evaluator.dup2x1();
                break;
            }
            case Opcodes.ARRAYLENGTH: {
                evaluator.expr1("{0}.length", Type.INT_TYPE);
                break;
            }
            case Opcodes.I2L: {
                evaluator.expr1("((long){0})", Type.LONG_TYPE);
                break;
            }
            case Opcodes.I2C: {
                evaluator.expr1("((char){0})", Type.CHAR_TYPE);
                break;
            }
            case Opcodes.I2B: {
                evaluator.expr1("((byte){0})", Type.BYTE_TYPE);
                break;
            }
            case Opcodes.F2D:
            case Opcodes.L2D:
            case Opcodes.I2D: {
                evaluator.expr1("((double){0})", Type.DOUBLE_TYPE);
                break;
            }
            case Opcodes.D2I:
            case Opcodes.L2I: {
                evaluator.expr1("((int){0})", Type.INT_TYPE);
                break;
            }
            case Opcodes.D2F:
            case Opcodes.I2F:
            case Opcodes.L2F: {
                evaluator.expr1("((float){0})", Type.FLOAT_TYPE);
                break;
            }
            case Opcodes.IOR: {
                evaluator.expr2("{0} } {1}", Type.INT_TYPE);
                break;
            }
            case Opcodes.IAND: {
                evaluator.expr2("{0} & {1}", Type.INT_TYPE);
                break;
            }
            case Opcodes.IADD: {
                evaluator.expr2("{0} + {1}", Type.INT_TYPE);
                break;
            }
            case Opcodes.ISUB: {
                evaluator.expr2("{0} - {1}", Type.INT_TYPE);
                break;
            }
            case Opcodes.IDIV: {
                evaluator.expr2("{0} / {1}", Type.INT_TYPE);
                break;
            }
            case Opcodes.IREM: {
                evaluator.expr2("{0} % {1}", Type.INT_TYPE);
                break;
            }
            case Opcodes.IXOR: {
                evaluator.expr2("{0} ^ {1}", Type.INT_TYPE);
                break;
            }
            case Opcodes.LREM: {
                evaluator.expr2("{0} % {1}", Type.LONG_TYPE);
                break;
            }
            case Opcodes.IMUL: {
                evaluator.expr2("{0} * {1}", Type.INT_TYPE);
                break;
            }
            case Opcodes.FMUL: {
                evaluator.expr2("{0} * {1}", Type.FLOAT_TYPE);
                break;
            }
            case Opcodes.FDIV: {
                evaluator.expr2("{0} / {1}", Type.FLOAT_TYPE);
                break;
            }
            case Opcodes.DNEG: {
                evaluator.expr1("-{0}", Type.DOUBLE_TYPE);
                break;
            }
            case Opcodes.FNEG: {
                evaluator.expr1("-{0}", Type.FLOAT_TYPE);
                break;
            }
            case Opcodes.INEG: {
                evaluator.expr1("-{0}", Type.INT_TYPE);
                break;
            }
            case Opcodes.LNEG: {
                evaluator.expr1("-{0}", Type.LONG_TYPE);
                break;
            }
            case Opcodes.DSUB: {
                evaluator.expr2("{0} - {1}", Type.DOUBLE_TYPE);
                break;
            }
            case Opcodes.DDIV: {
                evaluator.expr2("{0} / {1}", Type.DOUBLE_TYPE);
                break;
            }
            case Opcodes.DMUL: {
                evaluator.expr2("{0} * {1}", Type.DOUBLE_TYPE);
                break;
            }
            case Opcodes.DADD: {
                evaluator.expr2("{0} + {1}", Type.DOUBLE_TYPE);
                break;
            }
            case Opcodes.LXOR: {
                evaluator.expr2("{0} ^ {1}", Type.LONG_TYPE);
                break;
            }
            case Opcodes.LAND: {
                evaluator.expr2("{0} & {1}", Type.LONG_TYPE);
                break;
            }
            case Opcodes.LADD: {
                evaluator.expr2("{0} + {1}", Type.LONG_TYPE);
                break;
            }
            case Opcodes.LSUB: {
                evaluator.expr2("{0} - {1}", Type.LONG_TYPE);
                break;
            }
            case Opcodes.LDIV: {
                evaluator.expr2("{0} / {1}", Type.LONG_TYPE);
                break;
            }
            case Opcodes.LMUL: {
                evaluator.expr2("{0} * {1}", Type.LONG_TYPE);
                break;
            }
            case Opcodes.LOR: {
                evaluator.expr2("{0} | {1}", Type.LONG_TYPE);
                break;
            }
            case Opcodes.DALOAD: {
                evaluator.expr2("{0}[{1}]", Type.DOUBLE_TYPE);
                break;
            }
            case Opcodes.BALOAD: {
                evaluator.expr2("{0}[{1}]", Type.BYTE_TYPE);
                break;
            }
            case Opcodes.LALOAD: {
                evaluator.expr2("{0}[{1}]", Type.LONG_TYPE);
                break;
            }
            case Opcodes.IALOAD: {
                evaluator.expr2("{0}[{1}]", Type.INT_TYPE);
                break;
            }
            case Opcodes.CALOAD: {
                evaluator.expr2("{0}[{1}]", Type.CHAR_TYPE);
                break;
            }
            case Opcodes.AALOAD: {
                evaluator.expr2("{0}[{1}]", Type.getType(Object.class));
                break;
            }
            case Opcodes.LUSHR: {
                evaluator.expr2("{0} >>> {1}", Type.LONG_TYPE);
                break;
            }
            case Opcodes.IUSHR: {
                evaluator.expr2("{0} >>> {1}", Type.INT_TYPE);
                break;
            }
            case Opcodes.LSHR: {
                evaluator.expr2("{0} >> {1}", Type.LONG_TYPE);
                break;
            }
            case Opcodes.ISHL: {
                evaluator.expr2("{0} << {1}", Type.INT_TYPE);
                break;
            }
            case Opcodes.ISHR: {
                evaluator.expr2("{0} >> {1}", Type.INT_TYPE);
                break;
            }
            case Opcodes.DCMPL:
            case Opcodes.DCMPG: {
                evaluator.expr2("{0} > {1} ? 1 : ({0} == {1} ? 0 : -1) /* TODO */", Type.INT_TYPE);
                break;
            }
            case Opcodes.LCMP: {
                evaluator.expr2("{0} > {1} ? 1 : ({0} == {1} ? 0 : -1)", Type.INT_TYPE);
                break;
            }
            case Opcodes.BASTORE:
            case Opcodes.FASTORE:
            case Opcodes.DASTORE:
            case Opcodes.CASTORE:
            case Opcodes.LASTORE:
            case Opcodes.IASTORE:
            case Opcodes.AASTORE: {
                evaluator.stmt3("({0})[{1}]={2}");
                break;
            }
            case Opcodes.ARETURN:
            case Opcodes.DRETURN:
            case Opcodes.LRETURN:
            case Opcodes.FRETURN:
            case Opcodes.IRETURN: {
                evaluator.stmt1("return {0}");
                break;
            }
            case Opcodes.ATHROW: {
                evaluator.stmt1("throw {0}");
                break;
            }
            case Opcodes.RETURN: {
                evaluator.stmt0("return");
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
                evaluator.load(String.valueOf(operand), Type.INT_TYPE);
                break;
            }
            case Opcodes.NEWARRAY: {
                Type t = null;
                switch (operand) {
                    case 4:
                        t = Type.BOOLEAN_TYPE;
                        break;
                    case 5:
                        t = Type.CHAR_TYPE;
                        break;
                    case 6:
                        t = Type.FLOAT_TYPE;
                        break;
                    case 7:
                        t = Type.DOUBLE_TYPE;
                        break;
                    case 8:
                        t = Type.BYTE_TYPE;
                        break;
                    case 9:
                        t = Type.SHORT_TYPE;
                        break;
                    case 10:
                        t = Type.INT_TYPE;
                        break;
                    case 11:
                        t = Type.LONG_TYPE;
                        break;
                    default: {
                        throw new IllegalArgumentException();
                    }
                }
                evaluator.expr1("new " + DecompileVisitor.toJavaType(t) + "[{0}]", Type.getType("[" + t.toString()));
                break;
            }
            case Opcodes.BIPUSH: {
                evaluator.load(String.valueOf(operand), Type.BYTE_TYPE);
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
                evaluator.load(methodInfo.getVarName(var), Type.INT_TYPE);
                break;
            }
            case Opcodes.DLOAD: {
                evaluator.load(methodInfo.getVarName(var), Type.DOUBLE_TYPE);
                break;
            }
            case Opcodes.LLOAD: {
                evaluator.load(methodInfo.getVarName(var), Type.LONG_TYPE);
                break;
            }
            case Opcodes.FLOAD: {
                evaluator.load(methodInfo.getVarName(var), Type.FLOAT_TYPE);
                break;
            }
            case Opcodes.ALOAD: {
                evaluator.load(methodInfo.getVarName(var), methodInfo.getVarType(var));
                break;
            }
            case Opcodes.ASTORE:
            case Opcodes.FSTORE:
            case Opcodes.LSTORE:
            case Opcodes.ISTORE:
            case Opcodes.DSTORE: {
                evaluator.stmt1(methodInfo.getVarName(var) + " = {0}");
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
            case Opcodes.NEW: {
                evaluator.expr0("new " + type, Type.getType(type));
                return;
            }
            case Opcodes.CHECKCAST: {
                Type t = Type.getType(type);
                evaluator.expr1("({0} instanceof " + DecompileVisitor.toJavaType(t) + ") ? {0} : throw new ClassCastException()", t);
                return;
            }
            case Opcodes.INSTANCEOF: {
                Type t = Type.getType(type);
                evaluator.expr1("{0} instanceof " + DecompileVisitor.toJavaType(t), t);
                return;
            }
            default: {
                evaluator.flush();
                writer.writeComment("visitTypeInsn: " + opcode + " - " + type);
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
            case Opcodes.GETSTATIC: {
                evaluator.load(owner + "." + name, Type.getType(desc));
                return;
            }
            case Opcodes.PUTFIELD: {
                evaluator.stmt2("{0}." + name + " = {1}");
                return;
            }
            case Opcodes.PUTSTATIC: {
                evaluator.stmt1(owner + "." + name + " = {0}");
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
            case Opcodes.INVOKEINTERFACE: {
                Type returnType = Type.getReturnType(desc);
                Type[] types = Type.getArgumentTypes(desc);
                evaluator.virtualCall(name, types.length, returnType);
                return;
            }
            case Opcodes.INVOKESPECIAL: {
                Type returnType = Type.getReturnType(desc);
                Type[] types = Type.getArgumentTypes(desc);
                evaluator.staticCall(name, types.length, returnType);
                return;
            }
            default: {
                evaluator.flush();
                writer.writeComment("visitMethodInsn: " + opcode + " - " + owner + " - " + name + " - " + desc);
            }
        }
    }

    @Override
    public void visitJumpInsn(int opcode, Label label) {
        switch (opcode) {
            case Opcodes.IF_ACMPNE: {
                evaluator.condJump2("!=", label);
                break;
            }
            case Opcodes.IF_ACMPEQ: {
                evaluator.condJump2("==", label);
                break;
            }
            case Opcodes.IF_ICMPEQ: {
                evaluator.condJump2("==", label);
                break;
            }
            case Opcodes.IF_ICMPGE: {
                evaluator.condJump2(">=", label);
                break;
            }
            case Opcodes.IF_ICMPLE: {
                evaluator.condJump2("<=", label);
                break;
            }
            case Opcodes.IF_ICMPGT: {
                evaluator.condJump2(">", label);
                break;
            }
            case Opcodes.IF_ICMPLT: {
                evaluator.condJump2("<", label);
                break;
            }
            case Opcodes.IF_ICMPNE: {
                evaluator.condJump2("!=", label);
                break;
            }
            case Opcodes.IFGE: {
                evaluator.condJump1(">=0", label);
                break;
            }
            case Opcodes.IFGT: {
                evaluator.condJump1(">0", label);
                break;
            }
            case Opcodes.IFLE: {
                evaluator.condJump1("<=0", label);
                break;
            }
            case Opcodes.IFLT: {
                evaluator.condJump1("<0", label);
                break;
            }
            case Opcodes.IFNE: {
                evaluator.condJump1("!=0", label);
                break;
            }
            case Opcodes.IFEQ: {
                evaluator.condJump1("==0", label);
                break;
            }
            case Opcodes.GOTO: {
                writer.writeComment("GOTO " + label);
                break;
            }
            case Opcodes.IFNULL: {
                evaluator.condJump1(" == null", label);
                break;
            }
            case Opcodes.IFNONNULL: {
                evaluator.condJump1(" != null", label);
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
        writer.writeComment("visitLabel: " + label);
    }

    @Override
    public void visitLdcInsn(Object cst) {
        if (cst instanceof String) {
            evaluator.load("\"" + cst.toString() + "\"", Type.getType(cst.getClass()));
        } else if (cst instanceof Double) {
            evaluator.load(cst.toString() + "d", Type.DOUBLE_TYPE);
        } else if (cst instanceof Integer) {
            evaluator.load(cst.toString(), Type.INT_TYPE);
        } else if (cst instanceof Long) {
            evaluator.load(cst.toString() + "l", Type.LONG_TYPE);
        } else if (cst instanceof Type) {
            evaluator.load(DecompileVisitor.toJavaType((Type) cst) + ".class", Type.getType(Class.class));
        } else {
            evaluator.flush();
            writer.writeComment("visitLdcInsn: " + cst);
        }
    }

    @Override
    public void visitIincInsn(int var, int increment) {
        evaluator.stmt0(methodInfo.getVarName(var) + "+=" + increment);
    }

    @Override
    public void visitTableSwitchInsn(int min, int max, Label dflt, Label[] labels) {
        String expr = "switch ({0}) '{'";
        for (int i = 0; i < max - min + 1; ++i) {
            expr += "case " + (i + min) + ": GOTO " + labels[i] + "\n";
        }
        expr += "default: " + ": GOTO " + dflt + "\n";
        expr += "}";
        evaluator.stmt1(expr);
    }

    @Override
    public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
        String expr = "switch ({0}) '{'";
        for (int i = 0; i < keys.length; ++i) {
            expr += "case " + keys[i] + ": GOTO " + labels[i] + "\n";
        }
        expr += "default: " + ": GOTO " + dflt + "\n";
        expr += "}";
        evaluator.stmt1(expr);
    }

    @Override
    public void visitMultiANewArrayInsn(String desc, int dims) {
        String expr = "";
        for (int i = 0; i < dims; i++) {
            expr += "[{" + i + "}]";
        }
        Type elementType = Type.getType(desc).getElementType();
        evaluator.expr("new " + DecompileVisitor.toJavaType(elementType) + expr, Type.getType(desc), dims);
    }

    @Override
    public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
        evaluator.flush();
        writer.writeComment("visitTryCatchBlock: " + start + " - " + end + " - " + handler + " - " + type);
    }

    @Override
    public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
    }

    @Override
    public void visitLineNumber(int line, Label start) {
    }

    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
    }

    @Override
    public void visitEnd() {
        evaluator.flush(false);
        writer.endMethod();
    }
}
