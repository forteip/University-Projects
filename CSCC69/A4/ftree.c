#include <stdio.h>
#include <stdlib.h>
#include <unistd.h> /* Things needed for lstat */
#include <stdio.h>
#include <sys/stat.h>
#include <sys/types.h> /* Things needed for lstat */
#include <dirent.h>
#include <string.h>
#include <libgen.h>
#include <netdb.h>
#include <arpa/inet.h>
#include "ftree.h"
#include "hash.h"


void show_hash(char *hash_val, long block_size) {
    for(int i = 0; i < block_size; i++) {
        printf("%.2hhx ", hash_val[i]);
    }
    printf("\n");
}

int isdir(const char *filename){/*Checks if given filename is a directory returns 1 if it is*/
	int ret;
	struct stat filesStat;
	lstat(filename, &filesStat);
	if((ret = S_ISDIR(filesStat.st_mode)) == 0){
		return 0;
	}
	else
	return 1;
}

int size(const char *filename){
	struct stat filesStat;
	lstat(filename, &filesStat);
	return filesStat.st_size;
}

int islink(const char *filename){ /*Checks if given filename is a link*/
	struct stat fileStat;
	lstat(filename, &fileStat);
	if(S_ISLNK(fileStat.st_mode) == 0){
		return 0;
	}
	else
	return 1;
}

int isemptydir(const char *fname){ /*Checks if given filename is an empty directory*/
	DIR *d;
	struct dirent *buff;

	if(isdir(fname) == 1){
		d = opendir(fname);
		buff = readdir(d);
 			if(strcmp(".",buff->d_name) == 0 || strcmp("..",buff->d_name) == 0){ /*loop until you find the first entry that's not . or ..*/
				while(strcmp(".",buff->d_name) == 0 || strcmp("..",buff->d_name) == 0){
					if((buff = readdir(d)) == NULL){
						closedir(d);						
						return 1;
					}			
				}			
		
		
		  	}
	closedir(d);
	}
return 0;	
}

int hashcompare(struct fileinfo finfo){
	FILE *df; 

	df = fopen(finfo.path, "rb");
	if(df == NULL){
		fprintf(stderr, "Error opening desination file: %s", finfo.path);
		exit(1);
	}

	char *hash1 = hash(df);
	char *hash2 = finfo.hash;
	char hasharr[8] = {'0'};
	for(int i = 0; i< 8; i++){
		hasharr[i] = hash1[i]; 
	}
	if(strcmp(hasharr, hash2) == 0){
		fclose(df);
		return 0;
	}
	else
	fclose(df);
	return 1;
}

int exists(const char *filename){
	struct stat filestat;
	int x = lstat(filename, &filestat);
	if(x < 0){
		return 0;
	}
	else
		return 1;
}

int qualifies(struct fileinfo finfo){ //Checks for only the finfo
	char path[sizeof(finfo.path)];
	strcpy(path, finfo.path);
	char * basefile = basename(finfo.path);

	if(S_ISLNK(finfo.mode) != 0){
		return MATCH;
	}

	if(basefile[0] == '.'){
		return MATCH;
	}

	if(exists(finfo.path) == 0){ //In the case the dest doesn't exist
		if(S_ISDIR(finfo.mode) != 0){ //If the path was a dir, make it
			if(mkdir(finfo.path, finfo.mode) < 0){
				perror("An error has occured during directory creation");
			}
			return MATCH;
		}
		if(S_ISDIR(finfo.mode) == 0){ //If the path was a file, make it
			return MISMATCH;
		}
	}
	
	if(isdir(finfo.path) == 0 && S_ISDIR(finfo.mode) == 0){ //If the dest is a file AND source is a file

			if(size(finfo.path) != finfo.size){ //If the dest file has a different size
				return MISMATCH;
			}

			if(size(finfo.path) == finfo.size){ // If the dest file has the same size
				int mhash = hashcompare(finfo);	

				if(mhash == 0){ //If the dest file has the same hash
					if(chmod(finfo.path, finfo.mode) < 0){
						fprintf(stderr, "An error has occured while changing permissions to %s\n", finfo.path);
					}
					return MATCH;
				}

				if(mhash != 0){ //If the dest file has a different hash
					return MISMATCH;
				}	
			}
	}	
	
	if(isdir(path) == 1 && S_ISDIR(finfo.mode) == 1){ //if dest is a dir and exists AND src is a dir
		if(chmod(finfo.path, finfo.mode) < 0){
			fprintf(stderr, "An error has occured while changing permissions to %s\n", finfo.path);
		}
		return MATCH;
	}

	if(isdir(path) != S_ISDIR(finfo.mode)){ //if Dest type mismatches src
		return MATCH_ERROR;
	} 

	return 0;
}

