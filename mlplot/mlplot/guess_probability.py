import random
def guess_two(v):
    if v == 31 or v == 367:
        return True
    return False

def guess_one(v):
    if v == 31:
        return True
    return False

def guess_two_curve(attempts, bits):
    guessed_in = []

    for x in range(0, attempts):
        r = list(range(0, 1024))
        count = 0
        while r:
            count += 1
            v = random.choice(r)
            r.remove(v)
            correct = guess_two(v)
            if correct:
                guessed_in.append(count)
                r.clear()

    return guessed_in


def guess_things(attempts, bits, guess_func=guess_two):
    values = list(range(0, 2**bits))
    for attempt in range(attempts):
        v = values.pop(random.randrange(0, len(values)))
        if guess_func(v):
            return True
    return False

if __name__ == "__main__":

    ITERS = 10000

    for attempts in [1000]

    count = 0
    for x in range(0, ITERS):
        if guess_things(attempts, bits, guess_two):
            count += 1
    print("Avg. probability of guessing (two values) with 10 bits and 1000 attempts")
    print(count/ITERS)

    for x in range(0, ITERS):
        if guess_things(1000, 30, guess_two):
            count += 1
    print("Avg. probability of guessing (two values) with 30 bits and 1000 attempts")
    print(count/ITERS)

    for x in range(0, ITERS):
        if guess_things(1000, 10, guess_one):
            count += 1

    print("Avg. probability of guessing (one value) with 10 bits and 1000 attempts")
    print(count/ITERS)



