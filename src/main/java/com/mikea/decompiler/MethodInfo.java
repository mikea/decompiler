package com.mikea.decompiler;

import org.objectweb.asm.Type;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 */
public class MethodInfo {

    private final int access;
    private final String name;
    private final String desc;
    private final String signature;
    private final String[] exceptions;

    private final Map<Integer, VarInfo> vars = new HashMap<Integer, VarInfo>();

    public MethodInfo(int access, String name, String desc, String signature, String[] exceptions) {
        this.access = access;
        this.name = name;
        this.desc = desc;
        this.signature = signature;
        this.exceptions = exceptions;
    }

    public void addVar(int index, String name, Type type) {
        vars.put(index, new VarInfo(name, type));
    }

    public String getTag() {
        return getMethodTag(access, name, desc, signature, exceptions);
    }

    public static String getMethodTag(int access, String name, String desc, String signature, String[] exceptions) {
        return String.format("%d-%s-%s-%s-%s", access, name, desc, signature,
                exceptions != null ? Arrays.asList(exceptions).toString() : "");
    }

    public String getVarName(int var) {
        VarInfo varInfo = vars.get(var);
        if (varInfo == null) {
            return "__local_" + var;
        }
        return varInfo.name;
    }

    public Type getVarType(int var) {
        VarInfo varInfo = vars.get(var);
        if (varInfo == null) {
            System.err.println("error: Null type");
            return Type.getObjectType("UnknownType");
        }
        return varInfo.type;
    }

    private static class VarInfo {
        private final String name;
        private final Type type;

        public VarInfo(String name, Type type) {
            this.name = name;
            this.type = type;
        }
    }
}
