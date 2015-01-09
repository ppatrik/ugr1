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

void kresli_ciaru(int x1, int y1, int x2, int y2, int farba)
{
	/* vymena suradnic pri druhom a -druhom oktante */
    int vymen = 0;
    if(abs(y2-y1) > abs(x2-x1)) {
        vymen = 1;
        SWAP(x1, y1);
        SWAP(x2, y2);
    }

    /* Obratenie suradnic ak ciara ide zprava dolava */
    if(x2<x1) {
        SWAP(x1, x2);
        SWAP(y1, y2);
    }

    /* Osetri ak su v kladnom alebo zapornom oktante */
    int krok = 1;
    if(y2 < y1) {
        krok = -1;
    }

    /* samotny algoritmus */
    int dx = abs(x2 - x1);
    int dy = abs(y2 - y1);

    int y = y1;
    int k1 = 2*dy;
    int k2 = 2*dy-2*dx;
    int d = 2*dy-dx;
    for (int x = x1; x <= x2; x++) {
        /* dodatok algoritmu ktory vymiena suradnice */
        if(vymen) 
        	kresli_bod(y, x, farba);
        else 
        	kresli_bod(x, y, farba);
        if(d < 0) {
            d = d+k1;
        } else {
            d = d+k2;
            y = y+krok;
        }
    }
}

int main(void)
{
	ugr_asm_setmode(0x13);

	kresli_kruh(160, 100, 90, 15);
	int uhol = -90;
	float u;
	while(!kbhit()) {
		u = uhol * M_PI / 180.0;
		kresli_ciaru(160, 100, 160 + 85 * cos(u), 100 + 85 * sin(u), 15);
		delay(25);
		uhol++;
		kresli_ciaru(160, 100, 160 + 85 * cos(u), 100 + 85 * sin(u), 0);
	}

	// pockat na vstup uzivatela
	getch();
	ugr_asm_setmode(0x03);

	return 0;
}