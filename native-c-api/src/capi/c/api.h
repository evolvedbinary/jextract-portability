#pragma once

#ifdef __cplusplus
extern "C" {
#endif

#include <stdbool.h>
#include <stddef.h>
#include <stdint.h>

// Fill in a buffer of longs with start..start + (stride * count)
// When we print it out bytewise on the Java side, stride should vary.
int setLong(long *buf, long start, long stride, long count);

// Fill in a series of buffers with start..start + (stride * count)
// Exhaust each buffer (per_buf_size elements) in turn
// This ensures we can lay out pointers consistently
int setLongBuffers(long **bufs, long start, long stride, long segnum, size_t partSize);

#ifdef __cplusplus
} /* end extern "C" */
#endif
