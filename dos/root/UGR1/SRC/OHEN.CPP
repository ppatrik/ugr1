#include <ugr.h>

/* Ukazka velmi jednoducheho ohna vyuzivajuceho 64 farieb.
Kazdy pixel sa vypocita ako priemer suctov svojej hodnoty a hodnoty troch
pixelov pod nim.

.....
..1..
.121.
.....

Pricom pixel pod aktualnym pixelom je najvyznamnejsi, preto jeho hodnotu
zapocitame dvakrat.

Tymto sposobom stale prechadzame obrazovku odhora az dole.
Na uplne najspodnejsom riadku vygenerujeme pred kazdym novym prechodom
nahodne farby z daneho rozsahu.
*/

int main(void)
{
  uint16_t x,y,i;
  /* Zapneme graficky rezim */
  ugr_asm_setmode(0x13);
  ugr_grbuffer_enable(1);
  ugr_grbuffer_clear();

  /* Nastavime prvych 64 farieb na rozne urovne cervenej. Budeme teda
  vyuzivat len prvych 64 farieb (od 0 do 63). */
  for(i=0;i<64;i++){
	ugr_asm_palette(i,i,i/6,0);
  }

  /* Kym nie je stlacena klavesa */
  while(!kbhit()){
	/* Prechadzame obroazovku riadok po riadku od hora dole a preskakujeme
	kazdy neparny riadok. */
	for(y=100;y<198;y+=2){
	  /* A kazdy riadok zlava doprava */
	  for(x=0;x<320;x++){
		/* Precitame farbu pixelu na pozicii [x,y] */
		i=ugr_mem_getpixel(x,y);
		/* a na pozicii [x,y+2], tento je vyznamny, preto ho nasobime dvoma */
		i+=ugr_mem_getpixel(x,y+2)*2;
		/* a este farby na [x-1,y+2] a [x+1,y+2] */
		if(x   > 0  ) i+=ugr_mem_getpixel(x-1,y+2);
		if(x+1 < 320) i+=ugr_mem_getpixel(x+1,y+2);
		/* Cele to vydelime 5, aby sme dostali cislo z rozsahu <0;63> */
		i/=5;
		/* Tuto farbu zapiseme na [x,y] a [x,y+1] */
		ugr_mem_putpixel(i,x,y);
		ugr_mem_putpixel(i,x,y+1);
	  }
	}
	/* Na uplne posledny riadok vyegenerujeme nahodne farby z intervalu <0;63> */
	for(x=0;x<320;x++){
	  ugr_mem_putpixel(random(64),x,198);
	}
	ugr_grbuffer_blit();
  }
  getch();

  ugr_asm_setmode(0x03);
  return 0;
}