struct fileinfo smaker(char *src_path, char *dest_path){
	int dir = 0;
	char *basefile = basename(src_path);
	char dest_add[strlen(basefile) + strlen(dest_path) + 1];
	strcpy(dest_add, dest_path);
	strcat(dest_add, "/");
	strcat(dest_add, basefile);

	struct fileinfo *finfo;
	finfo = (struct fileinfo*)malloc(sizeof(struct fileinfo));

	if(exists(src_path) == 0){
		fprintf(stderr, "Error: source file does not exist");
		exit(1);
	}


	struct stat filestat;
	lstat(src_path, &filestat);

	if(S_ISDIR(filestat.st_mode) != 0){
		dir = 1;
	}
	
	strcpy(finfo->path, dest_add);
	finfo->mode = filestat.st_mode;

	finfo->size = filestat.st_size;

	if(dir == 0){
		FILE *sf;	//Create the hash
		sf = fopen(src_path, "rb");
		char *hashy= hash(sf);
		char hash[8];
		for(int i = 0; i< 8; i++){
			hash[i] = hashy[i]; 
		}
		fclose(sf);
		strcpy(finfo->hash, hash);
	}
	return *finfo;

}

void wstruct(int sock_fd, struct fileinfo finfo){
	int n = 0;

	if((n = write(sock_fd, finfo.path, sizeof(finfo.path))) < 0){
		fprintf(stderr, "Error writing path to socket\n");
	}
	int converted_number = htonl(finfo.mode);
	if((n = write(sock_fd, &converted_number, sizeof(converted_number))) < 0){
		perror("Error writing mode to socket");
	}
	if((n = write(sock_fd, finfo.hash, sizeof(finfo.hash))) < 0){
		fprintf(stderr, "Error writing hash to socket\n");
	}

	converted_number = htonl(finfo.size);
	if((n = write(sock_fd, &converted_number, sizeof(converted_number))) < 0){
		fprintf(stderr, "Error writing size to socket\n");
	}

}

