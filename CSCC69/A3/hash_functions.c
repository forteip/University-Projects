#include <stdio.h>
#include <stdlib.h>


char *hash(FILE *f) {
	char x;
	int counter = 0;
	int block_size = 8;
	char *hash_val = (char *)malloc(sizeof(char) * 9);
	while(fscanf(f, "%c", &x) != EOF){
		if(counter < block_size){
			hash_val[counter] = (hash_val[counter] ^ x);
			counter++;
		}/* if block size isn't reached, Xor at and advance counter */
		else if(counter == block_size) {
			counter = 0;
			hash_val[counter] = (hash_val[counter] ^ x);
			counter++;
		} /* reset counter if block size is reached, but not EOF */
	}
	return hash_val;
}


int check_hash(const char *hash1, const char *hash2, long block_size) {
	int counter = 0;
	while(counter < block_size){
		if(hash1[counter] == hash2[counter]){
			counter++;
		} /* If current character at index counter are the same, advance */
		else if(hash1[counter] != hash2[counter]){
			return counter;
		} /* Else return where the counter has stopped */

	}
return(counter);
} /* In the case it goes through completely */
