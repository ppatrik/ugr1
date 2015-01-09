#include "ugr.h"

void nastav_rezim(unsigned char rezim)
{
	asm {
		mov ah, 0x00
		mov al, rezim
		int 0x10
	}
}

void nastav_blikanie(uint8_t zapnute)
{
	asm {
		mov ax, 0x1003
		mov bl, zapnute
		mov bh, 0x00
		int 0x10
	}
}

int main(void)
{
	nastav_rezim(0x13);
	nastav_blikanie(0);

	// ziskaj ukazovatel na graficku pamat
	uint8_t far *obrazovka = (uint8_t far *) 0xA0000000L;

	int i, x = 10, y = 20;
	int w = 80, h =50;
	int start = 320 * (y - 1) + (x - 1);

	// vyhreslit horizontalne ciary
	for(i = 0; i < w; i++) {
		// farba pre hornu ciaru
		obrazovka[start + i] = 15;
		// farba pre dolnu ciaru
		obrazovka[start + ((h - 1) * 320) + i] = 15;
	}

	// vykreslit vertikalne ciary
	for(i = 0; i < h; i++) {
		// vykreslit lavu ciaru
		obrazovka[start + (320 * i)] = 15;
		// vykreslit pravu ciaru
		obrazovka[start + w - 1 + (320 * i)] = 15;
	}

	// pockat na vstup uzivatela
	getch();
	nastav_rezim(0x03);

	return 0;
}