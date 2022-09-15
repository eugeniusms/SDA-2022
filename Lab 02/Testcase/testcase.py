import random

file_keluaran = input("Masukkan nama file output: ")
isi_file = ""

jml_toples = int(input("Jumlah Toples: "))
jml_kue_per_toples = int(input("Jumlah Kue Per Toples: "))
jml_query = int(input("Jumlah Query: "))

isi_file += str(jml_toples) + "\n" + str(jml_kue_per_toples) + "\n" + str(jml_query) + "\n"

daftar_query = ["GESER_KANAN", "BELI_RASA "]

# daftar kue
for toples in range(jml_toples):
    for kue in range(jml_kue_per_toples):
        isi_file += str(random.randint(0,3)) + " "
    isi_file += "\n"

# daftar queries
for query in range(jml_query):
    perintah = daftar_query[random.randint(0,1)]
    if perintah != "GESER_KANAN":
        perintah += str(random.randint(0,5))
    isi_file += perintah + "\n"

# Menulis text yang sudah dioperasikan ke dalam text output
my_file = open(file_keluaran, mode='w')
print(isi_file, file=my_file)
my_file.close()