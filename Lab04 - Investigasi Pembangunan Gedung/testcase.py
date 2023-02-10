import random

file_keluaran = input("Masukkan nama file output: ")
isi_file = ""

gedung = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"

jumlah_gedung = int(input("masukkan jumlah gedung: "))
jumlah_perintah = int(input("masukkan jumlah perintah: "))

lst = ["GERAK", "HANCUR", "TAMBAH", "PINDAH"]

isi_file += str(jumlah_gedung) + "\n"
for i in range(jumlah_gedung):
    isi_file += gedung[i] + " " + str(random.randint(1,1000)) + "\n"

isi_file += "A 1\nC 1\n"

isi_file += str(jumlah_perintah) + "\n"
for i in range(jumlah_perintah):
    isi_file += random.choice(lst) + "\n"

# Menulis text yang sudah dioperasikan ke dalam text output
my_file = open(file_keluaran, mode='w')
print(isi_file, file=my_file)
my_file.close()