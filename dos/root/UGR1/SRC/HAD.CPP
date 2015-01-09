/*
    Had - jednoducha hra vyuzivajuca kniznicu UGR
    Copyright (C) 2010 Samuel Kupka

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
#include <ugr.h>

/* Nastavte na nenulovu hodnotu, ak chcete ovladat bieleho hada */
int CHCEM_HRAT=0;

/* Ak nastavene na nenulu, tak bude zobrazovat aj cesty hadov */
int CHCEM_VIDIET_CESTY=1;

/* Konstanty */
#define GAME_W 64
#define GAME_H 40
#define MAX_DLZKA 50
#define MAX_HAD 5
#define MAX_JEDLO 5
#define MAX_Q 10000

/* Pocet hadov a pocet jediel */
int poc_had=MAX_HAD;
int poc_jedlo=MAX_JEDLO;

/* Do tejto mapy budu ulozene prekazky */
uint8_t mapa[GAME_H][GAME_W];

/* Struktura, v ktorej je ulozeny kazdy had */
typedef struct {
    int hx[MAX_DLZKA],hy[MAX_DLZKA],h;
    uint8_t c1,c2,d;
} HAD;
HAD had[MAX_HAD];

/* Smerove pole */
const int delta[5]= {0,1,0,-1,0};

/* Pole s jedlom */
int jx[MAX_JEDLO],jy[MAX_JEDLO];

/* Krok skace medzi hodnotami 0 a 1 a sluzi na animaciu jedla */
int krok=0;

/* Nakresli jeden pixel znazornujuci cestu hada k jedlu */
void cesta_kresli(uint8_t color, int x, int y)
{
    if(!CHCEM_VIDIET_CESTY) return;
    x*=5;
    y*=5;
    ugr_mem_putpixel(color,x+2,y+2);
}

/* Nakresli jedno policko 5x5 - telo hada */
void had_kresli(uint8_t color, int x,int y)
{
    x*=5;
    y*=5;
    for(int xx=0; xx<5; xx++) {
        for(int yy=0; yy<5; yy++) {
            ugr_mem_putpixel(color,x+xx,y+yy);
        }
    }
}

/* Nakresli jedlo - policko 5x5 */
void jedlo_kresli(int x,int y)
{
    x*=5;
    y*=5;
    for(int xx=0; xx<5; xx++) {
        for(int yy=0; yy<5; yy++) {
            ugr_mem_putpixel(((xx+yy+krok)&1)?10:0,x+xx,y+yy);
        }
    }
}


/* Posunie hada id smerom, ktorym hovori jeho vnutorna premenna d. Zaroven
dava pozor, ci had nepresiel mimo hracieho pola a ak ano, tak ho presunie
na druhy koniec. */
void had_posun(int id)
{
    for(int i=MAX_DLZKA-1; i>0; i--) {
        had[id].hx[i]=had[id].hx[i-1];
        had[id].hy[i]=had[id].hy[i-1];
    }
    had[id].hx[0]+=delta[had[id].d];
    had[id].hy[0]+=delta[had[id].d+1];
    if(had[id].hx[0]<0) had[id].hx[0]=GAME_W-1;
    if(had[id].hy[0]<0) had[id].hy[0]=GAME_H-1;
    if(had[id].hx[0]>=GAME_W) had[id].hx[0]=0;
    if(had[id].hy[0]>=GAME_H) had[id].hy[0]=0;
    for(i=0; i<had[id].h; i++) {
        had_kresli((i==0)?had[id].c1:had[id].c2,had[id].hx[i],had[id].hy[i]);
    }
}

/* Vracia 0, ak je na policku nejaky had rozny od myid, 1 - policko je volne,
2 a viac - na policku je jedlo */
int je_volno(int x,int y,int myid=-1)
{
    if(!mapa[y][x]) return -1;
    for(int id=0; id<poc_had; id++) {
        if(id!=myid) {
            for(int i=0; i<had[id].h; i++) {
                if(had[id].hx[i]==x&&had[id].hy[i]==y) return 0;
            }
        }
    }
    for(id=0; id<poc_jedlo; id++) {
        if(x==jx[id]&&y==jy[id]) return 2+id;
    }
    return 1;
}

/* Premiestni/vlozi jedlo id na novu poziciu, ktora musi byt prazdna */
void jedlo_umiestni(int id)
{
    int x,y;
    jx[id]=-1;
    jy[id]=-1;
    while(1) {
        x=random(GAME_W);
        y=random(GAME_H);
        if(je_volno(x,y)==1) break;
    }
    jx[id]=x;
    jy[id]=y;
}

