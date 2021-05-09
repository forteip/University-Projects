#include <stdio.h>
#include <stdlib.h>

char *hash(FILE *f) {
	int BLOCK_SIZE = 8;
    char *hash_val = malloc(BLOCK_SIZE);
    char ch;
    int hash_index = 0;

    for (int index = 0; index < BLOCK_SIZE; index++) {
        hash_val[index] = '\0';
    }

    while(fread(&ch, 1, 1, f) != 0) {
        hash_val[hash_index] ^= ch;
        hash_index = (hash_index + 1) % BLOCK_SIZE;
    }

    return hash_val;
}
