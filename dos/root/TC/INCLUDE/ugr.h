#ifndef _HEAD_UGR
#define _HEAD_UGR

#include <stdio.h>
#include <conio.h>
#include <stdlib.h>
#include <dos.h>
#include <math.h>
#include <malloc.h>
#include <string.h>

/* Zakladne datove typy */
#define uint8_t unsigned char
#define uint16_t unsigned short
#define uint32_t unsigned long
#define int8_t char
#define int16_t short
#define int32_t long

#define SWAP(a,b) {(a)^=(b);(b)^=(a);(a)^=(b);}

void ugr_asm_setmode(uint8_t mode);
void ugr_asm_blinking(uint8_t enabled);

void ugr_asm_putchar(uint8_t znak, uint8_t attr, uint8_t x, uint8_t y);
void ugr_mem_putchar(uint8_t znak, uint8_t attr, uint8_t x, uint8_t y);

void ugr_mem_clrscr(void);
void ugr_asm_palette(uint8_t color, uint8_t r, uint8_t g, uint8_t b);

void ugr_asm_putpixel(uint8_t color, int16_t x, int16_t y);
uint8_t ugr_asm_getpixel(int16_t x, int16_t y);
void ugr_mem_putpixel(uint8_t color, int16_t x, int16_t y);
uint8_t ugr_mem_getpixel(int16_t x, int16_t y);

void ugr_vline(uint8_t color,int16_t x,int16_t ay,int16_t by);
void ugr_hline(uint8_t color,int16_t ax,int16_t y,int16_t bx);

void ugr_line(uint8_t c,int16_t x1,int16_t y1,int16_t x2,int16_t y2);
void ugr_triangle(uint8_t color,int16_t x1,int16_t y1,int16_t x2,int16_t y2,int16_t x3,int16_t y3);

void cv03_vzor(uint8_t id);

void ugr_grbuffer_enable(uint8_t enabled);
void ugr_grbuffer_clear(void);
void ugr_grbuffer_blit(void);

#endif
