#include "ugr.h"

void kresli_bod(int x, int y, int farba)
{
	ugr_asm_putpixel(farba, x, y);
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

void kresli_trojuholnik_part(int x1, int y1, int x2, int y2, int farba, int x3, int y3)
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
        	kresli_ciaru(y, x, x3, y3, farba);
        else 
        	kresli_ciaru(x, y, x3, y3, farba);
        if(d < 0) {
            d = d+k1;
        } else {
            d = d+k2;
            y = y+krok;
        }
    }
}

void kresli_trojuholnik(int x1, int y1, int x2, int y2, int x3, int y3, int farba)
{
	kresli_trojuholnik_part(x1, y1, x2, y2, farba, x3, y3);
	kresli_trojuholnik_part(x1, y1, x3, y3, farba, x2, y2);
	kresli_trojuholnik_part(x3, y3, x2, y2, farba, x1, y1);
}

int main(void)
{
	ugr_asm_setmode(0x13);

	for(int i = 0; i < 100; i++) {
		kresli_trojuholnik(rand() % 320, rand() % 200, rand() % 320, rand() % 200, rand() % 320, rand() % 200, rand() % 256);
	}
	
	// pockat na vstup uzivatela
	getch();
	ugr_asm_setmode(0x03);

	return 0;
}