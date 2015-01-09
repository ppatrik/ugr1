#include <ugr.h>

int main(void)
{
  ugr_asm_setmode(0x13);

  getch();
  ugr_asm_setmode(0x3);
  return 0;
}
