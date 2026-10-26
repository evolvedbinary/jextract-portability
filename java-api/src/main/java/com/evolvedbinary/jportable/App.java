package com.evolvedbinary.jportable;

import java.lang.foreign.Arena;
import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.VarHandle;
import java.util.HexFormat;

import com.evolvedbinary.capi.generated.CAPILib;

/**
 * Hello world!
 */
public class App {
    static {
        System.loadLibrary("capi");
    }

    public static void main(String[] args) {
        System.out.println("Hello World!");
        try (Arena arena = Arena.ofConfined()) {
            long memSize = 128;
            MemorySegment segment = arena.allocate(MemoryLayout.sequenceLayout(memSize, CAPILib.C_LONG));
            int status = CAPILib.setLong(segment, 7, 15, memSize);

            StringBuilder sb = new StringBuilder();
            HexFormat hex = HexFormat.of();
            VarHandle byteHandle = ValueLayout.JAVA_BYTE.varHandle();
            for (int i = 0; i < memSize; i++) {
                var b = byteHandle.get(segment, i);
                sb.append(hex.toHexDigits((Byte) b));
                if (i % 2 == 1) {
                    sb.append(' ');
                }
                if (i % 8 == 7) {
                    sb.append('\n');
                }
            }
            System.out.println(sb);
        }
    }
}
