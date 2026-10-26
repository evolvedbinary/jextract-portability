#include <stdbool.h>
#include <stddef.h>
#include <stdint.h>

#include "api.h"

// Fill in a buffer of longs with start..start + (stride * count)
// When we print it out bytewise on the Java side, stride should vary.
int setLong(long *buf, long start, long stride, long count) {

    long val = start;
    for (long i = 0; i < count; i++) {
        buf[i] = val;
        val += stride;
    }
    return 0L;
}

// Fill in a series of buffers with start..start + (stride * count)
// Exhaust each buffer (per_buf_size elements) in turn
// This ensures we can lay out pointers consistently
int setLongBuffers(long **bufs, size_t per_buf_size, long start, long stride, long count) {

}