/**
 * Copyright © 2026, Evolved Binary Ltd
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the <organization> nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
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
