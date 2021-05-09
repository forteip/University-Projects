#include <stdio.h>
#include "ftree.h"

#ifndef PORT
  #define PORT 30000
#endif

int main(int argc, char** argv) {
	  if(argc < 4 || argc > 4){
		fprintf(stderr, "Usage error: Please enter IP address only\n");
		exit(0);
	}

	int ret = fcopy_client(argv[1], argv[2], argv[3], PORT);
return 0;
}
