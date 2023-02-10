from random import randint

posisi_budi = 0

jumlah_mesin = 30
banyak_skor_max = 100
nilai_skor_max = 1000

perintah = "HAPUS"
jumlah_perintah_hapus = 100

print(jumlah_mesin)

mesins = []

for i in range(jumlah_mesin):
    banyak_skor = randint(1, banyak_skor_max)
    skor_mesin = []

    for j in range(banyak_skor):
        skor_mesin.append(randint(0, nilai_skor_max))

    print(f"{banyak_skor}", end=" ")
    print(*skor_mesin)

    skor_mesin.sort()
    mesins.append(skor_mesin)

print(jumlah_perintah_hapus)

hasil = []

for i in range(jumlah_perintah_hapus):
    jumlah_penghapusan = randint(1, len(mesins[posisi_budi]) + 1)

    print(f"HAPUS {jumlah_penghapusan}")

    if len(mesins[posisi_budi]) <= jumlah_penghapusan:
        hasil.append(sum(mesins[posisi_budi]))

        mesins[posisi_budi] = []
        mesins.append(mesins.pop(posisi_budi))
    else:
        hasil.append(sum(mesins[posisi_budi][-1:-1-jumlah_penghapusan:-1]))
        mesins[posisi_budi] = mesins[posisi_budi][0:-jumlah_penghapusan]

print()
print("output: ")
print(mesins)
print(*hasil, sep="\n")