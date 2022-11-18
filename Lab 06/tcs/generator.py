import random

file_keluaran = input("Masukkan nama file output: ")
isi_file = ""

jumlah_saham = int(input("N: "))
jumlah_tambah = int(input("Tambah: "))
jumlah_ubah = int(input("Ubah: "))

isi_file += str(jumlah_saham) + "\n"
for i in range(jumlah_saham):
    isi_file += str(random.randint(1, 1000000000)) + " "

isi_file += "\n" + str(jumlah_tambah+jumlah_ubah) + "\n"
# tambah
for i in range(jumlah_tambah):
    isi_file += "TAMBAH " + str(random.randint(1, 1000000000)) + "\n"

#ubah
for i in range(jumlah_ubah):
    isi_file += "UBAH " + str(random.randint(1, jumlah_saham+jumlah_tambah)) + " " + str(random.randint(1, 1000000000)) + "\n"

# Menulis text yang sudah dioperasikan ke dalam text output
my_file = open(file_keluaran, mode='w')
print(isi_file, file=my_file)
my_file.close()