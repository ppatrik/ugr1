#include <ugr.h>

/* Pomocna funkcia, ktora vracia druhu mocninu */
double sqr(double x)
{
  return x*x;
}

int main(void)
{
  uint16_t x,y;
  /* Zapnutie grafickeho rezimu */
  ugr_asm_setmode(0x13);
  ugr_grbuffer_enable(1);

  /* Celu obrazovku prekreslime */
  for(y=0;y<200;y++){
	for(x=0;x<320;x++){
	  /* Farbu pre pixel urcime podla vzdialenosti od stredu obrazovky.
	  Cim je vzdialenejsi, tym je cislo vyssie. */
	  ugr_mem_putpixel(sqrt(sqr((double)x-160)+sqr((double)y-100)),x,y);
	}
  }
  ugr_grbuffer_blit();


  /* Pockame na stlacenie klavesy */
  getch();

  y=0;
  /* Kym sa nestlaci klavesa */
  while(!kbhit()){
	/* Pre vsetky farby (teda pre farby od 0 do 255) */
	for(x=0;x<256;x++){
	  /* Nastavime kazdej modru zlozku podla jednoduchej funkcie */
	  ugr_asm_palette(x,0,0,32-(sin((double)(x-y)/10)*31));
	}
	/* K premennej y pripocitavame 1, aby sme mali animaciu */
	y++;
	/* Pockame desat milisekund */
	delay(10);
  }
  getch();

  ugr_asm_setmode(0x03);
  return 0;
}
