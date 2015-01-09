#include <ugr.h>

/* Tento program nakresli Sierpinskeho trojuholnik.
Najprv sa urcia tri vrcholy trojuholnika. My pouzijeme [160,0], [60,199]
a [260,199]. Poziciu pera nastavime na prvy z nich. Cely proces spociva
vo vybrati nahodneho z tychto troch bodov a presunutie pera do polovice
vzdialenosti medzi predchadzajucou polohou pera a vybranym bodom. */

int main(void)
{
  uint16_t x,y,x1,y1;
  uint8_t r;
  /* Zapneme graficky rezim */
  ugr_asm_setmode(0x13);

  /* Nastavenie zaciatocnej pozicie na [160,0] */
  x=160;
  y=0;
  while(!kbhit()){
    /* Nahodne vyberieme jeden z troch vrcholov */
    r=random(3);
    if(r==0){ x1=160; y1=  0; }
    if(r==1){ x1= 60; y1=199; }
    if(r==2){ x1=260; y1=199; }
    /* Vypocitame novu poziciu pera ako stred medzi vybranym vrcholom
    a povodnou poziciou pera */
    x=(x+x1)/2;
    y=(y+y1)/2;
    /* Nakreslime tam pixel nahodnej farby */
    ugr_mem_putpixel(random(256),x,y);
  }
  getch();

  ugr_asm_setmode(0x03);
  return 0;
}
