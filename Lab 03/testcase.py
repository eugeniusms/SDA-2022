import random

jumlah = int(input("jumlah n: "))
lst = ['R', 'B']

for i in range(jumlah):
    print(lst[random.randint(0,1)])

# masukan = input()
# masukan_banyak = input().split()

# print(len(masukan_banyak))