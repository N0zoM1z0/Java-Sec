package com.ctf.entity;

import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

/* loaded from: TimeCapsule-1.0.jar:BOOT-INF/classes/com/ctf/util/SafeObjectInputStream.class */
public class SafeObjectInputStream extends ObjectInputStream {
    public SafeObjectInputStream(InputStream in) throws IOException {
        super(in);
    }

    @Override // java.io.ObjectInputStream
    protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
        if (!desc.getName().startsWith("com.ctf.") && !desc.getName().startsWith("java.") && !desc.getName().equals("[B")) {
            throw new InvalidClassException("Unauthorized class deserialization", desc.getName());
        }
        return super.resolveClass(desc);
    }
}
