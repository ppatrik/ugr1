#include "ugr.h"

void kresli_bod(int x, int y, int farba)
{
	ugr_asm_putpixel(farba, x, y);
}

	//cv03_vzor(1);
	//FloodFill4(160, 100, 0, 1);
	
	
	// dochadza ku stack overflow
	//cv03_vzor(2);
	//FloodFill4(160, 100, 0, 1);

void FloodFill4(int x, int y, int povodna_farba, int farba)
{
	if(ugr_asm_getpixel(x, y) == povodna_farba) {
		kresli_bod(x, y, farba);
		delay(10);
		FloodFill4(x + 1, y, povodna_farba, farba);
		FloodFill4(x - 1, y, povodna_farba, farba);
		FloodFill4(x, y + 1, povodna_farba, farba);
		FloodFill4(x, y - 1, povodna_farba, farba);
	}
}

	// aby osemsmerove fungovalo potrebovalo by mat hrubsie steny
	//cv03_vzor(1);
	//FloodFill8(160, 100, 0, 1);

void FloodFill8(int x, int y, int povodna_farba, int farba)
{
	if(ugr_asm_getpixel(x, y) == povodna_farba) {
		kresli_bod(x, y, farba);
		delay(10);
		FloodFill8(x + 1, y, povodna_farba, farba);
		FloodFill8(x - 1, y, povodna_farba, farba);
		FloodFill8(x, y + 1, povodna_farba, farba);
		FloodFill8(x, y - 1, povodna_farba, farba);
		
		FloodFill8(x + 1, y + 1, povodna_farba, farba);
		FloodFill8(x + 1, y - 1, povodna_farba, farba);
		FloodFill8(x - 1, y + 1, povodna_farba, farba);
		FloodFill8(x - 1, y - 1, povodna_farba, farba);
		
	}
}

	//cv03_vzor(2);
	//SeedFill(160, 100, 0, 15, 1);

	//cv03_vzor(3);
	//SeedFill(160, 100, 0, 15, 1);

void SeedFill(int x, int y, int povodna_farba, int stena, int farba) 
{
    int xR = x;
    int xL = x;
    while(0<=xL && xL<360 && ugr_mem_getpixel(xL, y)==povodna_farba) xL--;
    while(0<=xR && xR<360 && ugr_mem_getpixel(xR, y)==povodna_farba) xR++;
    xL++;
    xR--;
    ugr_hline(farba, xL, y, xR);
    //delay(24);
    for(x = xL; x <= xR; x++) {
        if(
            (0<=y-1 && y-1<200) && 
            (ugr_mem_getpixel(x, y-1)==povodna_farba) && 
            //(ugr_mem_getpixel(x, y-1)!=f_stena) && 
            (((0<=x-1 && x-1<360) && ugr_mem_getpixel(x-1, y-1)==stena) || x==xL)
        ) {
            SeedFill(x, y-1, povodna_farba, stena, farba);
        }
        if(
            (0<=y+1 && y+1<200) && 
            (ugr_mem_getpixel(x, y+1)==povodna_farba) && 
            //(ugr_mem_getpixel(x, y+1)!=f_stena) && 
            (((0<=x-1 && x-1<360) && ugr_mem_getpixel(x-1, y+1)==stena) || x==xL)
        ) {
            SeedFill(x, y+1, povodna_farba, stena, farba);
        }

    }
}

int main(void)
{
	ugr_asm_setmode(0x13);

	cv03_vzor(4);
	SeedFill(160, 50, 0, 15, 13);
	SeedFill(1, 1, 0, 15, 10);
	SeedFill(160, 100, 0, 15, 14);

	// pockat na vstup uzivatela
	getch();
	ugr_asm_setmode(0x03);

	return 0;
}