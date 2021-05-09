#include <stdio.h>
#include "ftree.h"

#ifndef PORT
  #define PORT 30000
#endif

int main(int argc, char** argv) {
	  if(argc > 1){
		fprintf(stderr, "Usage error: no arguments to be given\n");
		exit(0);
	}
fcopy_server(PORT);
return 0;
}

