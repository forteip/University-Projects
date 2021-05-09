PORT=52888
DEPENDENCIES = hash.h ftree.h
FLAGS = -DPORT=$(PORT) -Wall -std=gnu99

all: rcopy_client rcopy_server

rcopy_client: rcopy_client.o ftree.o hash_functions.o
	gcc ${CFLAGS} -o $@ $^

rcopy_server: rcopy_server.o ftree.o hash_functions.o
	gcc ${CFLAGS} -o $@ $^

%.o: %.c ${DEPENDENCIES}
	gcc ${FLAGS} -c $<

clean:
	rm *.o rcopy_server rcopy_client
