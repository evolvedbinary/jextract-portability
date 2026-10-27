package com.evolvedbinary.jportable;

import java.lang.foreign.*;
import java.lang.invoke.VarHandle;
import java.util.ArrayList;
import java.util.HexFormat;
import java.util.List;

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

            long segnum = 16;
            long partSize = 8;
            long numLongs = segnum * partSize;

            MemorySegment segment = arena.allocate(MemoryLayout.sequenceLayout(numLongs, CAPILib.C_LONG));

            int status = CAPILib.setLong(segment, 7, 15, numLongs);
            dumpSegment(segment, numLongs * CAPILib.C_LONG.byteSize());

            MemorySegment segmentPtrs = arena.allocate(MemoryLayout.sequenceLayout(segnum, CAPILib.C_POINTER));
            List<MemorySegment> partList = new ArrayList<>();
            for (int i = 0; i < segnum; i++) {
                // Allocate another segment, and put it in the slot
                MemorySegment segmentI = arena.allocate(MemoryLayout.sequenceLayout(partSize, CAPILib.C_LONG));
                partList.add(segmentI);
                segmentPtrs.setAtIndex(CAPILib.C_POINTER, i, segmentI);
            }

            status = CAPILib.setLongBuffers(segmentPtrs, 7, 15, segnum, partSize);
            for (MemorySegment part : partList) {
                dumpSegment(part, partSize * CAPILib.C_LONG.byteSize());
            }
        }
    }

    private static void dumpSegment(MemorySegment segment, long segSize) {
        StringBuilder sb = new StringBuilder();
        HexFormat hex = HexFormat.of();
        VarHandle byteHandle = ValueLayout.JAVA_BYTE.varHandle();
        System.out.println("----" + hex.toHexDigits(segment.address()));
        for (int i = 0; i < segSize; i++) {
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