void rstruct(int client_fd){
	int n = 0;
	char pbuffer[255];
	int mbuffer;
	char hbuffer[8];
	int sbuffer;

	struct fileinfo finfo;
	while(1){
		if((n = read(client_fd, pbuffer, sizeof(pbuffer))) > 0){
			pbuffer[255] = '\0';
			strcpy(finfo.path, pbuffer);
			//printf("Your path is %s\n", pbuffer);
		}

		if((n = read(client_fd, &mbuffer, sizeof(mbuffer))) > 0){
			mbuffer = ntohl(mbuffer);
			finfo.mode = mbuffer;
			//printf("Your mode is %d\n", mbuffer);
		}

		if((n = read(client_fd, hbuffer, 8)) > 0){
			hbuffer[8] = '\0';
			strcpy(finfo.hash, hbuffer);
			//printf("Your hash is %s\n", finfo.hash);
		}

		if((n = read(client_fd, &sbuffer, sizeof(sbuffer))) > 0){
			sbuffer = ntohl(sbuffer);
	
			finfo.size = sbuffer;
			//printf("Your size is %d\n", sbuffer);
		}

		int verdict;
		verdict = qualifies(finfo);
		verdict = htonl(verdict);

		write(client_fd, &verdict, sizeof(verdict));
	
		verdict = ntohl(verdict);

		if(verdict == MISMATCH){


			char wbuffer[finfo.size];
			int counter = 0;
			FILE *df;
			df = fopen(finfo.path, "ab"); //Create file
				if(df == NULL){
					fprintf(stderr, "Error creating file\n");
					exit(1);
				}

			n = read(client_fd, wbuffer, sizeof(wbuffer));
			counter += n;
			fwrite(wbuffer, 1, n, df);
		
			fclose(df);
			int mess;
			if(counter < finfo.size){
				mess = htonl(TRANSMIT_ERROR);
			}
			if(counter == finfo.size){
				mess = htonl(TRANSMIT_OK);
			}
			int t;
			if((t = write(client_fd, &mess ,sizeof(mess))) == 0){
				fprintf(stderr, "Error Sending Transmit status\n");
			}
		
		}
		if(verdict == MATCH){
		}
		if((n = recv(client_fd, pbuffer, sizeof(pbuffer), MSG_PEEK)) == 0){
			break;
		}
	}
	
}
void filecopy(char *src_path, char *dest_path, int sockfd){
	int n;
	int verdict; //store if server result here;
	int data;
	struct fileinfo finfo;
	finfo = smaker(src_path, dest_path); //Create the fileinfo struct

	wstruct(sockfd, finfo); // sent it to the server

	if((n = read(sockfd, &verdict, sizeof(verdict))) == 0){ //Read result;
		fprintf(stderr, "Error, No reply from server\n");
		exit(1);
	} 

	verdict = ntohl(verdict);

	if(verdict == MISMATCH){ //In this case, it's time to open the src and read->write to server.
		unsigned char buffer[1024] = {0}; 
		FILE *sf;	//Open and read file
		sf = fopen(src_path, "rb");
		if(sf == NULL){
			fprintf(stderr, "Error opening file: %s\n", src_path);
		}

		while((data = fread(buffer, 1, 1024 , sf)) > 0){
			if((n = write(sockfd, buffer, data)) == 0){
				fprintf(stderr, "Error Transmitting file %s to server\n", src_path);
				exit(1);
			}
			
		} 
		fclose(sf); 
		int transfer_status;

		if((n = read(sockfd, &transfer_status, sizeof(transfer_status))) == 0){
			fprintf(stderr, "Error confirming Transfer status for %s\n", dest_path);
			exit(1);
		}	

		transfer_status = ntohl(transfer_status);

		if(transfer_status == TRANSMIT_ERROR){
			fprintf(stderr, "Error with File transmit %s\n", src_path);
		}
	}





}

void dircopy(char *src_path, char *dest_path, int sockfd){
	DIR *d;
	struct dirent *buf;
	int n;
	int verdict; //store if server result here;
	int data;

	struct fileinfo finfo;

	finfo = smaker(src_path, dest_path); //Create the fileinfo struct

	wstruct(sockfd, finfo); // sent it to the server

	if((n = read(sockfd, &verdict, sizeof(verdict))) == 0){ //Read result;
		fprintf(stderr, "Error, No reply from server\n");
		exit(1);
	} 

	verdict = ntohl(verdict);

	if(verdict == MISMATCH){ //In this case, it's time to open the src and read->write to server.
		unsigned char buffer[1024] = {0}; 
		FILE *sf;	//Open and read file
		sf = fopen(src_path, "rb");
		if(sf == NULL){
			fprintf(stderr, "Error opening file: %s\n", src_path);
		}

		while((data = fread(buffer, 1, 1024 , sf)) > 0){ //Reads until nothing left
			if((n = write(sockfd, buffer, data)) == 0){
				fprintf(stderr, "Error Transmitting file %s to server\n", src_path);
				exit(1);
			}
			
		} 
		fclose(sf); 			
		int transfer_status;

		if((n = read(sockfd, &transfer_status, sizeof(transfer_status))) == 0){
			fprintf(stderr, "Error confirming Transfer status for %s\n", dest_path);
			exit(1);
		}	
		transfer_status = ntohl(transfer_status);
		if(transfer_status == TRANSMIT_ERROR){
			fprintf(stderr, "Error with File transmit %s\n", src_path);
		}
	}

	d = opendir(src_path);
	buf = readdir(d);


	while(buf != NULL){
			if(strcmp(".",buf->d_name) == 0 || strcmp("..",buf->d_name) == 0){
				while(strcmp(".",buf->d_name) == 0 || strcmp("..",buf->d_name) == 0){
					if((buf = readdir(d)) == NULL){ /*If the destination directory is empty*/
						closedir(d);
						return;
					}
				}

			}

		char address[sizeof(src_path) + sizeof(buf->d_name) + 1];
		strcpy(address, src_path);
		strcat(address, "/");
		strcat(address, buf->d_name);
		if(buf->d_name[0] == '.'){
			buf = readdir(d);
		}
		if(isdir(address) == 1){
			dircopy(address, finfo.path, sockfd);
			buf = readdir(d);
		}
		if(isdir(address) == 0){
			if(islink(address) != 0){
				buf = readdir(d);

			}
			else
			filecopy(address, finfo.path, sockfd);
			buf = readdir(d);
		}

	}

}

