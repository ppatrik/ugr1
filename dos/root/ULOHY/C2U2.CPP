#include "ugr.h"

void kresli_bod(int x, int y, int farba)
{
	ugr_asm_putpixel(farba, x, y);
}

void kresli_kruh(int sx, int sy, int r, int farba)
{
	int x = 0, y = r;
	int d = 3 - (2 * r);
	while( x <= y ) {
		kresli_bod(sx + x, sy + y, farba);
		kresli_bod(sx + x, sy - y, farba);
		kresli_bod(sx - x, sy + y, farba);
		kresli_bod(sx - x, sy - y, farba);
		kresli_bod(sx + y, sy + x, farba);
		kresli_bod(sx + y, sy - x, farba);
		kresli_bod(sx - y, sy + x, farba);
		kresli_bod(sx - y, sy - x, farba);
		if(d < 0) {
			d = d + (4 * x) + 6;
		} else {
			d = d + (4 * (x - y)) + 10;
			y = y - 1;
		}
		x = x + 1;
	}
}

int main(void)
{
	ugr_asm_setmode(0x13);

	ugr_asm_palette(0, 63, 63, 63);
	ugr_asm_palette(3, 0, 0, 0);

	kresli_kruh(90, 80, 30, 1); // modra
	kresli_kruh(160, 80, 30, 3); // cierna
	kresli_kruh(230, 80, 30, 4); // cervena
	kresli_kruh(125, 110, 30, 14); // zlta
	kresli_kruh(195, 110, 30, 2); // zelena

	// pockat na vstup uzivatela
	getch();
	ugr_asm_setmode(0x03);

	return 0;
}