/* Spusti vyhladavanie do sirky od hlavy hada a najde smer k najblizsiemu
jedlu. Ak neexistuje cesta k jedlu, tak ide smerom k najvzdialenejsiemu
dostupnemu policku - co samozrejme nie je velmi ono, ale mozno to hada
obcas dokaze zachranit. Taktiez fronta nie je napisana dobre, ak chcete,
mozete to vylepsit. */
void had_ako_dalej(int idd)
{
    int qx[MAX_Q],qy[MAX_Q],qd[MAX_Q],qs=0,qe=1;
    uint8_t v[GAME_H][GAME_W];
    memcpy(v,mapa,GAME_W*GAME_H);
    for(int id=0; id<poc_had; id++) {
        for(int i=0; i<had[id].h; i++) {
            v[had[id].hy[i]][had[id].hx[i]]=0;
        }
    }
    qx[0]=had[idd].hx[0];
    qy[0]=had[idd].hy[0];
    qe=1;
    int x,y,xx,yy,d,j,i,naslo=0;
    while(qs<qe) {
        xx=qx[qs];
        yy=qy[qs];
        qs++;
        for(i=0; i<4; i++) {
            x=xx+delta[i];
            y=yy+delta[i+1];
            if(x<0) x=GAME_W-1;
            if(y<0) y=GAME_H-1;
            if(x>=GAME_W) x=0;
            if(y>=GAME_H) y=0;
            if(v[y][x]==1&&qe<MAX_Q) {
                v[y][x]=(i+1)<<4;
                for(j=0; j<poc_jedlo; j++) {
                    if(x==jx[j]&&y==jy[j]) {
                        naslo=1;
                        break;
                    }
                }
                if(naslo) break;
                qx[qe]=x;
                qy[qe]=y;
                qe++;
            }
        }
        if(naslo) break;
    }
    while(1) {
        d=v[y][x]>>4;
        if(d==0) break;
        d--;
        x-=delta[d];
        y-=delta[d+1];
        if(x<0) x=GAME_W-1;
        if(y<0) y=GAME_H-1;
        if(x>=GAME_W) x=0;
        if(y>=GAME_H) y=0;
        if(x==had[idd].hx[0]&&y==had[idd].hy[0]) {
            had[idd].d=d;
            return;
        }
        cesta_kresli(had[idd].c1,x,y);
    }
}

/* Premiestni/vlozi hada id na novu poziciu, ktora musi byt v tom momente
prazdna. */
void had_umiestni(int id)
{
    int x,y;
    had[id].hx[0]=-1;
    had[id].hy[0]=-1;
    had[id].h=1;
    had[id].c1=(15-id);
    had[id].c2=had[id].c1-8;
    had[id].d=random(4);
    while(1) {
        x=random(GAME_W);
        y=random(GAME_H);
        if(je_volno(x,y)==1) break;
    }
    had[id].hx[0]=x;
    had[id].hy[0]=y;
}

/* Skontroluje, ci nejaky had neziedol jedlo, v tom pripade to dane jedlo
premiestni a hada predlzi, ak je to mozne. Taktiez skontroluje, ci had
nekusol do nejakeho hada, ak ano, tak ho skrati na 1 a presunie na novu
poziciu. */
void zozri(void)
{
    int r,x,y;
    for(int id=0; id<poc_had; id++) {
        r=je_volno(had[id].hx[0],had[id].hy[0],id);
        if(r>=2) {
            had[id].h++;
            if(had[id].h>MAX_DLZKA-1) had[id].h=MAX_DLZKA-1;
            jedlo_umiestni(r-2);
        }
        if(r<=0) {
            had_umiestni(id);
        }
    }
}


int main(void)
{
    randomize();

    ugr_asm_setmode(0x13);
    /* Nastavime pouzivanie zadneho bufferu, do ktoreho sa kresli. */
    ugr_grbuffer_enable(1);

    uint16_t z,x,y;

    /* Vygenerujeme peknu mapu s prekazkami */
    memset(mapa,1,GAME_W*GAME_H);
    for(y=0; y<GAME_H; y++) {
        for(x=0; x<GAME_W; x++) {
            if(x%16==0&&y<(GAME_H*2/3)) mapa[y][x]=0;
            if(x%16==8&&y>(GAME_H/3)) mapa[y][x]=0;
            if(y==0&&(x%32)<16) mapa[y][x]=0;
            if(y==(GAME_H-1)&&(x%32)>8&&(x%32)<24) mapa[y][x]=0;
        }
    }
    int id;
    /* Na zaciatok rozmiestnime hadov */
    for(id=0; id<poc_had; id++) {
        had_umiestni(id);
    }
    /* a taktiez jedla */
    for(id=0; id<poc_jedlo; id++) {
        jedlo_umiestni(id);
    }
    while(1) {
        if(kbhit()) {
            z=getch();
            /* Ak bola stlacena nula, to znamena, ze pride este jedna hodnota. */
            if(z==0) z=getch()<<8;
        } else z=0;
        /* Ak bol stlaceny ESC, tak skonci */
        if(z==27) break;

        /* Ak chcem hrat, tak pozri, ci neboli stlacene klavesy. */
        if(CHCEM_HRAT) {
            if(z==20480&&had[0].d!=2) had[0].d=0;
            if(z==19712&&had[0].d!=3) had[0].d=1;
            if(z==18432&&had[0].d!=0) had[0].d=2;
            if(z==19200&&had[0].d!=1) had[0].d=3;
        }

        /* Vycisti zadny buffer */
        ugr_grbuffer_clear();
        for(y=0; y<GAME_H; y++) {
            for(x=0; x<GAME_W; x++) {
                if(mapa[y][x]==0) had_kresli(1,x,y);
            }
        }

        /* Nakresli vsetky jedla */
        for(id=0; id<poc_jedlo; id++) {
            jedlo_kresli(jx[id],jy[id]);
        }
        /* Poposuvaj vsetkych hadov */
        for(id=0; id<poc_had; id++) {
            if(id>0||CHCEM_HRAT==0) {
                /* Ak ide o automatickeho hada, tak najdi smer */
                had_ako_dalej(id);
            }
            had_posun(id);
        }
        /* Skontroluj, ci nejaky had nieco nezozral */
        zozri();
        /* Skopiruj zadny buffer na obrazovku */
        ugr_grbuffer_blit();
        /* Pockaj nejaky cas */
        delay(80);
        /* Zneguj premennu krok */
        krok=!krok;
    }
    ugr_asm_setmode(0x03);
    return 0;
}
