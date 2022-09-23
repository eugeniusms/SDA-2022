import random

jumlah = int(input("jumlah n: "))
lst = ['R', 'B']

for i in range(jumlah):
    print(lst[random.randint(0,1)], end=" ")