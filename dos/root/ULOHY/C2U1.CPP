#include "ugr.h"

void kresli_bod(int x, int y)
{
	ugr_asm_putpixel(15, x, y);
}

void kresli_ciaru(int x1, int y1, int x2, int y2)
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
        	kresli_bod(y, x);
        else 
        	kresli_bod(x, y);
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

	float u;

	for(int uhol = 0; uhol < 360; uhol += 10) {
		u = uhol * M_PI / 180.0;
		kresli_ciaru(160, 100, 160 + (80 * cos(u)), 100 + (80 * sin(u)));
	}

	// pockat na vstup uzivatela
	getch();
	ugr_asm_setmode(0x03);

	return 0;
}