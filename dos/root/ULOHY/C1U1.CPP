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
	nastav_rezim(3);
	nastav_blikanie(0);

	// ziskaj ukazovatel na graficku pamat
	uint8_t far *obrazovka = (uint8_t far *) 0xB8000000L;

	int riadok = 80*2;
	int zaciatok = 2*riadok+6*2;
	for (int r=0;r<10;r++) {
		uint8_t farba = (uint8_t) r*16+r+1;
		for(int i=0;i<26;i++) {
			// kazdy bod sa skalda z dvoch bajtov
			// 1 => znak
			// 2 => farba
			int pozicia = zaciatok + i*2 + r*riadok;
			obrazovka[pozicia] = 65+i;
			obrazovka[pozicia+1] = farba;
		}
	}

	// pockat na vstup uzivatela
	getch();

	return 0;
}