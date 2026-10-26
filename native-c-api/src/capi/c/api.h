#pragma once

#ifdef __cplusplus
extern "C" {
#endif

#include <stdbool.h>
#include <stddef.h>
#include <stdint.h>

typedef struct api_struct_t {
    const char *buffer;
    size_t size;
    char c;
    short s;
    int i;
    long l;
    long long ll;
    uint8_t u8;
    uint16_t u16;
    uint32_t u32;
    uint64_t u64;
} api_struct_t;

char *method1(api_struct_t *s1, const char *buf, size_t size);

void method2(api_struct_t sArray[], api_struct_t **sPtrPtr, char c, short s, int i, long l, long long ll);

void method3(api_struct_t sArray[8], api_struct_t **sPtrPtr, uint8_t u8, uint16_t u16, uint32_t u32, uint64_t u64);

#ifdef __cplusplus
} /* end extern "C" */
#endif
