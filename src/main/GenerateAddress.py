from random import Random


RANDOM = True
NUMBER_OF_ADDRESSES = 2048
RANDOM_RANGE_INIT = 0
RANDOM_RANGE_FINISH = 2**16

with open("generated.bin", "wb") as file:
    if RANDOM:
        for c in range(0, NUMBER_OF_ADDRESSES):
            file.write(int(Random().randint(RANDOM_RANGE_INIT, RANDOM_RANGE_FINISH)).to_bytes(4, 'big'))
    else:
        for c in range(0, NUMBER_OF_ADDRESSES):
            file.write(int(c).to_bytes(4, 'big'))
    file.close()