int fcopy_client(char *src_path, char *dest_path, char *host, int port){
	int sock_fd = socket(AF_INET, SOCK_STREAM, 0);
	if(sock_fd < 0){
	    perror("randclient: socket");
	    exit(1);
	}
	struct sockaddr_in server;
	server.sin_family = AF_INET;
	server.sin_port = htons(PORT);

	if (inet_pton(AF_INET, host, &server.sin_addr)< 0) {
		perror("client: inet_pton");
		close(sock_fd);
		exit(1);
	}

	if (connect(sock_fd, (struct sockaddr *) &server, sizeof(server)) == -1) {
		perror("client: connect");
		close(sock_fd);
		exit(1);
	}

	printf("Connected\n");
	
	//Begin Filtering	
	
	char * basefile = basename(src_path); //if hidden file src
	if(basefile[0] == '.'){
		fprintf(stderr, "Error: Source file is a hidden file\n");
		exit(1);
	}
	int dirchk;
	
	dirchk = islink(src_path); //if soft link src
	
	if(dirchk != 0){
		fprintf(stderr, "Error: Source file is a Symbolic Link\n");
		exit(1);
	}	

	dirchk = isdir(src_path);
	
	if(dirchk == 0){ //src is a file
		filecopy(src_path, dest_path, sock_fd);
	}

	if(dirchk == 1){ //src is a dest.
		dircopy(src_path, dest_path, sock_fd);
	}
	close(sock_fd);
	return 0;
}

/* Server fcopy function.
 * Should never return, as it keeps waiting for new connections.
 */ 
void fcopy_server(int port){
	int on = 1;
	// Create socket
    int sock_fd = socket(AF_INET, SOCK_STREAM, 0);
	if (sock_fd < 0) {
		perror("server: socket");
		exit(1);
	}

	if (setsockopt(sock_fd, SOL_SOCKET, SO_REUSEADDR, &on, sizeof(on)) == -1) {
		perror("setsockopt -- REUSEADDR");
	}

    	// Bind socket to an address
	struct sockaddr_in server;
	server.sin_family = AF_INET;
	server.sin_port = htons(PORT);  // Note use of htons here
	server.sin_addr.s_addr = INADDR_ANY;
	memset(&server.sin_zero, 0, 8);  // Initialize sin_zero to 0

	if (bind(sock_fd, (struct sockaddr *)&server, sizeof(struct sockaddr_in)) < 0) {
		perror("server: bind");
		close(sock_fd);
		exit(1);
	}

    // Create queue in kernel for new connection requests
	if (listen(sock_fd, 5) < 0) {
		perror("server: listen");
		close(sock_fd);
		exit(1);
	}
	printf("Now accepting new connections. \n");
    // Accept a new connection
	while(1){
	    int client_fd = accept(sock_fd, NULL, NULL);
	    if (client_fd < 0) {
		perror("server: accept");
		close(sock_fd);
		exit(1);
	    }
		printf("Got a new connection.\n");


		rstruct(client_fd);
		
		printf("Closing client connection \n");

		close(client_fd);
	}

}
