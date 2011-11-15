package com.mikea.decompiler;

import org.objectweb.asm.Type;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 */
public class MethodInfo {

    final String className;
    private final int access;
    private final String name;
    private final String desc;
    private final String signature;
    private final String[] exceptions;

    private final Map<Integer, VarInfo> locals = new HashMap<Integer, VarInfo>();
    private final Set<String> localNames = new HashSet<String>();

    public MethodInfo(String className, int access, String name, String desc, String signature, String[] exceptions) {
        this.className = className;
        this.access = access;
        this.name = name;
        this.desc = desc;
        this.signature = signature;
        this.exceptions = exceptions;
    }

    public void addVar(int index, String name, Type type) {
        String realName = name;
        int i = 0;
        while (localNames.contains(realName)) {
            realName = name + i;
            i++;
        }
        locals.put(index, new VarInfo(realName, type));
        localNames.add(realName);
    }

    public String getTag() {
        return getMethodTag(access, name, desc, signature, exceptions);
    }

    public static String getMethodTag(int access, String name, String desc, String signature, String[] exceptions) {
        return String.format("%d-%s-%s-%s-%s", access, name, desc, signature,
                exceptions != null ? Arrays.asList(exceptions).toString() : "");
    }

    public String getVarName(int var) {
        VarInfo varInfo = locals.get(var);
        if (varInfo == null) {
            return "__local_" + var;
        }
        return varInfo.name;
    }

    public Type getVarType(int var) {
        VarInfo varInfo = locals.get(var);
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
