#include <stdio.h>
// Add your system includes here.
#include <stdlib.h>
#include "ftree.h"
#include "hash.h"
#include <unistd.h> /* Things needed for lstat */
#include <stdio.h>
#include <sys/stat.h>
#include <sys/types.h> /* Things needed for lstat */
#include <dirent.h>
#include <string.h>

struct TreeNode *contentTree(const char *address, const char *name);
struct TreeNode *nextTree(const char *caddress, DIR *cdir, struct dirent *cbuf);

int isdir(const char *filename){
	struct stat filesStat;
	lstat(filename, &filesStat);
	if(S_ISDIR(filesStat.st_mode) == 0){
		return 0;
	}
	else
	return 1;
}

int isdirex(const char *filename){
	struct stat filesStat;
	lstat(filename, &filesStat);
	return (S_ISDIR(filesStat.st_mode));

}

int isemptydir(const char *fname){
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

void phelper(struct TreeNode *root, int depth){
int spaces = depth*2;
	if(root != NULL){	
		if(isdir(root->fname) == 0 && root != NULL){
			printf("%*s%s (%d)\n", spaces, "", root->fname, root->permissions);  		
			phelper(root->next, depth);
		}
		if(isdir(root->fname) == 1 && isemptydir(root->fname) == 1 ){
		   printf("%*s=====%s===== (%d)\n", spaces, "", root->fname, root->permissions);
		   phelper(root->next, depth);				
		}
		if (isdir(root->fname) == 1 && isemptydir(root->fname) == 0){
		   printf("%*s=====%s===== (%d)\n", spaces, "", root->fname, root->permissions);
		   chdir(root->fname);
		   phelper(root->contents, depth+1);
		   chdir("..");
		   phelper(root->next, depth);
		}
 	}
}
struct TreeNode *nextTree(const char *caddress, DIR *cdir, struct dirent *cbuf){ /*caddress is the CURRENT directory, used for cating a new*/
	DIR *d;									/* directory for contentTree  which is caddress+newdir*/
	struct dirent *buf;
   	FILE *fp;
	DIR *nd;
	struct dirent *nbuf;
	struct stat fileStat;
	struct TreeNode *tnode = malloc(sizeof(struct TreeNode));
	lstat(caddress, &fileStat);
	d = cdir;
	buf = cbuf;
	char *address = malloc(strlen(caddress) + strlen(buf->d_name) +1);
	strcpy(address, caddress);
	strcat(address, "/");
	strcat(address, buf->d_name);
	buf = readdir(d);
	
	if(buf == NULL){
		lstat(address, &fileStat);
		tnode->fname = cbuf->d_name;
		tnode->permissions = (fileStat.st_mode & 0777);
		tnode->contents = NULL;
		tnode->hash = NULL; 
		fp = fopen(address, "r");
		if (fp != NULL){
		tnode->hash = hash(fp); 
		}
		fclose(fp);
		tnode->next = NULL;
		return tnode;
	}
	/*ELSE next item exists OR first item is empty dir*/
	if(strcmp(".",buf->d_name) == 0 || strcmp("..",buf->d_name) == 0){	/*If something odd was sent up*/
		while(strcmp(".",buf->d_name) == 0 || strcmp("..",buf->d_name) == 0){
			if((buf = readdir(d)) == NULL){ 
				buf = NULL;
				break;
			}
		}
	}
	if(buf == NULL && isdir(address) == 0 && isemptydir(address) == 0){ /* is an item without a neighbor*/
		tnode->fname = cbuf->d_name;
		tnode->permissions = (fileStat.st_mode & 0777);
		tnode->contents = NULL;
		tnode->hash = NULL; 
		fp = fopen(address, "r");
		if (fp != NULL){
		tnode->hash = hash(fp); 
		}
		fclose(fp);
		tnode->next = NULL;
		return tnode;
	}

	if(buf != NULL && isdir(address) == 0 && isemptydir(address) == 0){ /* is an item WITH a neighbor */
		lstat(address, &fileStat);
		tnode->fname = cbuf->d_name;
		tnode->permissions = (fileStat.st_mode & 0777);
		tnode->contents = NULL;
		tnode->hash = NULL; 
		fp = fopen(address, "r");
		if (fp != NULL){
		tnode->hash = hash(fp); 
		}
		fclose(fp);
		tnode->next = nextTree(caddress, d, buf);
		return tnode;
	}

	if(buf == NULL && isdir(address) == 1 && isemptydir(address) == 1){ /* is an empty dir with no neighbor*/
		tnode->fname = cbuf->d_name;
		tnode->permissions = (fileStat.st_mode & 0777);
		tnode->contents = NULL;
		tnode->hash = NULL;
		tnode->next = NULL;
		return tnode;
	}
	if(buf != NULL && isemptydir(address) == 1 && isdir(caddress) == 1){ /* is an empty dir with a neighbor*/
		lstat(address, &fileStat);
		tnode->fname = cbuf->d_name;
		tnode->permissions = (fileStat.st_mode & 0777);
		tnode->contents = NULL;
		tnode->hash = NULL;
		tnode->next = nextTree(caddress, d, buf); 
		return tnode;
	}

	if(buf == NULL && isdir(address) == 1 && isemptydir(address) == 0){ /* is an dir without a neighbor*/
	d = opendir(address);
	buf = readdir(d);
		tnode->fname = cbuf->d_name;
		tnode->permissions = (fileStat.st_mode & 0777);
		tnode->contents = nextTree(address, d, buf);
		tnode->hash = NULL; 
		tnode->next = NULL;
		return tnode;
	}
	if(buf != NULL && isdir(address) == 1 && isemptydir(address) == 0){ /* is a dir with a neighbor*/
	lstat(address, &fileStat);
	nd = opendir(address);
	nbuf = readdir(nd);
	if(strcmp(".",nbuf->d_name) == 0 || strcmp("..",nbuf->d_name) == 0){	/*loops until it finds the first item IN the dir*/
		while(strcmp(".",nbuf->d_name) == 0 || strcmp("..",nbuf->d_name) == 0){
			if((nbuf = readdir(nd)) == NULL){ 
				buf = NULL;
				break;
			}
		}
	}
		lstat(address, &fileStat);
		tnode->fname = cbuf->d_name;
		tnode->permissions = (fileStat.st_mode & 0777);
		tnode->contents = nextTree(address, nd, nbuf);
		tnode->hash = NULL;
		tnode->next = nextTree(caddress, d, buf); 
		return tnode;
	}

	
	printf("An unexpected outcome has occured. Now exiting");
	exit(1);
}


/*
 * Returns the FTree rooted at the path fname.
 */
struct TreeNode *generate_ftree(const char *fname) {
    // Your implementation here.
   DIR *d;
   FILE *fp;
   struct dirent *buf;
   struct stat fileStat;
   struct TreeNode *tnode = (struct TreeNode *)malloc(sizeof(struct TreeNode));
	if(lstat(fname, &fileStat) < 0){
		printf("lstat: the name %s is not a file or directory\n", fname);
		exit(1);
	}
		if(isdir(fname) == 0){ /*In the case a file is given, then proceed no further*/
			tnode->fname = (char *)fname;
			tnode->permissions = (fileStat.st_mode & 0777);
			tnode->contents = NULL; 
			tnode->hash = NULL;
			fp = fopen(fname, "r");
			if (fp != NULL){
			tnode->hash = hash(fp); 
			}
			fclose(fp);	
			tnode->next = NULL;
				
		}
		else if(isdir(fname) == 1){
			char *address = malloc(strlen(fname) +1);
			strcpy(address, fname);
			d = opendir(fname);
			buf = readdir(d);
			if(strcmp(".",buf->d_name) == 0 || strcmp("..",buf->d_name) == 0){
				while(strcmp(".",buf->d_name) == 0 || strcmp("..",buf->d_name) == 0){
					if((buf = readdir(d)) == NULL){ /*If the directory is empty*/
						tnode->fname = (char *) fname;
						tnode->permissions = (fileStat.st_mode & 0777);
						tnode->contents = NULL;
						tnode->hash = NULL;
						tnode->next = NULL; 
						return tnode;
					}
				}
	
			}
			if(buf != NULL){
				tnode->fname = (char *) fname;
				tnode->permissions = (fileStat.st_mode & 0777);
				tnode->contents = nextTree(fname, d, buf);
				tnode->hash = NULL;
				tnode->next = NULL; /*change this later*/
			return tnode;

			}

		}
return tnode;
} 
   	/*
 * Prints the TreeNodes encountered on a preorder traversal of an FTree.
 */
void print_ftree(struct TreeNode *root) {
    // Here's a trick for remembering what depth (in the tree) you're at
    // and printing 2 * that many spaces at the beginning of the line.
    static int depth = 0;
    printf("%*s", depth * 2, "");
    // Your implementation here.
		phelper(root, 0);
}
