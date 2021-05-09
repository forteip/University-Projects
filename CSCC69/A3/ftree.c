#include <stdio.h>
#include <stdlib.h>
#include "ftree.h"
#include "hash.h"
#include <unistd.h> /* Things needed for lstat */
#include <stdio.h>
#include <sys/stat.h>
#include <sys/types.h> /* Things needed for lstat */
#include <dirent.h>
#include <string.h>
#include <libgen.h>
#include <sys/wait.h>

int isdir(const char *filename){/*Checks if given filename is a directory returns 1 if it is*/
	struct stat filesStat;
	lstat(filename, &filesStat);
	if(S_ISDIR(filesStat.st_mode) == 0){
		return 0;
	}
	else
	return 1;
}

int islink(const char *filename){ /*Checks if given filename is a link*/
	struct stat fileStat;
	lstat(filename, &fileStat);
	return (S_ISLNK(fileStat.st_mode));
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

int hashcompare(const char*file1, const char*file2){
	FILE *sf, *df; 
	sf = fopen(file1, "rb");

	
	if(sf == NULL){
		perror("Error opening source file");
		return(-1);
	}

	df = fopen(file2, "rb");
	if(df == NULL){
		perror("Error opening desination");
		return(-1);
	}

	char *hash1 = hash(sf);
	char *hash2 = hash(df);
	if(strcmp(hash1, hash2) == 0){
		fclose(sf);
		fclose(df);
		return 0;
	}
	else
	fclose(sf);
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

int qualifies_copy(const char *checking_address, const char *location){ /*Checks if file already exists and if the filesizes are different returns 1 if overwrite should occur, 0 if not,*/
	DIR *d;								/*and -1 if file does not exist*/
	struct dirent *buf;
	struct stat filestat;
	struct stat filestat2;
	char * basefile = basename((char*)checking_address);
	d = opendir(location);
	buf = readdir(d);

	if(basefile[0] == '.'){ //If starts with .
		return 0;
	}

	while(buf != NULL){
		if(strcmp(".",buf->d_name) == 0 || strcmp("..",buf->d_name) == 0){
			while(strcmp(".",buf->d_name) == 0 || strcmp("..",buf->d_name) == 0){
				if((buf = readdir(d)) == NULL){ 
					return 1;
				}
			}

		}
		char address[(sizeof(location) + sizeof(buf->d_name) + 1)];
		strcpy(address, location);
		strcat(address, "/");
		strcat(address, buf->d_name);
		lstat(address, &filestat);
		lstat(checking_address, &filestat2);
		if(strcmp(buf->d_name, basefile) == 0){

			if(S_ISLNK(filestat2.st_mode) == 1){ // if the source file is a softlink
				return 0;
			}

			if( (S_ISLNK(filestat2.st_mode) == 0) && (isdir(checking_address) != isdir(address)) ){
				fprintf(stderr, "Mismatch error, Destination contains file or directory with the same name as Source\n");
				return(0);
			}
			

			if((filestat.st_size == filestat2.st_size)){ /* if sizes are same, they are the same type of file*/ 
				if(hashcompare(address, checking_address) != 0) {
					return(1); //Copy is performed because hash is different
				}
				
				if(hashcompare(address, checking_address) == 0) {
					return(0); //no copy because identical file exists
				}														
			}

		}
		if(strcmp(buf->d_name, basefile) != 0){
			buf = readdir(d);
		}
	}
	if (buf == NULL){
		closedir(d);
		return 1; /*End of folder, no matches found failsafe*/
	}
  printf("An unknown error has occured\n");
  exit(1);
}

int copy_helper_file(const char *source, const char *destination){
	FILE *sf, *df; 
	int data;
	char buffer[500];
	char * basefile = basename((char*)source);
	char address[sizeof(destination) + sizeof(basefile) + 1];
	strcpy(address, destination);
	strcat(address, "/");
	strcat(address, basefile);
	
	sf = fopen(source, "rb");
	
	
	if(sf == NULL){
		perror("Error opening source file");
		return(-1);
	}
	
	df = fopen(address, "wb");

	if(df == NULL){
		perror("Error opening desination");
		return(-1);
	}

	while((data = fread(buffer, 1, 500 , sf))!= 0){
		if( fwrite(buffer, 1, data, df)!= data){
			fclose(sf);
			fclose(df);
			return -1;
		}
	}
	fclose(sf);
	fclose(df);
	return 1;

}

int copy_helper_dir(const char *source, const char *destination){
	DIR *d;
	struct dirent *buf;
	struct stat filestat;
	int status;
	signed char returnv = 0;
	pid_t pid;
	char * basefile = basename((char*)source);
	int success = 0;

	char address2[sizeof(destination) + sizeof(basefile) + 1];
	strcpy(address2, destination);
	strcat(address2, "/");
	strcat(address2, basefile);
	lstat(source, &filestat);
	if((exists(address2) == 1) && isdir(source) == 1){
		if(chmod(address2, filestat.st_mode) < 0){
			perror("An error has occured while changing permissions to destination directory");
		}
	}
	if((exists(address2) == 0) && (isdir(source) == 1)){
		if(mkdir(address2, filestat.st_mode) < 0){
			perror("An error has occured during directory creation");
		}
	}

	d = opendir(source);
	buf = readdir(d);
	while(buf != NULL){
		if(strcmp(".",buf->d_name) == 0 || strcmp("..",buf->d_name) == 0){
			while(strcmp(".",buf->d_name) == 0 || strcmp("..",buf->d_name) == 0){
				if((buf = readdir(d)) == NULL){ /*If the destination directory is empty*/
					closedir(d);
					return (1 + returnv);
				}
			}

		}
		char address[sizeof(source) + sizeof(buf->d_name) + 1];
		strcpy(address, source);
		strcat(address, "/");
		strcat(address, buf->d_name);
		if(isdir(address) == 1){
			pid = fork();
			if(pid < 0){
				perror("Error making fork\n");
				return(-1);
			}
			if(pid == 0){
				int r = copy_helper_dir(address, address2);
				exit(r);
				
			}
			if(pid > 0){
			wait(&status);
				if(WIFEXITED(status)){
					returnv = returnv + WEXITSTATUS(status);
				}
			}
		buf = readdir(d);
		}
		if(isdir(address) == 0){
			int qualifier = qualifies_copy(address, address2);
			if(qualifier == 1){
					int suc = copy_helper_file(address, address2);
					if(suc < 0){
						success -= 1;
						buf = readdir(d);
					}
					else
					buf = readdir(d);
			}
			else
			buf = readdir(d);
		}

	}
	closedir(d);
	if(success < 0 || returnv < 0){
		if(returnv < 0){
			return (returnv -1);
		}
		else
		return (-(returnv +1));
	}
	else
	return((returnv + 1));
}



int copy_ftree(const char *src, const char *dest){


		if(isdir(dest) == 0){ /*In the case a file is given, 0 means is file*/
			perror("Error, destination is an invalid location");
			return -1;

		}
		if(exists(src) == 0){
			perror("Error with source");
			return -1;
		}
		if(islink(src) == 1){
			fprintf(stderr, "Error: The source file is a symbolic link\n");
			return -1;
		}
		if(isdir(src) == 0){ // if the src is not a dir

			int qualifier = qualifies_copy(src, dest);

			if(qualifier == 1){ // source file already exists in dest AND should be overwritten*/
				return copy_helper_file(src, dest);

			}
			if(qualifier == 0){
				return -1;
			}

		return -1;
		}

		//
		//if(isdir(src) == 1){
		//printf("is a dir\n");
		//return 1;
		//}
		
		if((isdir(src) == 1)){ //dest is a dir
			return copy_helper_dir(src, dest);

		}


return 0;
}
