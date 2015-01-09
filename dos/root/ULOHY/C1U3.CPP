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

	int i, j, x = 10, y = 20;
	int w = 256, h = 50;
	int start = 320 * (y - 1) + (x - 1);

	// vyhreslit horizontalne ciary
	for(i = 0; i < w; i++) {
		for(j = 0; j < h; j++) {
			obrazovka[start + i + (320 * j)] = i;
		}
	}

	// pockat na vstup uzivatela
	getch();
	nastav_rezim(0x03);

	return 0;
}